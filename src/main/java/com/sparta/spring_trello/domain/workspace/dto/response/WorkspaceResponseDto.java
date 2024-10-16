package com.sparta.spring_trello.domain.workspace.dto.response;

import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import lombok.Getter;

@Getter
public class WorkspaceResponseDto {

    private final Long id;
    private final Long workspaceId;
    private final String title;
    private final String description;


    public WorkspaceResponseDto(Workspace workspace) {
        this.id = getId();
        this.workspaceId = getWorkspaceId();
        this.title = getTitle();
        this.description = getDescription();
    }
}
