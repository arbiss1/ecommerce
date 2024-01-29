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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class PostService {

    public final PostRepository postRepository;
    public final SearchService searchService;
    public final MessageSource messageByLocale;
    public final ImageUploadService imageUploadService;

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
            Post editablePost = mapToPost(postRequest, authUser);
            editablePost.setId(findPost.get().getId());
            return new PostResponse(postRepository.save(editablePost).getId());
        } else {
            throw new PostCustomException(buildError("error.409.postServerError"));
        }
    }

    public String changeStatus(String postId) throws PostCustomException {
        Optional<Post> findIfPostExist = postRepository.findById(postId);
        if (findIfPostExist.isPresent()) {
            findIfPostExist.get().setStatus(PostStatus.ACTIVE.mapToStatus());
        } else {
            throw new PostCustomException(buildError("error.404.postNotFound"));
        }
        return postRepository.save(findIfPostExist.get()).getId();
    }

    public Page<PostDetails> findAll(Integer page, Integer size) {
        Page<Post> postPage = postRepository.findAll(PageRequest.of(page, size));
        List<PostDetails> postDetailsList = postPage.getContent().stream()
                .map(post -> mapToPostDetail(post, imageUploadService.getImages(post)
                        .stream().map(String::valueOf).toList()))
                .toList();

        return new PageImpl<>(postDetailsList, postPage.getPageable(), postPage.getTotalElements());
    }


    public Page<PostDetails> search(SearchBuilderRequest searchBuilderRequest, Integer page, Integer size) {
        if (searchBuilderRequest == null) {
            Page<Post> postPage = postRepository.findAll(PageRequest.of(page, size));
            List<PostDetails> postDetailsList = postPage.getContent().stream()
                    .map(post -> mapToPostDetail(post, imageUploadService.getImages(post)
                            .stream().map(String::valueOf).toList()))
                    .toList();

            return new PageImpl<>(postDetailsList, postPage.getPageable(), postPage.getTotalElements());
        }
        Page<Post> response = searchService.searchPosts(searchBuilderRequest, page, size);
        List<PostDetails> postDetailsList = response.stream()
                .map(post -> mapToPostDetail(post, imageUploadService.getImages(post)
                        .stream().map(String::valueOf).toList()))
                .toList();

        return new PageImpl<>(postDetailsList, response.getPageable(), response.getTotalElements());
    }

    public Page<PostDetails> listByUser(String userId, Integer page, Integer size) {
        Page<Post> postPage = postRepository.findByUserId(userId, PageRequest.of(page, size));
        List<PostDetails> postDetailsList = postPage.getContent().stream()
                .map(post -> mapToPostDetail(post, imageUploadService.getImages(post)
                        .stream().map(String::valueOf).toList()))
                .toList();

        return new PageImpl<>(postDetailsList, postPage.getPageable(), postPage.getTotalElements());
    }

    public void deleteById(String postId) throws PostCustomException {
        Post findPost = postRepository.findById(postId).orElseThrow(() ->
                new PostCustomException(buildError("error.404.postNotFound")));
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
        post.setModel(postRequest.getModel());
        post.setPostType(postRequest.getPostType());
        return post;
    }

    public PostDetails mapToPostDetail(Post post, List<String> images){
        return new PostDetails(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getPostType(),
                post.getCurrency(),
                post.getPostAdvertIndex(),
                post.getType(),
                post.getBrand(),
                post.getModel(),
                post.getColor(),
                post.getTransmission(),
                post.getKilometers(),
                post.getFuel(),
                post.getPower(),
                post.getPrice(),
                post.getFirstRegistration(),
                post.getEngineSize(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                images
        );
    }

    private String buildError(String message) {
        return messageByLocale.getMessage(message, null, Locale.ENGLISH);
    }
}
