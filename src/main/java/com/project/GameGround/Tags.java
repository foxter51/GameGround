package com.project.GameGround;

import lombok.Data;

@Data
public class Tags {
    private String tagsString;

    public Tags(){}

    public Tags(String tagsString){
        this.tagsString = tagsString;
    }
}
