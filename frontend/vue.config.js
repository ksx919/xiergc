const webpack = require("webpack");
const path = require("path");

module.exports = {
  devServer: {
    proxy: {
      "/upload": {
        target: "http://localhost:8080",
        changeOrigin: true,
        pathRewrite: {
          "^/upload": "/upload",
        },
      },
      "/user/avatar": {
        target: "http://localhost:8080",
        changeOrigin: true,
        pathRewrite: {
          "^/user/avatar": "/user/avatar",
        },
      },
      "/user/name": {
        target: "http://localhost:8080",
        changeOrigin: true,
        pathRewrite: {
          "^/user/name": "/user/name",
        },
      },
      "/user/password": {
        target: "http://localhost:8080",
        changeOrigin: true,
        pathRewrite: {
          "^/user/password": "/user/password",
        },
      },
    },
  },
  transpileDependencies: [/@tmagic/],
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(false), // 定义特性标志
      }),
    ],
    resolve: {
      alias: {
        "@utils": path.resolve(__dirname, "src/utils"),
      },
    },
  },
};
