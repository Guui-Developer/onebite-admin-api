package dev.onebite.admin.application.service;

import dev.onebite.admin.domain.Category;
import dev.onebite.admin.domain.CategoryGroup;
import dev.onebite.admin.domain.Content;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.infra.repository.CategoryContentRepository;
import dev.onebite.admin.infra.repository.CategoryGroupRepository;
import dev.onebite.admin.infra.repository.CategoryRepository;
import dev.onebite.admin.infra.repository.ContentRepository;
import dev.onebite.admin.persentation.dto.ContentDto;
import dev.onebite.admin.persentation.dto.request.CreateContentRequest;
import dev.onebite.admin.persentation.dto.request.DeleteContentRequest;
import dev.onebite.admin.persentation.dto.request.UpdateContentCommand;
import dev.onebite.admin.persentation.exception.ApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class ContentServiceTest {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Autowired
    private CategoryContentRepository categoryContentRepository;

    @Autowired
    private ContentService contentService;

    private Category savedCategory;

    @BeforeEach
    void setUp() {
        CategoryGroup categoryGroup = createDummyCategoryGroup("LANG", "언어");
        categoryGroupRepository.save(categoryGroup);

        Category category = createDummyCategory("JAVA", "자바", categoryGroup);
        savedCategory = categoryRepository.save(category);
    }

    @AfterEach
    void tearDown() {
        categoryContentRepository.deleteAllInBatch();
        contentRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
        categoryGroupRepository.deleteAllInBatch();
    }

    private CategoryGroup createDummyCategoryGroup(String code, String label) {
        return CategoryGroup.of(code, label, "icon.png", 1);
    }

    private Category createDummyCategory(String code, String label, CategoryGroup categoryGroup) {
        return Category.of(code, label, categoryGroup, "icon.png", "description", 1);
    }

    private Content createDummyContent(String title, String type) {
        return Content.of(
                type, title, "code", "desc", "answer",
                null, null, null, null, null
        );
    }


    @Test
    @DisplayName("콘텐츠 생성 테스트")
    void createContent() {
        // given
        CreateContentRequest request = new CreateContentRequest(
                "QUIZ",
                "테스트 제목",
                "console.log('test')",
                "테스트 설명",
                "정답",
                "이전 코드",
                "이후 코드",
                "피드백",
                "https://example.com/image.jpg",
                "질문 내용",
                List.of(savedCategory.getCode())
        );

        // when
        Long contentId = contentService.create(request);

        // then
        Content content = contentRepository.findById(contentId).orElseThrow();
        assertThat(content).isNotNull();
        assertThat(content.getId()).isEqualTo(contentId);
        assertThat(content.getTitle()).isEqualTo("테스트 제목");
        assertThat(content.getCode()).isEqualTo("console.log('test')");
    }

    @Test
    @DisplayName("생성 시 생성일(createdAt)과 수정일(updatedAt)이 자동으로 기록된다")
    void createContent_checkAuditing() {
        // given
        CreateContentRequest request = new CreateContentRequest(
                "QUIZ", "제목", "코드", "설명", "정답", null, null, null, null, "질문", List.of(savedCategory.getCode())
        );

        // when
        Long contentId = contentService.create(request);

        // then
        Content content = contentRepository.findById(contentId).orElseThrow();

        assertThat(content.getCreatedAt()).isNotNull();
        assertThat(content.getUpdatedAt()).isNotNull();

        assertThat(content.getCreatedAt()).isEqualTo(content.getUpdatedAt());
    }


    @Test
    @DisplayName("콘텐츠 목록 조회 테스트")
    void getContents() {
        // given
        contentRepository.saveAll(List.of(
                createDummyContent("Title 1", "QUIZ"),
                createDummyContent("Title 2", "CODE"),
                createDummyContent("Title 3", "QUIZ")
        ));

        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Page<ContentDto> result = contentService.findContents(null, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Title 1");
    }


    @Test
    @DisplayName("존재하지 않는 콘텐츠 조회 시 예외 발생 테스트")
    void getContentNotFound() {
        // given
        Long nonExistentId = 999999L;

        // when
        Optional<Content> result = contentRepository.findById(nonExistentId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("콘텐츠 정보 수정 테스트")
    void updateContent() {
        // given
        Content original = Content.of(
                "QUIZ",
                "원래 제목",
                "old code",
                "원래 설명",
                "원래 정답",
                "old before",
                "old after",
                "old feedback",
                "https://old.com",
                "원래 질문"
        );
        contentRepository.save(original);

        UpdateContentCommand command = new UpdateContentCommand(
                original.getId(),
                "CODE",
                "수정된 제목",
                "new code",
                "수정된 설명",
                "수정된 정답",
                "new before",
                "new after",
                "new feedback",
                "https://new.com",
                "수정된 질문",
                List.of(savedCategory.getCode())
        );

        // when
        contentService.update(command);

        // then
        Content updatedContent = contentRepository.findById(original.getId()).orElseThrow();

        assertThat(updatedContent.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedContent.getType()).isEqualTo("CODE"); // Enum 혹은 String 비교
        assertThat(updatedContent.getCode()).isEqualTo("new code");
        assertThat(updatedContent.getImageUrl()).isEqualTo("https://new.com");
        assertThat(updatedContent.getId()).isEqualTo(original.getId());
    }

    @Test
    @DisplayName("존재하지 않는 콘텐츠 수정 시 예외 발생 테스트")
    void updateContentNotFound() {

        Long nonExistentId = 999999L;

        UpdateContentCommand command = new UpdateContentCommand(
                nonExistentId,
                "QUIZ",
                "제목",
                "코드",
                "설명",
                "정답",
                null, null, null, null, null,
                List.of(savedCategory.getCode())
        );

        // when & then
        assertThatThrownBy(() -> contentService.update(command))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.CONTENT_NOT_FOUND.getMessage());
    }



    @Test
    @DisplayName("콘텐츠 수정 시 일부 필드만 수정할 수 있다")
    void updateContent_partialUpdate() {

        // given
        Content original = Content.of(
                "QUIZ",
                "원래 제목",
                "원래 코드",
                "원래 설명",
                "원래 정답",
                "원래 이전코드",
                "원래 이후코드",
                "원래 피드백",
                "https://original.com",
                "원래 질문"
        );
        contentRepository.save(original);

        UpdateContentCommand command = new UpdateContentCommand(
                original.getId(),
                original.getType(),       // 유지
                "수정된 제목!!!",           // [변경]
                original.getCode(),       // 유지
                original.getDescription(),// 유지
                original.getAnswer(),     // 유지
                original.getBeforeCode(), // 유지
                original.getAfterCode(),  // 유지
                "수정된 피드백!!!",         // [변경]
                original.getImageUrl(),   // 유지
                original.getQuestionText(),// 유지
                List.of(savedCategory.getCode())
        );

        // when
        contentService.update(command);

        // then
        Content updatedContent = contentRepository.findById(original.getId()).orElseThrow();

        assertThat(updatedContent.getTitle()).isEqualTo("수정된 제목!!!");
        assertThat(updatedContent.getFeedback()).isEqualTo("수정된 피드백!!!");
        assertThat(updatedContent.getCode()).isEqualTo("원래 코드");
        assertThat(updatedContent.getImageUrl()).isEqualTo("https://original.com");
    }

    @Test
    @DisplayName("삭제된 콘텐츠는 재조회할 수 없다")
    void getContent_afterDelete() {
        // given
        Content content = Content.of(
                "QUIZ", "삭제될 제목", "code", "desc", "answer",
                null, null, null, null, null
        );
        contentRepository.save(content);
        Long targetId = content.getId();

        DeleteContentRequest request = new DeleteContentRequest(List.of(targetId));

        // when
        contentService.delete(request);

        // then
        assertThat(contentRepository.findById(targetId)).isEmpty();
    }

    @Test
    @DisplayName("빈 ID 리스트로 삭제 시 아무 동작도 하지 않는다")
    void deleteContents_emptyList() {
        // given
        Content survivor = Content.of(
                "QUIZ", "생존자", "code", "desc", "answer",
                null, null, null, null, null
        );
        contentRepository.save(survivor);

        DeleteContentRequest request = new DeleteContentRequest(List.of());

        // when
        assertThatThrownBy(()-> contentService.delete(request))
                .isInstanceOf(ApplicationException.class)
                        .hasMessage(ErrorCode.DELETE_DATA_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("일부만 존재하는 ID로 삭제 시 존재하는 것만 삭제된다")
    void deleteContents_partiallyExist() {
        // given
        Content target = Content.of(
                "QUIZ", "삭제될 녀석", "code", "desc", "answer",
                null, null, null, null, null
        );
        Content saved = contentRepository.save(target);

        Content survivor = Content.of(
                "QUIZ", "생존할 녀석", "code", "desc", "answer",
                null, null, null, null, null
        );
        contentRepository.save(survivor);

        Long nonExistentId = 999999L;
        DeleteContentRequest request = new DeleteContentRequest(List.of(saved.getId(), nonExistentId));

        // when
        contentService.delete(request);

        // then
        assertThat(contentRepository.findById(target.getId())).isEmpty();
        assertThat(contentRepository.findById(survivor.getId())).isPresent();
    }

    @Test
    @DisplayName("키워드로 콘텐츠를 필터링할 수 있다 (Type 필터링 대용)")
    void getContents_filterByType() {

        // given
        contentRepository.save(createDummyContent("JAVA Basic", "QUIZ"));
        contentRepository.save(createDummyContent("JAVA Advanced", "CODE"));

        contentRepository.save(createDummyContent("PYTHON Basic", "QUIZ"));

        // when
        Page<ContentDto> result = contentService.findContents("JAVA", PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting("title")
                .containsExactlyInAnyOrder("JAVA Basic", "JAVA Advanced");
    }

    @Test
    @DisplayName("생성 시 views와 bookmarks가 0으로 초기화된다")
    void createContent_defaultCounters() {
        CreateContentRequest request = new CreateContentRequest(
                "QUIZ", "제목", "코드", "설명", "정답",
                null, null, null, null, null, List.of(savedCategory.getCode())
        );

        // when
        Long contentId = contentService.create(request);

        // then
        Content content = contentRepository.findById(contentId).orElseThrow();

        assertThat(content.getViews()).isZero();
        assertThat(content.getBookmarks()).isZero();
    }

    @Test
    @DisplayName("생성 시 createdAt과 updatedAt이 자동 설정된다")
    void createContent_autoTimestamps() {
        // given
        CreateContentRequest request = new CreateContentRequest(
                "QUIZ", "제목", "코드", "설명", "정답",
                null, null, null, null, null, List.of(savedCategory.getCode())
        );

        // when
        Long contentId = contentService.create(request);

        // then
        Content content = contentRepository.findById(contentId).orElseThrow();

        assertThat(content.getCreatedAt()).isNotNull();
        assertThat(content.getUpdatedAt()).isNotNull();

        assertThat(content.getCreatedAt()).isEqualTo(content.getUpdatedAt());
    }

    @Test
    @DisplayName("페이지 크기만큼 콘텐츠가 조회된다")
    void getContents_withPagination() {
        for (int i = 1; i <= 10; i++) {
            contentRepository.save(createDummyContent("Title " + i, "QUIZ"));
        }

        int pageSize = 3;
        PageRequest pageRequest = PageRequest.of(0, pageSize);

        // when
        Page<ContentDto> result = contentService.findContents(null, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(pageSize);
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.hasNext()).isTrue();
    }

    @Test
    @DisplayName("마지막 페이지에서는 남은 개수만큼만 조회된다")
    void getContents_lastPage() {
        // given
        for (int i = 1; i <= 10; i++) {
            contentRepository.save(createDummyContent("Title " + i, "QUIZ"));
        }

        int pageSize = 3;
        PageRequest pageRequest = PageRequest.of(3, pageSize);

        // when
        Page<ContentDto> result = contentService.findContents(null, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.isLast()).isTrue();
    }
}