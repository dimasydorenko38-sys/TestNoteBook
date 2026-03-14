package com.QoQTestProject.notes.dto.response;

import java.util.Map;

public record NoteStatisticResponseDto(
        Map<String, Long> statistic
) {
}
