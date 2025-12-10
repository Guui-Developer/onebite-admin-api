package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.CategoryContent;
import dev.onebite.admin.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryContentRepository extends JpaRepository<CategoryContent, Long> {
    void deleteByContentId(Content content);
}
