package com.fm.music.model.response.wrapper;

import java.util.List;

public record ValidationPayload(String code, List<String> validationFails) {
}
