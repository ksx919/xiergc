<template>
    <div class="markdown-viewer" v-html="processedContent"></div>
  </template>
  
  <script setup>
  import { computed } from 'vue';
  import MarkdownIt from 'markdown-it';
  import DOMPurify from 'dompurify';
  
  const props = defineProps({
    content: {
      type: String,
      default: ''
    },
    maxLength: {
      type: Number,
      default: 0
    }
  });
  
  const md = new MarkdownIt({
    html: false,
    linkify: true,
    breaks: true,
    typographer: true
  });
  
  const processedContent = computed(() => {
    // 安全过滤
    const safeContent = DOMPurify.sanitize(props.content || '', {
      ALLOWED_TAGS: ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'strong', 'em', 'blockquote', 
                    'code', 'pre', 'a', 'ul', 'ol', 'li', 'img', 'br', 'hr'],
      ALLOWED_ATTR: ['href', 'src', 'alt', 'title']
    });
  
    // 内容截取
    let processed = safeContent;
    if (props.maxLength > 0) {
      const truncated = safeContent.slice(0, props.maxLength);
      // 查找最后一个段落结束
      const lastParagraphEnd = truncated.lastIndexOf('\n\n');
      processed = lastParagraphEnd > 0 ? 
        truncated.slice(0, lastParagraphEnd) : 
        truncated;
    }
  
    // 渲染 Markdown
    const rendered = md.render(processed);
    
    // 添加省略提示
    return props.maxLength > 0 && safeContent.length > props.maxLength ?
      `${rendered}<p class="truncate-hint">...</p>` :
      rendered;
  });
  </script>
  
  <style scoped>
  .markdown-viewer {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
    line-height: 1.6;
    color: #e0e0e0;
  }
  
  .markdown-viewer :deep() h1,
  .markdown-viewer :deep() h2,
  .markdown-viewer :deep() h3 {
    color: #ffffff;
    margin: 15px 0 10px;
  }
  
  .markdown-viewer :deep() p {
    margin: 8px 0;
  }
  
  .markdown-viewer :deep() a {
    color: #00a1d6;
    text-decoration: none;
  }
  
  .markdown-viewer :deep() code {
    background-color: rgba(255, 255, 255, 0.1);
    padding: 2px 4px;
    border-radius: 3px;
  }
  
  .markdown-viewer :deep() pre {
    background-color: rgba(0, 0, 0, 0.3);
    padding: 10px;
    border-radius: 5px;
    overflow-x: auto;
  }
  
  .markdown-viewer :deep() blockquote {
    border-left: 3px solid #666;
    color: #999;
    padding-left: 10px;
    margin: 10px 0;
  }
  
  .truncate-hint {
    color: #999 !important;
    margin: 5px 0 !important;
  }
  </style>