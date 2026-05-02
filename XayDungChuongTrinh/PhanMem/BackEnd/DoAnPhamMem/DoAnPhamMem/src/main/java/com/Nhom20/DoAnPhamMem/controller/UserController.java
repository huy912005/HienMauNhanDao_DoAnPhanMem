package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.UserRequest;
import com.Nhom20.DoAnPhamMem.dto.response.UserResponse;
import com.Nhom20.DoAnPhamMem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .status(true)
                .message("Tạo user thành công")
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable String id, @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .status(true)
                .message("Cập nhật user thành công")
                .data(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable String id) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .status(true)
                .message("Lấy thông tin user thành công")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        List<UserResponse> response = userService.getAll();
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .status(true)
                .message("Lấy danh sách user thành công")
                .data(response)
                .build());
    }
}
