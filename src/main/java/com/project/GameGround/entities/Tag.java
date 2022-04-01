package com.project.GameGround.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String tagName;

    public Tag(String tagName) {
        removeTagMistakes(tagName);
    }

    public Tag() {}

    public void removeTagMistakes(String tagName){
        if(tagName.split("#").length > 2 || tagName.endsWith("#")){  //if tag contains more than one "#"
            tagName = "#" + tagName.replace("#", "");
        }
        if(!(tagName.contains("#"))){  //if tag doesn't contains "#"
            tagName = "#" + tagName;
        }
        this.tagName = tagName;
    }
}