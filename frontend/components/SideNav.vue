<template>
  <nav class="side-nav">
    <ul>
      <li v-for="(item, index) in navItems" :key="index">
        <button
          @click="updateActive(index)"
          :class="{ active: activeIndex === index }"
        >
          <!-- 动态绑定图片的宽度、高度和内边距 -->
          <img
            :src="item.imageSrc"
            :width="item.width"
            :height="item.height"
            :style="{
              paddingTop: item.paddingTop + 'px',
              paddingBottom: item.paddingBottom + 'px',
            }"
            alt="nav icon"
          />
        </button>
      </li>
    </ul>
  </nav>
</template>

<script setup>
import { ref } from "vue";

// 导航项数据，包含图片路径、宽度、高度和上下内边距
const navItems = [
  {
    imageSrc: require("@/assets/image/navside1.png"),
    width: 30,
    height: 30,
    paddingTop: 10,
    paddingBottom: 10,
  },
  {
    imageSrc: require("@/assets/image/navside2.png"),
    width: 30,
    height: 30,
    paddingTop: 10,
    paddingBottom: 10,
  },
  {
    imageSrc: require("@/assets/image/navside3.png"),
    width: 30,
    height: 30,
    paddingTop: 10,
    paddingBottom: 10,
  },
  {
    imageSrc: require("@/assets/image/navside4.png"),
    width: 30,
    height: 30,
    paddingTop: 10,
    paddingBottom: 10,
  },
  {
    imageSrc: require("@/assets/image/navside5.png"),
    width: 70,
    height: 50,
    paddingTop: 3,
    paddingBottom: 3,
  },
  {
    imageSrc: require("@/assets/image/navside6.png"),
    width: 50,
    height: 50,
    paddingTop: 0,
    paddingBottom: 5,
  },
];

// 记录当前激活的导航项索引
const activeIndex = ref(0);

// 更新激活的导航项
const updateActive = (index) => {
  activeIndex.value = index;
};
</script>

<style scoped>
.side-nav {
  position: fixed;
  top: 120px;
  left: 280px;
  background-color: #000000;
  color: white;
  border-right: 1px solid rgba(0, 0, 0, 0.3);
  border-radius: 0 10px 10px 0;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
  z-index: 100;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 25px;
}

.side-nav ul {
  list-style-type: none;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0; /* 去除 ul 默认的内边距 */
}

.side-nav ul li {
  padding: 8px; /* 调整每个导航项的内边距 */
  text-align: center;
  transition: background-color 0.3s ease;
}

.side-nav ul li button {
  background: none;
  border: none;
  cursor: pointer;
  outline: none;
  padding: 0; /* 去除按钮默认的内边距 */
}

.side-nav ul li button img {
  object-fit: contain;
}

.side-nav ul li button.active img {
  transform: scale(1.1); /* 激活状态图片放大效果 */
}
</style>
