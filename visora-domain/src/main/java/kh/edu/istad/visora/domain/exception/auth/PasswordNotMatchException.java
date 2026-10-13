package kh.edu.istad.visora.domain.exception.auth;

public class PasswordNotMatchException extends DomainAuthException {
    public PasswordNotMatchException(String message) {
        super(message);
    }

  public PasswordNotMatchException(String message, Throwable cause) {
    super(message, cause);
  }
}
