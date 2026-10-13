package kh.edu.istad.visora.visora.infrastructure.adapter;

import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import kh.edu.istad.visora.visora.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserAdapter implements CurrentUser {

    private final UserRepository userRepository;

    @Override
    public User get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        System.out.println(auth.getPrincipal());
        if (auth.getPrincipal() instanceof User user) {
            return user;
        }
        if (auth.getPrincipal() instanceof Jwt jwt) {
            String identifier = jwt.getClaimAsString("uid");
            if (identifier == null) {
                identifier = jwt.getSubject();
            }
            return identifier != null
                    ? userRepository.findUserByUuid(identifier).orElse(null)
                    : null;
        }
        return null;
    }

    @Override
    public String getUuid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        if (auth.getPrincipal() instanceof UserPrincipal user) {
            return user.getUsername();
        }
        if (auth.getPrincipal() instanceof Jwt jwt) {
            String identifier = jwt.getClaimAsString("uid");
            if (identifier == null) {
                identifier = jwt.getSubject();
            }
            return identifier;
        }
        return null;
    }
}