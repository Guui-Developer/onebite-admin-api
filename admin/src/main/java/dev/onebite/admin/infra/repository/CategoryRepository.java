package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
