package com.example.xiergc.controller;

import com.example.xiergc.entity.*;
import com.example.xiergc.service.ArticleService;
import com.example.xiergc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // 获取推荐文章榜（按点击量排序）
    @GetMapping("/ranking")
    public Result<List<Article>> getRanking() {
        List<Article> articles = articleService.getRankingArticles();
        return Result.success(articles);
    }

    // 获取最新发布的文章
    @GetMapping("/latest")
    public Result<List<Article>> getLatest() {
        List<Article> articles = articleService.getLatestArticles();
        return Result.success(articles);
    }

    // 获取文章内容
    @GetMapping("/{id}")
    public Result<Article> getArticleDetails(@PathVariable int id) {
        Article article = articleService.getArticleById(id);
        return Result.success(article);
    }

    // 获取文章详情（带状态）
    @GetMapping("/{id}/status")
    public Result<ArticleDTO> getArticleStatus(@PathVariable int id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        int userId = claims != null ? (int) claims.get("id") : 0;
        ArticleDTO article = articleService.getArticleWithStatus(id, userId);
        return Result.success(article);
    }

    // 点赞/取消点赞
    @PostMapping("/{id}/like")
    public Result toggleLike(@PathVariable int id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        int userId = (int) claims.get("id");
        articleService.toggleLike(id, userId);
        return Result.success();
    }

    // 收藏/取消收藏
    @PostMapping("/{id}/collect")
    public Result toggleCollect(@PathVariable int id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        int userId = (int) claims.get("id");
        articleService.toggleCollect(id, userId);
        return Result.success();
    }

    //评论文章
    @PostMapping("/{id}/comments")
    public Result commentArticle(@PathVariable int id, @RequestBody Comment comment) {
        comment.setArticleId(id);
        articleService.addComment(comment);
        return Result.success();
    }

    //发布子评论
    @PostMapping("/{articleId}/comments/{parentCommentId}")
    public Result<Comment> SubComment(
            @PathVariable int articleId,
            @PathVariable int parentCommentId,
            @RequestBody Comment subComment) {

        Comment savedComment = articleService.addSubComment(articleId, parentCommentId, subComment);
        return Result.success(savedComment);
    }

    //获取文章的所有评论
    @GetMapping("/{articleId}/GetComment")
    public Result<List<Comment>> GetComment(@PathVariable int articleId) {
        List<Comment> comments = articleService.GetComment(articleId);
        return Result.success(comments);
    }

    // 发布文章
    @PostMapping("/post")
    public Result postArticle(@RequestBody Article article) {
        articleService.publishArticle(article);
        return Result.success();
    }

    //添加点击数
    @PostMapping("/{id}/click")
    public Result incrementClicks(@PathVariable int id) {
        articleService.incrementArticleClicks(id);
        return Result.success();
    }

    //删除评论
    @DeleteMapping("/{articleId}/comments/{commentId}")
    public Result deleteComment(
            @PathVariable int articleId,
            @PathVariable int commentId) {

        articleService.deleteComment(articleId, commentId);
        return Result.success();
    }

    //删除文章
    @DeleteMapping("/{articleId}/delete")
    public Result deleteArticle(@PathVariable int articleId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        int userId = (int) claims.get("id");
        int authorId = articleService.getAuthorIdById(articleId);
        if(userId != authorId){
            return Result.error("你无权删除该文章");
        }
        articleService.deleteArticle(articleId);
        return Result.success();
    }

    // 实现模糊搜索接口
    @PostMapping("/search")
    public Result<List<Article>> searchArticles(@RequestBody KeyWordDTO searchRequest) {
        String keyword = searchRequest.getKeyword();
        List<Article> articles = articleService.searchArticles(keyword);
        if (articles.isEmpty()) {
            return Result.error("没有找到相关文章");
        }
        return Result.success(articles);
    }
}
