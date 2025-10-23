package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import sch.travellocal.common.entity.BaseTimeEntity;
import sch.travellocal.domain.user.entity.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tour_program")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourProgram extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    private String description;

    @Column(name = "guide_price", nullable = false)
    private int guidePrice;

    @Column(nullable = false)
    private String region;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    // CascadeType.PERSIST
    // @Builder는 기본 초기화를 무시하기에 객체를 생성하면 tourProgramHashtags는 null로 설정되고, add() 호출 시 NPE 발생
    @Builder.Default
    @OneToMany(mappedBy = "tourProgram")
    private Set<TourProgramHashtag> tourProgramHashtags = new HashSet<>();

    public void addHashtag(Hashtag hashtag) {

        TourProgramHashtag tph = TourProgramHashtag.builder()
                .tourProgram(this)
                .hashtag(hashtag)
                .build();
        this.tourProgramHashtags.add(tph);
        hashtag.getTourProgramHashtags().add(tph);
    }

    // case "reviewDesc" -> Sort.by("reviewCount").descending(); 정렬 조건에 필요
    @Formula("(SELECT c.review_count FROM tour_program_count c WHERE c.tour_program_id = id)")
    private Integer reviewCount;

    @Formula("(SELECT c.wishlist_count FROM tour_program_count c WHERE c.tour_program_id = id)")
    private Integer wishlistCount;
}
