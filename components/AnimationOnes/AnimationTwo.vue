<template>
  <div class="animation2-container">
    <img ref="logoRef" :src="logoSrc" alt="Logo" />
    <div ref="topCircleRef" class="circle top-circle"></div>
    <div ref="bottomCircleRef" class="circle bottom-circle"></div>
    <img
      v-for="(logo, index) in logo1to7Src"
      :key="index"
      ref="logo1to7Refs"
      :src="logo"
      alt="Logo"
    />
    <LoginRegister v-if="showLogin" ref="loginRegisterRef" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { gsap } from "gsap";
import { animation2Positions } from "@/scripts/animation2.js";
import LoginRegister from "../login-page/LoginRegister.vue";

// 元素引用
const logoRef = ref(null);
const topCircleRef = ref(null);
const bottomCircleRef = ref(null);
const logo1to7Refs = ref([]);
const loginRegisterRef = ref(null);

// 组件状态控制
const isMounted = ref(false);
const showLogin = ref(false);

// 资源路径
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

// GSAP 上下文
let ctx = null;
let animationTimers = [];

onMounted(() => {
  isMounted.value = true;

  // 初始化 GSAP 上下文
  ctx = gsap.context(() => {
    const { initial, first08s, second08s } = animation2Positions;

    // 初始化元素状态
    const initElementState = (element, state) => {
      if (!element) return;
      Object.keys(state).forEach((key) => {
        element.style[key] = state[key];
      });
    };

    // 设置初始状态
    initElementState(logoRef.value, initial.logo);
    logo1to7Refs.value.forEach((logo, index) => {
      initElementState(logo, initial.logo1to7[index]);
    });
    initElementState(topCircleRef.value, initial.topCircle);
    initElementState(bottomCircleRef.value, initial.bottomCircle);

    // 延迟加载登录组件
    animationTimers.push(
      setTimeout(() => {
        showLogin.value = true;

        // 等待 DOM 更新
        requestAnimationFrame(() => {
          if (loginRegisterRef.value?.$el) {
            initElementState(loginRegisterRef.value.$el, initial.loginRegister);
          }
        });
      }, 800)
    );

    // 第一阶段动画 (0 - 0.8s)
    const tl1 = gsap.timeline();
    tl1.to(logoRef.value, {
      ...first08s.logo,
      duration: 0.8,
    });

    logo1to7Refs.value.forEach((logo, index) => {
      tl1.to(
        logo,
        {
          ...first08s.logo1to7[index],
          duration: 0.8,
        },
        0
      );
    });

    tl1.to(
      topCircleRef.value,
      {
        ...first08s.topCircle,
        duration: 0.8,
      },
      0
    );

    tl1.to(
      bottomCircleRef.value,
      {
        ...first08s.bottomCircle,
        duration: 0.8,
      },
      0
    );

    // 第二阶段动画 (1.6s - 2.4s)
    animationTimers.push(
      setTimeout(() => {
        if (!isMounted.value) return;

        // 登录组件动画
        if (loginRegisterRef.value?.$el) {
          gsap.to(loginRegisterRef.value.$el, {
            ...second08s.loginRegister,
            duration: 0.8,
            onComplete: () => {
              if (!isMounted.value) return;
              console.log("登录组件动画完成");
            },
          });
        }

        // logo1-7 第二阶段动画
        logo1to7Refs.value.forEach((logo, index) => {
          gsap.to(logo, {
            ...second08s.logo1to7[index],
            duration: 0.8,
          });
        });
      }, 1600)
    );
  });
});

onUnmounted(() => {
  isMounted.value = false;

  // 清理所有动画
  ctx?.revert();

  // 清除定时器
  animationTimers.forEach((timer) => clearTimeout(timer));
  animationTimers = [];
});

// 配置 GSAP 安全模式
gsap.config({
  nullTargetWarn: false,
  onwarn: (msg) => {
    if (!msg.includes("target not found")) {
      console.warn(msg);
    }
  },
});
</script>

<style scoped>
.animation2-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  background: #130b3c;
  overflow: hidden;
}

.circle {
  position: absolute;
  width: 800px;
  height: 800px;
  border-radius: 50%;
  z-index: 0;
  filter: blur(200px);
}

.top-circle {
  background-color: #47aaea;
}

.bottom-circle {
  background-color: #b20ba2;
}

img {
  position: absolute;
  transition: opacity 0.3s;
}
</style>
