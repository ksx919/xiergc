<template>
  <div class="article-box">
    <div class="article-top">
      <div class="article-info">
        <h2>{{ article.title }}</h2>
        <p>{{ article.description }}</p>
      </div>
    </div>
    <div class="article-images">
      <!-- 封面图片 -->
      <img class="cover" :src="article.coverSrc" alt="封面" />
      <!-- 其他图片 -->
      <img
        v-for="(img, index) in article.otherImages"
        :key="index"
        :src="img"
        alt="文章图片"
      />
    </div>
    <div class="article-bottom">
      <span class="date">{{ article.date }}</span>
      <div class="article-actions">
        <img class="action-icon" :src="clickIconSrc" alt="点击数" />
        <span>{{ article.clickCount }}</span>
        <font-awesome-icon
          :icon="isLiked ? faHeart : faHeartRegular"
          class="action-icon"
          :class="{ 'gray-icon': isLiked }"
          @click="toggleLike"
        />
        <span>{{ article.likeCount }}</span>
        <font-awesome-icon
          :icon="isCollected ? faBookmark : faBookmarkRegular"
          class="action-icon"
          :class="{ 'gray-icon': isCollected }"
          @click="toggleCollect"
        />
        <span>{{ article.collectCount }}</span>
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
import { useAttrs } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faHeart, faBookmark } from "@fortawesome/free-solid-svg-icons";
import {
  faHeart as faHeartRegular,
  faBookmark as faBookmarkRegular,
} from "@fortawesome/free-regular-svg-icons";
import axios from "axios";

const attrs = useAttrs();
const article = ref(attrs.article);

// 图片路径
const clickIconSrc = require("@/assets/image/click.png");
const commentIconSrc = require("@/assets/image/comment.png");
const sendIconSrc = require("@/assets/image/write.png");

// 确保这里的路径是正确的
article.value = {
  id: 1,
  title: "文章标题",
  description: "文章描述",
  // 使用 require 来正确处理图片路径
  coverSrc: require("@/assets/image/postimg.png"),
  otherImages: [require("@/assets/image/postimg2.png")],
  date: "2024-01-01",
  clickCount: 100,
  collectCount: 20,
  likeCount: 30,
  commentCount: 10,
};

// 点赞和收藏状态
const isLiked = ref(false);
const isCollected = ref(false);

// 评论相关状态
const commentContent = ref("");

const toggleLike = async () => {
  try {
    const url = `http://localhost:8080/articles/${article.value.id}/like`;
    const response = await axios.post(url);
    if (response.status === 200) {
      isLiked.value = !isLiked.value;
      if (isLiked.value) {
        article.value.likeCount++;
      } else {
        article.value.likeCount--;
      }
    }
  } catch (error) {
    console.error("点赞失败:", error);
  }
};

const toggleCollect = async () => {
  try {
    const url = `http://localhost:8080/articles/${article.value.id}/collect`;
    const response = await axios.post(url);
    if (response.status === 200) {
      isCollected.value = !isCollected.value;
      if (isCollected.value) {
        article.value.collectCount++;
      } else {
        article.value.collectCount--;
      }
    }
  } catch (error) {
    console.error("收藏失败:", error);
  }
};

const sendComment = async () => {
  if (commentContent.value.trim() === "") {
    return;
  }
  try {
    const url = `http://localhost:8080/articles/${article.value.id}/comments`;
    const data = {
      content: commentContent.value,
    };
    const response = await axios.post(url, data);
    if (response.status === 200) {
      console.log("发送评论成功:", response.data);
      article.value.commentCount++;
      commentContent.value = "";
    }
  } catch (error) {
    console.error("发送评论失败:", error);
  }
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
  cursor: pointer;
}

/* 新增灰色图标样式 */
.gray-icon {
  color: gray;
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
