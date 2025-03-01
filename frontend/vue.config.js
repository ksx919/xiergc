module.exports = {
  // 这里可以添加自定义配置选项
  publicPath: "/",
  outputDir: "dist",
  assetsDir: "static",
  // 其他配置...

  devServer: {
    proxy: {
      '/api': {
        target: process.env.VUE_APP_API_BASE,
        changeOrigin: true,
        pathRewrite: { '^/api': '' },
      },
    },
  },
  
  chainWebpack: (config) => {
    // 配置@路径别名
    config.resolve.alias.set("@", resolve("src"));

    config.plugin("define").tap((definitions) => {
      Object.assign(definitions[0], {
        __VUE_OPTIONS_API__: "true",
        __VUE_PROD_DEVTOOLS__: "false",
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: "false",
      });
      return definitions;
    });
  },
};

// 引入path模块以使用resolve方法
const path = require("path");

function resolve(dir) {
  return path.join(__dirname, dir);
}
