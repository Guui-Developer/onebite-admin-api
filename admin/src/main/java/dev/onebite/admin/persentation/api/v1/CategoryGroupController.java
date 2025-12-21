package dev.onebite.admin.persentation.api.v1;

import dev.onebite.admin.application.service.CategoryGroupService;
import dev.onebite.admin.persentation.dto.request.CategoryGroupReOrderRequest;
import dev.onebite.admin.persentation.dto.request.DeleteCategoryGroupRequest;
import dev.onebite.admin.persentation.dto.CategoryGroupDto;
import dev.onebite.admin.persentation.dto.request.ApiResponse;
import dev.onebite.admin.persentation.dto.request.CreateCategoryGroupRequest;
import dev.onebite.admin.persentation.dto.request.UpdateCategoryGroupCommand;
import dev.onebite.admin.persentation.dto.request.UpdateCategoryGroupRequest;
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
public class CategoryGroupController {

    private final CategoryGroupService categoryGroupService;


    @GetMapping("/group")
    public ApiResponse<PageResponse<CategoryGroupDto>> findAllCategoryGroups(
            String keyword,
            @PageableDefault(size = 10, sort = "displayOrder", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<@NonNull CategoryGroupDto> categoryGroups = categoryGroupService.findCategoryGroups(keyword, pageable);
        return ApiResponse.success(PageResponse.of(categoryGroups));
    }

    @PostMapping("/group")
    public ApiResponse<Void> createCategoryGroup(@RequestBody CreateCategoryGroupRequest request) {
        categoryGroupService.create(request);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }

    @PutMapping("/group/{groupId}")
    public ApiResponse<Void> findAllCategoryGroups(@PathVariable Long groupId, @RequestBody UpdateCategoryGroupRequest request) {
        var updateRequest = new UpdateCategoryGroupCommand(groupId, request.groupCode(), request.groupLabel(), request.iconUrl());
        categoryGroupService.update(updateRequest);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }

    @PutMapping("/group/reorder")
    public ApiResponse<Void> reorderCategoryGroups(@RequestBody CategoryGroupReOrderRequest request) {
        categoryGroupService.reOrder(request);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }

    @DeleteMapping("/group")
    public ApiResponse<Void> deleteCategoryGroups(@RequestBody DeleteCategoryGroupRequest request) {
        categoryGroupService.delete(request);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }


}
