//package sch.travellocal;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.persistence.EntityManager;
//import jakarta.transaction.Transactional;
//import org.hamcrest.Matchers;
//import org.hibernate.Session;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import sch.travellocal.domain.reservation.dto.ReservationRequestDTO;
//import sch.travellocal.domain.tourprogram.entity.TourProgram;
//import sch.travellocal.domain.tourprogram.repository.TourProgramRepository;
//import sch.travellocal.domain.user.entity.User;
//import sch.travellocal.domain.user.enums.UserRole;
//import sch.travellocal.domain.user.repository.UserRepository;
//
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class reservation_payment_test {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TourProgramRepository tourProgramRepository;
//
//    private User savedUser;
//
//
//    private TourProgram savedProgram;
//    @Autowired
//    private EntityManager entityManager;
//
//
//    @BeforeEach
//    void setUp() {
//        User user = User.builder()
//                .username("test_user")
//                .name("테스트 사용자")
//                .email("test@example.com")
//                .gender("MALE")
//                .birthYear("1990")
//                .mobile("01012345678")
//                .role(UserRole.GUIDE_CONSUMER)
//                .protectNumber("0000")
//                .build();
//
//        // 수동으로 ID 3 지정
//        try {
//            Field idField = User.class.getDeclaredField("id");
//            idField.setAccessible(true);
//            idField.set(user, 3L);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        assertNull(user.getId()); // ID가 없어야 persist 가능
//        entityManager.persist(user); // ✅
//
//
//        entityManager.flush();
//        savedUser = user;
//
//        TourProgram program = TourProgram.builder()
//                .title("테스트 투어")
//                .description("테스트용 투어 설명")
//                .guidePrice(100)
//                .region("서울")
//                .user(savedUser)
//                .build();
//        savedProgram = tourProgramRepository.save(program);
//    }
//
//
//
//    @Test
//    void 예약_및_결제_성공() throws Exception {
//        // given
//        ReservationRequestDTO dto = ReservationRequestDTO.builder()
//                .tourProgramId(savedProgram.getId())
//                .numOfPeople(2)
//                .guideStartDate(LocalDateTime.of(2025, 6, 1, 9, 0))
//                .guideEndDate(LocalDateTime.of(2025, 6, 5, 18, 0))
//                .totalPrice(50000)
//                .paymentMethod("CARD")
//                .build();
//
//        String jsonBody = objectMapper.writeValueAsString(dto);
//
//        // when & then
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("impUid", "test-imp-uid-123")
//                        .param("merchantUid", "test-merchant-uid-456")
//                        .param("userId", "3") // ✅ 고정된 ID 사용
//                        .content(jsonBody))
//                .andExpect(status().isOk())
//                .andExpect(content().string(Matchers.containsString("예약 및 결제 완료")));
//    }
//}
