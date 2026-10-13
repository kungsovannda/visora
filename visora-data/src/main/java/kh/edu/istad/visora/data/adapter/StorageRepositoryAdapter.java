package kh.edu.istad.visora.data.adapter;

import kh.edu.istad.visora.data.mapper.StorageDataMapper;
import kh.edu.istad.visora.data.mapper.UserDataMapper;
import kh.edu.istad.visora.data.repository.StorageJpaRepository;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Storage;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.ports.outbound.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StorageRepositoryAdapter implements StorageRepository {

    private final StorageJpaRepository storageJpaRepository;
    private final StorageDataMapper storageDataMapper;
    private final UserDataMapper userDataMapper;

    @Override
    public Storage save(Storage storage) {
        return storageDataMapper.toDomain(storageJpaRepository.save(storageDataMapper.toEntity(storage)));
    }

    @Override
    public Optional<Storage> findByFileName(String fileName) {
        return storageJpaRepository.findByFileName(fileName).map(storageDataMapper::toDomain);
    }

    @Override
    public Page<Storage> findAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        var query = storageJpaRepository.findAll(pageRequest);
        return new Page<>(
                query.getContent().stream().map(storageDataMapper::toDomain).toList(),
                query.getNumber(),
                query.getSize(),
                query.getTotalElements(),
                query.getTotalPages()
        );
    }

    @Override
    public Page<Storage> findAllByUser(int pageNumber, int pageSize, User user) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        var query = storageJpaRepository.findAllByOwner(pageRequest, userDataMapper.toEntity(user));
        return new Page<>(
                query.getContent().stream().map(storageDataMapper::toDomain).toList(),
                query.getNumber(),
                query.getSize(),
                query.getTotalElements(),
                query.getTotalPages()
        );
    }
}