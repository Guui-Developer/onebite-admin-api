package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {

    @Query("SELECT MAX(cg.displayOrder) FROM CategoryGroup cg")
    Optional<Integer> findMaxDisplayOrder();

}
