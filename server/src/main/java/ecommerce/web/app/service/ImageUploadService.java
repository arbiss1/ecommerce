package ecommerce.web.app.service;

import ecommerce.web.app.entities.ImageUpload;
import ecommerce.web.app.entities.Post;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.repository.ImageUploadRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ImageUploadService {

    public final ImageUploadRepository imageUploadRepository;

    public ImageUploadService(ImageUploadRepository imageUploadRepository){
        this.imageUploadRepository = imageUploadRepository;
    }

    public void postImageUpload(List<String> imageUploads, Post post) throws PostCustomException {
        if(isValidBase64String(imageUploads)) {
            for (String postsImageUrl : imageUploads) {
                ImageUpload imageUpload = new ImageUpload();
                imageUpload.setPost(post);
                imageUpload.setProfileImage(postsImageUrl);
                imageUploadRepository.save(imageUpload);
            }
        } else {
            throw new PostCustomException("Image urls does not look like base64");
        }
    }

    public void deleteImageUploaded(String id){
        imageUploadRepository.deleteById(id);
    }

    public List<ImageUpload> getListsOfImageByPost(Post post){
        return imageUploadRepository.findAllByPost(post);
    }

    @Transactional
    public void deleteImages(Post post){
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
        return true;
    }
}

