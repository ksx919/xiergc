package com.example.xiergc.service;

import com.example.xiergc.entity.Article;
import com.example.xiergc.entity.ArticleDTO;
import com.example.xiergc.entity.Comment;

import java.util.List;

public interface ArticleService {

    // 获取按点击量排序的推荐文章
    List<Article> getRankingArticles();

    // 获取最新发布的文章
    List<Article> getLatestArticles();

    // 获取指定 ID 的文章内容
    Article getArticleById(int id);

    // 添加评论
    void addComment(Comment comment);

    // 发布新文章
    void publishArticle(Article article);

    void incrementArticleClicks(int id);

    ArticleDTO getArticleWithStatus(int id, int userId);

    void toggleLike(int articleId, int userId);

    void toggleCollect(int articleId, int userId);

    Comment addSubComment(int articleId, int parentCommentId, Comment subComment);

    void deleteComment(int articleId, int commentId);
}
