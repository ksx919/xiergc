import axios from "axios";
export const getArticleById = (id) => {
    return axios.get(`/api/articles/${id}`); // 改为代理路径
  };