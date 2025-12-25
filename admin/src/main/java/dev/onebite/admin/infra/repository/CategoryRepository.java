package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.Category;
import dev.onebite.admin.domain.CategoryGroup;
import dev.onebite.admin.persentation.dto.CategoryDto;
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
public interface CategoryRepository extends JpaRepository<@NonNull Category, @NonNull Long> {
    Optional<Category> findByCode(String code);

    @Query("SELECT MAX(cg.displayOrder) FROM Category cg")
    Optional<Integer> findMaxDisplayOrder();

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    List<Category> findByIdIn(Collection<Long> ids);

    @Query(
            value = "SELECT new dev.onebite.admin.persentation.dto.CategoryDto(c.id, c.code, c.label, c.categoryGroup.id,c.iconUrl, c.displayOrder) " +
                    "FROM Category c",
            countQuery = "SELECT count(c) FROM Category c"
    )
    Page<@NonNull CategoryDto> findAllDto(Pageable pageable);


    @Query(
            value = "SELECT new dev.onebite.admin.persentation.dto.CategoryDto(c.id, c.code, c.label, c.categoryGroup.id,c.iconUrl, c.displayOrder) " +
                    "FROM Category c " +
                    "WHERE c.label LIKE :keyword "
            ,
            countQuery = "SELECT count(c) FROM Category c WHERE c.label LIKE :keyword"
    )
    Page<@NonNull CategoryDto> searchDto(@Param("keyword") String keyword, Pageable pageable);

    List<Category> findByCodeIn(List<String> tags);

    boolean existsByCategoryGroupIdIn(List<Long> ids);

    @Query("SELECT COUNT(c) FROM Category c WHERE c.categoryGroup.id IN :categoryGroupIds")
    long countByCategoryGroupIds(@Param("categoryGroupIds") List<Long> categoryGroupIds);
}
