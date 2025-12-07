package dev.onebite.admin.application.service;

import dev.onebite.admin.domain.CategoryGroup;
import dev.onebite.admin.domain.CategoryGroupEditor;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.infra.repository.CategoryGroupRepository;
import dev.onebite.admin.persentation.dto.CategoryGroupDto;
import dev.onebite.admin.persentation.dto.request.CreateCategoryGroupRequest;
import dev.onebite.admin.persentation.dto.request.DeleteCategoryGroupRequest;
import dev.onebite.admin.persentation.dto.request.UpdateCategoryGroupCommand;
import dev.onebite.admin.persentation.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;

    @Transactional
    public void create(CreateCategoryGroupRequest request) {

        if (categoryGroupRepository.existsByCode(request.groupCode())) {
            throw new ApplicationException(ErrorCode.DUPLICATED_CODE);
        }

        Integer maxOrder = categoryGroupRepository.findMaxDisplayOrder().orElse(0);
        Integer newOrder = maxOrder + 1;

        CategoryGroup categoryGroup = CategoryGroup.of(
                request.groupCode(),
                request.groupLabel(),
                request.iconUrl(),
                newOrder
        );

        categoryGroupRepository.save(categoryGroup);
    }

    @Transactional
    public void update(UpdateCategoryGroupCommand request) {
        CategoryGroup categoryGroup = categoryGroupRepository.findById(request.groupId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.ID_NOT_FOUND));

        if (categoryGroupRepository.existsByCode(request.groupCode())) {
            throw new ApplicationException(ErrorCode.DUPLICATED_CODE);
        }

        CategoryGroupEditor.CategoryGroupEditorBuilder groupEditor = categoryGroup.toEditor();
        groupEditor.code(request.groupCode());
        groupEditor.label(request.groupLabel());
        groupEditor.iconUrl(request.iconUrl());
        groupEditor.displayOrder(request.displayOrder());
        categoryGroup.edit(groupEditor.build());
    }

    @Transactional
    public void delete(DeleteCategoryGroupRequest request) {

        List<CategoryGroup> categoryGroups = categoryGroupRepository.findByIdIn(request.ids());

        if (categoryGroups.isEmpty()) {
            throw new ApplicationException(ErrorCode.DELETE_DATA_NOT_FOUND);
        }

        categoryGroupRepository.deleteAllByIdInBatch(request.ids());
    }

    @Transactional(readOnly = true)
    public Page<@NonNull CategoryGroupDto> findCategoryGroups(String keyword, Pageable pageable){

        if (StringUtils.hasText(keyword)) {
            String searchKeyword = "%" + keyword + "%";
            return categoryGroupRepository.searchDto(searchKeyword, pageable);
        }

        return categoryGroupRepository.findAllDto(pageable);
    }
}
