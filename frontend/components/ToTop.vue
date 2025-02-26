<template>
  <button class="to-top-button" @click="scrollToTop">
    <img class="to-top-image" :src="toTopIconSrc" alt="回到顶部" />
  </button>
</template>

<script setup>
// 引入图片路径，确保带上正确的扩展名
const toTopIconSrc = require("@/assets/image/totop.png");

// 滚动到顶部的方法
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};
</script>

<style scoped>
.to-top-button {
  position: fixed;
  /* 中间偏右的底部位置 */
  bottom: 20px;
  right: 12%;
  /* 确保在最高层 */
  z-index: 9999;
  background-color: transparent;
  border: none;
  cursor: pointer;
  /* 一直显示 */
  opacity: 1;
  visibility: visible;
}

.to-top-image {
  width: 25px;
  height: 25px;
  object-fit: contain;
  background-color: transparent;
  /* 顺时针旋转 90 度 */
  transform: rotate(90deg);
}
</style>
