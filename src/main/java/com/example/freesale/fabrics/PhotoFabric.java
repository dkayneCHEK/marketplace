package com.example.freesale.fabrics;

import com.example.freesale.entities.PhotoEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class PhotoFabric {
    public PhotoEntity toPhotoEntity(MultipartFile file) throws IOException {
        return PhotoEntity.builder()
                .name(file.getName())
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .bytes(file.getBytes())
                .build();
    }
}
