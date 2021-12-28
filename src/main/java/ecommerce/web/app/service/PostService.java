package ecommerce.web.app.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import ecommerce.web.app.model.ImageUpload;
import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.model.mapper.MapStructMapper;
import ecommerce.web.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MapStructMapper mapStructMapper;

    @Autowired
    ImageUploadService imageUploadService;

    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dxrixqpjg",
            "api_key", "966843127668939",
            "api_secret", "dqIiglHTAoRuYD2j887wmCk56vU"));

    public List<ImageUpload> postImageUpload(List<ImageUpload> absoluteFilePath){
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

    public void deleteImage(String publicId) throws IOException {

                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            }


    public Post savePost(Post post, Optional<User> userAuth, List<ImageUpload> postsImageUrls){
        List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
        post.setAddress(userAuth.get().getAddress());
        post.setFirstName(userAuth.get().getFirstName());
        post.setLastName(userAuth.get().getLastName());
        post.setNumber(userAuth.get().getNumber());
        post.setUser(userAuth.get());
        post.setPostDate(date);
        post.setPostTime(time);
        post.setPostImageUrl(uploadImagesToCloudinary);
        return postRepository.save(post);
    }

    public List<Post> findAll() {
      return postRepository.findAll();
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
            });
            List<ImageUpload> uploadImagesToCloudinary = postImageUpload(postsImageUrls);
            post.setPostId(postId);
            post.setAddress(authenticatedUser.get().getAddress());
            post.setFirstName(authenticatedUser.get().getFirstName());
            post.setLastName(authenticatedUser.get().getLastName());
            post.setNumber(authenticatedUser.get().getNumber());
            post.setUser(authenticatedUser.get());
            post.setPostDate(date);
            post.setPostTime(time);
            post.setPostImageUrl(uploadImagesToCloudinary);
            return postRepository.save(post);
        }
        else {
            return post;
        }
    }
}
