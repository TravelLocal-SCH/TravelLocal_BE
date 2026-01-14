package sch.travellocal.domain.place.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.place.dto.request.PlaceReviewDto;
import sch.travellocal.domain.place.entity.Place;
import sch.travellocal.domain.place.entity.PlaceReview;
import sch.travellocal.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {

    @Query("SELECT new sch.travellocal.domain.place.dto.request.PlaceReviewDto(" +
            "r.user.id, r.user.name, r.id, r.rating, r.content, r.createdAt, r.updatedAt) " +
            "FROM PlaceReview r " +
            "WHERE r.place.id = :placeId")
    Page<PlaceReviewDto> findReviewsByPlaceId(@Param("placeId") Long placeId, Pageable pageable);

    Optional<PlaceReview> findByIdAndPlaceAndUserId(Long placeReviewId, Place place, Long userId);

    boolean existsByUserIdAndPlace(Long user_id, Place place);

    Page<PlaceReview> findByUser(User user, Pageable pageable);
}
