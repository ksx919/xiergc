<template>
  <div class="article-box">
    <div class="article-top">
      <div class="article-info">
        <h2>{{ article.title }}</h2>
        <MarkdownViewer :content="article.content" class="markdown-content" />
      </div>
    </div>

    <div class="article-bottom">
      <span class="date">{{ article.date }}</span>
      <div class="article-actions">
        <img class="action-icon" :src="clickIconSrc" alt="点击数" />
        <span>{{ article.clickCount }}</span>
        <font-awesome-icon
          :icon="isLiked ? faHeart : faHeartRegular"
          class="action-icon"
          :class="{ 'liked-icon': isLiked }"
          @click="toggleLike(route.params.id)"
        />
        <span>{{ article.likeCount }}</span>
        <font-awesome-icon
          :icon="isCollected ? faBookmark : faBookmarkRegular"
          class="action-icon"
          :class="{ 'collected-icon': isCollected }"
          @click="toggleCollect(route.params.id)"
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
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faHeart, faBookmark } from "@fortawesome/free-solid-svg-icons";
import {
  faHeart as faHeartRegular,
  faBookmark as faBookmarkRegular,
} from "@fortawesome/free-regular-svg-icons";
import instance from "@/api/axios.js";
import MarkdownViewer from "@/components/MakeDown/MarkdownViewer.vue";

const isLiked = ref(false);
const isCollected = ref(false);
const route = useRoute();

// 图片路径
const clickIconSrc = require("@/assets/image/click.png");
const commentIconSrc = require("@/assets/image/comment.png");
const sendIconSrc = require("@/assets/image/write.png");

// 初始化文章数据
const article = ref({
  title: "加载中...",
  content: "",
  date: "",
  clickCount: 0,
  collectCount: 0,
  likeCount: 0,
  commentCount: 0,
});
const formatDate = (dateArray) => {
  if (!dateArray) return "";
  const [year, month, day, hour, minute] = dateArray;
  return `${year}-${month.toString().padStart(2, "0")}-${day
    .toString()
    .padStart(2, "0")} ${hour}:${minute.toString().padStart(2, "0")}`;
};

const fetchArticle = async (id) => {
  try {
    const jwtToken = localStorage.getItem("jwtToken");
    const { data } = await instance.get(`/articles/${id}`, {
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
        commentCount: data.data.comment,
      };
    } else {
      console.error("获取文章失败:", data.msg);
    }
  } catch (error) {
    console.error("请求失败:", error);
  }
};

const GetLC = async (id) => {
  try {
    const jwtToken = localStorage.getItem("jwtToken");
    const { data } = await instance.get(`/articles/${id}/status`, {
      headers: {
        Authorization: `${jwtToken}`,
      },
    });
    if (data.code === 0) {
      // 直接更新响应式变量
      isLiked.value = data.data.isLiked;
      isCollected.value = data.data.isCollected;
    }
  } catch (error) {
    console.error("获取状态失败", error);
  }
};

const toggleLike = async (id) => {
  try {
    const jwtToken = localStorage.getItem("jwtToken");
    const { data } = await instance.post(`/articles/${id}/like`, null, {
      headers: {
        Authorization: `${jwtToken}`,
      },
    });
    if (data.code === 0) {
      isLiked.value = !isLiked.value;
      article.value.likeCount += isLiked.value ? 1 : -1;
    }
  } catch (error) {
    console.error("点赞失败:", error);
  }
};

const toggleCollect = async (id) => {
  try {
    const jwtToken = localStorage.getItem("jwtToken");
    const url = `/articles/${id}/collect`;
    const { data } = await instance.post(url, null, {
      headers: {
        Authorization: `${jwtToken}`,
      },
    });
    if (data.code === 0) {
      isCollected.value = !isCollected.value;
      article.value.collectCount += isCollected.value ? 1 : -1;
    }
  } catch (error) {
    console.error("收藏失败:", error);
  }
};

const commentContent = ref("");

const sendComment = async () => {
  if (commentContent.value.trim() === "") {
    return;
  }
  try {
    const jwtToken = localStorage.getItem("jwtToken");
    const articleId = route.params.id;
    const url = `/articles/${articleId}/comments`;
    const data = {
      content: commentContent.value,
    };
    const response = await instance.post(url, data, {
      headers: {
        Authorization: `${jwtToken}`,
      },
    });
    if (response.status === 200) {
      console.log("发送评论成功:", response.data);
      article.value.commentCount++;
      commentContent.value = "";
      alert("发送评论成功！");
      location.reload();
    }
  } catch (error) {
    console.error("发送评论失败:", error);
  }
};

// 确保正确获取路由参数
onMounted(() => {
  const articleId = route.params.id;
  if (articleId) {
    fetchArticle(articleId);
    GetLC(articleId);
  }
});
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
  width: 100%; /* 设置宽度为100%，占满父容器 */
  box-sizing: border-box; /* 让padding和border包含在宽度内 */
}

.article-top {
  position: relative;
  text-align: left;
}

.article-info {
  margin-bottom: 30px;
  word-wrap: break-word; /* 让文字自动换行 */
}

.article-images {
  /* 移除 flex 布局，让图片独占一行 */
  display: block;
}

.cover,
.article-images img {
  /* 让图片宽度占满父容器 */
  max-width: 100%; /* 设置最大宽度为父容器宽度 */
  height: auto;
  object-fit: cover;
  margin-bottom: 20px; /* 为每张图片添加底部间距 */
}

.markdown-content img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 1rem 0;
}

/* 如果样式未生效时使用的深度选择器 */
:deep(.markdown-content) img {
  max-width: 100%;
  height: auto;
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

.liked-icon {
  color: #ff4757 !important; /* 点赞后的红色 */
}

.collected-icon {
  color: #ffd700 !important; /* 收藏后的金色 */
}

/* 新增文章底部评论框样式 */
.article-comment-box {
  position: relative;
  background-color: rgba(0, 0, 0, 0.488);
  border: 1px solid #000000;
  border-radius: 10px;
  padding: 10px;
  backdrop-filter: blur(5px);
  height: 80px;
}

.comment-input {
  width: 96%; /* 宽度占满容器 */
  background-color: transparent;
  border: none;
  color: white;
  padding: 10px;
  border-radius: 5px;
  resize: none;
  min-height: 60px; /* 增大评论框高度 */
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
