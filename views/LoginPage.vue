<template>
  <div class="animation2-container">
    <!-- 移除 logo 图片 -->
    <div ref="topCircleRef" class="circle top-circle"></div>
    <div ref="bottomCircleRef" class="circle bottom-circle"></div>
    <img
      v-for="(logo, index) in logo1to7Src"
      :key="index"
      ref="logo1to7Refs"
      :src="logo"
      alt="Logo"
    />
    <LoginRegister ref="loginRegisterRef" />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { animation2Positions } from "@/scripts/animation2.js";
import LoginRegister from "@/components/login-page/LoginRegister.vue";

const topCircleRef = ref(null);
const bottomCircleRef = ref(null);
const logo1to7Refs = ref([]);
const loginRegisterRef = ref(null);

const logo1to7Src = [
  require("@/assets/image/logo1.png"),
  require("@/assets/image/logo2.png"),
  require("@/assets/image/logo3.png"),
  require("@/assets/image/logo4.png"),
  require("@/assets/image/logo5.png"),
  require("@/assets/image/logo6.png"),
  require("@/assets/image/logo7.png"),
];

const { second08s } = animation2Positions;

onMounted(() => {
  // 设置 logo1 - 7 最终状态
  logo1to7Refs.value.forEach((logo, index) => {
    Object.assign(logo.style, second08s.logo1to7[index]);
  });
  // 设置登录注册组件最终状态
  Object.assign(loginRegisterRef.value.$el.style, second08s.loginRegister);
});
import "@/components/background/background.css";
</script>

<style scoped>
.animation2-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

.circle {
  /* 这里的样式可根据需要调整 */
  position: absolute;
}

img {
  position: absolute;
}
</style>
