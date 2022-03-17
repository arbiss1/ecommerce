package ecommerce.web.app.configs.mapper;

import ecommerce.web.app.entity.Post;
import ecommerce.web.app.entity.User;
import ecommerce.web.app.domain.model.PostRequest;
import ecommerce.web.app.domain.model.UserRegisterRequest;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {


    User userPostDtoToUser(UserRegisterRequest userRegisterRequest);

    Post postDtoToPost (PostRequest postRequest);
}
