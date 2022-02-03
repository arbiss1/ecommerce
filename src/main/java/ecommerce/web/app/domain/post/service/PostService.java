package ecommerce.web.app.domain.post.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import ecommerce.web.app.domain.post.model.ImageUpload;
import ecommerce.web.app.domain.post.model.Post;
import ecommerce.web.app.domain.post.repository.PostRepository;
import ecommerce.web.app.domain.user.model.User;
import ecommerce.web.app.mapper.MapStructMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MapStructMapper mapStructMapper;

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
        post.setNumber(userAuth.get().getNumber());
        post.setPostDate(date);
        post.setPostTime(time);
        post.setPostImageUrl(uploadImagesToCloudinary);
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

    public Post findByPostId(long postId) {
        return postRepository.findByPostId(postId);
    }

    public Post editPost(long postId,Post post, Optional<User> authenticatedUser,List<ImageUpload> postsImageUrls) {
        Post findPost = postRepository.findByPostId(postId);
        if(!findPost.equals("")){
            findPost.getPostImageUrl().forEach(imageUpload -> {
                String imageTag = imageUpload.getImageUrl().substring(imageUpload.getImageUrl().lastIndexOf("/") + 1);
                String publicId = imageTag.substring(0 , imageTag.lastIndexOf("."));
                try {
                    deleteImage(publicId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                imageUploadService.deleteImageUploaded(imageUpload.getId());
            });
//            postRepository.deleteByPostImageUrl(findPost.getPostImageUrl());
            List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
            post.setPostId(postId);
            post.setUser(authenticatedUser.get());
            post.setAddress(authenticatedUser.get().getAddress());
            post.setFirstName(authenticatedUser.get().getFirstName());
            post.setLastName(authenticatedUser.get().getLastName());
            post.setNumber(authenticatedUser.get().getNumber());
            post.setPostDate(date);
            post.setPostTime(time);
            post.setPostImageUrl(uploadImagesToCloudinary);
            return postRepository.save(post);
        }
        //s
        else {
            return post;
        }
    }

    public void deleteById(long postId) {
        postRepository.deleteById(postId);
    }
}
