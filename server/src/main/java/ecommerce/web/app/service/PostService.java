package ecommerce.web.app.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import ecommerce.web.app.controller.enums.PostStatus;
import ecommerce.web.app.controller.model.PostRequest;
import ecommerce.web.app.enums.AdvertIndex;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.repository.CategoryRepository;
import ecommerce.web.app.repository.PostRepository;
import ecommerce.web.app.repository.SubcategoryRepository;
import ecommerce.web.app.entities.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    public final PostRepository postRepository;
    public final MessageSource messageByLocale;
    public final CategoryRepository categoryRepository;
    public final SubcategoryRepository subcategoryRepository;


    public PostService(PostRepository postRepository,
                       MessageSource messageByLocale,
                       CategoryRepository categoryRepository,
                       SubcategoryRepository subcategoryRepository) {
        this.postRepository = postRepository;
        this.messageByLocale = messageByLocale;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    private final Locale locale = Locale.ENGLISH;

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dxrixqpjg",
            "api_key", "966843127668939",
            "api_secret", "dqIiglHTAoRuYD2j887wmCk56vU"));

    public List<ImageUpload> postImageUpload(@NotNull List<ImageUpload> absoluteFilePath) {
        List<Object> transferUrlsToStorage = new ArrayList<>();
        absoluteFilePath.forEach(imageUpload -> {
            File file = new File(imageUpload.getImageUrl());
            Map uploadResult = null;
            {
                try {
                    uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap
                            (
                                    "transformation", new Transformation().width(1600).height(1000).quality(40).crop("pad").background("auto")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            transferUrlsToStorage.add(uploadResult != null ? uploadResult.get("url") : null);
        });
        final int[] count = {0};
        transferUrlsToStorage.forEach(transferUrlToStorage ->
            absoluteFilePath.get(count[0]++).setImageUrl(transferUrlToStorage.toString())
        );
        if (absoluteFilePath.isEmpty()) {
            return null;
        } else {
            return absoluteFilePath;
        }
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public void deleteImage(String publicId) throws IOException {

        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

    }

    public Post savePost(PostRequest postRequest, Optional<User> userAuth, List<ImageUpload> postsImageUrls)
            throws UserNotFoundException {
        List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
        User getAuthenticatedUser;
        if(userAuth.isPresent()){
            getAuthenticatedUser = userAuth.get();
        }
        else {
            throw new UserNotFoundException(messageByLocale.getMessage(
                    "error.404.userNotFound", null, locale)
            );
        }
        return postRepository.save(mapToPost(postRequest, getAuthenticatedUser, uploadImagesToCloudinary));
    }
    
    private Post mapToPost(PostRequest postRequest, User getAuthenticatedUser, List<ImageUpload> uploadImagesToCloudinary){
        
        Post post = new Post();
        post.setUser(getAuthenticatedUser);
        post.setAddress(getAuthenticatedUser.getAddress());
        post.setFirstName(getAuthenticatedUser.getFirstName());
        post.setLastName(getAuthenticatedUser.getLastName());
        post.setPhoneNumber(getAuthenticatedUser.getPhoneNumber());
        post.setCity(getAuthenticatedUser.getCity());
        post.setCountry(getAuthenticatedUser.getCountry());
        post.setCreatedDate(LocalDateTime.now());
        post.setCreatedBy(getAuthenticatedUser.getUsername());
        post.setLastModifiedBy(getAuthenticatedUser.getUsername());
        post.setLastModifiedDate(LocalDateTime.now());
        post.setImageUrls(uploadImagesToCloudinary);
        post.setStatus(PostStatus.PENDING.mapToStatus());
        post.setPostAdvertIndex(postRequest.getAdvertIndex() != null ? postRequest.getAdvertIndex().mapToStatus() : AdvertIndex.FREE);
        post.setCurrency(postRequest.getCurrency().mapToStatus());
        post.setCategory(postRequest.getCategory());
        post.setDescription(postRequest.getDescription());
        post.setPrice(postRequest.getPrice());
        post.setTitle(postRequest.getTitle());
        post.setSlug(postRequest.getSlug());
        post.setInSale(postRequest.isInSale());
        post.setSubcategory(postRequest.getSubCategory());
        return post;
    }

    public Post changeStatusToActive(Post post, Optional<User> userAuth) throws PostCustomException {
        Optional<Post> findIfPostExist = postRepository.findById(post.getId());
        if (findIfPostExist.isPresent()) {
            post.setStatus(PostStatus.ACTIVE.mapToStatus());
        } else {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale)
            );
        }
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.searchPosts(keyword);
    }

    public List<Post> findByUserId(String userId) {
        return postRepository.findByUserId(userId);
    }

    public Optional<Post> findByPostId(long postId) {
        return postRepository.findById(postId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Post editPost(long postId, PostRequest postRequest, Optional<User> authenticatedUser, List<ImageUpload> postsImageUrls)
            throws PostCustomException, UserNotFoundException {
        if(!authenticatedUser.isPresent()){
            throw new UserNotFoundException(
                    messageByLocale.getMessage("error.404.userNotFound", null, locale)
            );
        }
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isPresent()) {
            Post editablePost = findPost.get();
            editablePost.getImageUrls().forEach(imageUpload -> {
                String imageTag = imageUpload.getImageUrl().substring(imageUpload.getImageUrl().lastIndexOf("/") + 1);
                String publicId = imageTag.substring(0, imageTag.lastIndexOf("."));
                try {
                    deleteImage(publicId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
            editablePost.setPrice(postRequest.getPrice());
            editablePost.setDescription(postRequest.getDescription());
            editablePost.setCurrency(postRequest.getCurrency().mapToStatus());
            editablePost.setCategory(postRequest.getCategory());
            editablePost.setSubcategory(postRequest.getSubCategory());
            editablePost.setTitle(postRequest.getTitle());
            editablePost.setLastModifiedBy(authenticatedUser.get().getUsername());
            editablePost.setLastModifiedDate(LocalDateTime.now());
            editablePost.setImageUrls(uploadImagesToCloudinary);
            return postRepository.save(editablePost);
        } else {
            throw new PostCustomException(messageByLocale.getMessage(
                    "error.409.postNotPostedServerError", null, locale)
            );
        }
    }

    public void deleteById(long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isPresent()) {
            Post getPost = findPost.get();
            getPost.getImageUrls().forEach(imageUpload -> {
                String imageTag = imageUpload.getImageUrl().substring(imageUpload.getImageUrl().lastIndexOf("/") + 1);
                String publicId = imageTag.substring(0, imageTag.lastIndexOf("."));
                try {
                    deleteImage(publicId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        postRepository.deleteById(postId);
    }
}
