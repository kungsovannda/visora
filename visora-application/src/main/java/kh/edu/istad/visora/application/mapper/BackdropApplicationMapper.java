package kh.edu.istad.visora.application.mapper;

import kh.edu.istad.visora.application.dto.backdrop.request.AnimationRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.CanvasRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.ComponentRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.CreateBackdropRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.GroupRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.PageRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.PositionRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.SizeRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.TransitionRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.UpdateBackdropRequest;
import kh.edu.istad.visora.application.dto.backdrop.response.AnimationResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.BackdropResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.BackdropSummary;
import kh.edu.istad.visora.application.dto.backdrop.response.CanvasResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.ComponentResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.GroupResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.PageResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.PositionResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.SizeResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.TransitionResponse;
import kh.edu.istad.visora.application.dto.template.request.CreateTemplateRequest;
import kh.edu.istad.visora.application.dto.template.request.SubmitTemplateRequest;
import kh.edu.istad.visora.application.dto.template.request.UpdateTemplateRequest;
import kh.edu.istad.visora.application.dto.template.response.TemplateResponse;
import kh.edu.istad.visora.application.dto.template.response.TemplateSummary;
import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.vo.Animation;
import kh.edu.istad.visora.domain.entity.vo.Component;
import kh.edu.istad.visora.domain.entity.vo.Group;
import kh.edu.istad.visora.domain.entity.vo.Page;
import kh.edu.istad.visora.domain.entity.vo.Position;
import kh.edu.istad.visora.domain.entity.vo.Size;
import kh.edu.istad.visora.domain.entity.vo.Transition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BackdropApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "pageCount", ignore = true)
    @Mapping(target = "hasTimer", ignore = true)
    @Mapping(target = "canExport", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastOpenedAt", ignore = true)
    @Mapping(target = "lastSavedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Backdrop toDomain(CreateBackdropRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "pageCount", ignore = true)
    @Mapping(target = "hasTimer", ignore = true)
    @Mapping(target = "canExport", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lastOpenedAt", ignore = true)
    @Mapping(target = "lastSavedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Backdrop toDomain(UpdateBackdropRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "clientSchemaVersion", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "sourceTemplateUuid", ignore = true)
    @Mapping(target = "sourceBackdropUuid", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "orientation", ignore = true)
    @Mapping(target = "canvas", ignore = true)
    @Mapping(target = "pages", ignore = true)
    @Mapping(target = "pageCount", ignore = true)
    @Mapping(target = "hasTimer", ignore = true)
    @Mapping(target = "canExport", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "templateStatus", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastOpenedAt", ignore = true)
    @Mapping(target = "lastSavedAt", ignore = true)
    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Template toDomain(SubmitTemplateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "pageCount", ignore = true)
    @Mapping(target = "hasTimer", ignore = true)
    @Mapping(target = "canExport", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "templateStatus", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "sourceBackdropUuid", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastOpenedAt", ignore = true)
    @Mapping(target = "lastSavedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "proposedName", source = "name")
    Template toDomain(CreateTemplateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "pageCount", ignore = true)
    @Mapping(target = "hasTimer", ignore = true)
    @Mapping(target = "canExport", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "templateStatus", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "proposedName", ignore = true)
    @Mapping(target = "sourceBackdropUuid", ignore = true)
    @Mapping(target = "sourceTemplateUuid", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "lastOpenedAt", ignore = true)
    @Mapping(target = "lastSavedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Template toDomain(UpdateTemplateRequest request);

    Page toDomain(PageRequest request);

    Component toDomain(ComponentRequest request);

    Position toDomain(PositionRequest request);

    Size toDomain(SizeRequest request);

    Size toDomain(CanvasRequest request);

    Animation toDomain(AnimationRequest request);

    Group toDomain(GroupRequest request);

    Transition toDomain(TransitionRequest request);

    @Mapping(target = "ownerUuid", source = "owner.uuid")
    BackdropSummary toSummary(Backdrop backdrop);

    @Mapping(target = "ownerUuid", source = "owner.uuid")
    BackdropResponse toResponse(Backdrop backdrop);

    @Mapping(target = "ownerUuid", source = "owner.uuid")
    @Mapping(target = "submittedBy", source = "submittedBy.uuid")
    TemplateSummary toTemplateSummary(Template template);

    @Mapping(target = "ownerUuid", source = "owner.uuid")
    @Mapping(target = "submittedBy", source = "submittedBy.uuid")
    TemplateResponse toTemplateResponse(Template template);

    PageResponse toPageResponse(Page page);

    ComponentResponse toComponentResponse(Component component);

    PositionResponse toPositionResponse(Position position);

    SizeResponse toSizeResponse(Size size);

    CanvasResponse toCanvasResponse(Size size);

    AnimationResponse toAnimationResponse(Animation animation);

    GroupResponse toGroupResponse(Group group);

    TransitionResponse toTransitionResponse(Transition transition);

}