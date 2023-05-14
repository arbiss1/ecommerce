package ecommerce.web.app.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dxrixqpjg",
            "api_key", "966843127668939",
            "api_secret", "dqIiglHTAoRuYD2j887wmCk56vU"));

    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public Map uploader(Object file, Map map) throws IOException {
        return cloudinary.uploader().upload(file, map);
    }
}
