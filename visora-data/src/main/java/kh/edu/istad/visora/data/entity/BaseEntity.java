package kh.edu.istad.visora.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity extends Auditable {

    @Column(nullable = false)
    private boolean isDeleted;

}
