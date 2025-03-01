<template>
    <div class="app-container">
      <to-top />
      <div class="content-container">
        <div class="home-page">
          <!-- 导航栏 -->
          <Navbar @search-result="updateArticles"/>
          
          <!-- 页面主体 -->
          <div id="container-page">
            <!-- 侧边栏 -->
            <SideNav />
            <!-- 文章内容区域 -->
            <article class="box">
              <!-- 搜索结果列表组件 -->
               <!-- 幻灯片 -->
              <SlideShow />
              <ReturnArticle 
                :search-query="currentSearchQuery"
                :key="componentRefreshKey"
                :articles="articles"
              />
            </article>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import SlideShow from "../components/SlideShow.vue";
  import { ref, computed, watch } from 'vue'
  import { useRoute } from 'vue-router'
  import Navbar from '@/components/NavBar.vue'
  import SideNav from '@/components/SideNav.vue'
  import ReturnArticle from '@/components/ReturnArticle.vue'
  import ToTop from '@/components/ToTop.vue'
  import '@/components/background/background.css'
  
  // 路由实例
  const route = useRoute()
  
  const articles = ref([]);
  // 组件刷新控制
  const componentRefreshKey = ref(0)
  
  // 当前搜索词（从路由参数获取）
  const currentSearchQuery = computed(() => {
    return route.query.q || ''
  })
  
  // 监听搜索词变化
  watch(
    () => route.query.q,
    (newVal, oldVal) => {
      if (newVal !== oldVal) {
        componentRefreshKey.value++ // 强制刷新子组件
      }
    }
  )
  
  // 更新 articles
  const updateArticles = (newArticles) => {
    articles.value = newArticles; // 将新的搜索结果存入 articles
  }
  
  // 初始加载处理
  const initialLoad = () => {
    if (route.query.q) {
      componentRefreshKey.value++
    }
  }
  
  // 组件挂载时执行
  initialLoad()
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
  