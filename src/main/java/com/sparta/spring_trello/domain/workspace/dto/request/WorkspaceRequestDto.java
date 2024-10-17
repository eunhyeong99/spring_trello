package com.sparta.spring_trello.domain.workspace.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceRequestDto {

    @NotNull
    private Long workspaceId;

    @NotNull
    private String title;

    @NotNull
    private String description;

}
