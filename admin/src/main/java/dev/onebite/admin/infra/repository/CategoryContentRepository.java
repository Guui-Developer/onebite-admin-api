package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.CategoryContent;
import dev.onebite.admin.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryContentRepository extends JpaRepository<CategoryContent, Long> {
    void deleteByContentId(Content content);

    @Query("SELECT CASE WHEN COUNT(cc) > 0 THEN true ELSE false END " +
            "FROM CategoryContent cc " +
            "WHERE cc.categoryId.id IN :categoryIds")
    boolean existsByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
}
