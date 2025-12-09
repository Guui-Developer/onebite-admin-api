package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.CategoryGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@ActiveProfiles("test")
class CategoryGroupRepositoryTest {

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Test
    @DisplayName("카테고리 그룹 생성이 올바르게 되는지 테스트한다.")
    void createCategoryGroup() {
        //given
        CategoryGroup categoryGroup1 = CategoryGroup.of("code1", "label1", "iconUrl1", 1);
        CategoryGroup categoryGroup2 = CategoryGroup.of("code2", "label2", "iconUrl2", 2);
        CategoryGroup categoryGroup3 = CategoryGroup.of("code3", "label3", "iconUrl3", 3);

        //when
        categoryGroupRepository.save(categoryGroup1);
        categoryGroupRepository.save(categoryGroup2);
        categoryGroupRepository.save(categoryGroup3);

        List<CategoryGroup> groups = categoryGroupRepository.findAll();
        Assertions.assertThat(groups).hasSize(3)
                .extracting("code","label","iconUrl","displayOrder")
                .containsExactlyInAnyOrder(
                        tuple("code1", "label1", "iconUrl1", 1),
                        tuple("code2", "label2", "iconUrl2", 2),
                        tuple("code3", "label3", "iconUrl3", 3)
                );


    }


}