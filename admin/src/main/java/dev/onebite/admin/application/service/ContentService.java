package dev.onebite.admin.application.service;

import dev.onebite.admin.domain.Content;
import dev.onebite.admin.domain.ContentEditor;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.infra.repository.ContentRepository;
import dev.onebite.admin.persentation.dto.ContentDto;
import dev.onebite.admin.persentation.dto.request.CreateContentRequest;
import dev.onebite.admin.persentation.dto.request.DeleteContentRequest;
import dev.onebite.admin.persentation.dto.request.UpdateContentCommand;
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
public class ContentService {

    private final ContentRepository contentRepository;

    @Transactional(readOnly = true)
    public Page<@NonNull ContentDto> findContents(String keyword, Pageable pageable) {

        if (StringUtils.hasText(keyword)) {
            String searchKeyword = "%" + keyword + "%";
            return contentRepository.searchDto(searchKeyword, pageable);
        }

        return contentRepository.findAllDto(pageable);
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
