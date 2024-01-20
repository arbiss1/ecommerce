package ecommerce.web.app.service;

import ecommerce.web.app.entities.ImageUpload;
import ecommerce.web.app.entities.Post;
import ecommerce.web.app.repository.ImageUploadRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageUploadService {

    public final ImageUploadRepository imageUploadRepository;

    public ImageUploadService(ImageUploadRepository imageUploadRepository){
        this.imageUploadRepository = imageUploadRepository;
    }

    public void saveImage(ImageUpload imageUpload){
        imageUploadRepository.save(imageUpload);
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
}
