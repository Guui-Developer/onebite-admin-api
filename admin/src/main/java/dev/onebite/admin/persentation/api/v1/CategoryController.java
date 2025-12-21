package dev.onebite.admin.persentation.api.v1;

import dev.onebite.admin.application.service.CategoryService;
import dev.onebite.admin.persentation.dto.CategoryDto;
import dev.onebite.admin.persentation.dto.CategoryGroupDto;
import dev.onebite.admin.persentation.dto.request.*;
import dev.onebite.admin.persentation.dto.response.PageResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ApiResponse<PageResponse<CategoryDto>> findAllCategoryGroups(
            String keyword,
            @PageableDefault(size = 10, sort = "displayOrder", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<@NonNull CategoryDto> categories = categoryService.findCategories(keyword, pageable);
        return ApiResponse.success(PageResponse.of(categories));
    }

    @PostMapping("/categories")
    public ApiResponse<Void> createCategory(@RequestBody CreateCategoryRequest request) {
        categoryService.create(request);
        return ApiResponse.success("카테고리가 성공적으로 생성되었습니다.");
    }

    @PutMapping("/categories/{categoryId}")
    public ApiResponse<Void> updateCategory(@PathVariable Long categoryId, UpdateCategoryRequest request) {
        var updateRequest = new UpdateCategoryCommand(categoryId, request.code(), request.label(), request.categoryGroupId(), request.iconUrl(), request.displayOrder());
        categoryService.update(updateRequest);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }

    @DeleteMapping("/categories")
    public ApiResponse<Void> deleteCategoryGroups(@RequestBody DeleteCategoryRequest request) {
        categoryService.delete(request);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }

    @PutMapping("/categories/reorder")
    public ApiResponse<Void> reorderCategoryGroups(@RequestBody CategoryReOrderRequest request) {
        categoryService.reOrder(request);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }

}
