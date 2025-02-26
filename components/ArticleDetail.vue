<template>
  <div class="article-detail-container">
    <ArticleComponent :article="article" />
    <CommentSectionComponent :comments="comments" />
  </div>
</template>

<script setup>
import { ref } from "vue";
import ArticleComponent from "./ArticleOne.vue";
import CommentSectionComponent from "./ArticleComment.vue";

// 文章数据
const article = ref({
  id: 1,
  title: "文章标题",
  description: "文章描述",
  coverSrc: "@/assets/image/o.png",
  otherImages: ["@/assets/image/Userde.png", "@/assets/image/Userde.png"],
  date: "2024-01-01",
  clickCount: 100,
  collectCount: 20,
  likeCount: 30,
  commentCount: 10,
});

// 评论数据
const comments = ref([
  {
    id: 1,
    nickname: "用户昵称1",
    level: 4,
    comment: "这是一条评论",
    likeCount: 5,
    commentCount: 2,
    replies: [
      {
        id: 11,
        nickname: "回复用户1",
        date: "2024-01-02",
        comment: "这是对评论的回复",
        likeCount: 2,
        commentCount: 0,
      },
      {
        id: 12,
        nickname: "回复用户2",
        date: "2024-01-03",
        comment: "这是另一条对评论的回复",
        likeCount: 1,
        commentCount: 0,
      },
      {
        id: 13,
        nickname: "回复用户3",
        date: "2024-01-04",
        comment: "还有一条回复",
        likeCount: 0,
        commentCount: 0,
      },
    ],
  },
  {
    id: 2,
    nickname: "用户昵称2",
    level: 3,
    comment: "这是另一条评论",
    likeCount: 3,
    commentCount: 1,
    replies: [],
  },
]);
</script>

<style scoped>
.article-detail-container {
  width: 45%;
  margin-top: 80px;
  margin-left: 280px;
}
</style>
