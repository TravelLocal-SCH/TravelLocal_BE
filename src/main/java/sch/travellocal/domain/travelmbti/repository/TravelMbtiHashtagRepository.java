package sch.travellocal.domain.travelmbti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.travelmbti.entity.TravelMbtiHashtag;

@Repository
public interface TravelMbtiHashtagRepository extends JpaRepository<TravelMbtiHashtag, Long> {
}
