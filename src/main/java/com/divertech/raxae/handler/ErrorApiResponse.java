package com.divertech.raxae.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorApiResponse {
	private String message;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String description;

	@JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> details;
}