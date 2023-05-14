package ecommerce.web.app.service;

import ecommerce.web.app.repository.ImageUploadRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageUploadService {

    public final ImageUploadRepository imageUploadRepository;

    public ImageUploadService(ImageUploadRepository imageUploadRepository){
        this.imageUploadRepository = imageUploadRepository;
    }

    public void deleteImageUploaded(long id){
        imageUploadRepository.deleteById(id);
    }
}
