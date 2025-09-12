package sch.travellocal.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.place.entity.Place;
import sch.travellocal.domain.place.entity.PlaceCount;

import java.util.Optional;

@Repository
public interface PlaceCountRepository extends JpaRepository<PlaceCount, Long> {

    Optional<PlaceCount> findByPlace(Place place);
}
