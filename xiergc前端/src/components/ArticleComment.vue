<template>
  <div class="comment-section">
    <!-- 遍历评论列表 -->
    <div v-for="(comment, index) in comments" :key="index" class="comment-box">
      <div class="comment-header">
        <span class="nickname">{{ comment.nickname }}</span>
        <span class="comment-date">{{ formatDate(comment.rawData?.publishDate) }}</span>
      </div>
      <div class="comment-content">
        <p>{{ comment.comment }}</p>
      </div>
      <div class="comment-footer">
        <div class="comment-stats">
          <!-- 更多按钮 -->
          <button
            v-if="comment.replies.length > 0"
            @click="toggleReplies(index)"
            class="more-button"
          >
            {{ showMoreReplies[index] ? "收起" : "更多" }}
          </button>
          <!-- 评论数图片按钮，点击切换评论区显示状态 -->
          <img
            class="action-icon comment-count-icon"
            :src="commentIconSrc"
            alt="评论"
            title="点击评论"
            @click="toggleCommentSection(index)"
          />
          <span>{{ comment.commentCount }}</span>
        </div>
      </div>

      <!-- 新增的评论区，样式和第二段代码一致 -->
      <div v-if="isCommentSectionOpen[index]" class="article-comment-box">
        <textarea
          v-model="newComment[index]"
          class="comment-input"
          placeholder="请输入评论"
        ></textarea>
        <button class="send-button" @click="sendNewComment(index)">
          <img :src="sendIconSrc" alt="发送按钮" />
        </button>
      </div>

      <!-- 子评论 -->
      <div
        v-if="showMoreReplies[index] && comment.replies.length > 0"
        class="comment-replies"
      >
        <div
          v-for="(reply, replyIndex) in comment.replies"
          :key="replyIndex"
          class="reply-box"
        >
          <span class="reply-nickname">{{ reply.nickname }}</span>
          <span class="reply-date">{{ formatDate(reply.rawData?.publishDate) }}</span>
          <p>{{ reply.comment }}</p>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup>
import instance from "@/api/axios.js";
import { useRoute } from "vue-router";
import { toRaw } from 'vue';
import { ref } from "vue";

const route = useRoute();
const props = defineProps({
  comments: {
    type: Array,
    default: () => [],
  },
});

const formatDate = (dateArray) => {
  if (!dateArray || dateArray.length < 5) return '未知时间';
  
  // 解构日期组件（注意月份需要-1）
  const [year, month, day, hour, minute] = dateArray;
  
  // 创建Date对象（月份从0开始）
  const date = new Date(year, month - 1, day, hour, minute);
  
  // 格式化为 YYYY-MM-DD HH:mm
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};

// 响应式状态，只保留与子评论显示相关的状态
const showMoreReplies = ref(props.comments.map(() => false));
// 新增：控制评论区是否打开的响应式状态
const isCommentSectionOpen = ref(props.comments.map(() => false));
// 新增：存储新评论内容的响应式状态
const newComment = ref(props.comments.map(() => ""));

const commentIconSrc = require("@/assets/image/comment.png");
const sendIconSrc = require("@/assets/image/write.png");

// 切换子评论的显示与隐藏
const toggleReplies = (index) => {
  showMoreReplies.value[index] = !showMoreReplies.value[index];
};

// 点击评论数图片切换评论区显示状态的方法
const toggleCommentSection = (index) => {
  isCommentSectionOpen.value[index] = !isCommentSectionOpen.value[index];
};

// 发送新评论的方法
const sendNewComment = async (index) => {
  if (newComment.value[index].trim() === "") {
    return;
  }
  try {
    const jwtToken = localStorage.getItem("jwtToken");
    const rawComment = toRaw(props.comments[index]);

    console.log('原始评论对象:', rawComment);
    console.log('rawData内容:', rawComment.rawData);

    const articleId = route.params.id; // 获取当前文章的 ID
    const commentId = rawComment.rawData?.id;

    if (!commentId) {
      throw new Error("无法获取父评论ID");
    }
    console.log('当前评论对象:', props.comments[index]);
    
    const url = `/articles/${articleId}/comments/${commentId}`;  // 拼接评论的 URL
    
    const data = {
      content: newComment.value[index],
    };

    // 发送 POST 请求到服务器
    const { data: response } = await instance.post(url, data, {
      headers: {
        Authorization: `${jwtToken}`,
      },
    });

    // 如果请求成功
    if (response.code === 0) {
      console.log("发送评论成功:", response.data);
      // 可以在这里更新评论数量等操作
      newComment.value[index] = "";
      isCommentSectionOpen.value[index] = false;
      alert("评论发送成功！");
      location.reload();
    }
  } catch (error) {
    console.error("发送评论失败:", error);
  }
};
</script>

<style scoped>
/* 新增评论时间样式 */
.comment-header {
  display: flex;
  align-items: center;
  gap: 15px; /* 增加元素间距 */
  margin-bottom: 20px;
}

.comment-date {
  font-size: 0.7em;
  color: #888;
}

/* 调整子评论日期样式 */
.reply-date {
  font-size: 0.8em;
  color: #666;
  margin-left: 10px;
}

.comment-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 30px;
}

.comment-box {
  border: 1px solid #000000;
  padding: 35px;
  position: relative;
  background-color: rgba(0, 0, 0, 0.488);
  color: #ffffff;
  border-radius: 30px;
  backdrop-filter: blur(5px);
  margin-bottom: 20px;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  font-size: 25px;
}

.comment-content {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  margin-bottom: 20px;
}

.comment-footer {
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
}

.comment-stats {
  display: flex;
  align-items: center;
}

/* 缩小喜欢和评论数的图片 */
.action-icon {
  width: 20px;
  height: 20px;
  margin-left: 15px;
}

.more-button {
  background: transparent;
  border: none;
  color: white;
  cursor: pointer;
  margin-left: 15px;
}

.comment-replies {
  margin-top: -10px;
  margin-left: 30px;
  width: calc(100% - 40px); /* 宽度略小于父评论 */
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.reply-box {
  margin-top: 30px;
  border: 1px solid #000000;
  padding: 40px;
  background-color: rgba(0, 0, 0, 0.488);
  color: #ffffff;
  border-radius: 30px;
  backdrop-filter: blur(5px);
  position: relative;
}

.reply-nickname {
  font-size: 1.3em;
  color: #a0a0a0;
  margin-right: 10px;
}

.reply-date {
  font-size: 0.8em;
  color: #666;
}

/* 新增的评论区样式*/
.article-comment-box {
  margin-top: 20px;
  margin-bottom: 20px;
  position: relative;
  background-color: rgba(0, 0, 0, 0.488);
  border: 1px solid #000000;
  border-radius: 10px;
  padding: 10px;
  backdrop-filter: blur(5px);
  height: 80px;
}

.comment-input {
  width: 97%; /* 宽度占满容器 */
  background-color: transparent;
  border: none;
  color: rgb(255, 255, 255);
  border-radius: 10px;
  resize: none;
  padding: 10px;
  height: 80%; /* 增大评论框高度 */
}

.comment-input::placeholder {
  color: rgba(255, 255, 255, 0.8);
  opacity: 1;
}

.send-button {
  background: none;
  border: none;
  cursor: pointer;
  position: absolute;
  bottom: 15px; /* 调整到底部 */
  right: 15px; /* 调整到右侧 */
}

.send-button img {
  width: 60px;
  height: auto;
}
</style>
