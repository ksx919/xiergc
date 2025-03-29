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
              @click="showAvatarEditor = true"
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
        <div class="nickname">{{ userInfo.name }}</div>
        <!-- 两条横分割线 -->
        <div class="horizontal-dividers">
          <div class="divider"></div>
        </div>
        <!-- 文章、想法、点赞、收藏、购物、任务 -->
        <div class="function-buttons">
          <span @click="changeTab('articles')">文章</span>
          <span @click="changeTab('likedArticles')">点赞</span>
          <span @click="changeTab('collectedArticles')">收藏</span>
          <span>想法</span>
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
              :show-author="currentTab !== 'articles'"
              :show-delete="currentTab === 'articles'"
              :onDelete="deleteArticle"
              class="article-item-link"
              @click.prevent="handleArticleClick(article.id)"
            />
          </div>
        </div>
        <!-- 修改头像组件 -->
        <AvatarEditor
          v-if="showAvatarEditor"
          :avatarSrc="avatarSrc"
          @close="showAvatarEditor = false"
          @update:avatarSrc="avatarSrc = $event"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import ArticleItem from "./ArticleItem.vue";
import AvatarEditor from "./AvatarEditor.vue";
import instance from "@/api/axios.js";

// 图片路径
const backgroundImageSrc = require("@/assets/image/backgroundp.png");
const avatarSrc = ref(require("@/assets/image/user.png"));
const settingIconSrc = require("@/assets/image/setting.png");

// 设置中心跳转链接
const settingUrl = "/setting";

// 用户信息
const userInfo = ref({});

// 文章列表
const articles = ref([]);
// 点赞文章列表
const likedArticles = ref([]);
// 收藏文章列表
const collectedArticles = ref([]);

const isNavigating = ref(false);

const handleArticleClick = async (articleId) => {
  if (isNavigating.value) return;
  isNavigating.value = true;

  const jwtToken = localStorage.getItem("jwtToken");

  try {
    // 发送点击量统计请求
    await instance.post(
      `/articles/${articleId}/click`,
      {},
      {
        headers: {
          Authorization: `${jwtToken}`,
          'Content-Type': 'application/json'
        }
      }
    );
    
    // 请求完成后跳转页面
    window.location.href = `/article/${articleId}`;
  } finally {
    isNavigating.value = false;
  }
};

// 当前显示的文章列表
const currentTab = ref("articles");

const currentArticles = computed(() => {
  switch(currentTab.value) {
    case 'articles': return articles.value;
    case 'likedArticles': return likedArticles.value;
    case 'collectedArticles': return collectedArticles.value;
    default: return [];
  }
});

// 引入背景样式文件
import "@/components/background/background.css";

// 控制头像悬停状态
const isAvatarHovered = ref(false);
// 控制头像编辑组件的显示
const showAvatarEditor = ref(false);

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

// 切换显示的文章列表
const changeTab = (tab) => {
  currentTab.value = tab;
  // 根据当前标签刷新数据
  switch (tab) {
    case 'articles':
      fetchArticle();
      break;
    case 'liked':
      fetchLiked();
      break;
    case 'collected':
      fetchCollected();
      break;
  }
};

// 删除文章
const deleteArticle = async (articleId) => {
  const jwtToken = localStorage.getItem("jwtToken");
  try {
    const { data } = await instance.delete(`/articles/${articleId}/delete`,{
      headers:{
        Authorization: `${jwtToken}`,
      }
    })
    if(data.code === 0){
      alert("文章删除成功！");
      window.location.reload();
    }
  }catch (error) {
    console.error(error);
  }
};

// 获取用户个人信息
const fetchUserInfo = async () => {
  try {
    const jwtToken = localStorage.getItem("jwtToken");
    const { data } = await instance.get("/user/profile",{
      headers:{
        Authorization: `${jwtToken}`,
      }
    });
    if(data.code === 0){
      userInfo.value = data.data;
      avatarSrc.value = data.data.avatarUrl;
    }
  } catch (error) {
    console.error("获取用户信息出错:", error);
  }
};

const fetchArticle = async()=>{
  try{
    const jwtToken = localStorage.getItem("jwtToken");
    const { data } = await instance.get("/user/created",{
      headers:{
        Authorization: `${jwtToken}`,
      }
    });
    if(data.code === 0){
      articles.value = data.data
    }
  } catch (error) {
    console.error("获取用户信息出错:", error);
  }
};

const fetchLiked = async() => {
  try{
    const jwtToken = localStorage.getItem("jwtToken");
    const { data } = await instance.get("/user/liked",{
      headers:{
        Authorization: `${jwtToken}`,
      }
    });
    if(data.code === 0){
      likedArticles.value = data.data
    }
  } catch (error) {
    console.error("获取用户信息出错:", error);
  }
}

const fetchCollected = async() => {
  try{
    const jwtToken = localStorage.getItem("jwtToken");
    const { data } = await instance.get("/user/collected",{
      headers:{
        Authorization: `${jwtToken}`,
      }
    });
    if(data.code === 0){
      collectedArticles.value = data.data
    }
  } catch (error) {
    console.error("获取用户信息出错:", error);
  }
}

// 组件挂载时获取用户信息
onMounted(() => {
  fetchUserInfo();
  fetchArticle();
  fetchLiked();
  fetchCollected();
});
</script>

<style scoped>
/* 应用容器 */
.app-container {
  min-height: 100vh;
}

.article-item-link {
  cursor: pointer;
  display: block;
  text-decoration: none;
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

.avatar-container:hover.change-avatar-tip {
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
  width: 24px;
  height: 24px;
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
  transition: color 0.3s ease; /* 添加过渡效果 */
}

.function-buttons span:hover {
  color: #ccc; /* 鼠标悬停时改变颜色 */
}

.article-list-container {
  width: 100%;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.avatar-editor-modal {
  position: fixed;
  top: 30%; /* 调整为中间偏上的位置 */
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(16, 16, 16, 0.923);
  color: white;
  padding: 20px;
  border: 1px solid #000000;
  border-radius: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  width: 240px; /* 调整窗口宽度 */
  height: 200px; /* 调整窗口高度 */
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

.cropper-container {
  width: 100%;
  overflow: hidden;
}

.cropper-container img {
  width: 100%;
}

.cropper-buttons {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.center-button {
  width: 80px; /* 可以根据需要调整按钮宽度 */
  margin: 0 auto; /* 使按钮水平居中 */
}
</style>
