package kh.edu.istad.visora.domain.dto.request;

import kh.edu.istad.visora.domain.enums.EmailType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VerifyEmailPayload extends EmailPayload {

    private String familyName;
    private String givenName;
    private String username;
    private String email;
    private String token;
    private String url;

    public VerifyEmailPayload(String subject, List<String> recipients, String familyName, String givenName, String username, String email, String token, String url) {
        super(subject, recipients);
        this.familyName = familyName;
        this.givenName = givenName;
        this.username = username;
        this.email = email;
        this.token = token;
        this.url = url;
        this.setEmailType(EmailType.VERIFY_EMAIL);
    }
}
