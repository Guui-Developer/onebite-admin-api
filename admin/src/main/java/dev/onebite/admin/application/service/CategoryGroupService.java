package dev.onebite.admin.application.service;

import dev.onebite.admin.domain.CategoryGroup;
import dev.onebite.admin.domain.ProductEditor;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.infra.repository.CategoryGroupRepository;
import dev.onebite.admin.persentation.dto.request.CreateCategoryGroupRequest;
import dev.onebite.admin.persentation.dto.request.UpdateCategoryGroupRequest;
import dev.onebite.admin.persentation.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void update(UpdateCategoryGroupRequest request) {
        CategoryGroup categoryGroup = categoryGroupRepository.findById(request.groupId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.ID_NOT_FOUND));

        if (categoryGroupRepository.existsByCode(request.groupCode())) {
            throw new ApplicationException(ErrorCode.DUPLICATED_CODE);
        }

        ProductEditor.ProductEditorBuilder groupEditor = categoryGroup.toEditor();
        groupEditor.code(request.groupCode());
        groupEditor.label(request.groupLabel());
        groupEditor.iconUrl(request.iconUrl());
        groupEditor.displayOrder(request.displayOrder());
        categoryGroup.edit(groupEditor.build());
    }

    @Transactional
    public void delete(DeleteCategoryGroupRequest request) {

        List<CategoryGroup> categoryGroups = categoryGroupRepository.findByIdIn(request.ids());

        if (categoryGroups.size() != request.ids().size()) {
            throw new ApplicationException(ErrorCode.DELETE_DATA_NOT_FOUND);
        }

        categoryGroupRepository.deleteAllByIdInBatch(request.ids());
    }
}
