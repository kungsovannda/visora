package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Storage;
import kh.edu.istad.visora.domain.entity.User;

import java.util.Optional;

public interface StorageRepository {

    Page<Storage> findAll(int pageNumber, int pageSize);

    Page<Storage> findAllByUser(int pageNumber, int pageSize, User user);

    Storage save(Storage storage);

    Optional<Storage> findByFileName(String fileName);

}
