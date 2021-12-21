package ecommerce.web.app.service;

import ecommerce.web.app.model.Post;
import ecommerce.web.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public Post savePost(Post post){
        return postRepository.save(post);
    }
}
