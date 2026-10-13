package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.dto.request.TemplateFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.User;

import java.util.Optional;

public interface TemplateRepository {

    Template save(Template template);

    Optional<Template> findByUuid(String uuid);

    Optional<Template> findPendingBySourceBackdropUuid(String backdropUuid);

    Page<Template> findCurrentUserTemplates(User owner, TemplateFilter filter);

    Page<Template> findAllTemplates(TemplateFilter filter);

    void delete(Template template);

}