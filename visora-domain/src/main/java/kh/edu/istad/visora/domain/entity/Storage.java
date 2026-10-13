package kh.edu.istad.visora.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Storage extends Entity {

    private long id;

    private String uuid;

    private User owner;

    private String originalFileName;

    private String fileName;

    private String extension;

    private String mimeType;

    private String path;

}
