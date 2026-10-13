package kh.edu.istad.visora.domain.ports.inbound.usecase.user.adapter;

import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.user.EmailAlreadyExistException;
import kh.edu.istad.visora.domain.exception.user.PhoneNumberAlreadyExistException;
import kh.edu.istad.visora.domain.exception.user.UserNotFoundException;
import kh.edu.istad.visora.domain.exception.user.UsernameAlreadyExistException;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.UpdateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateUserUseCaseAdapter implements UpdateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public User updateUser(String uuid, User user) {
        User existingUser = userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!existingUser.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistException("Username already exist");
        }
        if (!existingUser.getPhoneNumber().equals(user.getPhoneNumber()) && userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistException("Phone number already exist");
        }
        if (!existingUser.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistException("Email already exist");
        }

        existingUser.setFamilyName(user.getFamilyName());
        existingUser.setGivenName(user.getGivenName());
        existingUser.setGender(user.getGender());
        existingUser.setBirthdate(user.getBirthdate());
        existingUser.setPicture(user.getPicture());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setEmail(user.getEmail());
        existingUser.setEmailVerified(user.isEmailVerified());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

}
