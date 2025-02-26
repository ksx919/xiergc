<template>
  <div class="form">
    <div class="form-toggle"></div>
    <div class="form-panel one">
      <div class="form-header">
        <img
          src="@/assets/image/user_head.png"
          alt="用户头像"
          style="width: 60px; height: auto"
        />
      </div>
      <div class="form-content">
        <form>
          <div class="form-group">
            <label for="login-username">Username</label>
            <input
              type="text"
              id="login-username"
              name="username"
              v-model="loginUsername"
              required
            />
          </div>
          <div class="form-group">
            <label for="login-password">Password</label>
            <input
              type="password"
              id="login-password"
              name="password"
              v-model="loginPassword"
              required
            />
          </div>
          <div class="form-group">
            <label class="form-remember">
              <input type="checkbox" v-model="rememberMe" />记住我
            </label>
            <a class="form-recovery" href="#">忘记密码？</a>
          </div>
          <div class="form-group">
            <button type="submit" @click.prevent="handleLogin">登录</button>
          </div>
        </form>
      </div>
    </div>
    <div class="form-panel two">
      <div class="form-header">
        <h1>注册 账户</h1>
      </div>
      <div class="form-content">
        <form>
          <div class="form-group">
            <label for="register-username">Username</label>
            <input
              type="text"
              id="register-username"
              name="username"
              v-model="registerUsername"
              required
            />
          </div>
          <div class="form-group">
            <label for="register-password">Password</label>
            <input
              type="password"
              id="register-password"
              name="password"
              v-model="registerPassword"
              required
            />
          </div>
          <div class="form-group">
            <label for="cpassword">Confirm Password</label>
            <input
              type="password"
              id="cpassword"
              name="cpassword"
              v-model="confirmPassword"
              required
            />
            <!-- 显示密码不匹配的错误提示 -->
            <span v-if="passwordMismatch" class="error-message"
              >两次输入的密码不一致，请重新输入！</span
            >
          </div>
          <div class="form-group">
            <label for="nickname">Nickname</label>
            <input
              type="text"
              id="nickname"
              name="nickname"
              v-model="registerNickname"
              required
            />
          </div>
          <div class="form-group">
            <button
              type="submit"
              @click.prevent="handleRegister"
              :disabled="passwordMismatch"
            >
              注册
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { onMounted } from "vue";
import $ from "jquery";
import axios from "axios";
import { useRouter } from "vue-router";
// import Swal from "sweetalert2";

const router = useRouter();

// 登录表单数据
const loginUsername = ref("");
const loginPassword = ref("");
const rememberMe = ref(false);

// 注册表单数据
const registerUsername = ref("");
const registerPassword = ref("");
const confirmPassword = ref("");
const registerNickname = ref("");

// 密码不匹配的标志
const passwordMismatch = ref(false);

// 登录接口请求处理
const handleLogin = async () => {
  const loginData = {
    username: loginUsername.value,
    password: loginPassword.value,
  };

  try {
    const response = await axios.post(
      "http://localhost:8080/user/login",
      loginData
    );
    const { code, msg, data } = response.data;

    if (code === 0) {
      // 登录成功，处理 token
      console.log("登录成功！Token:", data);
      // 将 JWT token 存储在本地（例如使用 localStorage 或 Vuex）
      localStorage.setItem("jwtToken", data);
      router.push("/home");
    } else {
      // 登录失败，显示错误信息
      console.error("登录失败:", msg);
    }
  } catch (error) {
    console.error("请求失败:", error);
  }
};

const handleRegister = async () => {
  if (registerPassword.value !== confirmPassword.value) {
    passwordMismatch.value = true;
    return;
  }

  passwordMismatch.value = false;

  const registerData = {
    username: registerUsername.value,
    password: registerPassword.value,
    name: registerNickname.value,
  };

  try {
    const response = await axios.post(
      "http://localhost:8080/user/register",
      registerData
    );
    const { code, msg } = response.data;

    if (code === 0) {
      // 注册成功
      console.log("注册成功！", msg);
      // 可以跳转到登录页面或显示成功消息
      // Swal.fire({
      //   title: "注册成功!",
      //   text: msg,
      //   icon: "success",
      //   confirmButtonText: "确定",
      // });
    } else {
      // 注册失败
      console.error("注册失败:", msg);
    }
  } catch (error) {
    console.error("请求失败:", error);
  }
};

onMounted(() => {
  const panelOne = $(".form-panel.two").height();
  const panelTwo = $(".form-panel.two")[0].scrollHeight;

  $(".form-panel.two")
    .not(".form-panel.two.active")
    .on("click", function (e) {
      e.preventDefault();
      $(".form-toggle").addClass("visible");
      $(".form-panel.one").addClass("hidden");
      $(".form-panel.two").addClass("active");
      $(".form").animate(
        {
          height: panelTwo,
        },
        200
      );
    });

  $(".form-toggle").on("click", function (e) {
    e.preventDefault();
    $(this).removeClass("visible");
    $(".form-panel.one").removeClass("hidden");
    $(".form-panel.two").removeClass("active");
    $(".form").animate(
      {
        height: panelOne,
      },
      200
    );
  });
});
</script>

<style scoped>
/* 这里放入原本在 style.css 中的 CSS 代码 */
@import "./css/style.css";
html {
  width: 100%;
  height: 100%;
}
/* 
body {
  background: linear-gradient(
    45deg,
    rgba(66, 183, 245, 0.8) 0%,
    rgba(66, 245, 189, 0.4) 100%
  );
  color: rgba(0, 0, 0, 0.6);
  font-family: "Roboto", sans-serif;
  font-size: 14px;
  line-height: 1.6em;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
} */
</style>
