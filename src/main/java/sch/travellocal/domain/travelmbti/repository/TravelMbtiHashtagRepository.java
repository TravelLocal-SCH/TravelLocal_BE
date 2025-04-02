package sch.travellocal.domain.travelmbti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.travelmbti.entity.TravelMbti;
import sch.travellocal.domain.travelmbti.entity.TravelMbtiHashtag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelMbtiHashtagRepository extends JpaRepository<TravelMbtiHashtag, Long> {

    Optional<List<TravelMbtiHashtag>> findAllByTravelMbti(TravelMbti travelMbti);
}
