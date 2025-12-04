package dev.onebite.admin.application.service;

import dev.onebite.admin.domain.CategoryGroup;
import dev.onebite.admin.infra.repository.CategoryGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CategoryGroupServiceTest {

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Test
    @DisplayName("카테고리 그룹 생성시 정렬 순서가 최대값인지 테스트한다.")
    void checkMaxSort() {
        //given
        CategoryGroup categoryGroup1 = CategoryGroup.of("code", "label", "iconUrl", 1);
        CategoryGroup categoryGroup2 = CategoryGroup.of("code1", "label1", "iconUrl1", 1);
        //when
        categoryGroupRepository.save(categoryGroup1);
        categoryGroupRepository.save(categoryGroup2);

        categoryGroupService.create();

        CategoryGroup categoryGroup = categoryGroupRepository.findById(3L).get();
        Integer maxDisplayOrder = categoryGroupRepository.findMaxDisplayOrder().get();

        //then
        assertThat(categoryGroup.getDisplayOrder()).isEqualTo(maxDisplayOrder);
    }

    @Test
    @DisplayName("카테고리 그룹 생성시 코드가 중복되는지 테스트한다.")
    void duplicatedCode() {
        //given
        CategoryGroup categoryGroup1 = CategoryGroup.of("code", "label", "iconUrl", 1);
        CategoryGroup categoryGroup2 = CategoryGroup.of("code", "label", "iconUrl", 2);
        //when
        //then
    }
}