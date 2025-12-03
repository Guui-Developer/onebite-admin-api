package dev.onebite.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "content_stats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentStats {

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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
