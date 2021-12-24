package ecommerce.web.app.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
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

    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dxrixqpjg",
            "api_key", "966843127668939",
            "api_secret", "dqIiglHTAoRuYD2j887wmCk56vU"));

    public Object postImageUpload(String absoluteFilePath){
        File file = new File(absoluteFilePath);
        Map uploadResult = null;
        {
            try {
                uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap
                        (
                                "transformation",new Transformation().width(3840).height(2160).quality(60).crop("pad").background("auto")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uploadResult.get("secure_url");
    }

    public Post savePost(Post post, Optional<User> userAuth, String absolutePath){
        String secureImageCloudinaryUrl = String.valueOf(postImageUpload(absolutePath));
        post.setAddress(userAuth.get().getAddress());
        post.setFirstName(userAuth.get().getFirstName());
        post.setLastName(userAuth.get().getLastName());
        post.setNumber(userAuth.get().getNumber());
        post.setUser(userAuth.get());
        post.setPostDate(date);
        post.setPostTime(time);
        post.setPostImageUrl(secureImageCloudinaryUrl);
        return postRepository.save(post);
    }

    public List<Post> findAll() {
      return postRepository.findAll();
    }

    public List<Post> findByUserId(long userId) {
        return postRepository.findByUserId(userId);
    }
}
