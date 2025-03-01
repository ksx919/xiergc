<template>
  <div class="article-detail-container">
    <ArticleComponent :article="article" />
    <CommentSectionComponent :comments="comments" />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import ArticleComponent from "./ArticleOne.vue";
import CommentSectionComponent from "./ArticleComment.vue";
import instance from "@/api/axios.js";
const route = useRoute();
// 文章数据
const article = ref({
  id: 1,
  title: "文章标题",
  content: "文章描述",
  date: "2024-01-01",
  clickCount: 100,
  collectCount: 20,
  likeCount: 30,
  commentCount: 10
});

// 评论数据
const comments = ref([]);


const fetchComments = async (id) => {
  try {
    const jwtToken = localStorage.getItem("jwtToken");
    const { data } = await instance.get(
      `/articles/${id}/GetComment`,
      {
        headers: {
          Authorization: `${jwtToken}`,
        },
      }
    );
    if (data.code === 0) {
      // 转换评论数据结构
      comments.value = transformComments(data.data);
    }
  } catch (error) {
    console.error("获取评论失败:", error);
  }
};

// 转换评论结构函数
const transformComments = (apiComments) => {
  const commentMap = new Map();
  
  // 第一遍：创建所有评论的映射并初始化replies
  apiComments.forEach(comment => {
    commentMap.set(comment.id, {
      nickname: comment.authorName,
      comment: comment.content,
      date: formatDate(comment.publishDate), 
      replies: [],
      likeCount: 0,
      commentCount: 0,
      rawData: comment
    });
  });

  // 第二遍：建立父子关系
  const result = [];
  apiComments.forEach(comment => {
    if (!comment.replyTo) {
      result.push(commentMap.get(comment.id));
    } else {
      const parent = commentMap.get(comment.replyTo);
      if (parent) {
        parent.replies.push(commentMap.get(comment.id));
        parent.commentCount++;
      }
    }
  });

  return result;
};

const formatDate = (dateArray) => {
  if (!dateArray) return '';
  const [year, month, day, hour, minute] = dateArray;
  return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hour}:${minute.toString().padStart(2, '0')}`;
};

onMounted(() => {
  const articleId = route.params.id
  if (articleId) {
    fetchComments(articleId)
  }
})
</script>

<style scoped>
.article-detail-container {
  width: 45%;
  margin-top: 80px;
  margin-left: 280px;
}
</style>
