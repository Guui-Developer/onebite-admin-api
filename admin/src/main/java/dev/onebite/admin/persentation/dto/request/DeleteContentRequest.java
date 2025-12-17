package dev.onebite.admin.persentation.dto.request;

import java.util.List;

public record DeleteContentRequest(
        List<Long> ids
) {
}
