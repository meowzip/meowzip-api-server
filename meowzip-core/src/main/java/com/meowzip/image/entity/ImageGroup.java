package com.meowzip.image.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "image_group")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImageGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "imageGroup", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    public boolean isChanged(List<MultipartFile> images) {
        if (images == null && this.images != null) {
            return true;
        }

        if (images != null && this.images == null) {
            return true;
        }

        if (images.size() != this.images.size()) {
            return true;
        }

        for (int i = 0; i < images.size(); i++) {
            long size = this.images.get(i).getSize();
            long originalSize = images.get(i).getSize();

            if (size != originalSize) {
                return true;
            }
        }

        return false;
    }
}
