package ecommerce.web.app.domain.model.mapper;

import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.dto.PostRequest;
import ecommerce.web.app.domain.model.dto.UserRegisterRequest;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {


    User userPostDtoToUser(UserRegisterRequest userRegisterRequest);

    Post postDtoToPost (PostRequest postRequest);
}
