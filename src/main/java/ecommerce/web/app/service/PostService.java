package ecommerce.web.app.service;

import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();

    public Post savePost(Post post, Optional<User> userAuth){
        post.setAddress(userAuth.get().getAddress());
        post.setFirstName(userAuth.get().getFirstName());
        post.setLastName(userAuth.get().getLastName());
        post.setNumber(userAuth.get().getNumber());
        post.setUser(userAuth.get());
        post.setPostDate(date);
        post.setPostTime(time);
        return postRepository.save(post);
    }

    public List<Post> findAll() {
      return postRepository.findAll();
    }

    public List<Post> findByUserId(long userId) {
        return postRepository.findByUserId(userId);
    }
}
