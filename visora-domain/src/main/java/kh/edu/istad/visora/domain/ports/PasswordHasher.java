package kh.edu.istad.visora.domain.ports;

public interface PasswordHasher {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String hashedPassword);

}