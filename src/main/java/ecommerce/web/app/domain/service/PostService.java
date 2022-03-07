package ecommerce.web.app.domain.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import ecommerce.web.app.domain.enums.AdvertIndex;
import ecommerce.web.app.domain.enums.PostStatus;
import ecommerce.web.app.domain.model.*;
import ecommerce.web.app.domain.repository.CategoryRepository;
import ecommerce.web.app.domain.repository.PostRepository;
import ecommerce.web.app.domain.model.mapper.MapStructMapper;
import ecommerce.web.app.domain.repository.SubcategoryRepository;
import ecommerce.web.app.exception.customExceptions.PostCustomException;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
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
    public final MapStructMapper mapStructMapper;
    public final CategoryRepository categoryRepository;
    public final SubcategoryRepository subcategoryRepository;


    public PostService(PostRepository postRepository,
                       MapStructMapper mapStructMapper,
                       MessageSource messageByLocale,
                       CategoryRepository categoryRepository,
                       SubcategoryRepository subcategoryRepository) {
        this.postRepository = postRepository;
        this.mapStructMapper = mapStructMapper;
        this.messageByLocale = messageByLocale;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    private final Locale locale = Locale.ENGLISH;

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dxrixqpjg",
            "api_key", "966843127668939",
            "api_secret", "dqIiglHTAoRuYD2j887wmCk56vU"));

    @Async
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
            transferUrlsToStorage.add(uploadResult.get("url"));
        });
        final int[] count = {0};
        transferUrlsToStorage.forEach(o -> {
            absoluteFilePath.get(count[0]++).setImageUrl(o.toString());
        });
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

    public Post savePost(Post post, Optional<User> userAuth, List<ImageUpload> postsImageUrls) {
        List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
        User getAuthenticatedUser = userAuth.get();
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
        post.setStatus(PostStatus.PENDING);
        post.setPostAdvertIndex(AdvertIndex.FREE);
        return postRepository.save(post);
    }


    public Post changeStatusToActive(Post post, Optional<User> userAuth) {
        Optional<Post> findIfPostExist = postRepository.findById(post.getId());
        if (findIfPostExist.isPresent()) {
            post.setStatus(PostStatus.ACTIVE);
        } else {
            post.setStatus(PostStatus.PENDING);
        }
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.searchPosts(keyword);
    }

    public List<Post> findByUserId(long userId) {
        return postRepository.findByUserId(userId);
    }

    public Optional<Post> findByPostId(long postId) {
        return postRepository.findById(postId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Post editPost(long postId, Post post, Optional<User> authenticatedUser, List<ImageUpload> postsImageUrls) throws PostCustomException {
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isPresent()) {
            Post ediatblePost = findPost.get();
            ediatblePost.getImageUrls().forEach(imageUpload -> {
                String imageTag = imageUpload.getImageUrl().substring(imageUpload.getImageUrl().lastIndexOf("/") + 1);
                String publicId = imageTag.substring(0, imageTag.lastIndexOf("."));
                try {
                    deleteImage(publicId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
            ediatblePost.setPrice(post.getPrice());
            ediatblePost.setDescription(post.getDescription());
            ediatblePost.setCurrency(post.getCurrency());
            ediatblePost.setCategory(post.getCategory());
            ediatblePost.setSubcategory(post.getSubcategory());
            ediatblePost.setTitle(post.getTitle());
            ediatblePost.setLastModifiedBy(authenticatedUser.get().getUsername());
            ediatblePost.setLastModifiedDate(LocalDateTime.now());
            ediatblePost.setImageUrls(uploadImagesToCloudinary);
            return postRepository.save(ediatblePost);
        } else {
            throw new PostCustomException(messageByLocale.getMessage("error.409.postNotPostedServerError", null, locale));
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
