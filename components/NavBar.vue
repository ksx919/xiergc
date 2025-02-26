<template>
  <nav class="nav-bar">
    <!-- logo -->
    <img class="logo" :src="logoSrc" alt="logo" />
    <!-- 搜索栏 -->
    <div class="search-bar">
      <button class="search-button" @click="search">
        <img
          :src="searchIconSrc"
          style="width: 21px; height: 21px"
          alt="搜索"
        />
      </button>
      <input class="search-input" v-model="searchValue" type="text" />
    </div>
    <!-- 图片按钮组 -->
    <div class="button-group">
      <a :href="button1Url" target="_self">
        <img
          :src="button1Hover ? button1SrcHover : button1Src"
          alt="按钮1"
          @mouseenter="button1Hover = true"
          @mouseleave="button1Hover = false"
          class="hoverable-button"
        />
      </a>
      <a :href="button2Url" target="_blank">
        <img
          :src="button2Hover ? button2SrcHover : button2Src"
          alt="按钮2"
          @mouseenter="button2Hover = true"
          @mouseleave="button2Hover = false"
          class="hoverable-button"
        />
      </a>
      <!-- 为按钮 3 添加 hoverable-button 类 -->
      <img :src="button3Src" alt="按钮3" class="hoverable-button" />
      <a :href="button4Url" target="_blank">
        <div
          class="user-button"
          @mouseenter="
            showDropdown = true;
            button4Hover = true;
          "
          @mouseleave="
            showDropdown = false;
            button4Hover = false;
          "
        >
          <img
            :src="button4Hover ? button4SrcHover : button4Src"
            alt="按钮4"
            class="hoverable-button"
          />
          <div v-show="showDropdown" class="dropdown">
            <div class="blur-overlay"></div>
            <div class="avatar-container">
              <img
                src="@/assets/image/user_head.png"
                alt="用户头像"
                style="width: 60px; height: auto"
              />
              <span class="avatar-text">Me</span>
            </div>
            <div class="user-info">
              <!-- 添加一个新的容器来包裹 level-text 和经验条 -->
              <div class="level-container">
                <p class="level-text">LV.7</p>
                <img
                  src="@/assets/image/level.png"
                  alt="经验条"
                  style="width: 100%; height: 100%; display: block"
                />
              </div>
              <div class="stats">
                <div>
                  <p class="small-text">关注</p>
                  <p class="small-text">100</p>
                </div>
                <div>
                  <p class="small-text">粉丝</p>
                  <p class="small-text">200</p>
                </div>
                <div>
                  <p class="small-text">获赞与收藏</p>
                  <p class="small-text">300</p>
                </div>
              </div>
            </div>
            <div class="divider-horizontal"></div>
            <div class="menu-items">
              <div class="menu-row">
                <a :href="personalPageUrl">
                  <img src="@/assets/image/personal.png" alt="个人主页" />
                  <span class="big-text">个人主页</span>
                </a>
                <div>
                  <img src="@/assets/image/task.png" alt="我的任务" />
                  <span class="big-text">我的任务</span>
                </div>
              </div>
              <div class="menu-row">
                <a :href="settingCenterUrl">
                  <img src="@/assets/image/setting.png" alt="设置中心" />
                  <span class="big-text">设置中心</span>
                </a>
                <div>
                  <img src="@/assets/image/shop.png" alt="我的购物" />
                  <span class="big-text">我的购物</span>
                </div>
              </div>
            </div>
            <div class="divider-horizontal"></div>
            <a href="#" class="logout">退出登录</a>
          </div>
        </div>
      </a>
    </div>
  </nav>
</template>

<script setup>
import { ref } from "vue";

// 图片路径
const logoSrc = require("@/assets/image/navlogo.png");
const searchIconSrc = require("@/assets/image/navsearch.png");
const button1Src = require("@/assets/image/home.png");
const button1SrcHover = require("@/assets/image/home.png");
const button2Src = require("@/assets/image/send.png");
const button2SrcHover = require("@/assets/image/send.png");
const button3Src = require("@/assets/image/bell.png");
const button4Src = require("@/assets/image/person.png");
const button4SrcHover = require("@/assets/image/person.png");

// 按钮跳转的网址
const button1Url = "/home"; // 假设主页路径为根路径，可根据实际情况修改
const button2Url = "/new"; // 请替换为实际网址
const button4Url = "/profile"; // 请替换为实际网址
const personalPageUrl = "/profile"; // 请替换为实际网址
const settingCenterUrl = "/setting"; // 请替换为实际网址

// 搜索相关
const searchValue = ref("");
const search = () => {
  // 这里可以添加搜索逻辑，例如调用 API 等
  console.log("搜索内容:", searchValue.value);
};

// 控制下拉菜单显示隐藏
const showDropdown = ref(false);

// 控制按钮悬停状态
const button1Hover = ref(false);
const button2Hover = ref(false);
const button4Hover = ref(false);
</script>

<style scoped>
/* 导航栏样式 */
.nav-bar {
  display: flex;
  justify-content: center; /* 让子元素在水平方向上居中 */
  align-items: center;
  padding: 10px 20px;
  background-color: rgba(0, 0, 0, 0.2); /* 透明背景 */
  backdrop-filter: blur(5px); /* 模糊后面底层元素 */
  /* 添加固定定位 */
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 9999; /* 设置 z-index 确保导航栏显示在最上层 */
  max-height: 50px;
}

/* logo样式 */
.logo {
  width: 80px; /* 调整logo图片大小 */
  height: auto;
  margin-right: 10px; /* 添加右侧外边距，使 logo 和搜索栏有间距 */
  padding-right: 50px;
}

/* 搜索栏样式 */
.search-bar {
  display: flex;
  align-items: center;
  border: 1px solid #5f5f5f; /* 保留搜索栏外部边框 */
  overflow: hidden;
  background-color: #757575; /* 设置搜索栏内部统一颜色 */
  width: 460px; /* 增加搜索框长度 */
  border-radius: 30px;
  height: 30px;
  margin-right: 10px; /* 添加右侧外边距，使搜索栏和图片按钮组有间距 */
}

/* 搜索按钮样式 */
.search-button {
  padding: 5px 5px;
  padding-top: 8px;
  background-color: transparent; /* 使按钮背景透明，与搜索栏颜色统一 */
  border: none;
}

/* 搜索输入框样式 */
.search-input {
  padding: 8px 10px;
  border: none;
  outline: none;
  background-color: transparent; /* 使输入框背景透明，与搜索栏颜色统一 */
  color: white; /* 设置输入框内文字颜色 */
  flex: 1; /* 让输入框占据剩余空间 */
}

/* 图片按钮组样式 */
.button-group {
  display: flex;
  align-items: center;
  padding-left: 40px;
}

/* 单个图片按钮样式 */
.button-group img {
  width: 25px;
  height: 25px;
  margin-right: 10px;
  padding: 20px;
  cursor: pointer;
  transition: width 0.3s, height 0.3s; /* 添加过渡效果 */
}

/* 可悬停按钮悬停时样式 */
.hoverable-button:hover {
  width: 40px; /* 悬停时宽度变大 */
  height: 40px; /* 悬停时高度变大 */
}

/* 用户按钮相关样式 */
.user-button {
  position: relative;
}

/* 下拉菜单样式（初始隐藏） */
.dropdown {
  display: none;
  position: absolute;
  border: none;
  border-radius: 4px;
  /* 增大内边距 */
  padding: 20px;
  top: 80px;
  right: 0;
  /* 增大宽度 */
  width: 300px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  transform-origin: top right; /* 设置缩放的原点为右上角 */
  transform: scale(0.8); /* 等比缩小到原来的 0.8 倍 */
}

/* 显示下拉菜单 */
.user-button:hover .dropdown {
  display: block;
}

.blur-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 110%;
  height: 110%;
  background-color: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(5px); /* 模糊效果 */
  z-index: -1;
  border-radius: 20px;
}

.avatar-container {
  display: flex;
  align-items: center;
  position: absolute;
  top: 20px;
  left: 20px;
}

.avatar-text {
  color: white;
  font-size: 30px; /* 与头像大小适配 */
  margin-left: 10px;
}

.user-info {
  /* 增大与头像的间距 */
  margin-top: 80px;
  color: white; /* 文字颜色设为白色 */
}

/* 新增的 level-container 样式 */
.level-container {
  display: block;
  flex-direction: column;
}

.level-text {
  font-size: 25px; /* LV.7 字体改大 */
  padding-top: 10%;
  padding-right: 67%;
  background: linear-gradient(to top, #005893, #b20ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 5px; /* 给经验条留出一些间距 */
}

.stats {
  display: flex;
  justify-content: space-around;
  /* 增大与经验条的间距 */
  margin-top: 20px;
  position: relative;
}

.small-text {
  font-size: 15px; /* 关注、粉丝、获赞与收藏等文字改小 */
}

.divider-horizontal {
  height: 1px;
  background-color: #fff;
  /* 增大分割线上下间距 */
  margin: 20px 0;
}

.menu-items {
  /* 增大与上面元素的间距 */
  margin-top: 30px;
  color: white; /* 文字颜色设为白色 */
}

.menu-row {
  display: flex;
  justify-content: space-between;
  /* 增大菜单行之间的间距 */
  margin-bottom: 30px;
}

.menu-items a,
.menu-items div {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: white; /* 文字颜色设为白色 */
}

.menu-items img {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}

.big-text {
  font-size: 18px; /* 个人主页、我的任务等文字改大 */
}

.logout {
  display: block;
  text-align: right;
  /* 增大与上面元素的间距 */
  margin-top: 20px;
  color: white; /* 文字颜色设为白色 */
  text-decoration: none;
}
</style>
