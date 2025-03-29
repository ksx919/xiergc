package com.example.xiergc.dto;

import lombok.Data;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private Boolean isLiked;
    private Boolean isCollected;
}
