package ecommerce.web.app.domain.post.service;

import ecommerce.web.app.domain.post.repository.ImageUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageUploadService {

    @Autowired
    ImageUploadRepository imageUploadRepository;

    public void deleteImageUploaded(long id){
        imageUploadRepository.deleteById(id);
    }
}
