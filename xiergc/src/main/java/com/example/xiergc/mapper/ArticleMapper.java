package com.example.xiergc.mapper;

import com.example.xiergc.entity.Article;
import com.example.xiergc.entity.ArticleDTO;
import com.example.xiergc.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("SELECT a.*, u.name AS authorName, u.avatar_url AS authorAvatarUrl, " +
            "EXISTS(SELECT 1 FROM article_likes WHERE user_id = #{userId} AND article_id = a.id) AS isLiked, " +
            "EXISTS(SELECT 1 FROM article_collections WHERE user_id = #{userId} AND article_id = a.id) AS isCollected " +
            "FROM articles a " +
            "JOIN user u ON a.author_id = u.id " +
            "WHERE a.id = #{id}")
    ArticleDTO getArticleWithStatus(@Param("id") int id, @Param("userId") int userId);

    @Select("SELECT COUNT(*) FROM article_likes WHERE user_id = #{userId} AND article_id = #{articleId}")
    boolean existsLike(@Param("userId") int userId, @Param("articleId") int articleId);

    @Insert("INSERT INTO article_likes (user_id, article_id) VALUES (#{userId}, #{articleId})")
    void addLike(@Param("userId") int userId, @Param("articleId") int articleId);

    @Delete("DELETE FROM article_likes WHERE user_id = #{userId} AND article_id = #{articleId}")
    void removeLike(@Param("userId") int userId, @Param("articleId") int articleId);

    @Select("SELECT COUNT(*) FROM article_collections WHERE user_id = #{userId} AND article_id = #{articleId}")
    boolean existsCollect(@Param("userId") int userId, @Param("articleId") int articleId);

    @Insert("INSERT INTO article_collections (user_id, article_id) VALUES (#{userId}, #{articleId})")
    void addCollect(@Param("userId") int userId, @Param("articleId") int articleId);

    @Delete("DELETE FROM article_collections WHERE user_id = #{userId} AND article_id = #{articleId}")
    void removeCollect(@Param("userId") int userId, @Param("articleId") int articleId);

    @Update("UPDATE articles SET likes = likes + #{amount} WHERE id = #{id}")
    void updateLikes(@Param("id") int id, @Param("amount") int amount);

    @Update("UPDATE articles SET collect = collect + #{amount} WHERE id = #{id}")
    void updateCollects(@Param("id") int id, @Param("amount") int amount);

    @Select("SELECT a.*,u.name as authorName,u.avatar_url as authorAvatarUrl FROM articles a left join user u on a.author_id=u.id ORDER BY clicks DESC LIMIT 10")
    List<Article> getArticlesRankedByClicks();

    @Select("SELECT a.*,u.name as authorName,u.avatar_url as authorAvatarUrl FROM articles a left join user u on a.author_id=u.id ORDER BY publish_date DESC LIMIT 10")
    List<Article> getLatestArticles();

    @Select("SELECT a.*, u.name AS authorName, u.avatar_url AS authorAvatarUrl " +
            "FROM articles a " +
            "JOIN user u ON a.author_id = u.id " +
            "WHERE a.id = #{id}")
    Article getArticleById(int id);

    @Insert("INSERT INTO article_likes (user_id, article_id) VALUES (#{userId}, #{articleId})")
    void addArticleLike(int userId, int articleId);

    @Insert("INSERT INTO article_collections (user_id, article_id) VALUES (#{userId}, #{articleId})")
    void addArticleCollection(int userId, int articleId);

    @Update("UPDATE articles SET likes = likes + 1 WHERE id = #{id}")
    void incrementArticleLikes(int id);

    @Update("UPDATE articles SET collect = collect + 1 WHERE id = #{id}")
    void incrementArticleCollect(int id);

    @Insert("INSERT INTO comments (article_id, author_id, content, publish_date) " +
            "VALUES (#{articleId}, #{authorId}, #{content}, NOW())")
    void addComment(int articleId,int authorId,String content);

    @Insert("INSERT INTO articles (title, author_id, content, publish_date) " +
            "VALUES (#{title}, #{authorId}, #{content}, NOW())")
    void addArticle(String title,int authorId,String content);

    @Update("UPDATE articles SET clicks = clicks + 1 WHERE id = #{id}")
    void incrementArticleClicks(int id);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO comments (article_id, author_id, content, publish_date, reply_to) " +
            "VALUES (#{articleId}, #{authorId}, #{content}, NOW(), #{replyTo})")
    int addSubComment(Comment comment);

    @Update("UPDATE articles SET comment = comment + 1 WHERE id = #{articleId}")
    void incrementCommentCount(int articleId);

    @Select("SELECT * FROM comments WHERE id = #{id}")
    @Results({
            @Result(property = "articleId", column = "article_id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "publishDate", column = "publish_date"),
            @Result(property = "replyTo", column = "reply_to")
    })
    Comment getCommentById(int id);

    @Delete("DELETE FROM comments WHERE id = #{commentId} OR reply_to = #{commentId}")
    int deleteCommentAndSubComments(@Param("commentId") int commentId);

    @Update("UPDATE articles SET comment = comment - #{count} WHERE id = #{articleId}")
    void decrementCommentCount(@Param("articleId") int articleId, @Param("count") int count);
}
