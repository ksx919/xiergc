package com.example.xiergc.service.impl;

import com.example.xiergc.entity.Article;
import com.example.xiergc.dto.ArticleDTO;
import com.example.xiergc.entity.Comment;
import com.example.xiergc.mapper.ArticleMapper;
import com.example.xiergc.service.ArticleService;
import com.example.xiergc.utils.ThreadLocalUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.example.xiergc.entity.RedisConstants.*;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public List<Article> getRankingArticles() {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String articlesJson = operations.get(CACHE_RANKING_KEY);
        if (articlesJson != null) {
            try {
                return objectMapper.readValue(articlesJson,new TypeReference<List<Article>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //获取互斥锁
        String lockKey = "lock:ranking";
        List<Article> articles = null;
        try {
            boolean isLock = tryLock(lockKey);
            if (!isLock) {
                Thread.sleep(50);
                return getRankingArticles();
            }
            articles = articleMapper.getArticlesRankedByClicks();

            if(articles == null){
                operations.set(CACHE_RANKING_KEY,null);
                return null;
            }

            try {
                int expireSeconds = 300 + new Random().nextInt(120);
                operations.set(CACHE_RANKING_KEY, objectMapper.writeValueAsString(articles)
                        ,expireSeconds,TimeUnit.SECONDS);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            unlock(lockKey);
        }
        return articles;
    }

    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }

    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }

    @Override
    public List<Article> getLatestArticles() {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String articlesJson = operations.get(CACHE_LATEST_KEY);
        if (articlesJson != null) {
            try {
                return objectMapper.readValue(articlesJson,new TypeReference<List<Article>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //获取互斥锁
        String lockKey = "lock:latest";
        List<Article> articles = null;
        try {
            boolean isLock = tryLock(lockKey);
            if (!isLock) {
                Thread.sleep(50);
                return getLatestArticles();
            }
            articles = articleMapper.getLatestArticles();

            if(articles == null){
                operations.set(CACHE_LATEST_KEY,null);
                return null;
            }

            try {
                int expireSeconds = 300 + new Random().nextInt(120);
                operations.set(CACHE_LATEST_KEY, objectMapper.writeValueAsString(articles)
                        ,expireSeconds,TimeUnit.SECONDS);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            unlock(lockKey);
        }
        return articles;
    }

    @Override
    public Article getArticleById(Long id) {
        String key = CACHE_ARTICLE_KEY + id;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        //1、从redis查询
        String articleJson = operations.get(key);
        //2、判断是否存在
        if (articleJson != null) {
            //3、存在，直接返回
            try {
                return objectMapper.readValue(articleJson, Article.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //4、不存在，根据id查询数据库
        String lockKey = "lock:article";
        Article article = null;
        try {
            boolean isLock = tryLock(lockKey);
            if (!isLock) {
                Thread.sleep(50);
                return getArticleById(id);
            }
            article = articleMapper.getArticleById(id);

            //5、不存在，返回错误
            if (article == null) {
                operations.set(key,null);
                return null;
            }
            //6、存在，写入redis
            try {
                int expireSeconds = 300 + new Random().nextInt(120);
                operations.set(key, objectMapper.writeValueAsString(article)
                ,expireSeconds,TimeUnit.SECONDS);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            unlock(lockKey);
        }
        //7、返回
        return article;
    }

    @Override
    public void addComment(Comment comment) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Long authorId=((Number) map.get("id")).longValue();
        Long articleId=comment.getArticleId();
        String content=comment.getContent();
        articleMapper.addComment(articleId,authorId,content);
        articleMapper.incrementCommentCount(articleId);
    }

    @Override
    @Transactional
    public Comment addSubComment(Long articleId, Long parentCommentId, Comment subComment) {
        // 验证父评论
        Comment parent = articleMapper.getCommentById(parentCommentId);
        if (parent == null || parent.getArticleId() != articleId) {
            throw new RuntimeException("Invalid parent comment");
        }

        // 设置必要字段
        Map<String,Object> map= ThreadLocalUtil.get();
        Long authorId=((Number) map.get("id")).longValue();
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
    public void deleteComment(Long articleId, Long commentId) {
        // 获取当前用户信息
        Map<String, Object> context = ThreadLocalUtil.get();
        Long currentUserId = ((Number)context.get("id")).longValue();
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
        Long deletedCount = articleMapper.deleteCommentAndSubComments(commentId);
        // 更新文章评论计数
        articleMapper.decrementCommentCount(articleId, deletedCount);
    }

    @Override
    public List<Comment> GetComment(Long articleId) {
        return articleMapper.GetComment(articleId);
    }

    @Override
    public void deleteArticle(Long articleId) {
        articleMapper.deleteArticle(articleId);
    }

    @Override
    public Long getAuthorIdById(Long articleId) {
        return articleMapper.getAuthorIdById(articleId);
    }

    @Override
    public List<Article> searchArticles(String keyword) {
        return articleMapper.searchArticles(keyword);
    }

    @Override
    public void publishArticle(Article article) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Long authorId=((Number) map.get("id")).longValue();
        String title=article.getTitle();
        String content=article.getContent();
        articleMapper.addArticle(title,authorId,content);
    }

    @Override
    public void incrementArticleClicks(Long id) {
        articleMapper.incrementArticleClicks(id);
    }

    @Override
    public ArticleDTO getArticleWithStatus(Long id, Long userId) {
        return articleMapper.getArticleWithStatus(id, userId);
    }

    @Override
    @Transactional
    public void toggleLike(Long articleId, Long userId) {
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
    public void toggleCollect(Long articleId, Long userId) {
        boolean exists = articleMapper.existsCollect(userId, articleId);
        if (exists) {
            articleMapper.removeCollect(userId, articleId);
            articleMapper.updateCollects(articleId, -1);
        }else {
            articleMapper.addCollect(userId, articleId);
            articleMapper.updateCollects(articleId, 1);
        }
    }
}
