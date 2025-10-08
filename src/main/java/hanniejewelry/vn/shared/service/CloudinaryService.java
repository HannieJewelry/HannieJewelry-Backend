package hanniejewelry.vn.shared.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String filename) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("public_id", filename, "resource_type", "auto"));
        return (String) uploadResult.get("secure_url");
    }
    public String uploadImageFromUrl(String imageUrl, String filename) throws IOException {
        try (InputStream in = new URL(imageUrl).openStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int n;
            while ((n = in.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            byte[] imageBytes = baos.toByteArray();
            Map uploadResult = cloudinary.uploader().upload(imageBytes,
                    ObjectUtils.asMap("public_id", filename, "resource_type", "auto"));
            return (String) uploadResult.get("secure_url");
        }
    }

}
