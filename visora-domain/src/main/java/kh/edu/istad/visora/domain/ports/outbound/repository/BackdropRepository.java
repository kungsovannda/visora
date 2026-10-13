package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.dto.request.BackdropFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.User;

import java.util.Optional;

public interface BackdropRepository {

    Backdrop save(Backdrop backdrop);

    Optional<Backdrop> findByUuid(String uuid);

    Page<Backdrop> findCurrentUserBackdrops(User owner, BackdropFilter filter);

}