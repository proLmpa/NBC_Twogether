package com.example.twogether.workspace.service;

import com.example.twogether.user.entity.User;
import com.example.twogether.user.repository.UserRepository;
import com.example.twogether.workspace.dto.WorkspaceRequestDto;
import com.example.twogether.workspace.dto.WorkspaceResponseDto;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createWorkspace(WorkspaceRequestDto workspaceRequestDto, User user){
        Workspace workspace = workspaceRequestDto.toEntity(user);
        workspaceRepository.save(workspace);
        //return workspaceResponseDto.of(workspace);
    }
}
