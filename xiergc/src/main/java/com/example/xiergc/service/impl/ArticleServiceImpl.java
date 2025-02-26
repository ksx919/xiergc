package com.example.xiergc.service.impl;

import com.example.xiergc.entity.Article;
import com.example.xiergc.entity.ArticleDTO;
import com.example.xiergc.entity.Comment;
import com.example.xiergc.mapper.ArticleMapper;
import com.example.xiergc.service.ArticleService;
import com.example.xiergc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Article> getRankingArticles() {
        return articleMapper.getArticlesRankedByClicks();
    }

    @Override
    public List<Article> getLatestArticles() {
        return articleMapper.getLatestArticles();
    }

    @Override
    public Article getArticleById(int id) {
        return articleMapper.getArticleById(id);
    }

    @Override
    public void addComment(Comment comment) {
        Map<String,Object> map= ThreadLocalUtil.get();
        int authorId=(int) map.get("id");
        int articleId=comment.getArticleId();
        String content=comment.getContent();
        articleMapper.addComment(articleId,authorId,content);
        articleMapper.incrementCommentCount(articleId);
    }

    @Override
    @Transactional
    public Comment addSubComment(int articleId, int parentCommentId, Comment subComment) {
        // 验证父评论
        Comment parent = articleMapper.getCommentById(parentCommentId);
        if (parent == null || parent.getArticleId() != articleId) {
            throw new RuntimeException("Invalid parent comment");
        }

        // 设置必要字段
        Map<String,Object> map= ThreadLocalUtil.get();
        int authorId=(int) map.get("id");
        subComment.setArticleId(articleId);
        subComment.setAuthorId(authorId);
        subComment.setReplyTo(parentCommentId);

        // 插入并获取生成的主键
        articleMapper.addSubComment(subComment);

        // 重新查询完整数据
        Comment newComment = articleMapper.getCommentById(subComment.getId());

        // 更新文章评论计数
        articleMapper.incrementCommentCount(articleId);

        return newComment;
    }

    @Override
    @Transactional
    public void deleteComment(int articleId, int commentId) {
        // 获取当前用户信息
        Map<String, Object> context = ThreadLocalUtil.get();
        int currentUserId = (int) context.get("id");
        // 查询要删除的评论
        Comment comment = articleMapper.getCommentById(commentId);
        // 验证评论是否存在
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        // 验证评论属于当前文章
        if (comment.getArticleId() != articleId) {
            throw new RuntimeException("评论不属于该文章");
        }
        // 验证操作权限（评论作者或管理员）
        if (comment.getAuthorId() != currentUserId /* && 非管理员 */) {
            throw new RuntimeException("无权限删除该评论");
        }
        // 执行级联删除并获取删除数量
        int deletedCount = articleMapper.deleteCommentAndSubComments(commentId);
        // 更新文章评论计数
        articleMapper.decrementCommentCount(articleId, deletedCount);
    }

    @Override
    public void publishArticle(Article article) {
        Map<String,Object> map= ThreadLocalUtil.get();
        int authorId=(int) map.get("id");
        String title=article.getTitle();
        String content=article.getContent();
        articleMapper.addArticle(title,authorId,content);
    }

    @Override
    public void incrementArticleClicks(int id) {
        articleMapper.incrementArticleClicks(id);
    }

    @Override
    public ArticleDTO getArticleWithStatus(int id, int userId) {
        return articleMapper.getArticleWithStatus(id, userId);
    }

    @Override
    @Transactional
    public void toggleLike(int articleId, int userId) {
        boolean exists = articleMapper.existsLike(userId, articleId);
        if (exists) {
            articleMapper.removeLike(userId, articleId);
            articleMapper.updateLikes(articleId, -1);
        } else {
            articleMapper.addLike(userId, articleId);
            articleMapper.updateLikes(articleId, 1);
        }
    }

    @Override
    @Transactional
    public void toggleCollect(int articleId, int userId) {
        boolean exists = articleMapper.existsCollect(userId, articleId);
        if (exists) {
            articleMapper.removeCollect(userId, articleId);
            articleMapper.updateCollects(articleId, -1);
        } else {
            articleMapper.addCollect(userId, articleId);
            articleMapper.updateCollects(articleId, 1);
        }
    }
}
