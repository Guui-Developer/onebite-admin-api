package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.Category;
import dev.onebite.admin.domain.CategoryGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Test
    @DisplayName("카테고리 저장이 올바르게 동작하는지 확인한다.")
    void createCategory() {

        CategoryGroup categoryGroup = CategoryGroup.of("code", "label", "iconUrl", 1);
        categoryGroupRepository.save(categoryGroup);

        CategoryGroup group = categoryGroupRepository.findByCode("code").get();


        //given
        Category category1 = Category.of("code1", "label1", group, "url1", "dis1", 1);
        Category category2 = Category.of("code2", "label2", group, "url2", "dis2", 2);
        Category category3 = Category.of("code3", "label3", group, "url3", "dis3", 3);

        //when
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        //then
        List<Category> all = categoryRepository.findAll();

        assertThat(all).hasSize(3)
                .extracting("code", "label", "displayOrder")
                .containsExactlyInAnyOrder(
                        tuple("code1","label1",1),
                        tuple("code2","label2",2),
                        tuple("code3","label3",3)
                );
    }

}