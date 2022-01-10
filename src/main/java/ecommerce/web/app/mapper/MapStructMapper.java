package ecommerce.web.app.mapper;

import ecommerce.web.app.domain.user.model.User;
import ecommerce.web.app.dto.UserGetDto;
import ecommerce.web.app.dto.UserPostDto;
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
