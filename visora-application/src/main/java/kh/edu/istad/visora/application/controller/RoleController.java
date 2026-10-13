package kh.edu.istad.visora.application.controller;

import jakarta.validation.Valid;
import kh.edu.istad.visora.application.dto.base.ApiResponse;
import kh.edu.istad.visora.application.dto.role.CreateRoleRequest;
import kh.edu.istad.visora.application.dto.role.RoleResponse;
import kh.edu.istad.visora.application.dto.role.UpdateRoleRequest;
import kh.edu.istad.visora.application.mapper.RoleApplicationMapper;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.ports.inbound.usecase.role.CreateRoleUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.role.DeleteRoleUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.role.QueryRoleUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.role.UpdateRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/roles")
@RestController
public class RoleController {

    private final CreateRoleUseCase createRoleUseCase;
    private final QueryRoleUseCase queryRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final RoleApplicationMapper roleApplicationMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody CreateRoleRequest request) {
        var create = createRoleUseCase.createRole(roleApplicationMapper.toDomain(request));
        var response = new ApiResponse<>(
                roleApplicationMapper.toResponse(create),
                201,
                true,
                "Role created successfully"
        );
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RoleResponse>>> getRoles(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "25", required = false) int pageSize
    ) {
        var query = queryRoleUseCase.getRoles(pageNumber, pageSize);
        var response = new ApiResponse<>(
                new Page<>(
                        query.contents().stream().map(roleApplicationMapper::toResponse).toList(),
                        query.pageNumber(),
                        query.pageSize(),
                        query.totalElements(),
                        query.totalPages()
                ),
                200,
                true,
                "Retrieve roles data"
        );
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleByUuid(@PathVariable String uuid) {
        var query = queryRoleUseCase.getRoleByUuid(uuid);
        var response = new ApiResponse<>(
                roleApplicationMapper.toResponse(query),
                200,
                true,
                "Retrieve role data"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable String uuid,
            @Valid @RequestBody UpdateRoleRequest request
    ) {
        var update = updateRoleUseCase.updateRole(uuid, roleApplicationMapper.toDomain(request));
        var response = new ApiResponse<>(
                roleApplicationMapper.toResponse(update),
                200,
                true,
                "Role updated successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<String>> deleteRole(@PathVariable String uuid) {
        deleteRoleUseCase.deleteRoleByUuid(uuid);
        var response = new ApiResponse<>(
                "",
                200,
                true,
                "Role deleted successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

}
