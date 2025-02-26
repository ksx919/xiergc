package com.example.xiergc.entity;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private int id;
    @Column(name = "article_id")
    private int articleId;
    @Column(name = "author_id")
    private int authorId;
    private String content;
    @Column(name = "publish_date")
    private LocalDateTime publishDate; // 使用Java 8时间类型
    @Column(name = "reply_to")
    private Integer replyTo;
}
