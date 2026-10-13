package kh.edu.istad.visora.domain.ports.inbound.usecase.notification;

import kh.edu.istad.visora.domain.dto.request.EmailPayload;

import java.util.List;

public interface EmailUseCase {

    void send(EmailPayload payload);

    void send(String subject, List<String> recipients, String html);

}
