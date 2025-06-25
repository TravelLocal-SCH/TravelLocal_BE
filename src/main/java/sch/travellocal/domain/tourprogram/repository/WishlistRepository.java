package sch.travellocal.domain.tourprogram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.tourprogram.dto.WishlistProgramDto;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.entity.Wishlist;
import sch.travellocal.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserAndTourProgram(User user, TourProgram tourProgram);

    @Query("SELECT new sch.travellocal.domain.tourprogram.dto.WishlistProgramDto(" +
            "p.id, p.thumbnailUrl, p.title, p.region, p.guidePrice, c.wishlistCount) " +
            "FROM Wishlist w " +
            "JOIN w.tourProgram p " +
            "JOIN TourProgramCount c On c.tourProgram.id = p.id " +
            "WHERE w.user.id = :userId")
    Page<WishlistProgramDto> findWishlistProgramsByUserId(@Param("userId") Long userId, Pageable pageable);

    void deleteByTourProgram(TourProgram existTourProgram);

    boolean existsByTourProgramAndUser(TourProgram tourProgram, User userByJwt);
}