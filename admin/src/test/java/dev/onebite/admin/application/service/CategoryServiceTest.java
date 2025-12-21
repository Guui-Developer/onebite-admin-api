package dev.onebite.admin.application.service;

import dev.onebite.admin.domain.Category;
import dev.onebite.admin.domain.CategoryContent;
import dev.onebite.admin.domain.CategoryGroup;
import dev.onebite.admin.domain.Content;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.infra.repository.CategoryContentRepository;
import dev.onebite.admin.infra.repository.CategoryGroupRepository;
import dev.onebite.admin.infra.repository.CategoryRepository;
import dev.onebite.admin.infra.repository.ContentRepository;
import dev.onebite.admin.persentation.dto.request.*;
import dev.onebite.admin.persentation.exception.ApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Autowired
    private CategoryContentRepository categoryContentRepository;

    @Autowired
    private ContentRepository contentRepository;

    private CategoryGroup categoryGroup;

    @BeforeEach
    void setUp() {
        categoryGroup = CategoryGroup.of("groupCode", "groupLabel", "iconUrl", 1);
        categoryGroupRepository.save(categoryGroup);
    }

    @Test
    @DisplayName("카테고리 그룹이 올바르지 않을때 예외를 던지는지 테스트한다.")
    void checkCategoryGroupId() {
        //given
        CreateCategoryRequest request1 = new CreateCategoryRequest(2L, "code1", "label1", "iconUrl", "des1");

        //when and then
        assertThatThrownBy(() -> categoryService.create(request1))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("카테고리 생성시 정렬 순서가 최대값인지 테스트한다.")
    void checkMaxSort() {
        //given
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "code1", "label1", "iconUrl", "des1");
        CreateCategoryRequest request2 = new CreateCategoryRequest(categoryGroup.getId(), "code2", "label2", "iconUrl", "des1");

        //when
        categoryService.create(request1);
        categoryService.create(request2);


        Category category = categoryRepository.findByCode("code2").get();

        //then
        assertThat(category.getDisplayOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("카테고리 생성시 코드가 중복되는지 테스트한다.")
    void duplicatedCode() {
        //given
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "code", "label", "iconUrl", "des1");
        CreateCategoryRequest request2 = new CreateCategoryRequest(categoryGroup.getId(), "code", "label", "iconUrl", "des1");

        //when
        categoryService.create(request1);

        //given
        assertThatThrownBy(() -> categoryService.create(request2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.DUPLICATED_CODE.getMessage());
    }

    @Test
    @DisplayName("카테고리가 없을때 displayOrder가 1로 설정되는지 테스트한다.")
    void defaultDisplayOrder() {
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "code", "label", "iconUrl", "des1");

        categoryService.create(request1);

        Category category = categoryRepository.findByCode("code").get();

        assertThat(category.getDisplayOrder()).isEqualTo(1);

    }

    @Test
    @DisplayName("카테고리 코드의 길이는 2~20자인지 테스트한다.")
    void lengthCheck() {
        //given
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "javascript", "label", "iconUrl", "des1");
        CreateCategoryRequest request2 = new CreateCategoryRequest(categoryGroup.getId(), "java/spring", "label", "iconUrl", "des1");

        //when
        categoryService.create(request1);
        categoryService.create(request2);

        //then
        assertThat(categoryRepository.findAll()).hasSize(2);

        Category category1 = categoryRepository.findByCode("javascript")
                .orElseThrow(() -> new AssertionError("javascript가 저장되지 않았습니다"));
        Category category2 = categoryRepository.findByCode("java/spring")
                .orElseThrow(() -> new AssertionError("java/spring이 저장되지 않았습니다"));

        assertThat(category1.getCode()).isEqualTo("javascript");
        assertThat(category2.getCode()).isEqualTo("java/spring");
        assertThat(category1.getCode().length()).isBetween(2, 20);
        assertThat(category2.getCode().length()).isBetween(2, 20);
    }

    @Test
    @DisplayName("코드가 null이거나 비어있을 때 예외가 발생하는지 테스트한다.")
    void validateCode() {
        //given
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), null, "label", "iconUrl", "des1");
        CreateCategoryRequest request2 = new CreateCategoryRequest(categoryGroup.getId(), "", "label", "iconUrl", "des1");

        //when and given
        assertThatThrownBy(() -> categoryService.create(request1))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.VALIDATION_ERROR.getMessage());

        assertThatThrownBy(() -> categoryService.create(request2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.VALIDATION_ERROR.getMessage());
    }

    @Test
    @DisplayName("라벨이 null이거나 비어있을 때 예외가 발생하는지 테스트한다.")
    void validateLabel() {
        //given
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "code", null, "iconUrl", "des1");
        CreateCategoryRequest request2 = new CreateCategoryRequest(categoryGroup.getId(), "code1", "", "iconUrl", "des1");

        //when and given
        assertThatThrownBy(() -> categoryService.create(request1))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.VALIDATION_ERROR.getMessage());

        assertThatThrownBy(() -> categoryService.create(request2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.VALIDATION_ERROR.getMessage());
    }

    @Test
    @DisplayName("코드 길이가 최대 길이를 초과할 때 예외가 발생하는지 테스트한다.")
    void validateCodeMaxLength() {
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "codeeeeeeeeeeeeeeeeeeeeeeeeeeee", "test", "iconUrl", "des1");

        assertThatThrownBy(() -> categoryService.create(request1))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.INVALID_CODE_LENGTH.getMessage());
    }

    @Test
    @DisplayName("코드 길이가 최소 길이 미만일 때 예외가 발생하는지 테스트한다.")
    void validateCodeMinLength() {
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "c", "test", "iconUrl", "des1");

        assertThatThrownBy(() -> categoryService.create(request1))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.INVALID_CODE_LENGTH.getMessage());
    }


    @Test
    @DisplayName("카테고리 정보가 정상적으로 수정되는지 테스트한다.")
    void update() {
        CreateCategoryRequest request = new CreateCategoryRequest(categoryGroup.getId(), "code1", "test", "iconUrl", "des1");
        categoryService.create(request);

        Category category = categoryRepository.findByCode("code1").get();


        UpdateCategoryCommand updateRequest = new UpdateCategoryCommand(category.getId(), "code2", "test2", categoryGroup.getId(), "iconUrl2", 2);
        categoryService.update(updateRequest);

        Category updateCategory = categoryRepository.findByCode("code2").get();

        assertThat(updateCategory.getCode()).isEqualTo("code2");
        assertThat(updateCategory.getLabel()).isEqualTo("test2");
        assertThat(updateCategory.getIconUrl()).isEqualTo("iconUrl2");
    }


    @Test
    @DisplayName("존재하지 않는 카테고리 수정 시 예외가 발생하는지 테스트한다.")
    void updateFail() {
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "code1", "test", "iconUrl", "des1");
        categoryService.create(request1);

        //given
        UpdateCategoryCommand request2 = new UpdateCategoryCommand(9999L, "code2", "label", categoryGroup.getId(), "url1", 1);

        //when and given
        assertThatThrownBy(() -> categoryService.update(request2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("수정 시 다른 카테고리의 코드와 중복될 경우 예외가 발생하는지 테스트한다.")
    void updateDuplicatedCode() {
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "code1", "test", "iconUrl", "des1");
        categoryService.create(request1);

        Category category = categoryRepository.findByCode("code1").get();

        //given
        UpdateCategoryCommand request2 = new UpdateCategoryCommand(category.getId(), "code1", "label", categoryGroup.getId(), "iconUrl", 1);

        //when and given
        assertThatThrownBy(() -> categoryService.update(request2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.DUPLICATED_CODE.getMessage());
    }


    @Test
    @DisplayName("유효한 ID 목록으로 여러 카테고리를 정상적으로 삭제하는지 테스트한다.")
    void deleteCategories_withValidIds_succeeds() {

        //given
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "code1", "test1", "iconUrl", "des1");
        CreateCategoryRequest request2 = new CreateCategoryRequest(categoryGroup.getId(), "code2", "test2", "iconUrl", "des1");
        CreateCategoryRequest request3 = new CreateCategoryRequest(categoryGroup.getId(), "code3", "test3", "iconUrl", "des1");
        categoryService.create(request1);
        categoryService.create(request2);
        categoryService.create(request3);

        Category category1 = categoryRepository.findByCode("code1").get();
        Category category2 = categoryRepository.findByCode("code2").get();
        Category category3 = categoryRepository.findByCode("code3").get();

        List<Long> ids = List.of(category1.getId(), category2.getId(), category3.getId());

        DeleteCategoryRequest deleteCategoryGroupRequest = new DeleteCategoryRequest(ids);
        categoryService.delete(deleteCategoryGroupRequest);

        assertThat(categoryRepository.findAll()).isEmpty();

    }


    @Test
    @DisplayName("모든 ID가 존재하지 않을 경우, 예외가 발생하는지 테스트한다.")
    void deleteCategories_withAllInvalidIds_throwsException() {
        List<Long> ids = List.of(1L, 3L, 4L, 5L);
        DeleteCategoryRequest deleteCategoryGroupRequest = new DeleteCategoryRequest(ids);

        assertThatThrownBy(() -> categoryService.delete(deleteCategoryGroupRequest))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.DELETE_DATA_NOT_FOUND.getMessage());


    }

    @Test
    @DisplayName("데이터 삭제시 하위 데이터가 존재하는 경우 예외가 발생하는지 테스트한다.")
    void deleteCategories_withChildData_throwsException() {
        CreateCategoryRequest request2 = new CreateCategoryRequest(categoryGroup.getId(), "code1", "label1", "iconUrl1", "des1");
        categoryService.create(request2);

        Category category = categoryRepository.findByCode("code1").get();

        Content content = Content.of(
                "QUIZ", "삭제될 제목", "code", "desc", "answer",
                null, null, null, null, null
        );
        contentRepository.save(content);

        categoryContentRepository.save(CategoryContent.of(content, category));

        assertThatThrownBy(() -> categoryService.delete(
                new DeleteCategoryRequest(List.of(category.getId()))))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("하위 데이터가 존재하여 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("카테고리 순서 정렬 작업시 요청한 ID 순서대로 displayOrder가 갱신되어야 한다.")
    void reOrder() {
        CreateCategoryRequest request1 = new CreateCategoryRequest(categoryGroup.getId(), "cat1", "label1", "icon", "desc");
        CreateCategoryRequest request2 = new CreateCategoryRequest(categoryGroup.getId(), "cat2", "label2", "icon", "desc");
        CreateCategoryRequest request3 = new CreateCategoryRequest(categoryGroup.getId(), "cat3", "label3", "icon", "desc");

        categoryService.create(request1);
        categoryService.create(request2);
        categoryService.create(request3);

        List<Category> categories = categoryRepository.findAll();
        List<Long> originalIds = categories.stream()
                .map(Category::getId)
                .toList();

        List<Long> requestOrder = new ArrayList<>(originalIds);
        Collections.reverse(requestOrder);

        // When
        CategoryReOrderRequest reOrderRequest = new CategoryReOrderRequest(requestOrder);
        categoryService.reOrder(reOrderRequest);

        // Then
        List<Category> result = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"));

        assertThat(result.get(0).getId()).isEqualTo(requestOrder.get(0));
        assertThat(result.get(1).getId()).isEqualTo(requestOrder.get(1));
        assertThat(result.get(2).getId()).isEqualTo(requestOrder.get(2));

        assertThat(result.get(0).getDisplayOrder()).isEqualTo(1);
        assertThat(result.get(1).getDisplayOrder()).isEqualTo(2);
        assertThat(result.get(2).getDisplayOrder()).isEqualTo(3);
    }

}