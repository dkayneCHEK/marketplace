package com.example.freesale.cotrollers;

import com.example.freesale.entities.PhotoEntity;
import com.example.freesale.repositories.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;

@Controller
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoRepository photoRepository;

    @GetMapping("/photos/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable("id") Long id) {
        PhotoEntity photo = photoRepository.findById(id).orElse(null);

        return ResponseEntity.ok()
                .header("fileName", photo.getOriginalFileName())
                .contentType(MediaType.valueOf(photo.getContentType()))
                .contentLength(photo.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(photo.getBytes())));
    }
}
