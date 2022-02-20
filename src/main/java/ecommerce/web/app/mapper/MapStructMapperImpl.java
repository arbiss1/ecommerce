package ecommerce.web.app.mapper;

import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.dto.UserLoginRequest;
import ecommerce.web.app.domain.model.dto.UserPostDto;
import org.springframework.stereotype.Component;

@Component
public class MapStructMapperImpl implements MapStructMapper{


    @Override
    public User dtoToUser(UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            return null;
        }
        User user = new User();

        user.setUsername(userLoginRequest.getUsername());
        user.setPassword(userLoginRequest.getPassword());

        return user;

    }


    @Override
    public User userPostDtoToUser(UserPostDto userPostDto) {
        if(userPostDto == null){
            return null;
        }
        User user = new User();

        user.setId(userPostDto.getId());
        user.setAddress(userPostDto.getAddress());
        user.setFirstName(userPostDto.getFirstName());
        user.setPhoneNumber(userPostDto.getPhoneNumber());
        user.setUsername(userPostDto.getUsername());
        user.setLastName(userPostDto.getLastName());
        user.setRole(userPostDto.getRole());
        user.setCity(userPostDto.getCity());
        user.setCountry(userPostDto.getCountry());
        user.setEmail(userPostDto.getEmail());
        user.setPassword(userPostDto.getPassword());

        return user;

    }

//
//    @Override
//    public Post postDtoToPost(PostDto postDto) {
//
//        if(postDto == null){
//            return null;
//        }
//
//        Post post = new Post();
//        post.setPostTime(postDto.getPostTime());
//        post.setPostDate(postDto.getPostDate());
//        post.setUser(userGetDtoToUser(postDto.getUser()));
//        post.setPostId(postDto.getPostId());
//        post.setNumber(postDto.getNumber());
//        post.setLastName(postDto.getLastName());
//        post.setFirstName(postDto.getFirstName());
//        post.setPostCode(postDto.getPostCode());
//        post.setPostImageUrl(postDto.getPostImageUrl());
//        post.setInSale(postDto.isInSale());
//        post.setPostAdvertIndex(postDto.getPostAdvertIndex());
//        post.setPostDescription(postDto.getPostDescription());
//        post.setPostCategories(postDto.getPostCategories());
//        post.setPostTitle(postDto.getPostTitle());
//        post.setAddress(postDto.getAddress());
//        post.setPostQuantity(postDto.getPostQuantity());
//        post.setSubPostCategory(postDto.getSubPostCategory());
//        post.setPostSlug(postDto.getPostSlug());
//
//        return post;
//    }
}
