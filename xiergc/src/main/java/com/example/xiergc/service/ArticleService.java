package com.example.xiergc.service;

import com.example.xiergc.entity.Article;
import com.example.xiergc.dto.ArticleDTO;
import com.example.xiergc.entity.Comment;

import java.util.List;

public interface ArticleService {

    // 获取按点击量排序的推荐文章
    List<Article> getRankingArticles();

    // 获取最新发布的文章
    List<Article> getLatestArticles();

    // 获取指定 ID 的文章内容
    Article getArticleById(Long id);

    // 添加评论
    void addComment(Comment comment);

    // 发布新文章
    void publishArticle(Article article);

    void incrementArticleClicks(Long id);

    ArticleDTO getArticleWithStatus(Long id, Long userId);

    void toggleLike(Long articleId, Long userId);

    void toggleCollect(Long articleId, Long userId);

    Comment addSubComment(Long articleId, Long parentCommentId, Comment subComment);

    void deleteComment(Long articleId, Long commentId);

    List<Comment> GetComment(Long articleId);

    void deleteArticle(Long articleId);

    Long getAuthorIdById(Long articleId);

    List<Article> searchArticles(String keyword);
}
