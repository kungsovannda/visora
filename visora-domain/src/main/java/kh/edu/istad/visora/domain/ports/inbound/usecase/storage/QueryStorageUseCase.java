package kh.edu.istad.visora.domain.ports.inbound.usecase.storage;

import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Storage;
import kh.edu.istad.visora.domain.exception.storage.StorageNotFoundException;
import kh.edu.istad.visora.domain.exception.user.UserNotFoundException;
import kh.edu.istad.visora.domain.ports.outbound.repository.S3Repository;
import kh.edu.istad.visora.domain.ports.outbound.repository.StorageRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class QueryStorageUseCase {

    private final StorageRepository storageRepository;
    private final S3Repository s3Repository;
    private final UserRepository userRepository;

    public Storage getByFileName(String fileName) {
        return storageRepository.findByFileName(fileName)
                .orElseThrow(() -> new StorageNotFoundException("Storage file not found"));
    }

    public InputStream getContent(String path) {
        return s3Repository.get(path);
    }

    public Page<Storage> getAllStorages(int pageNumber, int pageSize){
        return storageRepository.findAll(pageNumber, pageSize);
    }

    public Page<Storage> getAllStoragesByUser(int pageNumber, int pageSize, String userUuid){
        var user = userRepository.findUserByUuid(userUuid).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        return storageRepository.findAllByUser(pageNumber, pageSize, user);

    }


}