package kh.edu.istad.visora.domain.ports;

import kh.edu.istad.visora.domain.entity.User;

public interface CurrentUser {

    User get();

    String getUuid();

}
