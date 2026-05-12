package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.dto.request.LoginRequest;
import com.Nhom20.DoAnPhamMem.dto.response.LoginResponse;
import com.Nhom20.DoAnPhamMem.dto.RegisterRequest;
import com.Nhom20.DoAnPhamMem.entity.InvalidatedTokenEntity;
import com.Nhom20.DoAnPhamMem.entity.TaiKhoanEntity;
import com.Nhom20.DoAnPhamMem.entity.VaiTroEntity;
import com.Nhom20.DoAnPhamMem.repository.InvalidatedTokenRepository;
import com.Nhom20.DoAnPhamMem.repository.TaiKhoanRepository;
import com.Nhom20.DoAnPhamMem.repository.VaiTroRepository;
import com.Nhom20.DoAnPhamMem.security.JwtTokenProvider;
import com.Nhom20.DoAnPhamMem.service.TaiKhoanService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    public TaiKhoanServiceImpl(AuthenticationManager authenticationManager,
                               JwtTokenProvider tokenProvider,
                               PasswordEncoder passwordEncoder,
                               TaiKhoanRepository taiKhoanRepository,
                               VaiTroRepository vaiTroRepository,
                               InvalidatedTokenRepository invalidatedTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.taiKhoanRepository = taiKhoanRepository;
        this.vaiTroRepository = vaiTroRepository;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getMatKhau()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo 2 token (access + refresh)
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        Long expiresIn = tokenProvider.getAccessTokenExpiresIn();

        TaiKhoanEntity taiKhoan = taiKhoanRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).tokenType("Bearer").expiresIn(expiresIn).userId(taiKhoan.getMaTaiKhoan()).email(taiKhoan.getEmail()).maVaiTro(taiKhoan.getVaiTro().getMaVaiTro()).build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String jwt = bearerToken.substring(7);
            LocalDateTime expiryTime = tokenProvider.getExpirationDateFromToken(jwt)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            InvalidatedTokenEntity invalidatedToken = InvalidatedTokenEntity.builder()
                    .id(jwt)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        }
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        if (taiKhoanRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Đoạn code cũ của Leader (Đã comment lại vì lỗi Duplicate Key khi tên vai trò là "Tình nguyện viên")
        // VaiTroEntity vaiTro = vaiTroRepository.findByTenVaiTro("TNV") // Mặc định là Tình nguyện viên
        // Đoạn code mới sửa: dùng findById để tìm theo Mã Vai Trò thay vì Tên
        VaiTroEntity vaiTro = vaiTroRepository.findById("TNV")
                .orElseGet(() -> {
                    VaiTroEntity newRole = new VaiTroEntity();
                    newRole.setMaVaiTro("TNV");
                    newRole.setTenVaiTro("Tình nguyện viên");
                    return vaiTroRepository.save(newRole);
                });

        TaiKhoanEntity taiKhoan = new TaiKhoanEntity();
        int soDuoiMaTaiKhoan = taiKhoanRepository.findMaMax();
        taiKhoan.setMaTaiKhoan(String.format("TK%05d", soDuoiMaTaiKhoan));
        taiKhoan.setEmail(registerRequest.getEmail());
        taiKhoan.setMatKhau(passwordEncoder.encode(registerRequest.getMatKhau()));
        taiKhoan.setVaiTro(vaiTro);
        taiKhoan.setTrangThai(true);

        taiKhoanRepository.save(taiKhoan);
    }
}
