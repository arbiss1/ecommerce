package ecommerce.web.app.domain.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import ecommerce.web.app.domain.enums.PostStatus;
import ecommerce.web.app.domain.model.ImageUpload;
import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.repository.PostRepository;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.mapper.MapStructMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class PostService {

    public final PostRepository postRepository;

    public final MapStructMapper mapStructMapper;

    public PostService(PostRepository postRepository, MapStructMapper mapStructMapper){
        this.postRepository= postRepository;
        this.mapStructMapper =mapStructMapper;
    }

    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();


    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dxrixqpjg",
            "api_key", "966843127668939",
            "api_secret", "dqIiglHTAoRuYD2j887wmCk56vU"));

    @Async
    public List<ImageUpload> postImageUpload(@NotNull List<ImageUpload> absoluteFilePath){
        List<Object> transferUrlsToStorage = new ArrayList<>();
        absoluteFilePath.forEach(imageUpload -> {
            File file = new File(imageUpload.getImageUrl());
            Map uploadResult = null;
            {
                try {
                    uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap
                            (
                                    "transformation",new Transformation().width(2840).height(1650).quality(40).crop("pad").background("auto")));
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
      if(absoluteFilePath.isEmpty()){
          return null;
      }else {
          return absoluteFilePath;
      }
    }

    public Page<Post> findAll(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    public void deleteImage(String publicId) throws IOException {

                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            }

    public Post savePost(Post post, Optional<User> userAuth, List<ImageUpload> postsImageUrls){
        List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
        post.setAddress(userAuth.get().getAddress());
        post.setUser(userAuth.get());
        post.setFirstName(userAuth.get().getFirstName());
        post.setLastName(userAuth.get().getLastName());
        post.setPhone_number(userAuth.get().getNumber());
        post.setDate(date);
        post.setTime(time);
        post.setImageUrls(uploadImagesToCloudinary);
        post.setStatus(PostStatus.PENDING);
        return postRepository.save(post);
    }

    public Post changeStatusToActive(Post post, Optional<User> userAuth){
        Optional<Post> findIfPostExist = postRepository.findById(post.getId());
        if(findIfPostExist.isPresent()){
            post.setStatus(PostStatus.ACTIVE);
        }else {
            post.setStatus(PostStatus.PENDING);
        }
        return postRepository.save(post);
    }

    public List<Post> findAll() {
      return postRepository.findAll();
    }

    public List<Post> searchPosts(String keyword){
        return postRepository.searchPosts(keyword);
    }

    public List<Post> findByUserId(long userId) {
        return postRepository.findByUserId(userId);
    }

    public Optional<Post> findByPostId(String postId) {
        return postRepository.findById(postId);
    }

    public Post editPost(String postId,Post post, Optional<User> authenticatedUser,List<ImageUpload> postsImageUrls) {
        Optional<Post> findPost = postRepository.findById(postId);
        if(!findPost.isPresent()){
            Post ediatblePost = findPost.get();
            ediatblePost.getImageUrls().forEach(imageUpload -> {
                String imageTag = imageUpload.getImageUrl().substring(imageUpload.getImageUrl().lastIndexOf("/") + 1);
                String publicId = imageTag.substring(0 , imageTag.lastIndexOf("."));
                try {
                    deleteImage(publicId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
            post.setId(postId);
            post.setUser(authenticatedUser.get());
            post.setAddress(authenticatedUser.get().getAddress());
            post.setFirstName(authenticatedUser.get().getFirstName());
            post.setLastName(authenticatedUser.get().getLastName());
            post.setPhone_number(authenticatedUser.get().getNumber());
            post.setDate(date);
            post.setTime(time);
            post.setImageUrls(uploadImagesToCloudinary);
            return postRepository.save(post);
        }
        else {
            return post;
        }
    }

    public void deleteById(String postId) {
        postRepository.deleteById(postId);
    }
}
