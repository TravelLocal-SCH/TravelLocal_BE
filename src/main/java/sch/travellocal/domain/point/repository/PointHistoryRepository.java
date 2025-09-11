package sch.travellocal.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sch.travellocal.domain.point.entity.PointHistory;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
