package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.entity.TaiKhoanEntity;
import com.Nhom20.DoAnPhamMem.entity.VaiTroEntity;
import com.Nhom20.DoAnPhamMem.repository.TaiKhoanRepository;
import com.Nhom20.DoAnPhamMem.repository.VaiTroRepository;
import com.Nhom20.DoAnPhamMem.dto.request.UserRequest;
import com.Nhom20.DoAnPhamMem.dto.response.UserResponse;
import com.Nhom20.DoAnPhamMem.mapper.UserMapper;
import com.Nhom20.DoAnPhamMem.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(TaiKhoanRepository taiKhoanRepository, VaiTroRepository vaiTroRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.vaiTroRepository = vaiTroRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse create(UserRequest request) {
        if (taiKhoanRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống");
        }

        TaiKhoanEntity entity = userMapper.toEntity(request);
        entity.setMaTaiKhoan(UUID.randomUUID().toString().substring(0, 10));
        entity.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        entity.setTrangThai(true);

        VaiTroEntity vaiTro = vaiTroRepository.findByTenVaiTro("VOLUNTEER")
                .orElseGet(() -> {
                    VaiTroEntity newRole = new VaiTroEntity();
                    newRole.setMaVaiTro("ROLE_VOL");
                    newRole.setTenVaiTro("VOLUNTEER");
                    return vaiTroRepository.save(newRole);
                });
        entity.setVaiTro(vaiTro);

        TaiKhoanEntity saved = taiKhoanRepository.save(entity);
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponse update(String id, UserRequest request) {
        TaiKhoanEntity existing = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        
        existing.setEmail(request.getEmail());
        if (request.getMatKhau() != null && !request.getMatKhau().isEmpty()) {
            existing.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        }
        
        TaiKhoanEntity updated = taiKhoanRepository.save(existing);
        return userMapper.toResponse(updated);
    }

    @Override
    public UserResponse getById(String id) {
        TaiKhoanEntity entity = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        return userMapper.toResponse(entity);
    }

    @Override
    public List<UserResponse> getAll() {
        return taiKhoanRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
}
