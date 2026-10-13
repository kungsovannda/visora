package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.TemplateEntity;
import kh.edu.istad.visora.domain.entity.Template;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BackdropDataMapper.class, UserDataMapper.class, CategoryDataMapper.class})
public interface TemplateDataMapper {

    Template toDomain(TemplateEntity entity);

    TemplateEntity toEntity(Template template);

}
