package com.project.GameGround.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReviewDTO {

    @NotNull
    private Long id;

    @NotBlank
    @Max(64)
    private String reviewName;

    @NotBlank
    @Max(32)
    private String groupName;

    @NotBlank
    private String text;
}
