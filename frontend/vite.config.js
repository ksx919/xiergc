import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import path from "path";

export default defineConfig({
  base: "./", // 改为相对路径
  plugins: [vue()],
  define: {
    "process.env.NODE_ENV": JSON.stringify(process.env.NODE_ENV),
    __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(false), // 定义特性标志
  },
  resolve: {
    alias: {
      "@utils": path.resolve(__dirname, "src/utils"),
    },
  },
  server: {
    proxy: {
      "/upload": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/upload/, "/upload"),
      },
      "/user/avatar": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/user\/avatar/, "/user/avatar"),
      },
      "/user/name": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/user\/name/, "/user/name"),
      },
      "/user/password": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/user\/password/, "/user/password"),
      },
    },
  },
});
