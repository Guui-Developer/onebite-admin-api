package dev.onebite.admin.infra.repository;

import dev.onebite.admin.domain.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ContentRepositoryTest {
    
    @Autowired
    private ContentRepository contentRepository;


    @Test
    @DisplayName("컨텐츠가 올바르게 생성되는지 테스트한다.")
    void createContent() {
        // given
        Content content = Content.of(
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
                "java",
                List.of("CODE","CODE2")
        );

        // when
        Content savedContent = contentRepository.save(content);

        // then
        assertThat(savedContent.getId()).isNotNull();
        assertThat(savedContent.getTitle()).isEqualTo("테스트 제목");
        assertThat(savedContent.getType()).isEqualTo("QUIZ");
        assertThat(savedContent.getIsActive()).isTrue();
    }
}