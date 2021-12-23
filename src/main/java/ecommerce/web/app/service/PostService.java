package ecommerce.web.app.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.model.dto.UserGetDto;
import ecommerce.web.app.model.mapper.MapStructMapper;
import ecommerce.web.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Map postImageUpload(String absoluteFilePath,File fileName){
        File file = new File(absoluteFilePath);
        Map uploadResult = null;
        {
            try {
                uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap
                        ("public_id",fileName.getName(),
                                "transformation",new Transformation().width(600).height(300).crop("pad").background("auto")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uploadResult;
    }

    public String getImageUploaded(String publicId){
        return cloudinary.url().generate(publicId);
    }

    public Post savePost(Post post, Optional<User> userAuth,String absoluteFilePath,File fileName){
        postImageUpload(absoluteFilePath,fileName);
        post.setAddress(userAuth.get().getAddress());
        post.setFirstName(userAuth.get().getFirstName());
        post.setLastName(userAuth.get().getLastName());
        post.setNumber(userAuth.get().getNumber());
        post.setUser(userAuth.get());
        post.setPostDate(date);
        post.setPostTime(time);
        post.setPostImageUrl(getImageUploaded(fileName.getName() + ".jpg"));
        return postRepository.save(post);
    }

    public List<Post> findAll() {
      return postRepository.findAll();
    }

    public List<Post> findByUserId(long userId) {
        return postRepository.findByUserId(userId);
    }
}
