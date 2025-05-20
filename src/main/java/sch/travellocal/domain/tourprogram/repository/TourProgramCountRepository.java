package sch.travellocal.domain.tourprogram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.entity.TourProgramCount;

import java.util.Optional;

@Repository
public interface TourProgramCountRepository extends JpaRepository<TourProgramCount, Long> {

    Optional<TourProgramCount> findByTourProgramId(Long tourProgramId);

    void deleteByTourProgram(TourProgram tourProgram);
}
