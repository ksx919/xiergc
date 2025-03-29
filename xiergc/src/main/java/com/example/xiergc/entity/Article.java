package com.example.xiergc.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Article {
    private Long id;
    private String title;
    private Long authorId;
    private String content;
    private LocalDateTime publishDate;
    private Long clicks;
    private Long collect;
    private Long likes;
    private Long comment;

    private String authorName;  // 作者名字
    private String authorAvatarUrl;  // 作者头像 URL
}
