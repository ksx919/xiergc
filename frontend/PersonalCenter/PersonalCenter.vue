<template>
  <div class="app-container">
    <div class="content-container">
      <div class="black-rectangle">
        <!-- 顶部背景图片 -->
        <div class="background-image">
          <img :src="backgroundImageSrc" alt="背景图片" />
        </div>
        <!-- 个人信息区域 -->
        <div class="personal-info-box">
          <!-- 头像 -->
          <div class="avatar-container">
            <img
              class="avatar"
              :src="avatarSrc"
              alt="头像"
              @mouseenter="handleAvatarHover"
              @mouseleave="handleAvatarLeave"
              @click="showAvatarModal = true"
            />
            <span class="level">LV.7</span>
            <div v-if="isAvatarHovered" class="change-avatar-tip">修改头像</div>
          </div>
          <!-- 分割线 -->
          <div class="vertical-divider"></div>
          <!-- 关注 -->
          <div class="info-item">
            <span class="info-label">关注</span>
            <span class="info-value">100</span>
          </div>
          <!-- 分割线 -->
          <div class="vertical-divider"></div>
          <!-- 粉丝 -->
          <div class="info-item">
            <span class="info-label">粉丝</span>
            <span class="info-value">200</span>
          </div>
          <!-- 分割线 -->
          <div class="vertical-divider"></div>
          <!-- 足迹 -->
          <div class="info-item">
            <span class="info-label">足迹</span>
            <span class="info-value">300</span>
          </div>
          <!-- 分割线 -->
          <div class="vertical-divider"></div>
          <!-- 设置中心 -->
          <a :href="settingUrl" class="setting-link">
            <img class="setting-icon" :src="settingIconSrc" alt="设置中心" />
          </a>
        </div>
        <!-- 昵称 -->
        <div class="nickname">用户昵称</div>
        <!-- 两条横分割线 -->
        <div class="horizontal-dividers">
          <div class="divider"></div>
        </div>
        <!-- 文章、想法、点赞、收藏、购物、任务 -->
        <div class="function-buttons">
          <span @click="changeTab('articles')">文章</span>
          <span @click="changeTab('likedArticles')">点赞</span>
          <span>想法</span>
          <span>收藏</span>
          <span>购物</span>
          <span>任务</span>
        </div>
        <div class="divider"></div>
        <!-- 文章列表 -->
        <div class="article-list-container">
          <div class="article-list">
            <ArticleItem
              v-for="article in currentArticles"
              :key="article.id"
              :article="article"
              :onDelete="deleteArticle"
            />
          </div>
        </div>
        <!-- 修改头像模态框 -->
        <div v-if="showAvatarModal" class="avatar-modal">
          <div class="modal-header">
            <span>修改头像</span>
            <span @click="showAvatarModal = false" class="close-icon">×</span>
          </div>
          <div class="modal-content">
            <img :src="avatarSrc" alt="当前头像" width="100" height="100" />
            <div class="button-container">
              <!-- 自定义文件选择按钮 -->
              <label for="file-input" class="custom-file-button"
                >选择文件</label
              >
              <input
                type="file"
                id="file-input"
                @change="handleFileChange"
                style="display: none"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import ArticleItem from "./ArticleItem.vue";

// 图片路径
const backgroundImageSrc = require("@/assets/image/backgroundp.png");
const avatarSrc = ref(require("@/assets/image/user.png"));
const settingIconSrc = require("@/assets/image/setting.png");

// 设置中心跳转链接
const settingUrl = "/setting";

// 文章列表
const articles = ref([
  {
    id: 1,
    title: "文章标题 1",
    link: "/article/1",
    clickCount: 100,
    collectCount: 20,
    likeCount: 30,
    commentCount: 10,
    description: "文章描述 1",
  },
  {
    id: 2,
    title: "文章标题 2",
    link: "/article/2",
    clickCount: 200,
    collectCount: 30,
    likeCount: 40,
    commentCount: 20,
    description: "文章描述 2",
  },
  {
    id: 3,
    title: "文章标题 3",
    link: "/article/3",
    clickCount: 300,
    collectCount: 40,
    likeCount: 50,
    commentCount: 30,
    description: "文章描述 3",
  },
  {
    id: 4,
    title: "文章标题 4",
    link: "/article/4",
    clickCount: 400,
    collectCount: 50,
    likeCount: 60,
    commentCount: 40,
    description: "文章描述 4",
  },
]);

// 点赞文章列表
const likedArticles = ref([
  {
    id: 5,
    title: "点赞文章标题 1",
    link: "/liked-article/1",
    clickCount: 150,
    collectCount: 25,
    likeCount: 45,
    commentCount: 15,
    description: "点赞文章描述 1",
  },
  {
    id: 6,
    title: "点赞文章标题 2",
    link: "/liked-article/2",
    clickCount: 250,
    collectCount: 35,
    likeCount: 55,
    commentCount: 25,
    description: "点赞文章描述 2",
  },
]);

// 当前显示的文章列表
const currentTab = ref("articles");
const currentArticles = computed(() => {
  return currentTab.value === "articles" ? articles.value : likedArticles.value;
});

// 引入背景样式文件
import "@/components/background/background.css";

// 控制头像悬停状态
const isAvatarHovered = ref(false);
// 控制修改头像模态框的显示
const showAvatarModal = ref(false);

// 处理头像悬停事件
const handleAvatarHover = () => {
  isAvatarHovered.value = true;
  document.querySelector(".avatar").style.transform = "scale(1.2)";
};

// 处理头像离开事件
const handleAvatarLeave = () => {
  isAvatarHovered.value = false;
  document.querySelector(".avatar").style.transform = "scale(1)";
};

// 处理文件选择事件
const handleFileChange = (e) => {
  const file = e.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (event) => {
      avatarSrc.value = event.target.result;
    };
    reader.readAsDataURL(file);
  }
};

// 切换显示的文章列表
const changeTab = (tab) => {
  currentTab.value = tab;
};

// 删除文章
const deleteArticle = (articleId) => {
  if (currentTab.value === "articles") {
    articles.value = articles.value.filter(
      (article) => article.id !== articleId
    );
  } else {
    likedArticles.value = likedArticles.value.filter(
      (article) => article.id !== articleId
    );
  }
};
</script>

<style scoped>
/* 应用容器 */
.app-container {
  min-height: 100vh;
}

/* 内容容器，可滚动 */
.content-container {
  min-height: 100vh;
  overflow-y: auto;
  background-attachment: fixed; /* 固定背景 */
}

/* 顶部背景图片 */
.background-image {
  height: 200px; /* 可根据需要调整高度 */
  overflow: hidden;
}

.background-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 黑色矩形 */
.black-rectangle {
  background-color: rgba(0, 0, 0, 0.8);
  width: 53%;
  margin: 20px auto;
  padding: 20px;
  border-radius: 10px;
  color: white;
}

/* 个人信息区域 */
.personal-info-box {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  gap: 30px; /* 增加元素之间的间隔 */
}

.avatar-container {
  position: relative;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  transition: transform 0.3s ease;
}

.level {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: #ff9800;
  color: #fff;
  padding: 2px 5px;
  border-radius: 3px;
  font-size: 12px;
}

.change-avatar-tip {
  position: absolute;
  bottom: -20px; /* 放置在头像下方 */
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
  display: none;
  white-space: nowrap; /* 提示文字不换行 */
}

.avatar-container:hover .change-avatar-tip {
  display: block;
}

.vertical-divider {
  width: 1px;
  height: 60px;
  background-color: #ccc;
  margin: 0 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.info-label {
  font-size: 14px;
  color: white;
}

.info-value {
  font-size: 18px;
  font-weight: bold;
  color: white;
}

.setting-link {
  display: flex;
  align-items: center;
}

.setting-icon {
  width: 30px;
  height: 30px;
}

.nickname {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  padding: 20px 0;
  color: white;
}

.horizontal-dividers {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.divider {
  width: 95%;
  height: 1px;
  background-color: #ccc;
  margin: 5px auto; /* 使分割线水平居中 */
}

.function-buttons {
  display: flex;
  justify-content: center;
  padding: 20px 0;
  color: white;
}

.function-buttons span {
  margin: 0 20px;
  cursor: pointer;
}

.article-list-container {
  width: 100%;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.avatar-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(16, 16, 16, 0.923);
  color: white;
  padding: 20px;
  border: 1px solid #000000;
  border-bottom: 1px solid #ffffff;
  border-radius: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  width: 240px; /* 调整窗口宽度 */
  height: 200px; /* 调整窗口高度*/
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  padding-bottom: 10px;
}

.close-icon {
  cursor: pointer;
  font-size: 24px;
  transition: color 0.2s ease;
}

.close-icon:hover {
  color: #ccc;
}

.modal-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;
}

.modal-content img {
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid white;
}

.button-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
}

.custom-file-button {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 8px 15px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.custom-file-button:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

input[type="file"] {
  display: none;
}
</style>
