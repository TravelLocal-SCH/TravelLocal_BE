package sch.travellocal.domain.tourprogram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.tourprogram.entity.TourProgram;

@Repository
public interface TourProgramRepository extends JpaRepository<TourProgram, Long>, JpaSpecificationExecutor<TourProgram> {

}
