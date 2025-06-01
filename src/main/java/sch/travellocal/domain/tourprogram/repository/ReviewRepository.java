package sch.travellocal.domain.tourprogram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.tourprogram.dto.ReviewDto;
import sch.travellocal.domain.tourprogram.dto.UserReviewDto;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.entity.Review;
import sch.travellocal.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT new sch.travellocal.domain.tourprogram.dto.ReviewDto(" +
            "r.user.id, r.user.name, r.id, r.rating, r.content, r.createdAt, r.updatedAt) " +
            "FROM Review r " +
            "WHERE r.tourProgram.id = :tourProgramId")
    Page<ReviewDto> findReviewsByTourProgramId(@Param("tourProgramId") Long tourProgramId, Pageable pageable);

    @Query("SELECT new sch.travellocal.domain.tourprogram.dto.UserReviewDto(" +
            "r.tourProgram.id, r.tourProgram.title, r.id, r.rating, r.content, r.createdAt, r.updatedAt) " +
            "FROM Review r " +
            "WHERE r.user.id = :userId")
    Page<UserReviewDto> findReviewsByUserId(@Param("userId") Long userId, Pageable pageable);

    boolean existsByTourProgramAndUser(TourProgram tourProgram, User user);

    Optional<Review> findByTourProgramIdAndUserId(Long tourProgramId, Long userId);

    List<Review> findAllByTourProgramId(Long tourProgramId);
}
