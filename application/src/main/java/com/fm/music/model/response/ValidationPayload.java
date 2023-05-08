package com.fm.music.model.response;

import java.util.List;

public record ValidationPayload(String code, List<String> validationFails) {
}
