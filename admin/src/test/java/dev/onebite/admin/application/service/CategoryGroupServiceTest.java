package dev.onebite.admin.application.service;

import dev.onebite.admin.domain.CategoryGroup;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.infra.repository.CategoryContentRepository;
import dev.onebite.admin.infra.repository.CategoryGroupRepository;
import dev.onebite.admin.persentation.dto.request.CreateCategoryGroupRequest;
import dev.onebite.admin.persentation.dto.request.CreateCategoryRequest;
import dev.onebite.admin.persentation.dto.request.DeleteCategoryGroupRequest;
import dev.onebite.admin.persentation.dto.request.UpdateCategoryGroupCommand;
import dev.onebite.admin.persentation.exception.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryGroupServiceTest {

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Autowired
    private CategoryService categoryService;



    @Test
    @DisplayName("카테고리 그룹 생성시 정렬 순서가 최대값인지 테스트한다.")
    void checkMaxSort() {
        //given
        CreateCategoryGroupRequest groupRequest1 = new CreateCategoryGroupRequest("code1", "label1", "iconUrl");
        CreateCategoryGroupRequest groupRequest2 = new CreateCategoryGroupRequest("code2", "label2", "iconUrl");

        //when
        categoryGroupService.create(groupRequest1);
        categoryGroupService.create(groupRequest2);


        CategoryGroup categoryGroup = categoryGroupRepository.findByCode("code2").get();

        //then
        assertThat(categoryGroup.getDisplayOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("카테고리 그룹 생성시 코드가 중복되는지 테스트한다.")
    void duplicatedCode() {
        //given
        CreateCategoryGroupRequest groupRequest1 = new CreateCategoryGroupRequest("code", "label", "iconUrl");
        CreateCategoryGroupRequest groupRequest2 = new CreateCategoryGroupRequest("code", "label", "iconUrl");

        //when
        categoryGroupService.create(groupRequest1);

        //given
        assertThatThrownBy(() -> categoryGroupService.create(groupRequest2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.DUPLICATED_CODE.getMessage());
    }

    @Test
    @DisplayName("카테고리 그룹이 없을때 displayOrder가 1로 설정되는지 테스트한다.")
    void defaultDisplayOrder() {
        CreateCategoryGroupRequest groupRequest1 = new CreateCategoryGroupRequest("code", "label", "iconUrl");

        categoryGroupService.create(groupRequest1);

        CategoryGroup categoryGroup = categoryGroupRepository.findByCode("code").get();

        assertThat(categoryGroup.getDisplayOrder()).isEqualTo(1);

    }

    @Test
    @DisplayName("카테고리 그룹의 길이는 2~20자인지 테스트한다.")
    void lengthCheck() {
        //given
        CreateCategoryGroupRequest groupRequest1 = new CreateCategoryGroupRequest("javascript", "label", "iconUrl");
        CreateCategoryGroupRequest groupRequest2 = new CreateCategoryGroupRequest("java/spring", "label", "iconUrl");

        //when
        categoryGroupService.create(groupRequest1);
        categoryGroupService.create(groupRequest2);

        //then
        assertThat(categoryGroupRepository.findAll()).hasSize(2);

        CategoryGroup group1 = categoryGroupRepository.findByCode("javascript")
                .orElseThrow(() -> new AssertionError("javascript가 저장되지 않았습니다"));
        CategoryGroup group2 = categoryGroupRepository.findByCode("java/spring")
                .orElseThrow(() -> new AssertionError("java/spring이 저장되지 않았습니다"));

        assertThat(group1.getCode()).isEqualTo("javascript");
        assertThat(group2.getCode()).isEqualTo("java/spring");
        assertThat(group1.getCode().length()).isBetween(2, 20);
        assertThat(group2.getCode().length()).isBetween(2, 20);
    }

    @Test
    @DisplayName("코드가 null이거나 비어있을 때 예외가 발생하는지 테스트한다.")
    void validateCode() {
        //given
        CreateCategoryGroupRequest groupRequest1 = new CreateCategoryGroupRequest(null, "label", "iconUrl");
        CreateCategoryGroupRequest groupRequest2 = new CreateCategoryGroupRequest("", "label", "iconUrl");

        //when and given
        assertThatThrownBy(() -> categoryGroupService.create(groupRequest1))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.VALIDATION_ERROR.getMessage());

        assertThatThrownBy(() -> categoryGroupService.create(groupRequest2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.VALIDATION_ERROR.getMessage());
    }

    @Test
    @DisplayName("라벨이 null이거나 비어있을 때 예외가 발생하는지 테스트한다.")
    void validateLabel() {
        //given
        CreateCategoryGroupRequest groupRequest1 = new CreateCategoryGroupRequest("code", null, "iconUrl");
        CreateCategoryGroupRequest groupRequest2 = new CreateCategoryGroupRequest("code1", "", "iconUrl");

        //when and given
        assertThatThrownBy(() -> categoryGroupService.create(groupRequest1))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.VALIDATION_ERROR.getMessage());

        assertThatThrownBy(() -> categoryGroupService.create(groupRequest2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.VALIDATION_ERROR.getMessage());
    }

    @Test
    @DisplayName("코드 길이가 최대 길이를 초과할 때 예외가 발생하는지 테스트한다.")
    void validateCodeMaxLength() {
        CreateCategoryGroupRequest groupRequest1 = new CreateCategoryGroupRequest("codeeeeeeeeeeeeeeeeeeeeeeeeeeee", "test", "iconUrl");

        assertThatThrownBy(() -> categoryGroupService.create(groupRequest1))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.INVALID_CODE_LENGTH.getMessage());
    }

    @Test
    @DisplayName("코드 길이가 최소 길이 미만일 때 예외가 발생하는지 테스트한다.")
    void validateCodeMinLength() {
        CreateCategoryGroupRequest groupRequest1 = new CreateCategoryGroupRequest("c", "test", "iconUrl");

        assertThatThrownBy(() -> categoryGroupService.create(groupRequest1))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.INVALID_CODE_LENGTH.getMessage());
    }


    @Test
    @DisplayName("카테고리 그룹 정보가 정상적으로 수정되는지 테스트한다.")
    void update() {
        CreateCategoryGroupRequest request = new CreateCategoryGroupRequest("groupCode1", "test", "iconUrl");
        categoryGroupService.create(request);

        CategoryGroup categoryGroup = categoryGroupRepository.findByCode("groupCode1").get();


        UpdateCategoryGroupCommand updateRequest = new UpdateCategoryGroupCommand(categoryGroup.getId(), "groupCode2", "test2", "iconUrl2", 2);
        categoryGroupService.update(updateRequest);

        CategoryGroup updateGroup = categoryGroupRepository.findByCode("groupCode2").get();

        assertThat(updateGroup.getCode()).isEqualTo("groupCode2");
        assertThat(updateGroup.getLabel()).isEqualTo("test2");
        assertThat(updateGroup.getIconUrl()).isEqualTo("iconUrl2");
        assertThat(updateGroup.getDisplayOrder()).isEqualTo(2);
    }


    @Test
    @DisplayName("존재하지 않는 카테고리 그룹 수정 시 예외가 발생하는지 테스트한다.")
    void updateFail() {
        CreateCategoryGroupRequest request1 = new CreateCategoryGroupRequest("groupCode1", "test", "iconUrl");
        categoryGroupService.create(request1);

        //given
        UpdateCategoryGroupCommand request2 = new UpdateCategoryGroupCommand(9999L, "groupCode2", "label", "iconUrl", 1);

        //when and given
        assertThatThrownBy(() -> categoryGroupService.update(request2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("수정 시 다른 그룹의 코드와 중복될 경우 예외가 발생하는지 테스트한다.")
    void updateDuplicatedCode() {
        CreateCategoryGroupRequest request1 = new CreateCategoryGroupRequest("groupCode1", "test", "iconUrl");
        categoryGroupService.create(request1);

        CategoryGroup categoryGroup = categoryGroupRepository.findByCode("groupCode1").get();

        //given
        UpdateCategoryGroupCommand request2 = new UpdateCategoryGroupCommand(categoryGroup.getId(), "groupCode1", "label", "iconUrl", 1);

        //when and given
        assertThatThrownBy(() -> categoryGroupService.update(request2))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.DUPLICATED_CODE.getMessage());
    }


    @Test
    @DisplayName("유효한 ID 목록으로 여러 카테고리 그룹을 정상적으로 삭제하는지 테스트한다.")
    void deleteCategoryGroups_withValidIds_succeeds() {

        //given
        CreateCategoryGroupRequest request1 = new CreateCategoryGroupRequest("groupCode1", "test1", "iconUrl");
        CreateCategoryGroupRequest request2 = new CreateCategoryGroupRequest("groupCode2", "test2", "iconUrl");
        CreateCategoryGroupRequest request3 = new CreateCategoryGroupRequest("groupCode3", "test3", "iconUrl");
        categoryGroupService.create(request1);
        categoryGroupService.create(request2);
        categoryGroupService.create(request3);

        CategoryGroup categoryGroup1 = categoryGroupRepository.findByCode("groupCode1").get();
        CategoryGroup categoryGroup2 = categoryGroupRepository.findByCode("groupCode2").get();
        CategoryGroup categoryGroup3 = categoryGroupRepository.findByCode("groupCode3").get();

        List<Long> ids = List.of(categoryGroup1.getId(), categoryGroup2.getId(), categoryGroup3.getId());

        DeleteCategoryGroupRequest deleteRequest = new DeleteCategoryGroupRequest(ids, false);
        categoryGroupService.delete(deleteRequest);

        assertThat(categoryGroupRepository.findAll()).isEmpty();

    }


    @Test
    @DisplayName("모든 ID가 존재하지 않을 경우, 예외가 발생하는지 테스트한다.")
    void deleteCategoryGroups_withAllInvalidIds_throwsException() {
        DeleteCategoryGroupRequest deleteRequest = new DeleteCategoryGroupRequest(List.of(1L, 3L, 4L, 5L), false);

        assertThatThrownBy(() -> categoryGroupService.delete(deleteRequest))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.DELETE_DATA_NOT_FOUND.getMessage());


    }

    @Test
    @DisplayName("데이터 삭제시 하위 데이터가 존재하는 경우 예외가 발생하는지 테스트한다.")
    void deleteCategoryGroups_withChildData_throwsException() {
        CreateCategoryGroupRequest request1 = new CreateCategoryGroupRequest("groupCode1", "test1", "iconUrl");
        categoryGroupService.create(request1);

        CategoryGroup categoryGroup = categoryGroupRepository.findByCode("groupCode1").get();

        CreateCategoryRequest request2 = new CreateCategoryRequest(categoryGroup.getId(), "code1", "label1", "iconUrl1", "des1");
        categoryService.create(request2);


        assertThatThrownBy(() -> categoryGroupService.delete(
                new DeleteCategoryGroupRequest(List.of(categoryGroup.getId()), false)))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("하위 데이터가 존재하여 삭제할 수 없습니다.");
    }

}