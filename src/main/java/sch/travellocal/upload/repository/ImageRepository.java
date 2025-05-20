package sch.travellocal.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.upload.entity.Image;
import sch.travellocal.upload.enums.ImageTargetType;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByTargetTypeAndTargetIdOrderBySequenceAsc(ImageTargetType targetType, Long targetId);
}
