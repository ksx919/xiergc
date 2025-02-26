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
            <img class="avatar" :src="avatarSrc" alt="头像" />
            <span class="level">LV.7</span>
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
          <span>文章</span>
          <span>想法</span>
          <span>点赞</span>
          <span>收藏</span>
          <span>购物</span>
          <span>任务</span>
        </div>
        <div class="divider"></div>
        <!-- 文章列表 -->
        <div class="article-list-container">
          <div class="article-list">
            <a
              v-for="article in articles"
              :key="article.id"
              :href="article.link"
              class="article-item-link"
            >
              <div class="article-item">
                <img class="deng-icon" :src="dengIconSrc" alt="灯图标" />
                <div class="article-text">
                  <h2>{{ article.title }}</h2>
                  <p>{{ article.description }}</p>
                </div>
                <div class="article-actions">
                  <img class="action-icon" :src="clickIconSrc" alt="点击数" />
                  <span>{{ article.clickCount }}</span>
                  <img class="action-icon" :src="collectIconSrc" alt="收藏" />
                  <span>{{ article.collectCount }}</span>
                  <img class="action-icon" :src="likeIconSrc" alt="喜欢" />
                  <span>{{ article.likeCount }}</span>
                  <img
                    class="action-icon"
                    :src="commentIconSrc"
                    alt="评论数量"
                  />
                  <span>{{ article.commentCount }}</span>
                </div>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

// 图片路径
const backgroundImageSrc = require("@/assets/image/backgroundp.png");
const avatarSrc = require("@/assets/image/user.png");
const settingIconSrc = require("@/assets/image/setting.png");
const dengIconSrc = require("@/assets/image/deng.png");
const clickIconSrc = require("@/assets/image/click.png");
const collectIconSrc = require("@/assets/image/collect.png");
const likeIconSrc = require("@/assets/image/like.png");
const commentIconSrc = require("@/assets/image/comment.png");

// 设置中心跳转链接
const settingUrl = "/setting";

const articles = ref([
  {
    id: 1,
    title: "文章标题 1",
    link: "/article/1",
    clickCount: 100,
    collectCount: 20,
    likeCount: 30,
    commentCount: 10,
  },
  {
    id: 2,
    title: "文章标题 2",
    link: "/article/2",
    clickCount: 200,
    collectCount: 30,
    likeCount: 40,
    commentCount: 20,
  },
  {
    id: 3,
    title: "文章标题 3",
    link: "/article/3",
    clickCount: 300,
    collectCount: 40,
    likeCount: 50,
    commentCount: 30,
  },
  {
    id: 4,
    title: "文章标题 4",
    link: "/article/4",
    clickCount: 400,
    collectCount: 50,
    likeCount: 60,
    commentCount: 40,
  },
]);

// 引入背景样式文件
import "@/components/background/background.css";
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

.article-item-link {
  text-decoration: none;
}

.article-item {
  border: 1px solid #000000;
  border-bottom: 1px solid #ffffff;
  padding: 20px;
  position: relative;
  background-color: rgba(16, 16, 16, 0.923);
  color: #ffffff;
  border-radius: 30px;
}
.article-item::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(105, 105, 105, 0.8); /* 半透明黑色背景 */
  opacity: 0.3;
  border-radius: 30px;
  z-index: -1;
}

.deng-icon {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 5px;
  height: 20px;
}

.article-text {
  margin: 20px;
  padding-right: 70%;
  padding-bottom: 5%;
}

.article-actions {
  display: flex;
  align-items: center;
  position: absolute;
  bottom: 20px;
  right: 20px;
}

.action-icon {
  width: 20px;
  height: 20px;
  margin-right: 5px;
  margin-left: 35px;
}
</style>
