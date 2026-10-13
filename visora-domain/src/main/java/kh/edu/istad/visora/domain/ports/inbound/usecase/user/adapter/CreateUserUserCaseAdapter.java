package kh.edu.istad.visora.domain.ports.inbound.usecase.user.adapter;

import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.user.EmailAlreadyExistException;
import kh.edu.istad.visora.domain.exception.user.PhoneNumberAlreadyExistException;
import kh.edu.istad.visora.domain.exception.user.UsernameAlreadyExistException;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CreateUserUserCaseAdapter implements CreateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {

        if(userRepository.existsByUsername(user.getUsername())){
            throw new UsernameAlreadyExistException("User already exist");
        }
        if(userRepository.existsByPhoneNumber(user.getPhoneNumber())){
            throw new PhoneNumberAlreadyExistException("Phone number already exist");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistException("Email already exist");
        }
        user.setUuid(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

}
