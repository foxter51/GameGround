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

    @Column(name = "tag_name", nullable = false, unique = true, length = 32)
    private String tagName;

    public Tag(String tagName) {
        removeTagMistakes(tagName);
    }

    public Tag() {}

    public void removeTagMistakes(String tagName){
        if(tagName.split("#").length > 2 || tagName.endsWith("#")){
            tagName = "#" + tagName.replace("#", "");
        }
        if(!(tagName.contains("#"))){
            tagName = "#" + tagName;
        }
        this.tagName = tagName;
    }
}
