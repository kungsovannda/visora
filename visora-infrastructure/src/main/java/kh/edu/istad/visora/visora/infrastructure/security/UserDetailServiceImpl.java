package kh.edu.istad.visora.visora.infrastructure.security;

import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.user.UserNotFoundException;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.QueryUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final QueryUserUseCase queryUserUseCase;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        try {
            User user = findByUuidOrEmail(identifier);
            return new UserPrincipal(user);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User not found : " + identifier, e);
        }
    }

    private User findByUuidOrEmail(String identifier) {
        try {
            return queryUserUseCase.getUserByUuid(identifier);
        } catch (UserNotFoundException e) {
            return queryUserUseCase.getUserByEmail(identifier);
        }
    }

}