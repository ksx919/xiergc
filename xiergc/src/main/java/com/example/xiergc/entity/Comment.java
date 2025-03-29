package com.example.xiergc.entity;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    @Column(name = "article_id")
    private Long articleId;
    @Column(name = "author_id")
    private Long authorId;
    private String content;
    @Column(name = "publish_date")
    private LocalDateTime publishDate; // 使用Java 8时间类型
    @Column(name = "reply_to")
    private Long replyTo;
    private String authorName;
    private String authorAvatar;
}
