package com.example.xiergc.entity;

import lombok.Data;

@Data
public class ArticleDTO {
    private int id;
    private String title;
    private String content;
    private Boolean isLiked;
    private Boolean isCollected;
}
