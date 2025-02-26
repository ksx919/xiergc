<template>
  <div class="animation1-container">
    <img ref="logoRef" :src="logoSrc" alt="Logo" :style="logoStyle" />
    <div class="circle top-circle" :style="topCircleStyle"></div>
    <div class="circle bottom-circle" :style="bottomCircleStyle"></div>
    <img
      v-for="(logo, index) in logo1to7Src"
      :key="index"
      ref="logo1to7Refs"
      :src="logo"
      alt="Logo"
      :style="logo1to7Styles[index]"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { gsap } from "gsap";
import { animation1Positions } from "@/scripts/animation1.js";

const logoSrc = require("@/assets/image/logo.png");
const logo1to7Src = [
  require("@/assets/image/logo1.png"),
  require("@/assets/image/logo2.png"),
  require("@/assets/image/logo3.png"),
  require("@/assets/image/logo4.png"),
  require("@/assets/image/logo5.png"),
  require("@/assets/image/logo6.png"),
  require("@/assets/image/logo7.png"),
];

const logoRef = ref(null);
const logo1to7Refs = ref([]);

const logoStyle = ref({
  ...animation1Positions.initial.logo,
  zIndex: 1,
});
const topCircleStyle = ref({
  ...animation1Positions.initial.topCircle,
  width: "800px",
  height: "800px",
  borderRadius: "50%",
  backgroundColor: "#47aaea",
  zIndex: 0,
  filter: "blur(200px)",
});
const bottomCircleStyle = ref({
  ...animation1Positions.initial.bottomCircle,
  width: "800px",
  height: "800px",
  borderRadius: "50%",
  backgroundColor: "#b20ba2",
  zIndex: 0,
  filter: "blur(200px)",
});
const logo1to7Styles = ref(animation1Positions.initial.logo1to7);

onMounted(() => {
  setTimeout(() => {
    // 动画一阶段：logo 移动到中间偏上并放大
    gsap.to(logoRef.value, {
      top: animation1Positions.after08s.logo.top,
      left: animation1Positions.after08s.logo.left,
      transform: animation1Positions.after08s.logo.transform,
      duration: 0.8,
    });

    // 动画一阶段：logo1 到 logo7 逐渐显示并放大
    logo1to7Refs.value.forEach((logo, index) => {
      gsap.to(logo, {
        top: animation1Positions.after08s.logo1to7[index].top,
        left: animation1Positions.after08s.logo1to7[index].left,
        transform: animation1Positions.after08s.logo1to7[index].transform,
        opacity: 1, // 添加 opacity 动画
        duration: 0.8,
      });
    });

    // 动画一阶段：上圆移动到中间最上面，下圆移动到中间最下面
    gsap.to(".top-circle", {
      top: animation1Positions.after08s.topCircle.top,
      left: animation1Positions.after08s.topCircle.left,
      transform: animation1Positions.after08s.topCircle.transform,
      duration: 0.8,
    });
    gsap.to(".bottom-circle", {
      top: animation1Positions.after08s.bottomCircle.top,
      left: animation1Positions.after08s.bottomCircle.left,
      transform: animation1Positions.after08s.bottomCircle.transform,
      duration: 0.8,
    });
  }, 800);
});
</script>

<style scoped>
.animation1-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  background: #130b3c;
  overflow: hidden; /* 确保背景外的元素不影响网页尺寸 */
}

.circle {
  position: absolute;
}

img {
  position: absolute;
}
</style>
