package kh.edu.istad.visora.data.config;

import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditConfig {

    private final CurrentUser currentUser;

    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> {
            String user = currentUser.getUuid();
            return user != null && !user.isBlank()
                    ? Optional.of(user)
                    : Optional.of("system");
        };
    }

}
