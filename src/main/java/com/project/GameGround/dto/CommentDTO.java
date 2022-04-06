package com.project.GameGround.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String text;
}
