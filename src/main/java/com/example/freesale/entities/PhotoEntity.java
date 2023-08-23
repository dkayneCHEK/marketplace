package com.example.freesale.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photos")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "size")
    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "is_preview")
    private boolean isPreview;

    @Lob
    private byte[] bytes;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private ProductEntity product;
}
