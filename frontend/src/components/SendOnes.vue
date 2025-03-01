<template>
  <div class="app-container">
    <NavBar />
    <div class="content-container">
      <div class="black-rectangle" v-if="!isSent">
        <MarkdownEditor ref="markdownEditor" />
        <div class="bottom-info">
          <div class="bottom-messages">
            <span>已保存草稿</span>
            <span>字数: {{ formattedWordCount }}</span>
          </div>
          <button
            class="send-button"
            @mouseover="hoverSendButton = true"
            @mouseout="hoverSendButton = false"
            @click="sendArticle"
            :disabled="isSending"
          >
            <img
              :src="hoverSendButton ? writeHoverIconSrc : writeIconSrc"
              alt="发送按钮"
              class="send-button-img"
            />
            <span v-if="isSending" class="sending-text">发送中...</span>
          </button>
        </div>
      </div>
      <div class="black-rectangle sent-message" v-else>
        <div class="animation-container">
          <img
            ref="flyImage"
            :src="flyIconSrc"
            alt="飞行图片"
            style="opacity: 1; transition: opacity 2s ease"
            @transitionend="handleFirstImageTransitionEnd"
          />
          <div
            class="second-image-container"
            ref="secondImageContainer"
            style="opacity: 0; transition: opacity 2s ease"
          >
            <img
              :src="writeFlyIconSrc"
              alt="飞行文字图片"
              class="second-image"
            />
            <span class="send-success-text">发送成功</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import instance from "@/api/axios.js";
import { useRouter } from "vue-router";
import { ref, computed, watch } from "vue";
import NavBar from "./NavBar.vue";
import MarkdownEditor from "@/components/MakeDown/MarkdownEditor.vue";
import writeIconSrc from "@/assets/image/write.png";
import writeHoverIconSrc from "@/assets/image/writte.png";
import flyIconSrc from "@/assets/image/fly.png";
import writeFlyIconSrc from "@/assets/image/sendget.png";
import "@/components/background/background.css";

const router = useRouter();
// 新增获取 MarkdownEditor 组件引用
const markdownEditor = ref(null)
// 记录是否发送成功
const isSent = ref(false);
// 记录鼠标是否悬停在发送按钮上
const hoverSendButton = ref(false);
// 正文的字数，暂时简单初始化，可根据需求完善计算逻辑
const wordCount = ref(0);
// 飞行图片元素引用
const flyImage = ref(null);
// 第二个图片和文字容器引用
const secondImageContainer = ref(null);

// 计算格式化字数
const formattedWordCount = computed(() => {
  return wordCount.value.toLocaleString();
});

// 监听内容变化计算字数
watch(
  () => markdownEditor.value?.input,
  (newContent) => {
    if (!newContent) {
      wordCount.value = 0;
      return;
    }
    // 统计规则：中文按字计数，英文按单词计数
    const chineseChars = (newContent.match(/[\u4e00-\u9fa5]/g) || []).length;
    const englishWords = newContent
      .replace(/[\u4e00-\u9fa5]/g, '') // 移除中文
      .split(/[\s\r\n]+/)
      .filter(word => word.length > 0).length;
    wordCount.value = chineseChars + englishWords;
  },
  { immediate: true, deep: true }
);




// 发送文章
const isSending = ref(false)

const sendArticle = async () => {
  if (isSending.value) return
  
  const jwtToken=localStorage.getItem('jwtToken');

  const title = markdownEditor.value.articleTitle
  const content = markdownEditor.value.input

  if (!title.trim()) {
    alert('请输入文章标题')
    return
  }

  if (content.length < 50) {
    alert('文章内容至少需要50字')
    return
  }

  try {
    isSending.value = true
    const { data } = await instance.post('/articles/post', {
      title,
      content
    },{
      headers:{
        Authorization: `${jwtToken}`
      }
    })

    if (data.code === 0) {
      isSent.value = true
      setTimeout(() => {
        flyImage.value.style.opacity = 0
      }, 100)
    }
  } catch (error) {
    console.error('发送失败:', error)
    alert(error.data?.data?.message || '发送失败，请稍后重试')
  } finally {
    isSending.value = false
  }
}

// 处理第一个图片过渡结束事件
const handleFirstImageTransitionEnd = () => {
  setTimeout(() => {
    secondImageContainer.value.style.opacity = 1;
    setTimeout(() => {
      router.push("/home");
    }, 2400);
  }, 1500);
};
</script>

<style scoped>
.sending-text {
  color: #888;
  font-size: 14px;
  margin-left: 10px;
}

/* 应用容器 */
.app-container {
  min-height: 100vh;
}

/* 内容容器，可滚动 */
.content-container {
  min-height: 100vh;
  overflow-y: auto;
  background-attachment: fixed; /* 固定背景 */
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 120px;
}

/* 黑色矩形框 */
.black-rectangle {
  background-color: rgba(0, 0, 0, 0.8);
  width: 60%;
  padding: 40px;
  border-radius: 10px;
  color: white;
  font-size: 18px;
  backdrop-filter: blur(10px); /* 添加模糊效果 */
}

/* 发送成功后的黑色矩形框 */
.sent-message {
  width: 60%; /* 手动设置宽度，不跟随内容改变 */
  height: 500px;
  padding: 28px;
  border-radius: 30px;
  backdrop-filter: blur(10px);
  box-sizing: border-box; /* 确保内边距和边框不会影响宽度 */
}

/* 底部信息 */
.bottom-info {
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  margin-top: 20px;
}

/* 底部消息 */
.bottom-messages {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  padding-top: 20px;
  margin-right: 25px; /* 调整与发送按钮的间距 */
  font-size: 16px; /* 调整字体大小 */
}

.bottom-messages span {
  margin-bottom: 10px; /* 增大底部消息之间的距离 */
}

/* 发送按钮 */
.send-button {
  background: none;
  border: none;
  cursor: pointer;
  margin-bottom: 12px; /* 调整发送按钮与底部消息的间距 */
}

/* 发送按钮图片 */
.send-button-img {
  width: 80px; /* 调整发送按钮图片宽度 */
  height: auto; /* 调整发送按钮图片高度 */
}

/* 发送成功消息框 */
.sent-message {
  text-align: center;
}

/* 动画容器 */
.animation-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%; /* 确保容器占据黑框的高度 */
}

/* 飞行图片 */
.animation-container img:first-child {
  max-width: 50%; /* 增大第一个图片尺寸 */
}

/* 第二个图片和文字容器 */
.second-image-container {
  display: flex; /* 让图片和文字左右同行 */
  justify-content: center;
  position: absolute;
  align-items: center;
  width: 100%; /* 确保容器宽度为 100% */
  margin-bottom: 20px;
}

/* 第二个图片 */
.second-image {
  width: 8%; /* 减小第二个图片的尺寸 */
  height: auto;
}

/* 发送成功文字 */
.send-success-text {
  color: white;
  margin-left: 20px;
  margin-bottom: 10px; /* 调整文字与图片的间距 */
  vertical-align: middle;
  font-size: 41px; /* 增大发送成功文字大小 */
  display: inline-block;
}

/* 适配 MarkdownEditor 组件内输入框宽度 */
.markdown-editor {
  width: 100%;
}

.markdown-editor .input-preview-container {
  width: 100%;
}

.markdown-editor .markdown-input,
.markdown-editor .markdown-preview {
  width: calc(50% - 5px); /* 减去间距 */
  box-sizing: border-box;
}

/* 设置输入框透明背景和白色提示词 */
.markdown-editor .article-title-input,
.markdown-editor .markdown-input {
  background-color: transparent;
  border: none;
  color: white;
  padding: 10px;
  border-radius: 10px;
}

.markdown-editor .article-title-input::placeholder,
.markdown-editor .markdown-input::placeholder {
  color: white;
}
</style>
