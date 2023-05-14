package ecommerce.web.app.service;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import ecommerce.web.app.controller.enums.PostStatus;
import ecommerce.web.app.controller.model.PostResponse;
import ecommerce.web.app.controller.model.PostDetails;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    public final PostRepository postRepository;
    public final MessageSource messageByLocale;
    public final CategoryRepository categoryRepository;
    public final SubcategoryRepository subcategoryRepository;
    public final CloudinaryService cloudinaryService;
    private final Locale locale = Locale.ENGLISH;



    public PostService(PostRepository postRepository, MessageSource messageByLocale, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, CloudinaryService cloudinaryService) {
        this.postRepository = postRepository;
        this.messageByLocale = messageByLocale;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public List<ImageUpload> postImageUpload(@NotNull List<ImageUpload> absoluteFilePath) {
        List<Object> transferUrlsToStorage = new ArrayList<>();
        absoluteFilePath.forEach(imageUpload -> {
            File file = new File(imageUpload.getImageUrl());
            Map uploadResult = null;
            {
                try {
                    uploadResult = cloudinaryService.uploader(file, ObjectUtils.asMap
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

    public PostResponse savePost(PostRequest postRequest, User userAuth, List<ImageUpload> postsImageUrls, BindingResult result
    ) throws UserNotFoundException {
        if (result.hasErrors()) {
            throw new UserNotFoundException(
                    messageByLocale.getMessage(
                            result.getAllErrors().toString(), null, locale
                    )
            );
        }
        List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
        return new PostResponse(postRepository.save(mapToPost(postRequest, userAuth, uploadImagesToCloudinary)).getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PostResponse editPost(String postId, PostRequest postRequest, User authUser, List<ImageUpload> postsImageUrls, BindingResult result)
            throws PostCustomException {
        if (result.hasErrors()) {
            throw new PostCustomException(
                    messageByLocale.getMessage(result.getAllErrors().toString(), null, locale)
            );
        }
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isPresent()) {
            Post editablePost = findPost.get();
            editablePost.getImageUrls().forEach(imageUpload -> {
                String imageTag = imageUpload.getImageUrl().substring(imageUpload.getImageUrl().lastIndexOf("/") + 1);
                String publicId = imageTag.substring(0, imageTag.lastIndexOf("."));
                try {
                    cloudinaryService.deleteImage(publicId);
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
            editablePost.setLastModifiedBy(authUser.getUsername());
            editablePost.setLastModifiedDate(LocalDateTime.now());
            editablePost.setImageUrls(uploadImagesToCloudinary);
            return new PostResponse(postRepository.save(editablePost).getId());
        } else {
            throw new PostCustomException(messageByLocale.getMessage(
                    "error.409.postNotPostedServerError", null, locale)
            );
        }
    }

    public String changeStatusToActive(String postId) throws PostCustomException {
        Optional<Post> findIfPostExist = postRepository.findById(postId);
        if (findIfPostExist.isPresent()) {
            findIfPostExist.get().setStatus(PostStatus.ACTIVE.mapToStatus());
        } else {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale)
            );
        }
        return postRepository.save(findIfPostExist.get()).getId();
    }

    public List<PostDetails> findAll() throws PostCustomException {
        List<Post> response = postRepository.findAll();
        if (response.isEmpty()) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale));
        }
       return mapToPostDetails(response);
    }

    public List<PostDetails> searchPosts(String keyword) throws PostCustomException {
        if (keyword.equals("") || keyword.equals(" ")) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale)
            );
        }
        List<Post> response = postRepository.searchPosts(keyword);
        if (response.isEmpty()) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale));
        }
        return mapToPostDetails(response);
    }

    public List<PostDetails> findPostsByUserId(String userId) throws PostCustomException {
        List<Post> response = postRepository.findByUserId(userId);
        if (response.isEmpty()) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale));
        }
        return mapToPostDetails(response);
    }

    public Optional<Post> findByPostId(String postId) {
        return postRepository.findById(postId);
    }

    public void deleteById(String postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isPresent()) {
            Post getPost = findPost.get();
            getPost.getImageUrls().forEach(imageUpload -> {
                String imageTag = imageUpload.getImageUrl().substring(imageUpload.getImageUrl().lastIndexOf("/") + 1);
                String publicId = imageTag.substring(0, imageTag.lastIndexOf("."));
                try {
                    cloudinaryService.deleteImage(publicId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        postRepository.deleteById(postId);
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

    public List<PostDetails> mapToPostDetails(List<Post> posts){
        return posts.stream().map(post -> new PostDetails(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getPrice(),
                post.getImageUrls(),
                post.getCategory(),
                post.getSubcategory()
        )).collect(Collectors.toList());
    }

}
