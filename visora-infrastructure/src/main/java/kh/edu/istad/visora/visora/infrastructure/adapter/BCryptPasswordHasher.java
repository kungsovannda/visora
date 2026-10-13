package kh.edu.istad.visora.visora.infrastructure.adapter;

import kh.edu.istad.visora.domain.ports.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordHasher implements PasswordHasher {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

}