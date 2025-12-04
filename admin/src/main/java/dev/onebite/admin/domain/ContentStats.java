package dev.onebite.admin.domain;

import dev.onebite.admin.domain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "content_stats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentStats extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content contentId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "views", columnDefinition = "int4 default 0")
    private Integer views;

    @Column(name = "bookmarks", columnDefinition = "int4 default 0")
    private Integer bookmarks;

}
