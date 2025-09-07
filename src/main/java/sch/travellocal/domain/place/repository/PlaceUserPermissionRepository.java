package sch.travellocal.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.place.entity.Place;
import sch.travellocal.domain.place.entity.PlaceUserPermission;
import sch.travellocal.domain.user.entity.User;

@Repository
public interface PlaceUserPermissionRepository extends JpaRepository<PlaceUserPermission, Long> {

    boolean existsByUserAndPlace(User user, Place place);
}
