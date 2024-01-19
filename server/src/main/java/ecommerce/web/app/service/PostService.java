package ecommerce.web.app.service;

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
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    public final PostRepository postRepository;
    public final MessageSource messageByLocale;
    public final CategoryRepository categoryRepository;
    public final SubcategoryRepository subcategoryRepository;
    public final ImageUploadService imageUploadService;
    private final Locale locale = Locale.ENGLISH;

    public void postImageUpload(List<ImageUpload> imageUploads){
        for (ImageUpload imageUpload : imageUploads) {
            imageUploadService.saveImage(imageUpload);
        }
    }

    public PostResponse save(PostRequest postRequest, User userAuth, List<String> postsImageUrls, BindingResult result
    ) throws UserNotFoundException {
        if (result.hasErrors()) {
            throw new UserNotFoundException(
                    messageByLocale.getMessage(
                            result.getAllErrors().toString(), null, locale
                    )
            );
        }
        Post post = postRepository.save(mapToPost(postRequest, userAuth));
        List<ImageUpload> imageUploads = new ArrayList<>();
        for (String postsImageUrl : postsImageUrls) {
            ImageUpload imageUpload = new ImageUpload();
            imageUpload.setPost(post);
            imageUpload.setProfileImage(postsImageUrl);
            imageUploads.add(imageUpload);
        }
        postImageUpload(imageUploads);
        return new PostResponse(post.getId());
    }

    public PostResponse edit(String postId, PostRequest postRequest, User authUser, BindingResult result)
            throws PostCustomException {
        if (result.hasErrors()) {
            throw new PostCustomException(
                    messageByLocale.getMessage(result.getAllErrors().toString(), null, locale)
            );
        }
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isPresent()) {
            Post editablePost = findPost.get();
            editablePost.setPrice(postRequest.getPrice());
            editablePost.setDescription(postRequest.getDescription());
            editablePost.setCurrency(postRequest.getCurrency().mapToStatus());
            editablePost.setCategory(postRequest.getCategory());
            editablePost.setSubcategory(postRequest.getSubCategory());
            editablePost.setTitle(postRequest.getTitle());
            editablePost.setModifiedBy(authUser.getUsername());
            editablePost.setModifiedAt(LocalDateTime.now());
            return new PostResponse(postRepository.save(editablePost).getId());
        } else {
            throw new PostCustomException(messageByLocale.getMessage(
                    "error.409.postNotPostedServerError", null, locale)
            );
        }
    }

    public String changeStatus(String postId) throws PostCustomException {
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

    public List<PostDetails> search(String keyword) throws PostCustomException {
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

    public List<PostDetails> findByAuthenticatedUser(String userId) throws PostCustomException {
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

    public void deleteById(String postId) throws PostCustomException {
        Post findPost = postRepository.findById(postId).orElseThrow(() ->
                new PostCustomException(
                        messageByLocale.getMessage("error.404.postNotFound", null, locale)));
        postRepository.deleteById(findPost.getId());
        imageUploadService.deleteImages(findPost);
    }

    private Post mapToPost(PostRequest postRequest, User getAuthenticatedUser){

        Post post = new Post();
        post.setUser(getAuthenticatedUser);
        post.setAddress(getAuthenticatedUser.getAddress());
        post.setFirstName(getAuthenticatedUser.getFirstName());
        post.setLastName(getAuthenticatedUser.getLastName());
        post.setPhoneNumber(getAuthenticatedUser.getPhoneNumber());
        post.setCity(getAuthenticatedUser.getCity());
        post.setCountry(getAuthenticatedUser.getCountry());
        post.setCreatedAt(LocalDateTime.now());
        post.setCreatedBy(getAuthenticatedUser.getUsername());
        post.setModifiedBy(getAuthenticatedUser.getUsername());
        post.setModifiedAt(LocalDateTime.now());
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
                post.getCategory(),
                post.getSubcategory()
        )).collect(Collectors.toList());
    }

}
