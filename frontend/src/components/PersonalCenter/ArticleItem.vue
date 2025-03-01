<template>
  <a :href="article.link" class="article-item-link">
    <div class="article-item">
      <!-- 鼠标悬停区域 -->
      <div class="dropdown"
        v-if="showDelete"
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
      <div v-if="showAuthor" class="author-info">
        <img :src="article.authorAvatarUrl" class="author-avatar" />
        <span class="author-name">{{ article.authorName }}</span>
      </div>
      <div class="article-text">
        <h2 class="single-line-title">{{ article.title }}</h2>
        <MarkdownViewer :content="article.content" :max-length="100" />
      </div>
      <div class="article-actions">
        <img class="action-icon" :src="clickIconSrc" />
        <span>{{ article.clicks }}</span>
        <img class="action-icon" :src="collectIconSrc" />
        <span>{{ article.collect }}</span>
        <img class="action-icon" :src="likeIconSrc" />
        <span>{{ article.likes }}</span>
        <img class="action-icon" :src="commentIconSrc" />
        <span>{{ article.comment }}</span>
      </div>
    </div>
  </a>
</template>

<script setup>
import { ref } from "vue";
import MarkdownViewer from '../MakeDown/MarkdownViewer.vue';
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
  showAuthor: {
    type: Boolean,
    default: false
  },
  showDelete: {
    type: Boolean,
    default: false
  }
});

const showDeleteMenu = ref(false);

const handleDelete = () => {
  props.onDelete(props.article.id);
  showDeleteMenu.value = false;
};

</script>

<style scoped>
.author-info {
  position: absolute;
  top: 10px;
  left: 10px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

.author-name {
  font-size: 14px;
  color: #999;
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
  padding-right: 20%;
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
