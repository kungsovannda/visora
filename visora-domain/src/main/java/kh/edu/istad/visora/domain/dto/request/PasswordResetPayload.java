package kh.edu.istad.visora.domain.dto.request;

import kh.edu.istad.visora.domain.enums.EmailType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PasswordResetPayload extends EmailPayload {

    private String givenName;
    private String familyName;
    private String username;
    private String token;
    private String url;

    public PasswordResetPayload(String subject, List<String> recipients, String givenName, String familyName, String username, String token, String url) {
        super(subject, recipients);
        this.givenName = givenName;
        this.familyName = familyName;
        this.username = username;
        this.token = token;
        this.url = url;
        this.setEmailType(EmailType.PASSWORD_RESET);
    }
}