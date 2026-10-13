package kh.edu.istad.visora.domain.enums;

public enum EmailType {
    VERIFY_EMAIL("verify-email"),
    PASSWORD_RESET("password-reset");

    private final String template;
    private final String templatePath = "email/";

    EmailType(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return this.templatePath + template;
    }
}
