package com.example.xiergc.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Article {
    private int id;
    private String title;
    private int authorId;
    private String content;
    private LocalDateTime publishDate;
    private int clicks;
    private int collect;
    private int likes;
    private int comment;

    private String authorName;  // 作者名字
    private String authorAvatarUrl;  // 作者头像 URL
}
