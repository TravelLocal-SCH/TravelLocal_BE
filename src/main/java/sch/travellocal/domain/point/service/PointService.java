package sch.travellocal.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.point.dto.request.PointTransactionRequestDto;
import sch.travellocal.domain.point.dto.response.PointBalanceResponseDto;
import sch.travellocal.domain.point.entity.PointHistory;
import sch.travellocal.domain.point.entity.UserPoint;
import sch.travellocal.domain.point.enums.PointTransactionActionType;
import sch.travellocal.domain.point.repository.PointHistoryRepository;
import sch.travellocal.domain.point.repository.UserPointRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.service.SecurityUserService;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final SecurityUserService securityUserService;

    public PointBalanceResponseDto earnPoints(PointTransactionRequestDto requestDto) {

        if (!PointTransactionActionType.EARN.equals(requestDto.getActionType())) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        User user = securityUserService.getUserByJwt();
        // 비관적 락을 통해 UserPoint 조회 및 수정
        UserPoint userPoint = userPointRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "사용자 포인트 정보를 찾을 수 없습니다."));

        userPoint.updateBalance(requestDto.getAmount());

        PointHistory history = PointHistory.builder()
                .actionType(requestDto.getActionType())
                .subjectType(requestDto.getActionSubject())
                .pointAmount(requestDto.getAmount())
                .remainPointAfter(userPoint.getPoint())
                .targetId(requestDto.getTargetId())
                .user(user)
                .build();
        pointHistoryRepository.save(history);

        return PointBalanceResponseDto.builder().balance(userPoint.getPoint()).build();
    }

    public PointBalanceResponseDto usePoints(PointTransactionRequestDto requestDto) {

        if (!PointTransactionActionType.USE.equals(requestDto.getActionType())) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        User user = securityUserService.getUserByJwt();
        // 비관적 락을 통해 UserPoint 조회 및 수정
        UserPoint userPoint = userPointRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "사용자 포인트 정보를 찾을 수 없습니다."));

        userPoint.updateBalance(-requestDto.getAmount());

        PointHistory history = PointHistory.builder()
                .actionType(requestDto.getActionType())
                .subjectType(requestDto.getActionSubject())
                .pointAmount(requestDto.getAmount())
                .remainPointAfter(userPoint.getPoint())
                .targetId(requestDto.getTargetId())
                .user(user)
                .build();
        pointHistoryRepository.save(history);

        return PointBalanceResponseDto.builder().balance(userPoint.getPoint()).build();
    }

    public PointBalanceResponseDto getPointBalance(String username) {

        UserPoint userPoint = userPointRepository.findByUser_Username(username)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "사용자 포인트 정보를 찾을 수 없습니다."));

        return PointBalanceResponseDto.builder().balance(userPoint.getPoint()).build();
    }

    public void createInitialPoints(User user) {
        userPointRepository.findByUser(user)
                .ifPresentOrElse(
                        // 이미 포인트가 있는 경우
                        userPoint -> {},
                        // 포인트가 없을 때만 저장
                        () -> {
                            userPointRepository.save(UserPoint.builder()
                                    .point(3000L)
                                    .user(user)
                                    .build());
                        }
                );
    }


}
