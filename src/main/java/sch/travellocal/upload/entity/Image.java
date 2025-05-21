package sch.travellocal.upload.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import sch.travellocal.upload.enums.ImageTargetType;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int sequence;

    @Column(name = "target_type", nullable = false)
    private ImageTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Image(String imageUrl, int sequence, ImageTargetType targetType, Long targetId) {
        this.imageUrl = imageUrl;
        this.sequence = sequence;
        this.targetType = targetType;
        this.targetId = targetId;
    }
}
