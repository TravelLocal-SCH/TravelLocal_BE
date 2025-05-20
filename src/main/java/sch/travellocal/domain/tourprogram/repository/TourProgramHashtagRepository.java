package sch.travellocal.domain.tourprogram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.entity.TourProgramHashtag;

@Repository
public interface TourProgramHashtagRepository extends JpaRepository<TourProgramHashtag, Long> {

    void deleteAllByTourProgram(TourProgram tourProgram);
}
