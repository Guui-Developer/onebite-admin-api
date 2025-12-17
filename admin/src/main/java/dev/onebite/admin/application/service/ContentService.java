package dev.onebite.admin.application.service;

import dev.onebite.admin.domain.Category;
import dev.onebite.admin.domain.CategoryContent;
import dev.onebite.admin.domain.Content;
import dev.onebite.admin.domain.ContentEditor;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.infra.repository.CategoryContentRepository;
import dev.onebite.admin.infra.repository.CategoryRepository;
import dev.onebite.admin.infra.repository.ContentRepository;
import dev.onebite.admin.persentation.dto.ContentDto;
import dev.onebite.admin.persentation.dto.request.CreateContentRequest;
import dev.onebite.admin.persentation.dto.request.DeleteContentRequest;
import dev.onebite.admin.persentation.dto.request.UpdateContentCommand;
import dev.onebite.admin.persentation.exception.ApplicationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryContentRepository categoryContentRepository;

    @Transactional(readOnly = true)
    public Page<@NonNull ContentDto> findContents(String keyword, Pageable pageable) {

        Page<ContentDto> contentPage;

        if (StringUtils.hasText(keyword)) {
            String searchKeyword = "%" + keyword + "%";
            contentPage = contentRepository.searchDto(searchKeyword, pageable);
        } else {
            contentPage = contentRepository.findAllDto(pageable);
        }

        if (contentPage.isEmpty()) {
            return contentPage;
        }

        List<Long> contentIds = contentPage.getContent().stream()
                .map(ContentDto::getContentId)
                .toList();

        List<CategoryContent> foundContentList = categoryContentRepository.findAllByContentIdIn(contentIds);

        Map<Long, List<String>> categoryMap = foundContentList.stream()
                .collect(Collectors.groupingBy(
                        cc -> cc.getContentId().getId(),
                        Collectors.mapping(
                                cc -> cc.getCategoryId().getCode(),
                                Collectors.toList()
                        )
                ));

        contentPage.getContent().forEach(dto -> {
            dto.assignTags(categoryMap.getOrDefault(dto.getContentId(), Collections.emptyList()));
        });

        return contentPage;
    }

    @Transactional
    public Long create(CreateContentRequest request) {

        Content content = Content.of(
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

        Content savedContent = contentRepository.save(content);

        List<Category> categories = categoryRepository.findByCodeIn(request.tags());

        if (!categories.isEmpty()) {
            List<CategoryContent> categoryContents = categories.stream()
                    .map(category -> CategoryContent.of(content, category))
                    .toList();
            categoryContentRepository.saveAll(categoryContents);
        }

        return savedContent.getId();
    }

    @Transactional
    public void update(UpdateContentCommand request) {

        Content content = contentRepository.findById(request.contentId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTENT_NOT_FOUND));

        ContentEditor contentEditor = content.toEditor()
                .type(request.type())
                .title(request.title())
                .code(request.code())
                .description(request.description())
                .answer(request.answer())
                .beforeCode(request.beforeCode())
                .afterCode(request.afterCode())
                .feedback(request.feedback())
                .imageUrl(request.imageUrl())
                .questionText(request.questionText())
                .build();

        content.edit(contentEditor);

        List<Category> categories = categoryRepository.findByCodeIn(request.tags());

        categoryContentRepository.deleteByContentId(content);

        if (!categories.isEmpty()) {
            List<CategoryContent> categoryContents = categories.stream()
                    .map(category -> CategoryContent.of(content, category))
                    .toList();

            categoryContentRepository.saveAll(categoryContents);
        }

        contentRepository.save(content);

    }

    @Transactional
    public void delete(DeleteContentRequest request) {
        List<Content> contents = contentRepository.findByIdIn(request.ids());

        if (contents.isEmpty()) {
            throw new ApplicationException(ErrorCode.DELETE_DATA_NOT_FOUND);
        }

        contentRepository.deleteAllByIdInBatch(request.ids());
    }

}
