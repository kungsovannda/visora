package kh.edu.istad.visora.main;

import kh.edu.istad.visora.domain.dto.request.VerifyEmailPayload;
import kh.edu.istad.visora.domain.ports.inbound.usecase.notification.EmailUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class VisoraMainApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    EmailUseCase emailUseCase;

    @Test
    void sendTestEmail(){
        emailUseCase.send(new VerifyEmailPayload(
                "Verify Email - Test",
                List.of("sovanndakung@gmail.com"),
                "Kung",
                "Sovannda",
                "kungsovannda",
                "sovanndakung@gmail.com",
                "token",
                "https://example.com/hello"
        ));

    }

}
