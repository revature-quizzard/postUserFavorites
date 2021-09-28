package com.revature.post_user_favorites;

import lombok.Data;

import java.util.List;

@Data
public class SetDTO {

    private String id;
    private String name;
    private List<Tag> tags;
    private boolean isPublic;
    private int views;
    private int plays;
    private int studies;
    private int favorites;

}
