package sch.travellocal.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sch.travellocal.domain.point.entity.PointHistory;
import sch.travellocal.domain.point.enums.PointTransactionActionType;
import sch.travellocal.domain.point.enums.PointTransactionSubjectType;
import sch.travellocal.domain.user.entity.User;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    boolean existsByActionTypeAndSubjectTypeAndTargetIdAndUser(
            PointTransactionActionType actionType,
            PointTransactionSubjectType subjectType,
            Long targetId,
            User user
    );


}
