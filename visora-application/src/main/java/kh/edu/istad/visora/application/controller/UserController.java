package kh.edu.istad.visora.application.controller;

import jakarta.validation.Valid;
import kh.edu.istad.visora.application.dto.base.ApiResponse;
import kh.edu.istad.visora.application.dto.role.AssignRoleRequest;
import kh.edu.istad.visora.application.dto.user.CreateUserRequest;
import kh.edu.istad.visora.application.dto.user.UpdateUserRequest;
import kh.edu.istad.visora.application.dto.user.UserResponse;
import kh.edu.istad.visora.application.mapper.UserApplicationMapper;
import kh.edu.istad.visora.domain.dto.request.UserFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.enums.UserSort;
import kh.edu.istad.visora.domain.ports.inbound.usecase.role.QueryRoleUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.CreateUserUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.DeleteUserUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.QueryUserUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.UpdateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
@RestController
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final QueryUserUseCase queryUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final QueryRoleUseCase queryRoleUseCase;
    private final UserApplicationMapper userApplicationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "25", required = false) int pageSize
    ) {
        var query = queryUserUseCase.getUsers(pageNumber, pageSize);
        var response = new ApiResponse<>(
                new Page<>(
                        query.contents().stream().map(userApplicationMapper::toResponse).toList(),
                        query.pageNumber(),
                        query.pageSize(),
                        query.totalElements(),
                        query.totalPages()
                ),
                200,
                true,
                "Retrieve users data"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> filterUsers(
            @RequestBody(required = false) UserFilter filter
    ) {
        var actual = (filter == null)
                ? new UserFilter(0, 25, null, null, UserSort.NEWEST)
                : normalize(filter);
        var query = queryUserUseCase.getUsers(actual);
        var response = new ApiResponse<>(
                new Page<>(
                        query.contents().stream().map(userApplicationMapper::toResponse).toList(),
                        query.pageNumber(),
                        query.pageSize(),
                        query.totalElements(),
                        query.totalPages()
                ),
                200,
                true,
                "Retrieve filtered users data"
        );
        return ResponseEntity.status(200).body(response);
    }

    private UserFilter normalize(UserFilter filter) {
        return new UserFilter(
                filter.pageNumber() == null || filter.pageNumber() < 0 ? 0 : filter.pageNumber(),
                filter.pageSize() == null || filter.pageSize() <= 0 ? 25 : filter.pageSize(),
                filter.search(),
                filter.role(),
                filter.sort() == null ? UserSort.NEWEST : filter.sort()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUuid(@PathVariable String uuid) {
        var query = queryUserUseCase.getUserByUuid(uuid);
        var response = new ApiResponse<>(
                userApplicationMapper.toResponse(query),
                200,
                true,
                "Retrieve user data"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        var create = createUserUseCase.createUser(userApplicationMapper.toDomain(request));
        var response = new ApiResponse<>(
                userApplicationMapper.toResponse(create),
                201,
                true,
                "User created successfully"
        );
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String uuid,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        var update = updateUserUseCase.updateUser(uuid, userApplicationMapper.toDomain(request));
        var response = new ApiResponse<>(
                userApplicationMapper.toResponse(update),
                200,
                true,
                "User updated successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String uuid) {
        deleteUserUseCase.deleteUserByUuid(uuid);
        var response = new ApiResponse<>(
                "",
                200,
                true,
                "User deleted successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/{uuid}/role")
    public ResponseEntity<ApiResponse<UserResponse>> assignRole(
            @PathVariable String uuid,
            @Valid @RequestBody AssignRoleRequest request
    ) {
        var user = queryUserUseCase.getUserByUuid(uuid);
        var role = queryRoleUseCase.getRoleByUuid(request.roleUuid());
        user.setRole(role);
        var update = updateUserUseCase.updateUser(uuid, user);
        var response = new ApiResponse<>(
                userApplicationMapper.toResponse(update),
                200,
                true,
                "Role assigned successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{uuid}/role")
    public ResponseEntity<ApiResponse<UserResponse>> unassignRole(@PathVariable String uuid) {
        var user = queryUserUseCase.getUserByUuid(uuid);
        user.setRole(null);
        var update = updateUserUseCase.updateUser(uuid, user);
        var response = new ApiResponse<>(
                userApplicationMapper.toResponse(update),
                200,
                true,
                "Role unassigned successfully"
        );
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(){
        var user = queryUserUseCase.getCurrentUser();
        var response = new ApiResponse<>(
                userApplicationMapper.toResponse(user),
                200,
                true,
                "Retrieve user data"
        );
        return ResponseEntity.status(200).body(response);
    }

}
