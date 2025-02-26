<template>
  <div class="article-box">
    <div class="article-top">
      <img
        class="top-right-icon"
        src="@/assets/image/deng.png"
        alt="右上角图标"
      />
      <div class="article-info">
        <h2>{{ article.title }}</h2>
        <p>{{ article.content }}</p>
      </div>
    </div>
    <div class="article-bottom">
      <span class="date">{{ article.date }}</span>
      <div class="article-actions">
        <img class="action-icon" :src="clickIconSrc" alt="点击数" />
        <span>{{ article.clickCount }}</span>
        <img class="action-icon" :src="collectIconSrc" alt="收藏" />
        <span>{{ article.collectCount }}</span>
        <img class="action-icon" :src="likeIconSrc" alt="喜欢" />
        <span>{{ article.likeCount }}</span>
        <img class="action-icon" :src="commentIconSrc" alt="评论数量" />
        <span>{{ article.commentCount }}</span>
      </div>
    </div>
    <!-- 新增文章底部评论框 -->
    <div class="article-comment-box">
      <textarea
        v-model="commentContent"
        class="comment-input"
        placeholder="请输入评论"
      ></textarea>
      <button class="send-button" @click="sendComment">
        <img :src="sendIconSrc" alt="发送按钮" />
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { onMounted } from "vue";
import axios from "axios";
import { useRoute } from 'vue-router';

const route = useRoute();

// 图片路径
const clickIconSrc = require("@/assets/image/click.png");
const collectIconSrc = require("@/assets/image/collect.png");
const likeIconSrc = require("@/assets/image/like.png");
const commentIconSrc = require("@/assets/image/comment.png");
const sendIconSrc = require("@/assets/image/write.png");

// 初始化文章数据
const article = ref({
  title: '加载中...',
  content: '',
  date: '',
  clickCount: 0,
  collectCount: 0,
  likeCount: 0,
  commentCount: 0
})
const formatDate = (dateArray) => {
  if (!dateArray) return '';
  const [year, month, day, hour, minute] = dateArray;
  return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hour}:${minute.toString().padStart(2, '0')}`;
};

const fetchArticle = async (id) => {
  try {
    const jwtToken =localStorage.getItem("jwtToken");
    const { data } = await axios.get(`http://localhost:8080/articles/${id}`,{
      headers: {
        Authorization: `${jwtToken}`,
      },
    });
    
    if (data.code === 0) {
      article.value = {
        title: data.data.title,
        content: data.data.content,
        date: formatDate(data.data.publishDate),
        clickCount: data.data.clicks,
        collectCount: data.data.collect,
        likeCount: data.data.likes,
        commentCount: data.data.comment
      };
    } else {
      console.error('获取文章失败:', data.msg);
    }
  } catch (error) {
    console.error('请求失败:', error);
  }
};

// 确保正确获取路由参数
onMounted(() => {
  const articleId = route.params.id
  if (articleId) {
    fetchArticle(articleId)
  }
})

// 评论相关状态
const commentContent = ref("");

const sendComment = () => {
  // 这里可以添加发送评论的逻辑
  console.log("发送评论:", commentContent.value);
  commentContent.value = "";
};
</script>

<style scoped>
.article-box {
  border: 1px solid #000000;
  padding: 50px;
  position: relative;
  background-color: rgba(0, 0, 0, 0.5);
  color: #ffffff;
  border-radius: 30px;
  margin-bottom: 40px;
  backdrop-filter: blur(5px);
}

.article-top {
  position: relative;
  text-align: left;
}

.top-right-icon {
  position: absolute;
  top: 0;
  right: 0;
  width: 5px;
  height: 20px;
}

.article-info {
  margin-bottom: 30px;
}

.article-images {
  /* 移除 flex 布局，让图片独占一行 */
  display: block;
}

.cover,
.article-images img {
  /* 让图片宽度占满父容器 */
  width: 100%;
  height: auto;
  object-fit: cover;
  margin-bottom: 20px; /* 为每张图片添加底部间距 */
}

.article-bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 30px;
}

.date {
  color: #ffffff;
  font-size: 15px;
  opacity: 0.9;
}

.article-actions {
  display: flex;
  align-items: center;
}

.action-icon {
  width: 20px;
  height: 20px;
  margin-left: 20px;
}

/* 新增文章底部评论框样式 */
.article-comment-box {
  position: relative;
  background-color: rgba(0, 0, 0, 0.488);
  border: 1px solid #000000;
  border-radius: 10px;
  padding: 10px;
  backdrop-filter: blur(5px);
}

.comment-input {
  width: 100%; /* 宽度占满容器 */
  background-color: transparent;
  border: none;
  color: white;
  padding: 10px;
  border-radius: 5px;
  resize: none;
  min-height: 150px; /* 增大评论框高度 */
}

.comment-input::placeholder {
  color: rgba(255, 255, 255, 0.8);
  opacity: 1;
}

.send-button {
  background: none;
  border: none;
  cursor: pointer;
  position: absolute;
  bottom: 15px; /* 调整到底部 */
  right: 15px; /* 调整到右侧 */
}

.send-button img {
  width: 60px;
  height: auto;
}
</style>
