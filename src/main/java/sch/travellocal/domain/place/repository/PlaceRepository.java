package sch.travellocal.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.place.entity.Place;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByGooglePlaceId(String googlePlaceId);

    boolean existsByGooglePlaceId(String googlePlaceId);
}
