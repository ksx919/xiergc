<template>
  <div class="article-show-container">
    <h3 class="related-title">相关推荐</h3>
    <!-- 相关推荐文章列表 -->
    <div class="related-articles">
      <a
        v-for="article in relatedArticles"
        :key="article.id"
        :href="article.link"
        class="article-item-link"
      >
        <div class="article-item">
          <h2 class="article-title">{{ article.title }}</h2>
          <p class="article-description">{{ article.description }}</p>
          <img class="cover" :src="coverSrc" alt="封面" />
          <div class="author-info">
            <span class="nickname">作者昵称</span>
            <span class="date">{{ article.date }}</span>
          </div>
        </div>
      </a>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

// 图片路径
const coverSrc = require("@/assets/image/postimg.png");

const relatedArticles = ref([
  {
    id: 1,
    title: "相关文章标题 1",
    description: "相关文章描述 1",
    link: "/article/1",
    date: "2024-01-01",
    clickCount: 100,
    collectCount: 20,
    likeCount: 30,
    commentCount: 10,
  },
  {
    id: 2,
    title: "相关文章标题 2",
    description: "相关文章描述 2",
    link: "/article/2",
    date: "2024-01-02",
    clickCount: 200,
    collectCount: 30,
    likeCount: 40,
    commentCount: 20,
  },
  {
    id: 3,
    title: "相关文章标题 3",
    description: "相关文章描述 3",
    link: "/article/2",
    date: "2024-01-02",
    clickCount: 200,
    collectCount: 30,
    likeCount: 40,
    commentCount: 20,
  },
  {
    id: 4,
    title: "相关文章标题 4",
    description: "相关文章描述 4",
    link: "/article/4",
    date: "2024-01-02",
    clickCount: 200,
    collectCount: 30,
    likeCount: 40,
    commentCount: 20,
  },
]);
</script>

<style scoped>
.article-show-container {
  position: fixed;
  /* 调整位置到中间偏右 */
  top: 70%;
  right: 15%;
  transform: translateY(-50%);
  width: 250px;
  height: 400px;
  overflow-y: auto;
  background-color: rgba(0, 0, 0, 0.488);
  border: 1px solid #000000;
  padding: 20px;
  border-radius: 30px;
  backdrop-filter: blur(5px);
  color: #ffffff;
  z-index: 10;

  /* 隐藏滚动条 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
}

.article-show-container::-webkit-scrollbar {
  display: none; /* Chrome, Safari and Opera */
}

.related-title {
  margin-top: 0;
  font-size: 30px;
  margin-bottom: 20px;
  text-align: left;
}

.related-articles {
  display: flex;
  flex-direction: column;
  font-size: 10px;
  gap: 20px;
}

.article-item-link {
  text-decoration: none;
  color: #ffffff;
}

.article-item {
  /* 去掉文章的方框，透明掉 */
  border: none;
  background-color: transparent;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.article-title {
  margin-top: 0;
  margin-bottom: 10px;
}

.article-description {
  margin-top: 0;
  margin-bottom: 10px;
}

.cover {
  width: 80%;
  height: auto;
  object-fit: cover;
  margin-bottom: 10px;
}

.author-info {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nickname {
  font-size: 13px;
}

.date {
  color: #ffffff;
  font-size: 10px;
  opacity: 0.9;
}
</style>
