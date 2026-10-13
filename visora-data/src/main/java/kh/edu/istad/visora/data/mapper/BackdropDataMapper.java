package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.BackdropEntity;
import kh.edu.istad.visora.data.entity.vo.AnimationEntity;
import kh.edu.istad.visora.data.entity.vo.ComponentEntity;
import kh.edu.istad.visora.data.entity.vo.GroupEntity;
import kh.edu.istad.visora.data.entity.vo.PageEntity;
import kh.edu.istad.visora.data.entity.vo.PositionEntity;
import kh.edu.istad.visora.data.entity.vo.SizeEntity;
import kh.edu.istad.visora.data.entity.vo.TransitionEntity;
import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.vo.Animation;
import kh.edu.istad.visora.domain.entity.vo.Component;
import kh.edu.istad.visora.domain.entity.vo.Group;
import kh.edu.istad.visora.domain.entity.vo.Page;
import kh.edu.istad.visora.domain.entity.vo.Position;
import kh.edu.istad.visora.domain.entity.vo.Size;
import kh.edu.istad.visora.domain.entity.vo.Transition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserDataMapper.class})
public interface BackdropDataMapper {

    Backdrop toDomain(BackdropEntity entity);

    BackdropEntity toEntity(Backdrop domain);

    PageEntity toPageEntity(Page page);

    Page toPageDomain(PageEntity entity);

    ComponentEntity toComponentEntity(Component component);

    Component toComponentDomain(ComponentEntity entity);

    PositionEntity toPositionEntity(Position position);

    Position toPositionDomain(PositionEntity entity);

    SizeEntity toSizeEntity(Size size);

    Size toSizeDomain(SizeEntity entity);

    AnimationEntity toAnimationEntity(Animation animation);

    Animation toAnimationDomain(AnimationEntity entity);

    GroupEntity toGroupEntity(Group group);

    Group toGroupDomain(GroupEntity entity);

    TransitionEntity toTransitionEntity(Transition transition);

    Transition toTransitionDomain(TransitionEntity entity);

}