package ecommerce.web.app.service;

import ecommerce.web.app.entities.ImageUpload;
import ecommerce.web.app.entities.Post;
import ecommerce.web.app.exceptions.ImageCustomException;
import ecommerce.web.app.repository.ImageUploadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    public final ImageUploadRepository imageUploadRepository;
    public final MessageSource messageSource;
    private final Locale locale = Locale.ENGLISH;

    public void postImageUpload(List<String> imageUploads, Post post) throws ImageCustomException {
        if (isValidBase64String(imageUploads)) {
            for (String postsImageUrl : imageUploads) {
                ImageUpload imageUpload = new ImageUpload();
                imageUpload.setPost(post);
                imageUpload.setProfileImage(postsImageUrl);
                imageUpload.setCreatedAt(LocalDateTime.now());
                imageUpload.setModifiedAt(LocalDateTime.now());
                imageUpload.setCreatedBy(post.getUser().getUsername());
                imageUpload.setModifiedBy(post.getUser().getUsername());
                imageUploadRepository.save(imageUpload);
            }
        } else {
            throw new ImageCustomException(messageSource.getMessage("error.409.imageNotBase64", null, locale));
        }
    }

    public List<ImageUpload> getImages(Post post){
        return imageUploadRepository.findAllByPost(post);
    }

    @Transactional
    public void deleteImages(Post post) {
        imageUploadRepository.deleteAllByPost(post);
    }

    public static boolean isValidBase64String(List<String> inputs) {
            for (String input : inputs) {
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(input);
                    String reencodedString = Base64.getEncoder().encodeToString(decodedBytes);
                    return input.equals(reencodedString);
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        return false;
    }
}

