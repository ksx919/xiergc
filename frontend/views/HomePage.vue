<template>
  <div class="app-container">
    <div class="content-container">
      <div class="home-page">
        <!-- 导航栏 -->
        <Navbar />
        <div id="container-page">
          <!-- 侧边栏 -->
          <SideNav />
          <article class="box">
            <!-- 幻灯片 -->
            <SlideShow />
            <!-- 文章列表 -->
            <ArticleList />
          </article>
        </div>
        <!-- 底部信息 -->
        <FooterComponent />
      </div>
    </div>
  </div>
</template>

<script setup>
import SlideShow from "../components/SlideShow.vue";
import ArticleList from "../components/ArticleList.vue";
import SideNav from "../components/SideNav.vue";
import Navbar from "@/components/NavBar.vue";
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
}

/* 主页样式 */
.home-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 容器页面样式 */
#container-page {
  display: flex;
  flex-grow: 1;
}

/* 文章区域样式 */
.box {
  flex-grow: 1;
  padding: 20px;
}
</style>
