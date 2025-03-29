package com.example.xiergc.mapper;

import com.example.xiergc.entity.Article;
import com.example.xiergc.dto.ArticleDTO;
import com.example.xiergc.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    ArticleDTO getArticleWithStatus(@Param("id") Long id, @Param("userId") Long userId);

    boolean existsLike(@Param("userId") Long userId, @Param("articleId") Long articleId);

    void addLike(@Param("userId") Long userId, @Param("articleId") Long articleId);

    void removeLike(@Param("userId") Long userId, @Param("articleId") Long articleId);

    boolean existsCollect(@Param("userId") Long userId, @Param("articleId") Long articleId);

    void addCollect(@Param("userId") Long userId, @Param("articleId") Long articleId);

    void removeCollect(@Param("userId") Long userId, @Param("articleId") Long articleId);

    void updateLikes(@Param("id") Long id, @Param("amount") int amount);

    void updateCollects(@Param("id") Long id, @Param("amount") int amount);

    List<Article> getArticlesRankedByClicks();

    List<Article> getLatestArticles();

    Article getArticleById(Long id);

    void addComment(Long articleId, Long authorId, String content);

    void addArticle(String title, Long authorId, String content);

    void incrementArticleClicks(Long id);

    Long addSubComment(Comment comment);

    void incrementCommentCount(Long articleId);

    Comment getCommentById(Long id);

    Long deleteCommentAndSubComments(@Param("commentId") Long commentId);

    void decrementCommentCount(@Param("articleId") Long articleId, @Param("count") Long count);

    List<Comment> GetComment(Long articleId);

    Long getAuthorIdById(Long articleId);

    void deleteArticle(Long articleId);

    List<Article> searchArticles(@Param("keyword") String keyword);
}
