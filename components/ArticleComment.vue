<template>
  <div class="comment-section">
    <div v-for="(comment, index) in comments" :key="index" class="comment-box">
      <div class="comment-header">
        <span class="nickname">{{ comment.nickname }}</span>
        <span class="level">LV.{{ comment.level }}</span>
      </div>
      <div class="comment-content">
        <p>{{ comment.comment }}</p>
        <img
          class="comment-img"
          src="@/assets/image/comment2.png"
          alt="评论图片"
        />
      </div>
      <div class="comment-footer">
        <div class="comment-stats">
          <!-- 缩小喜欢和评论数的图片 -->
          <img class="action-icon" :src="likeIconSrc" alt="喜欢" />
          <span>{{ comment.likeCount }}</span>
          <img
            class="action-icon comment-count-icon"
            :src="commentIconSrc"
            alt="评论数量"
            @click="toggleReplyMenu(index)"
            ref="commentCountIconRefs[index]"
          />
          <!-- 显示动态计算的评论数量 -->
          <span>{{ comment.replies.length }}</span>
        </div>
      </div>
      <!-- 评论的评论 -->
      <div v-if="comment.replies.length > 0" class="comment-replies">
        <div
          v-for="(reply, replyIndex) in comment.replies.slice(0, 2)"
          :key="replyIndex"
          class="reply-box"
        >
          <span class="reply-nickname">{{ reply.nickname }}</span>
          <span class="reply-date">{{ reply.date }}</span>
          <p>{{ reply.comment }}</p>
          <div class="reply-stats">
            <img class="action-icon" :src="likeIconSrc" alt="喜欢" />
            <span>{{ reply.likeCount }}</span>
            <img class="action-icon" :src="commentIconSrc" alt="评论数量" />
            <span>{{ reply.commentCount }}</span>
          </div>
        </div>
        <button
          v-if="comment.replies.length > 2"
          @click="toggleReplies(index)"
          class="more-button"
        >
          {{ showMoreReplies[index] ? "收起" : "更多" }}
        </button>
        <div v-if="showMoreReplies[index]" class="more-replies">
          <div
            v-for="(reply, replyIndex) in comment.replies.slice(2)"
            :key="replyIndex"
            class="reply-box"
          >
            <span class="reply-nickname">{{ reply.nickname }}</span>
            <span class="reply-date">{{ reply.date }}</span>
            <p>{{ reply.comment }}</p>
            <div class="reply-stats">
              <img class="action-icon" :src="likeIconSrc" alt="喜欢" />
              <span>{{ reply.likeCount }}</span>
              <img class="action-icon" :src="commentIconSrc" alt="评论数量" />
              <span>{{ reply.commentCount }}</span>
            </div>
          </div>
        </div>
      </div>
      <!-- 悬停右拉菜单 -->
      <div
        v-show="showReplyMenu[index]"
        class="reply-menu"
        :style="{
          left:
            commentCountIconRefs[index] &&
            commentCountIconRefs[index].getBoundingClientRect().right + 'px',
          top:
            commentCountIconRefs[index] &&
            commentCountIconRefs[index].getBoundingClientRect().top + 'px',
        }"
        ref="replyMenuRefs[index]"
      >
        <textarea
          v-model="replyContents[index]"
          class="reply-input"
          placeholder="请输入评论"
        ></textarea>
        <button class="send-text-button" @click="sendReply(index)">发送</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { useAttrs } from "vue";

const attrs = useAttrs();
const comments = ref(attrs.comments);

const showMoreReplies = ref(comments.value.map(() => false));
const toggleReplies = (index) => {
  showMoreReplies.value[index] = !showMoreReplies.value[index];
};

// 新增回复菜单显示状态
const showReplyMenu = ref(comments.value.map(() => false));
// 新增回复内容
const replyContents = ref(comments.value.map(() => ""));

const commentCountIconRefs = ref(comments.value.map(() => null));
const replyMenuRefs = ref(comments.value.map(() => null));

const toggleReplyMenu = (index) => {
  showReplyMenu.value[index] = !showReplyMenu.value[index];
};

const sendReply = (index) => {
  console.log("发送回复:", replyContents.value[index]);
  replyContents.value[index] = "";
  // 发送回复后更新评论数量
  comments.value[index].replies.push({
    nickname: "匿名用户", // 这里可以根据实际情况修改
    date: new Date().toLocaleDateString(),
    comment: replyContents.value[index],
    likeCount: 0,
    commentCount: 0,
  });
};

const handleDocumentClick = (event) => {
  showReplyMenu.value.forEach((isVisible, index) => {
    if (
      isVisible &&
      commentCountIconRefs.value[index] &&
      !commentCountIconRefs.value[index].contains(event.target) &&
      replyMenuRefs.value[index] &&
      !replyMenuRefs.value[index].contains(event.target)
    ) {
      showReplyMenu.value[index] = false;
    }
  });
};

onMounted(() => {
  document.addEventListener("click", handleDocumentClick);
});

onUnmounted(() => {
  document.removeEventListener("click", handleDocumentClick);
});

// 图片路径
const likeIconSrc = require("@/assets/image/like.png");
const commentIconSrc = require("@/assets/image/comment.png");
</script>

<style scoped>
.comment-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 50px;
}

.comment-box {
  border: 1px solid #000000;
  padding: 50px;
  position: relative;
  background-color: rgba(0, 0, 0, 0.488);
  color: #ffffff;
  border-radius: 30px;
  backdrop-filter: blur(5px);
  margin-bottom: 30px;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.nickname {
  /* 评论昵称居左 */
}

.level {
  margin-left: 10px;
}

.comment-content {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  margin-bottom: 30px;
  min-height: 120px;
}

.comment-img {
  width: 80px;
  height: auto;
  object-fit: cover;
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

.comment-replies {
  margin-top: 60px;
}

.reply-box {
  border-top: 1px solid #ffffff;
  padding-top: 20px;
  margin-top: 20px;
  position: relative;
  min-height: 100px;
}

.reply-nickname {
  position: absolute;
  top: 15px;
  left: 0;
}

.reply-date {
  position: absolute;
  bottom: 10px;
  left: 0;
  color: #ffffff;
  font-size: 10px;
  opacity: 0.9;
}

.reply-stats {
  display: flex;
  align-items: center;
  position: absolute;
  bottom: 10px;
  right: 0;
}

.more-button {
  background: transparent;
  border: none;
  color: white;
  cursor: pointer;
  position: absolute;
  bottom: 10px;
  left: 45px;
  padding-top: 30px;
}

.more-replies {
  margin-top: 10px;
}

/* 悬停右拉菜单样式 */
.reply-menu {
  position: absolute;
  width: 250px;
  height: 200px;
  background-color: rgba(0, 0, 0, 0.488);
  border: 1px solid #000000;
  border-radius: 30px;
  padding: 10px;
  backdrop-filter: blur(5px);
  /* 设置为最高层 */
  z-index: 9999;
  left: 460px;
  top: 270px;
}

.reply-input {
  margin-top: 20px;
  width: 80%;
  height: 60%;
  /* 设置背景透明 */
  background-color: transparent;
  border: none;
  color: white;
  padding: 10px;
  border-radius: 5px;
  resize: none;
  margin-bottom: 10px;
}

/* 设置输入框提示文字的样式 */
.reply-input::placeholder {
  color: rgba(255, 255, 255, 0.8);
  opacity: 1;
}

.send-text-button {
  background: transparent;
  border: none;
  color: white;
  cursor: pointer;
  text-align: right;
  width: 100%;
  padding-right: 20px;
}
</style>
