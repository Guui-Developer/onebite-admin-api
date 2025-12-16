package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.CategoryGroup;
import dev.onebite.admin.persentation.dto.CategoryGroupDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryGroupRepository extends JpaRepository<@NonNull CategoryGroup, @NonNull Long> {

    @Query("SELECT MAX(cg.displayOrder) FROM CategoryGroup cg")
    Optional<Integer> findMaxDisplayOrder();

    boolean existsByCode(String code);

    Optional<CategoryGroup> findByCode(String code);

    List<CategoryGroup> findByIdIn(Collection<Long> ids);


    @Query(
            value = "SELECT new dev.onebite.admin.persentation.dto.CategoryGroupDto(c.id, c.code, c.label, c.iconUrl, c.displayOrder) " +
                    "FROM CategoryGroup c",
            countQuery = "SELECT count(c) FROM CategoryGroup c"
    )
    Page<@NonNull CategoryGroupDto> findAllDto(Pageable pageable);


    @Query(
            value = "SELECT new dev.onebite.admin.persentation.dto.CategoryGroupDto(c.id, c.code, c.label, c.iconUrl, c.displayOrder) " +
                    "FROM CategoryGroup c " +
                    "WHERE c.label LIKE :keyword",
            countQuery = "SELECT count(c) FROM CategoryGroup c WHERE c.label LIKE :keyword"
    )
    Page<@NonNull CategoryGroupDto> searchDto(@Param("keyword") String keyword, Pageable pageable);

}
