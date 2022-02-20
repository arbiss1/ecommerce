package ecommerce.web.app.domain.model.mapper;

import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.dto.PostRequest;
import ecommerce.web.app.domain.model.dto.UserRegisterRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapStructMapperImpl implements MapStructMapper{

    @Override
    public User userPostDtoToUser(UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null){
            return null;
        }
        User user = new User();

        user.setAddress(userRegisterRequest.getAddress());
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setPhoneNumber(userRegisterRequest.getPhoneNumber());
        user.setUsername(userRegisterRequest.getUsername());
        user.setLastName(userRegisterRequest.getLastName());
        user.setCity(userRegisterRequest.getCity());
        user.setCountry(userRegisterRequest.getCountry());
        user.setEmail(userRegisterRequest.getEmail());
        user.setPassword(userRegisterRequest.getPassword());

        return user;

    }

    @Override
    public Post postDtoToPost(PostRequest postRequest) {
        if(postRequest == null){
            return null;
        }

        Post post = new Post();
        post.setImageUrls(postRequest.getImageUrls());
        post.setInSale(postRequest.isInSale());
        post.setPostAdvertIndex(postRequest.getAdvertIndex());
        post.setDescription(postRequest.getDescription());
        post.setCategory(postRequest.getCategory());
        post.setTitle(postRequest.getTitle());
        post.setSubcategory(postRequest.getSubCategory());
        post.setSlug(postRequest.getSlug());
        post.setCurrency(postRequest.getCurrency());
        post.setPrice(postRequest.getPrice());

        return post;
    }
}
