package sch.travellocal.domain.place.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.place.entity.Place;
import sch.travellocal.domain.place.entity.PlaceUserPermission;
import sch.travellocal.domain.user.entity.User;

@Repository
public interface PlaceUserPermissionRepository extends JpaRepository<PlaceUserPermission, Long> {

    boolean existsByUserAndPlace(User user, Place place);
    // 특정 장소에 대해 여러 유저의 권한 유무를 한 번에 조회 (IN 절 사용)
    @Query("SELECT p.user.id FROM PlaceUserPermission p WHERE p.place.id = :placeId AND p.user.id IN :userIds")
    List<Long> findUserIdsWithPermission(@Param("placeId") Long placeId, @Param("userIds") List<Long> userIds);
}
