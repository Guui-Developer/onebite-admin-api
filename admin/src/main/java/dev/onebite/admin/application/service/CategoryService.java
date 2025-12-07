package dev.onebite.admin.application.service;


import dev.onebite.admin.domain.Category;
import dev.onebite.admin.domain.CategoryEditor;
import dev.onebite.admin.domain.CategoryGroup;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.infra.repository.CategoryGroupRepository;
import dev.onebite.admin.infra.repository.CategoryRepository;
import dev.onebite.admin.persentation.dto.CategoryDto;
import dev.onebite.admin.persentation.dto.CategoryGroupDto;
import dev.onebite.admin.persentation.dto.request.CreateCategoryRequest;
import dev.onebite.admin.persentation.dto.request.DeleteCategoryRequest;
import dev.onebite.admin.persentation.dto.request.UpdateCategoryCommand;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryGroupRepository categoryGroupRepository;

    @Transactional
    public void create(CreateCategoryRequest request) {

        if (categoryRepository.existsByCode((request.code()))) {
            throw new ApplicationException(ErrorCode.DUPLICATED_CODE);
        }

        CategoryGroup categoryGroup = categoryGroupRepository.findById(request.categoryGroupId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.ID_NOT_FOUND));

        Integer maxOrder = categoryRepository.findMaxDisplayOrder().orElse(0);
        Integer newOrder = maxOrder + 1;

        Category category = Category.of(
                request.code(),
                request.label(),
                categoryGroup,
                request.iconUrl(),
                request.description(),
                newOrder
        );

        categoryRepository.save(category);

    }

    @Transactional
    public void update(UpdateCategoryCommand request) {

        if (categoryRepository.existsByCode(request.code())) {
            throw new ApplicationException(ErrorCode.DUPLICATED_CODE);
        }

        CategoryGroup categoryGroup = categoryGroupRepository.findById(request.categoryGroupId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.ID_NOT_FOUND));


        Category category = categoryRepository.findById(request.id())
                .orElseThrow(() -> new ApplicationException(ErrorCode.ID_NOT_FOUND));

        CategoryEditor.CategoryEditorBuilder categoryEditor = category.toEditor();
        categoryEditor.code(request.code());
        categoryEditor.label(request.label());
        categoryEditor.iconUrl(request.iconUrl());
        categoryEditor.groupId(categoryGroup);
        categoryEditor.displayOrder(request.displayOrder());
        category.edit(categoryEditor.build());
        categoryRepository.save(category);
    }

    @Transactional
    public void delete(DeleteCategoryRequest request) {
        List<Category> categories = categoryRepository.findByIdIn(request.ids());

        if (categories.size() != request.ids().size()) {
            throw new ApplicationException(ErrorCode.DELETE_DATA_NOT_FOUND);
        }

        categoryRepository.deleteAllByIdInBatch(request.ids());
    }

    @Transactional(readOnly = true)
    public Page<@NonNull CategoryDto> findCategories(String keyword, Pageable pageable) {

        if (StringUtils.hasText(keyword)) {
            String searchKeyword = "%" + keyword + "%";
            return categoryRepository.searchDto(searchKeyword, pageable);
        }

        return categoryRepository.findAllDto(pageable);
    }
}
