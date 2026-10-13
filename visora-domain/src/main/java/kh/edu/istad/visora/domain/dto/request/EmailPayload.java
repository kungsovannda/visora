package kh.edu.istad.visora.domain.dto.request;

import kh.edu.istad.visora.domain.enums.EmailType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class EmailPayload {

    private String subject;

    private EmailType emailType;

    private List<String> recipients;

    public EmailPayload(String subject, List<String> recipients) {
        this.subject = subject;
        this.recipients = recipients;
    }

}


