package app.railway.up.fabriziodevback.fabriziodevback.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinaryConfig;

    public CloudinaryService(Cloudinary cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }

    public Map upload(String folder, MultipartFile multipartFile) throws IOException{

        File file = convert(multipartFile);

        Map config = new HashMap();
        config.put("upload_preset", "fabrizio_dev");
        config.put("resource_type", "auto");
        config.put("folder", "fabrizio-dev/" + folder);
//        config.put("public_id", name);

        Map result = cloudinaryConfig.uploader().upload(file, config);
        file.delete();
        return result;
    }

    public Map delete(String id) throws IOException {
        Map result = cloudinaryConfig.uploader().destroy(id, ObjectUtils.emptyMap());
        return result;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}
