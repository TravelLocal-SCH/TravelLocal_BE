package sch.travellocal.domain.tourprogram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.entity.TourProgramSchedule;

import java.util.List;

@Repository
public interface TourProgramScheduleRepository extends JpaRepository<TourProgramSchedule, Long> {

    List<TourProgramSchedule> findAllByTourProgram(TourProgram tourProgram);

    void deleteAllByTourProgram(TourProgram tourProgram);
}
