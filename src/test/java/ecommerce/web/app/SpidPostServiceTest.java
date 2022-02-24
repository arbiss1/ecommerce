//package ecommerce.web.app;
//
//import ecommerce.web.app.domain.enums.AdvertIndex;
//import ecommerce.web.app.domain.enums.Currency;
//import ecommerce.web.app.domain.enums.PostStatus;
//import ecommerce.web.app.domain.model.ImageUpload;
//import ecommerce.web.app.domain.model.Post;
//import ecommerce.web.app.domain.model.User;
//import ecommerce.web.app.domain.model.mapper.MapStructMapperImpl;
//import ecommerce.web.app.domain.repository.PostRepository;
//import ecommerce.web.app.i18nConfig.MessageByLocaleImpl;
//import lombok.AllArgsConstructor;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.util.Assert;
//
//@AllArgsConstructor
//@ExtendWith(MockitoExtension.class)
//public class SpidPostServiceTest {
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Nested
//    public class EcommerceTest {
//        @DisplayName("Test if a post is saved successfully")
//        @Test
//        public void shouldSavePostWhenRequested(Post post) {
//
//            post = generatePost();
//
//            Mockito.when(postRepository.save(post)).thenReturn(post);
//            Post savedPost = postRepository.save(post);
//            Assertions.assertEquals(savedPost, "Post must not be null ");
//        }
//
//        public Post generatePost() {
//            Post newPost = new Post();
//            newPost.setDescription("");
//            newPost.setTitle("");
//            newPost.setSubcategory("");
//            newPost.setCategory("");
//            newPost.setSlug("");
//            newPost.setAddress("");
//            newPost.setPhoneNumber("");
//            newPost.setFirstName("");
//            newPost.setLastName("");
//            newPost.setCurrency(Currency.ALL);
//            newPost.setInSale(true);
//            newPost.setStatus(PostStatus.PENDING);
//            newPost.setUser(new User());
//
//            return newPost;
//        }
//    }
//}
//
