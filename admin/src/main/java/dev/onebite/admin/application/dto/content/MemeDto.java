package dev.onebite.admin.application.dto.content;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dev.onebite.admin.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "type", "title", "image", "description", "tags", "createdAt"})
public class MemeDto extends ContentDto {

    private String image;
    private String description;

    public static MemeDto from(Content content) {
        MemeDto dto = new MemeDto(
                content.getImageUrl(),
                content.getDescription()
        );
        dto.id = content.getId();
        dto.type = "meme";
        dto.title = content.getTitle();
        dto.createdAt = String.valueOf(content.getCreatedAt());
        return dto;
    }
}
