package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 생성이 올바르게 되는지 테스트한다.")
    void createCategory() {
        //given
        Category category = Category.of("code", "label", null, null, null, 1);

        //when
        categoryRepository.save(category);

        //then
        List<Category> all = categoryRepository.findAll();

        for (Category category1 : all) {
            assertThat(category1.getCode()).isEqualTo(category.getCode());
            assertThat(category1.getLabel()).isEqualTo(category.getLabel());
            assertThat(category1.getCategoryGroup()).isEqualTo(category.getCategoryGroup());
        }
    }

    @Test
    @DisplayName("카테고리 생성시 정렬 순서가 최대값인지 테스트한다.")
    void checkMaxSort() {
        //given
        //when
        //then
    }


}