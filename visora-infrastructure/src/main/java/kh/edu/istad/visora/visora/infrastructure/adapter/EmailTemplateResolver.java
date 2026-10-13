package kh.edu.istad.visora.visora.infrastructure.adapter;

import kh.edu.istad.visora.domain.dto.request.EmailPayload;
import kh.edu.istad.visora.domain.dto.request.PasswordResetPayload;
import kh.edu.istad.visora.domain.dto.request.VerifyEmailPayload;
import kh.edu.istad.visora.visora.infrastructure.config.EmailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class EmailTemplateResolver {

    private final TemplateEngine templateEngine;
    private final EmailProperties emailProperties;

    public String resolve(EmailPayload emailPayload){
        return switch (emailPayload){
            case VerifyEmailPayload payload -> this.resolve(payload);
            case PasswordResetPayload payload -> this.resolve(payload);
            default -> throw new RuntimeException("Invalid payload");
        };
    }

    public String resolve(VerifyEmailPayload payload){
        Context context = createEmailContext();
        context.setVariable("givenName", payload.getGivenName());
        context.setVariable("url", payload.getUrl());
        return templateEngine.process(payload.getEmailType().getTemplate(), context);
    }

    public String resolve(PasswordResetPayload payload){
        Context context = createEmailContext();
        context.setVariable("givenName", payload.getGivenName());
        context.setVariable("url", payload.getUrl());
        return templateEngine.process(payload.getEmailType().getTemplate(), context);
    }

    private Context createEmailContext(){
        Context context = new Context();
        context.setVariable("currentYear", String.valueOf(LocalDate.now().getYear()));
        context.setVariable("companyName", emailProperties.getDisplayName());
        context.setVariable("displayName", emailProperties.getDisplayName());
        context.setVariable("logo", emailProperties.getLogo());

        return context;
    }

}
