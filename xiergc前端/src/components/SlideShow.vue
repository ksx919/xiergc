<template>
  <div class="slider">
    <!-- 显示当前图片 -->
    <img :src="currentImageSrc" alt="幻灯片" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

// 图片数组，使用 require 引入图片
const images = [
  require("@/assets/image/banner1.png"),
  require("@/assets/image/ban2.png"),
  require("@/assets/image/ban3.png"),
];

// 当前显示的图片索引
const currentIndex = ref(0);

// 获取当前图片的 src
const currentImageSrc = ref(images[currentIndex.value]);

// 定时器 ID
let timerId;

// 切换到下一张图片
const nextSlide = () => {
  currentIndex.value = (currentIndex.value + 1) % images.length;
  currentImageSrc.value = images[currentIndex.value];
};

// 组件挂载时启动定时器
onMounted(() => {
  timerId = setInterval(nextSlide, 3000); // 每 3 秒切换一次
});

// 组件卸载时清除定时器
onUnmounted(() => {
  clearInterval(timerId);
});
</script>

<style scoped>
.slider {
  position: relative;
  top: 240px;
  left: 56%;
  transform: translate(-50%, -50%);
  width: 70%;
  /* 设置固定高度，这里假设固定高度为 400px，你可以根据实际情况修改 */
  height: 350px;
  text-align: center;
  overflow: hidden; /* 如果图片超出高度，隐藏溢出部分 */
}

.slider img {
  max-width: 80%;
  height: auto;
  /* 使图片在容器中垂直居中 */
  position: relative;
  top: 50%;
  transform: translateY(-50%);
}
</style>
