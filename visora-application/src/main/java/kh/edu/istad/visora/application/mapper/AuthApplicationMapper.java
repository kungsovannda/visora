package kh.edu.istad.visora.application.mapper;

import kh.edu.istad.visora.application.dto.auth.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthApplicationMapper {

    @Mapping(target = "givenName", source = "firstName")
    @Mapping(target = "familyName", source = "lastName")
    kh.edu.istad.visora.domain.dto.auth.RegisterRequest toRegisterDomain(RegisterRequest request);

}