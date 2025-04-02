package sch.travellocal.domain.travelmbti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.travelmbti.entity.TravelMbtiRegion;

@Repository
public interface TravelMbtiRegionRepository extends JpaRepository<TravelMbtiRegion, Long> {
}
