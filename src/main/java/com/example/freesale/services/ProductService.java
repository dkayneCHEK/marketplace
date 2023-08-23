package com.example.freesale.services;

import com.example.freesale.entities.PhotoEntity;
import com.example.freesale.entities.ProductEntity;
import com.example.freesale.entities.UserEntity;
import com.example.freesale.fabrics.PhotoFabric;
import com.example.freesale.repositories.ProductRepository;
import com.example.freesale.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PhotoFabric photoFabric;

    public List<ProductEntity> getProducts(String title) {
        if (title != null) return productRepository.findAllByTitle(title);
        return productRepository.findAll();
    }

    public void save(Principal principal, ProductEntity product, MultipartFile ... files) throws IOException {
        product.setUser(getUserByPrincipal(principal));

        boolean isPreview = true;
        for (MultipartFile file :
             files) {
            PhotoEntity photo = photoFabric.toPhotoEntity(file);
            if (isPreview) {
                photo.setPreview(true);
                isPreview = false;
            }
            product.addPhoto(photo);
        }

        log.info("Saving Product: Title: {}, Author email: {}", product.getTitle(), product.getUser().getEmail());
        ProductEntity productFromDB = productRepository.save(product);
        productFromDB.setPreviewPhotoId(productFromDB.getPhotos().get(0).getId());
        productRepository.save(product);
    }

    public UserEntity getUserByPrincipal(Principal principal) {
        if (principal == null)
            return new UserEntity();
        return userRepository.findByEmail(principal.getName());
    }

    public void delete(UserEntity user, Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            if (product.getUser().getId().equals(user.getId())) {
                productRepository.delete(product);
                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }
    }

    public ProductEntity getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
