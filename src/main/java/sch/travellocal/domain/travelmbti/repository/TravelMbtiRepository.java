package sch.travellocal.domain.travelmbti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.travelmbti.entity.TravelMbti;
import sch.travellocal.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelMbtiRepository extends JpaRepository<TravelMbti, Long> {

    Optional<List<TravelMbti>> findAllByUserOrderByCreatedAtDesc(User user);
}
