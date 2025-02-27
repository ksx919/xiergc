<template>
  <a :href="article.link" class="article-item-link">
    <div class="article-item">
      <!-- 鼠标悬停区域 -->
      <div
        class="dropdown"
        @mouseenter="showDeleteMenu = true"
        @mouseleave="showDeleteMenu = false"
      >
        <img class="deng-icon" :src="dengIconSrc" alt="设置图标" />
        <div
          v-if="showDeleteMenu"
          class="delete-menu"
          @click.stop="handleDelete"
        >
          <span>删除</span>
        </div>
      </div>
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
        <img class="action-icon" :src="commentIconSrc" alt="评论数量" />
        <span>{{ article.commentCount }}</span>
      </div>
    </div>
  </a>
</template>

<script setup>
import { ref, defineProps } from "vue";
// 直接导入图标
const dengIconSrc = require("@/assets/image/deng.png");
const clickIconSrc = require("@/assets/image/click.png");
const collectIconSrc = require("@/assets/image/collect.png");
const likeIconSrc = require("@/assets/image/like.png");
const commentIconSrc = require("@/assets/image/comment.png");

const props = defineProps({
  article: {
    type: Object,
    required: true,
  },
  onDelete: {
    type: Function,
    required: true,
  },
});

const showDeleteMenu = ref(false);

const handleDelete = () => {
  props.onDelete(props.article.id);
  showDeleteMenu.value = false;
};
</script>

<style scoped>
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
  background-color: rgba(105, 105, 105, 0.8);
  opacity: 0.3;
  border-radius: 30px;
  z-index: -1;
}

.deng-icon {
  position: absolute;
  left: 750px; /* 调整到最右侧 */
  width: 5x; /* 适当调整图片大小 */
  height: 20px;
  cursor: pointer;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.delete-menu {
  position: absolute;
  top: 20px; /* 调整菜单位置 */
  left: 730px;
  background-color: rgba(16, 16, 16, 0.923);
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  display: none;
  white-space: nowrap; /* 防止文字换行 */
}

.dropdown:hover .delete-menu {
  display: block;
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
