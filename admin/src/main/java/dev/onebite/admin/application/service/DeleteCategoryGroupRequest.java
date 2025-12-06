package dev.onebite.admin.application.service;

import java.util.List;

public record DeleteCategoryGroupRequest(
        List<Long> ids,
        boolean force
) {
}
