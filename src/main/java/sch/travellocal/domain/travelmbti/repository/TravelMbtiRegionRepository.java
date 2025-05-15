package sch.travellocal.domain.travelmbti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.travelmbti.entity.TravelMbti;
import sch.travellocal.domain.travelmbti.entity.TravelMbtiRegion;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelMbtiRegionRepository extends JpaRepository<TravelMbtiRegion, Long> {

    Optional<List<TravelMbtiRegion>> findAllByTravelMbti(TravelMbti travelMbti);
}
