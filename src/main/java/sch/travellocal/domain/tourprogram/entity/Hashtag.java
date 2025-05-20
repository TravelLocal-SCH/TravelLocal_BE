package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hashtag")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "hashtag")
    private Set<TourProgramHashtag> tourProgramHashtags = new HashSet<>();
}
