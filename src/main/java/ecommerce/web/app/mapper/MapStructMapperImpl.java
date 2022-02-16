package ecommerce.web.app.mapper;

import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.dto.UserGetDto;
import ecommerce.web.app.domain.model.dto.UserPostDto;
import org.springframework.stereotype.Component;

@Component
public class MapStructMapperImpl implements MapStructMapper{


    @Override
    public UserGetDto userToUserGetDto(User user) {
        if (user == null) {
            return null;
        }
        UserGetDto userGetDto = new UserGetDto();

        userGetDto.setId(user.getId());
        userGetDto.setAddress(user.getAddress());
        userGetDto.setFirstName(user.getFirstName());
        userGetDto.setNumber(user.getPhoneNumber());
        userGetDto.setUsername(user.getUsername());
        userGetDto.setLastName(userGetDto.getLastName());
        userGetDto.setRole(userGetDto.getRole());

        return userGetDto;

    }
    @Override
    public User userGetDtoToUser(UserGetDto userGetDto) {
        if (userGetDto == null) {
            return null;
        }
        User user = new User();

        user.setId(userGetDto.getId());
        user.setAddress(userGetDto.getAddress());
        user.setFirstName(userGetDto.getFirstName());
        user.setPhoneNumber(userGetDto.getNumber());
        user.setUsername(userGetDto.getUsername());
        user.setLastName(userGetDto.getLastName());
        user.setRole(userGetDto.getRole());

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
