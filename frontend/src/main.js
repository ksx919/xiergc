// src/main.js
import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "@fortawesome/fontawesome-free/css/all.css";
const app = createApp(App)

app.use(router)
app.mount("#app")

// 添加全局错误处理
app.config.errorHandler = (err) => {
  console.error('Vue error:', err)
}
