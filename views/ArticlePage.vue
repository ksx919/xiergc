<template>
  <div class="app-container">
    <!-- 导航栏 -->
    <NavBar />
    <div class="content-container">
      <!-- 文章详细内容 -->
      <ArticleDetail />
      <!-- 相关推荐 -->
      <ArticleShow />
      <!-- 固定在右边的组件 -->
      <UpOne />
    </div>
  </div>
</template>

<script setup>
import NavBar from "@/components/NavBar.vue";
import ArticleDetail from "@/components/ArticleDetail.vue";
import ArticleShow from "@/components/ArticleShow.vue";
import UpOne from "@/components/UpOne.vue";
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
</style>
