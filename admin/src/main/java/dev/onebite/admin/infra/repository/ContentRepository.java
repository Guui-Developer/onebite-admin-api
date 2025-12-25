package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.Content;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<@NonNull Content, @NonNull Long> {
    List<Content> findByIdIn(List<Long> ids);

//    @Query(
//            value = "SELECT new dev.onebite.admin.application.dto.content.ContentDto(" +
//                    "c.id, c.type, c.title, c.code, c.description, c.answer, " +
//                    "c.before, c.after, c.feedback, c.image, c.questionText, " +
//                    "c.views, c.bookmarks, c.isActive, c.createdAt, c.updatedAt) " +
//                    "FROM Content c",
//            countQuery = "SELECT count(c) FROM Content c"
//    )
//    Page<@NonNull ContentDto> findAllDto(Pageable pageable);
//
//
//    @Query(
//            value = "SELECT new dev.onebite.admin.application.dto.content.ContentDto(" +
//                    "c.id, c.type, c.title, c.code, c.description, c.answer, " +
//                    "c.before, c.after, c.feedback, c.image, c.questionText, " +
//                    "c.views, c.bookmarks, c.isActive, c.createdAt, c.updatedAt) " +
//                    "FROM Content c " +
//                    "WHERE c.title LIKE :keyword",
//            countQuery = "SELECT count(c) FROM Content c WHERE c.title LIKE :keyword"
//    )
//    Page<@NonNull ContentDto> searchDto(@Param("keyword") String keyword, Pageable pageable);


    Page<Content> findAll(Pageable pageable);

    Page<Content> findByTitleContaining(String keyword, Pageable pageable);
}
