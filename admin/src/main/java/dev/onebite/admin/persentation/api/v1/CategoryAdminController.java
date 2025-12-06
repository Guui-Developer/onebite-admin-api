package dev.onebite.admin.persentation.api.v1;

import dev.onebite.admin.application.service.CategoryGroupService;
import dev.onebite.admin.persentation.dto.request.ApiResponse;
import dev.onebite.admin.persentation.dto.request.CreateCategoryGroupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryGroupService categoryGroupService;

    @PostMapping("/group")
    public ApiResponse<Void> createCategoryGroup(CreateCategoryGroupRequest request) {
        categoryGroupService.create(request);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }

}
