package kh.edu.istad.visora.data.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@Entity
public class CategoryEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    private String description;

    private String icon;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<TemplateEntity> templates;

}
