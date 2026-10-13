package kh.edu.istad.visora.visora.infrastructure.adapter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kh.edu.istad.visora.domain.dto.request.EmailPayload;
import kh.edu.istad.visora.domain.ports.inbound.usecase.notification.EmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailUseCaseAdapter implements EmailUseCase {

    private final JavaMailSender javaMailSender;
    private final EmailTemplateResolver emailTemplateResolver;

    @Override
    public void send(EmailPayload payload) {
        this.send(payload.getSubject(), payload.getRecipients(), emailTemplateResolver.resolve(payload));
    }

    @Override
    public void send(String subject, List<String> recipients, String html) {
        for (String recipient : recipients) {
            this.send(subject, recipient, html);
        }
    }

    private void send(String subject, String recipient, String html){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom("noreply@istad.edu.kh", "VISORA");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(html, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMessage);
    }

}
