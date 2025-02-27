<template>
  <div class="app-container">
    <!-- 引入导航栏组件 -->
    <NavBar />
    <div class="content-container">
      <!-- 侧边分类栏 -->
      <nav class="side-nav">
        <div class="top-section">
          <img :src="settingIconSrc" alt="设置图标" width="30" height="30" />
          <span>设置中心</span>
        </div>
        <div class="divider"></div>
        <ul>
          <li
            @click="handleClick('profile')"
            :class="{ active: activeSection === 'profile' }"
          >
            个人资料
          </li>
          <li
            @click="handleClick('notifications')"
            :class="{ active: activeSection === 'notifications' }"
          >
            消息通知
          </li>
          <li
            @click="handleClick('account')"
            :class="{ active: activeSection === 'account' }"
          >
            账户设置
          </li>
          <li
            @click="handleClick('privacy')"
            :class="{ active: activeSection === 'privacy' }"
          >
            隐私
          </li>
          <li
            @click="handleClick('general')"
            :class="{ active: activeSection === 'general' }"
          >
            通用
          </li>
        </ul>
        <div class="divider"></div>
        <ul>
          <li @click="goToPersonalPage" class="left-align">我的主页</li>
        </ul>
      </nav>
      <!-- 右侧内容区域 -->
      <div class="right-content">
        <div class="black-rectangle">
          <!-- 根据激活的设置项显示不同内容 -->
          <div v-if="activeSection === 'profile'">
            <!-- 个人资料内容 -->
            <div class="setting-item">
              <span>用户名</span>
              <input
                v-model="profile.username"
                type="text"
                placeholder="请输入用户名"
              />
            </div>
            <div class="setting-item">
              <span>职业方向</span>
              <input
                v-model="profile.careerDirection"
                type="text"
                placeholder="请输入职业方向"
              />
            </div>
            <div class="setting-item">
              <span>职位</span>
              <input
                v-model="profile.position"
                type="text"
                placeholder="请输入职位"
              />
            </div>
            <div class="setting-item">
              <span>公司</span>
              <input
                v-model="profile.company"
                type="text"
                placeholder="请输入公司"
              />
            </div>
            <div class="setting-item">
              <span>个人介绍</span>
              <textarea
                v-model="profile.introduction"
                placeholder="请输入个人介绍"
              ></textarea>
            </div>
            <div class="save-button-container">
              <button class="save-button" @click="saveProfile">保存修改</button>
            </div>
          </div>
          <div v-if="activeSection === 'account'">
            <!-- 账户设置内容 -->
            <div class="setting-item">
              <span>输入旧密码</span>
              <div class="input-wrapper">
                <input
                  v-model="account.oldPassword"
                  :type="showOldPassword ? 'text' : 'password'"
                  placeholder="请输入旧密码"
                />
                <i
                  v-if="account.oldPassword"
                  class="fa fa-eye"
                  :class="{ 'fa-eye-slash': showOldPassword }"
                  @click.stop="toggleOldPasswordVisibility"
                  style="
                    position: absolute;
                    right: 10px;
                    top: 10px;
                    cursor: pointer;
                  "
                ></i>
              </div>
            </div>
            <div class="setting-item">
              <span>输入新密码</span>
              <div class="input-wrapper">
                <input
                  v-model="account.newPassword"
                  :type="showNewPassword ? 'text' : 'password'"
                  placeholder="请输入新密码"
                />
                <i
                  v-if="account.newPassword"
                  class="fa fa-eye"
                  :class="{ 'fa-eye-slash': showNewPassword }"
                  @click.stop="toggleNewPasswordVisibility"
                  style="
                    position: absolute;
                    right: 10px;
                    top: 10px;
                    cursor: pointer;
                  "
                ></i>
              </div>
            </div>
            <div class="setting-item">
              <span>确认新密码</span>
              <div class="input-wrapper">
                <input
                  v-model="account.confirmNewPassword"
                  :type="showConfirmPassword ? 'text' : 'password'"
                  placeholder="请再次输入新密码"
                  @input="checkPasswords"
                />
                <i
                  v-if="account.confirmNewPassword"
                  class="fa fa-eye"
                  :class="{ 'fa-eye-slash': showConfirmPassword }"
                  @click.stop="toggleConfirmPasswordVisibility"
                  style="
                    position: absolute;
                    right: 10px;
                    top: 10px;
                    cursor: pointer;
                  "
                ></i>
                <span v-if="passwordsDoNotMatch" class="error-message">
                  两次输入的密码不一致，请重新输入！
                </span>
              </div>
            </div>
            <div class="setting-item">
              <button @click="goToLoginPage">切换账户</button>
            </div>
            <div class="setting-item">
              <button style="color: red" @click="handleLogout">注销账户</button>
            </div>
            <div class="save-button-container">
              <button
                class="save-button"
                @click="saveAccount"
                :disabled="passwordsDoNotMatch"
              >
                保存修改
              </button>
            </div>
          </div>
          <!-- 其他设置内容可根据需要添加 -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import NavBar from "./NavBar.vue"; // 引入导航栏组件
import settingIconSrc from "@/assets/image/setting.png";
// 引入背景样式文件
import "@/components/background/background.css";

// 记录当前激活的设置项
const activeSection = ref("account");

// 个人资料数据
const profile = ref({
  username: "",
  careerDirection: "",
  position: "",
  company: "",
  introduction: "",
});

// 账户设置数据
const account = ref({
  oldPassword: "",
  newPassword: "",
  confirmNewPassword: "",
});

// 密码不一致的标志
const passwordsDoNotMatch = ref(false);

// 密码显示状态
const showOldPassword = ref(false);
const showNewPassword = ref(false);
const showConfirmPassword = ref(false);

// 处理侧边栏点击事件
const handleClick = (section) => {
  activeSection.value = section;
  if (section !== "account") {
    // 刷新页面（除账户设置外）
    // 这里可以根据实际需求决定是否刷新，暂时保留
    // window.location.reload();
  }
};

// 回到个人主页
const goToPersonalPage = () => {
  // 这里假设 PersonAl.vue 对应的路由路径是 /personal
  window.location.href = "/personal";
};

// 保存个人资料修改
const saveProfile = () => {
  console.log("保存个人资料修改:", { username: profile.value.username });
  // 这里可以添加实际的保存逻辑，比如发送请求到后端
};

// 检查密码是否一致
const checkPasswords = () => {
  passwordsDoNotMatch.value =
    account.value.newPassword !== account.value.confirmNewPassword;
};

// 保存账户设置修改
const saveAccount = () => {
  checkPasswords();
  if (passwordsDoNotMatch.value) {
    return;
  }
  console.log("保存账户设置修改:", {
    oldPassword: account.value.oldPassword,
    newPassword: account.value.newPassword,
  });
  // 这里可以添加实际的保存逻辑，比如发送请求到后端
};

// 回到登录页面
const goToLoginPage = () => {
  window.location.href = "/LoginPage";
};

// 注销账户处理（暂时为空）
const handleLogout = () => {
  // 后续可添加具体的注销逻辑
};

// 切换旧密码显示状态
const toggleOldPasswordVisibility = () => {
  showOldPassword.value = !showOldPassword.value;
};

// 切换新密码显示状态
const toggleNewPasswordVisibility = () => {
  showNewPassword.value = !showNewPassword.value;
};

// 切换确认新密码显示状态
const toggleConfirmPasswordVisibility = () => {
  showConfirmPassword.value = !showConfirmPassword.value;
};
</script>

<style scoped>
/* 应用容器 */
.app-container {
  min-height: 100vh;
}

/* 内容容器，可滚动 */
.content-container {
  min-height: 100vh;
  overflow-y: auto;
  background-attachment: fixed; /* 固定背景 */
  display: flex;
}

/* 侧边分类栏 */
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
  width: 200px; /* 增加宽度 */
  padding: 40px 20px; /* 调整内边距 */
}

.top-section {
  display: flex;
  align-items: center;
  gap: 10px;
}

.divider {
  width: 100%;
  height: 1px;
  background-color: #ccc;
  margin: 10px 0;
}

.side-nav ul {
  list-style-type: none;
  width: 100%;
  padding: 0;
  margin: 0;
}

.side-nav ul li {
  padding: 8px;
  text-align: center;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.side-nav ul li.active {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 调整 我的主页的位置 */
.side-nav ul li.left-align {
  text-align: left;
  padding-top: 25px;
  padding-left: 10px; /* 根据需要调整左内边距 */
}

/* 右侧内容区域 */
.right-content {
  margin-left: 400px; /* 根据侧边栏宽度调整 */
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 150px;
}

/* 黑色矩形框 */
.black-rectangle {
  background-color: rgba(0, 0, 0, 0.8);
  width: 600px;
  height: auto;
  padding: 60px; /* 增加内边距，使元素和方框有距离 */
  padding-left: 40px;
  border-radius: 10px;
  color: white;
  font-size: 18px; /* 增大文字大小 */
  position: relative; /* 为了让保存按钮可以相对于黑框定位 */
}

.setting-item {
  display: flex;
  margin-bottom: 30px;
  margin-left: 20px; /* 增加元素间的距离 */
  align-items: center; /* 垂直居中对齐 */
}

.setting-item span {
  min-width: 100px; /* 设置标签最小宽度 */
  margin-right: 20px; /* 标签与输入框之间的间距 */
}

.input-wrapper {
  flex: 1; /* 让输入框占据剩余空间 */
  display: flex;
  flex-direction: column;
  position: relative;
}

.setting-item input,
.setting-item textarea {
  border-radius: 5px;
  border: none;
  background-color: #333; /* 输入框内部颜色为灰色 */
  color: white; /* 输入框文字为白色 */
  font-size: 16px; /* 输入框文字大小 */
  padding: 8px; /* 增加内边距 */
  height: 18px; /* 增加高度 */
  width: 100%; /* 让输入框宽度充满父容器 */
}

.setting-item textarea {
  height: auto; /* 文本域高度自适应内容 */
}

.setting-item button {
  background-color: transparent;
  color: rgb(255, 255, 255);
  border: none; /* 无框 */
  cursor: pointer;
  font-size: 18px; /* 按钮文字大小 */
}

.save-button-container {
  position: absolute;
  bottom: 20px;
  right: 20px;
}

.save-button {
  background-color: transparent;
  color: rgb(255, 255, 255);
  border: none;
  cursor: pointer;
  font-size: 18px;
}

.error-message {
  color: red;
  font-size: 14px;
  margin-top: 5px;
}
</style>
