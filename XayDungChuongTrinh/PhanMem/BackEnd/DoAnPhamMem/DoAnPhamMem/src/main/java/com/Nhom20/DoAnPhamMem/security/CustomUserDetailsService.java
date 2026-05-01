package com.Nhom20.DoAnPhamMem.security;

import com.Nhom20.DoAnPhamMem.entity.TaiKhoanEntity;
import com.Nhom20.DoAnPhamMem.repository.TaiKhoanRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TaiKhoanRepository taiKhoanRepository;

    public CustomUserDetailsService(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        TaiKhoanEntity taiKhoan = taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

        return new User(
                taiKhoan.getEmail(),
                taiKhoan.getMatKhau(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + taiKhoan.getVaiTro().getTenVaiTro()))
        );
    }
}
