package ecommerce.web.app.model.mapper;

import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.model.dto.PostDto;
import ecommerce.web.app.model.dto.UserGetDto;
import ecommerce.web.app.model.dto.UserPostDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {

    UserGetDto userToUserGetDto(User user);

    User userPostDtoToUser(UserPostDto userPostDto);

//    Post postDtoToPost(PostDto postDto);

    User userGetDtoToUser(UserGetDto userGetDto);
}
