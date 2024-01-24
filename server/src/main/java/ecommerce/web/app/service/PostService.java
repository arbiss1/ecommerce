package ecommerce.web.app.service;

import ecommerce.web.app.controller.enums.PostStatus;
import ecommerce.web.app.controller.model.PostResponse;
import ecommerce.web.app.controller.model.PostDetails;
import ecommerce.web.app.controller.model.PostRequest;
import ecommerce.web.app.controller.model.SearchBuilderRequest;
import ecommerce.web.app.enums.AdvertIndex;
import ecommerce.web.app.exceptions.BindingException;
import ecommerce.web.app.exceptions.ImageCustomException;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.repository.PostRepository;
import ecommerce.web.app.entities.*;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    public final PostRepository postRepository;
    public final SearchService searchService;
    public final MessageSource messageByLocale;
    public final ImageUploadService imageUploadService;
    private final Locale locale = Locale.ENGLISH;

    public PostResponse save(PostRequest postRequest, User userAuth, List<String> postsImageUrls, BindingResult result
    ) throws BindingException, ImageCustomException {
        if (result.hasErrors()) {
            throw new BindingException(result.getAllErrors().toString());
        }
        Post post = postRepository.save(mapToPost(postRequest, userAuth));
        if(postsImageUrls != null && !postsImageUrls.isEmpty()) imageUploadService.postImageUpload(postsImageUrls, post);
        return new PostResponse(post.getId());
    }

    public PostResponse edit(String postId, PostRequest postRequest, User authUser, BindingResult result)
            throws PostCustomException, BindingException {
        if (result.hasErrors()) {
            throw new BindingException(result.getAllErrors().toString());
        }
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isPresent()) {
            Post editablePost = findPost.get();
            editablePost.setPrice(postRequest.getPrice());
            editablePost.setDescription(postRequest.getDescription());
            editablePost.setCurrency(postRequest.getCurrency().mapToStatus());
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

    public List<PostDetails> findAll() {
        List<PostDetails> postDetails = new ArrayList<>();
        List<String> images = new ArrayList<>();
        for (Post post : postRepository.findAll()) {
            for (ImageUpload image : imageUploadService.getImages(post)) {
                images.add(image.getProfileImage());
            }
            postDetails.add(mapToPostDetail(post, images));
            images = new ArrayList<>();
        }
       return postDetails;
    }

    public List<PostDetails> search(SearchBuilderRequest searchBuilderRequest) {
        if (searchBuilderRequest == null) {
            return mapToPostDetails(postRepository.findAll());
        }
        List<Post> response = searchService.searchPosts(searchBuilderRequest);
        return mapToPostDetails(response);
    }

    public List<PostDetails> listByUser(String userId) {
        return mapToPostDetails(postRepository.findByUserId(userId));
    }

    public Optional<Post> findByPostId(String postId) {
        return postRepository.findById(postId);
    }

    public void deleteById(String postId) throws PostCustomException {
        Post findPost = postRepository.findById(postId).orElseThrow(() ->
                new PostCustomException(
                        messageByLocale.getMessage("error.404.postNotFound", null, locale)));
        imageUploadService.deleteImages(findPost);
        postRepository.deleteById(findPost.getId());
    }

    private Post mapToPost(PostRequest postRequest, User getAuthenticatedUser){
        Post post = new Post();
        post.setUser(getAuthenticatedUser);
        post.setCreatedAt(LocalDateTime.now());
        post.setCreatedBy(getAuthenticatedUser.getUsername());
        post.setModifiedBy(getAuthenticatedUser.getUsername());
        post.setModifiedAt(LocalDateTime.now());
        post.setStatus(PostStatus.PENDING.mapToStatus());
        post.setPostAdvertIndex(postRequest.getPostAdvertIndex() != null ? postRequest.getPostAdvertIndex().mapToStatus() : AdvertIndex.FREE);
        post.setCurrency(postRequest.getCurrency().mapToStatus());
        post.setDescription(postRequest.getDescription());
        post.setPrice(postRequest.getPrice());
        post.setTitle(postRequest.getTitle());
        post.setBrand(postRequest.getBrand());
        post.setEngineSize(postRequest.getEngineSize());
        post.setFirstRegistration(postRequest.getFirstRegistration());
        post.setFuel(postRequest.getFuel());
        post.setColor(postRequest.getColor());
        post.setKilometers(postRequest.getKilometers());
        post.setPower(postRequest.getPower());
        post.setTransmission(postRequest.getTransmission());
        post.setType(postRequest.getType());
        post.setPostType(postRequest.getPostType());
        return post;
    }

    public List<PostDetails> mapToPostDetails(List<Post> posts){
        return posts.stream().map(post -> new PostDetails(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getPrice()
        )).collect(Collectors.toList());
    }

    public PostDetails mapToPostDetail(Post post, List<String> images){
        return new PostDetails(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getPrice(),
                images
        );
    }

}
