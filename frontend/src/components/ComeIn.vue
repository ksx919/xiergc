<template>
  <div class="come-in-container">
    <AnimationOne v-if="showAnimation1" />
    <AnimationTwo v-if="showAnimation2" />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router"; //
import AnimationOne from "./AnimationOnes/AnimationOne.vue";
import AnimationTwo from "./AnimationOnes/AnimationTwo.vue";

const router = useRouter(); //
const showAnimation1 = ref(true);
const showAnimation2 = ref(false);

onMounted(() => {
  // 第一个定时器，延迟 2.4 秒隐藏第一个动画并显示第二个动画
  setTimeout(() => {
    showAnimation1.value = false;
    showAnimation2.value = true;

    // 第二个定时器，在第二个动画显示后，再延迟一段时间（这里假设为 2 秒）进行页面跳转
    setTimeout(() => {
      router.push("/login");
    }, 2400);
  }, 2400);
});
</script>

<style scoped>
.come-in-container {
  width: 100vw;
  height: 100vh;
  background: #130b3c;
  /* 设置定位为绝对定位 */
  position: absolute;
  top: 0;
  left: 0;
}
</style>
