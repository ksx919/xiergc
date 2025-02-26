<template>
  <div class="card">
    <div class="imgBx">
      <!-- 修改图片路径 -->
      <img :src="avatarSrc" alt="Profile Image" />
    </div>
    <div class="content">
      <div class="details">
        <h2>WTF<br /><span>UI设计师</span></h2>
      </div>
    </div>
  </div>
</template>

<script setup>
// 引入图片路径
const avatarSrc = require("@/assets/image/user.png");
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Poppins", sans-serif;
}

body {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(45deg, #fbda61, #ff5acd);
}

.card {
  /* 固定在窗口右上角 */
  position: fixed;
  top: 110px;
  right: 233px;
  width: 350px;
  height: 130px;
  /* 应用黑色框属性 */
  background-color: rgba(0, 0, 0, 0.488);
  border: 1px solid #000000;
  border-radius: 30px;
  box-shadow: 0 35px 80px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(5px);
  transition: 0.5s;
  color: #ffffff;
  /* 等比缩小卡片 */
  transform: scale(0.8);
  transform-origin: top right;
  /* 显示在最高层 */
  z-index: 999;
}

.card:hover {
  height: 300px;
}

.imgBx {
  position: absolute;
  left: 50%;
  top: -50px;
  transform: translateX(-50%);
  width: 150px;
  height: 150px;
  /* 头像背景透明 */
  background: none;
  border-radius: 20px;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.35);
  overflow: hidden;
  transition: 0.5s;
}

.card:hover .imgBx {
  width: 200px;
  height: 200px;
  top: 20px;
}

.imgBx img {
  position: absolute;
  top: 15%;
  left: 15%;
  width: 70%;
  height: 70%;
  object-fit: cover;
}

.card .content {
  position: absolute;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  overflow: hidden;
}

.card .content .details {
  padding: 40px;
  text-align: center;
  width: 100%;
  transition: 0.5s;
  transform: translateY(150px);
}

.card:hover .content .details {
  transform: translateY(0px);
}

.card .content .details h2 {
  font-size: 1.25em;
  font-weight: 600;
  color: #ffffff;
  line-height: 1.2em;
}

.card .content .details h2 span {
  font-size: 0.75em;
  font-weight: 500;
  opacity: 0.5;
}
</style>
