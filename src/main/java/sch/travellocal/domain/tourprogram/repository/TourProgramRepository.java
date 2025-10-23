package sch.travellocal.domain.tourprogram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.user.entity.User;

@Repository
public interface TourProgramRepository extends JpaRepository<TourProgram, Long>, JpaSpecificationExecutor<TourProgram> {

    static Page<TourProgram> findByUser(User user, Pageable pageable) {
        return null;
    }
}
