package dev.onebite.admin.persentation.api.v1;


import dev.onebite.admin.application.service.ContentService;
import dev.onebite.admin.persentation.dto.ContentDto;
import dev.onebite.admin.persentation.dto.request.*;
import dev.onebite.admin.persentation.dto.response.PageResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/content")
    public ApiResponse<PageResponse<ContentDto>> findAllCategoryGroups(
            String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<@NonNull ContentDto> contents = contentService.findContents(keyword, pageable);
        return ApiResponse.success(PageResponse.of(contents));
    }


    @PostMapping("/content")
    public ApiResponse<Void> createContent(@Valid @RequestBody CreateContentRequest request) {
        contentService.create(request);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }

    @PutMapping("/content/{contentId}")
    public ApiResponse<Void> findAllCategoryGroups(@PathVariable Long contentId,@Valid @RequestBody UpdateContentRequest request) {
        UpdateContentCommand command = new UpdateContentCommand(
                contentId,
                request.type(),
                request.title(),
                request.code(),
                request.description(),
                request.answer(),
                request.beforeCode(),
                request.afterCode(),
                request.feedback(),
                request.imageUrl(),
                request.question()
        );
        contentService.update(command);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }

    @DeleteMapping("/content")
    public ApiResponse<Void> deleteCategoryGroups(@Valid @RequestBody DeleteContentRequest request) {
        contentService.delete(request);
        return ApiResponse.success("작업이 성공적으로 완료되었습니다.");
    }



}
