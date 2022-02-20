package ecommerce.web.app.mapper;

import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.dto.UserLoginRequest;
import ecommerce.web.app.domain.model.dto.UserPostDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {


    User userPostDtoToUser(UserPostDto userPostDto);

//    Post postDtoToPost(PostDto postDto);

    User dtoToUser(UserLoginRequest userLoginRequest);
}
