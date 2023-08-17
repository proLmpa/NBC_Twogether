package com.example.twogether.workspace.service;

import com.example.twogether.common.security.UserDetailsImpl;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.repository.UserRepository;
import com.example.twogether.workspace.dto.WorkspaceRequestDto;
import com.example.twogether.workspace.dto.WorkspaceResponseDto;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.repository.WorkspaceRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkspaceResponseDto createWorkspace(WorkspaceRequestDto workspaceRequestDto, User user){
        Workspace workspace = workspaceRequestDto.toEntity(user);
        workspaceRepository.save(workspace);
        return WorkspaceResponseDto.of(workspace);
    }


    //   @Transactional(readOnly = true)
    //   public List<WorkspaceResponseDto> getAllWorkspaces() {
    //       workspaceRepository.findAllByOrderByCreatedAtDesc().stream()
    //           .map(WorkspaceResponseDto::new)
    //           .collect(Collectors.toList());
    //   }

     @Transactional(readOnly = true)
     public List<WorkspaceResponseDto> getAllWorkspaces() {
//         List<Workspace> workspaces = workspaceRepository.findAllByUserOrderByCreatedAtDesc(user);
//         return WorkspaceResponseDto.of(workspaces);
         return workspaceRepository.findAll().stream().map(WorkspaceResponseDto::of).toList();
     }

    @Transactional(readOnly = true)
    public WorkspaceResponseDto getWorkspace(User user, Long Id) {
        Workspace workspace = findWorkspace(user, Id);
        return WorkspaceResponseDto.of(workspace);
    }




    private Workspace findWorkspace(User user, Long workspaceId) {
        return workspaceRepository.findById(workspaceId).orElseThrow();
    }

}
