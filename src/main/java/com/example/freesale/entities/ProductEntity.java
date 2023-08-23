package com.example.freesale.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "city")
    private String city;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
    mappedBy = "product")
    private List<PhotoEntity> photos = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private UserEntity user;
    private Long previewPhotoId;

    private LocalDateTime createdAt;

    @PrePersist
    private void init() {
        createdAt = LocalDateTime.now();
    }

    public void addPhoto(PhotoEntity photoEntity) {
        photoEntity.setProduct(this);
        photos.add(photoEntity);
    }
}
