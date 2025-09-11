package sch.travellocal.domain.tourprogram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.entity.TourProgramHashtag;

import java.util.List;

@Repository
public interface TourProgramHashtagRepository extends JpaRepository<TourProgramHashtag, Long> {

    void deleteAllByTourProgram(TourProgram tourProgram);

    @Query("""
       SELECT h.name
       FROM TourProgramHashtag tph
       JOIN tph.hashtag h
       WHERE tph.tourProgram.id = :programId
       """)
    List<String> findHashtagNamesByProgramId(Long programId);
}