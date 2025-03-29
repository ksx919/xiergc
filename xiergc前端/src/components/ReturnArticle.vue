<template>
    <div class="article-list-container">
      <!-- 顶部的分类分区 -->
      <div class="category-box">
        <span class="category-item">搜索结果：</span>
      </div>
      <!-- 文章列表 -->
      <div class="article-list">
        <a
          v-for="article in articles"
          :key="article.id"
          class="article-item-link"
          @click.prevent="handleArticleClick(article.id)"
        >
          <div class="article-item">
            <div class="article-header">
              <!-- 修改头像部分 -->
              <img 
                class="avatar" 
                :src="article.authorAvatarUrl || defaultAvatarSrc" 
                alt="作者头像"
              />
              <div class="author-info">
                <!-- 修改作者名称部分 -->
                <span class="nickname">{{ article.authorName || '匿名作者' }}</span>
                <span class="date">{{ formatDate(article.publishDate) }}</span>
              </div>
            </div>
            <div class="article-content">
              <div class="article-text">
                <h2>{{ article.title }}</h2>
                <div 
                  class="markdown-preview"
                  v-html="renderMarkdownPreview(article.content)"
                ></div>
              </div>
              
            </div>
            <div class="article-actions">
              <img class="action-icon" :src="clickIconSrc" alt="点击数" />
              <span>{{ article.clicks }}</span>
              <img class="action-icon" :src="collectIconSrc" alt="收藏" />
              <span>{{ article.collect }}</span>
              <img class="action-icon" :src="likeIconSrc" alt="喜欢" />
              <span>{{ article.likes }}</span>
              <img class="action-icon" :src="commentIconSrc" alt="评论数量" />
              <span>{{ article.comment }}</span>
            </div>
          </div>
        </a>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import instance from "@/api/axios.js";
  import MarkdownIt from 'markdown-it';
  
  // 图片路径
  const defaultAvatarSrc = require("@/assets/image/Userde.png");
  const clickIconSrc = require("@/assets/image/click.png");
  const collectIconSrc = require("@/assets/image/collect.png");
  const likeIconSrc = require("@/assets/image/like.png");
  const commentIconSrc = require("@/assets/image/comment.png");
  
  // 文章数据
  const jwtToken = localStorage.getItem("jwtToken");
  
  const { articles } = defineProps({
  articles: {
    type: Array,
    required: true
  }
})

  const md = new MarkdownIt({
    html: false,
    breaks: true,
    linkify: true
  });
  
  const isNavigating = ref(false);
  
  const handleArticleClick = async (articleId) => {
    if (isNavigating.value) return;
    isNavigating.value = true;
  
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
  
  const renderMarkdownPreview = (content) => {
    if (!content) return '';
    const maxLength = 100;
    const preview = content.slice(0, maxLength);
    const lastNewLine = preview.lastIndexOf('\n');
    const sanitizedContent = preview.slice(0, lastNewLine > 0 ? lastNewLine : maxLength);
    return md.render(sanitizedContent + (content.length > maxLength ? '...' : ''));
  };
  
  // 新版日期格式化函数
  const formatDate = (dateArray) => {
    try {
      if (!Array.isArray(dateArray)) return '无效日期格式';
      
      // 确保数组长度足够
      const paddedDate = [...dateArray];
      while(paddedDate.length < 6) paddedDate.push(0);
  
      const [year, month, day, hours, minutes] = paddedDate;
  
      // 基础验证
      if (
        typeof year !== 'number' || year < 1970 || year > 2100 ||
        typeof month !== 'number' || month < 1 || month > 12 ||
        typeof day !== 'number' || day < 1 || day > 31 ||
        typeof hours !== 'number' || hours < 0 || hours > 23 ||
        typeof minutes !== 'number' || minutes < 0 || minutes > 59
      ) {
        return '日期数据异常';
      }
  
      // 直接拼接成目标格式
      return `${year}-${month}-${day} ${hours}:${minutes.toString().padStart(2, '0')}`;
      
    } catch (error) {
      console.error('日期处理失败:', error);
      return '日期解析错误';
    }
  };
  
  </script>
  
  <style scoped>
  .markdown-preview :deep() p {
    color: #e0e0e0;
    line-height: 1.6;
    margin: 8px 0;
    font-size: 15px;
  }
  
  .markdown-preview :deep() a {
    color: #00a1d6;
    text-decoration: none;
  }
  
  .markdown-preview :deep() code {
    background: rgba(255,255,255,0.1);
    padding: 2px 4px;
    border-radius: 3px;
  }
  
  .article-list-container {
    width: 53%;
    padding-left: 28%;
  }
  
  .category-box {
    display: flex;
    border: 1px solid #000000;
    background-color: #000000;
    padding: 10px;
    opacity: 0.8;
    margin-bottom: 20px;
    justify-content: flex-start; /* 让子元素从左开始排列 */
    border-radius: 20px;
  }
  
  .category-item {
    cursor: pointer;
    margin-right: 30px; /* 给每个分类项之间添加一些间距 */
    margin-top: 10px;
    margin-bottom: 10px;
    margin-left: 20px;
    font-size: 20px;
    color: #ffffff;
  }
  
  .category-item:hover {
    color: #ffffff;
  }
  
  .article-list {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .article-item-link {
    cursor: pointer;
    display: block;
    text-decoration: none;
  }
  
  .article-item-link:hover .article-item {
    background-color: rgba(255, 255, 255, 0.05);
    transition: background-color 0.3s ease;
  }
  
  .article-item {
    border: 1px solid #000000;
    padding: 20px;
    position: relative;
    background-color: transparent; /* 设置背景为透明 */
    color: #ffffff; /* 文字颜色为白色 */
    border-radius: 30px;
  }
  
  .article-item::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.8); /* 半透明黑色背景 */
    opacity: 0.8;
    border-radius: 30px;
    z-index: -1;
  }
  
  .article-header {
    display: flex;
    align-items: flex-start;
    margin-bottom: 10px;
    height: 30px;
    width: auto;
  }
  
  .avatar {
    width: 60px; /* 增大头像大小 */
    height: 60px;
    border-radius: 50%;
    margin-right: 10px;
  }
  
  .author-info {
    display: flex;
    flex-direction: column;
  }
  
  .nickname {
    font-size: 20px;
  }
  
  .date {
    margin-top: 10px;
    color: #ffffff;
    font-size: 10px;
    opacity: 0.9;
  }
  
  .article-content {
    position: relative;
    display: flex;
    flex-direction: row;
  }
  
  .article-text {
    flex: 1;
    margin-top: 20px;
    margin-right: 60%;
  }
  
  .cover {
    width: 100px; /* 可以根据需要调整封面图片的宽度 */
    height: auto;
    object-fit: cover;
    position: relative;
    margin-top: 20px;
    margin-bottom: 10%;
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
  