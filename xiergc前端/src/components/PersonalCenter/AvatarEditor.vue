<template>
  <div class="avatar-editor-modal">
    <div class="modal-header">
      <span>修改头像</span>
      <span @click="closeModal" class="close-icon">×</span>
    </div>
    <div class="modal-content">
      <img :src="props.avatarSrc" alt="当前头像" width="100" height="100" />
      <div class="button-container">
        <!-- 自定义文件选择按钮 -->
        <label for="file-input" class="custom-file-button"> 选择文件 </label>
        <!-- 确保 id="file-input" 与 label 中的 for 匹配 -->
        <input
          type="file"
          ref="fileInput"
          accept="image/*"
          id="file-input"
          @change="handleFileChange"
          style="display: none"
        />
      </div>
      <div v-if="showCropper" class="cropper-container">
        <img id="image" :src="selectedImageSrc" alt="待裁剪图片" />
      </div>
      <div v-if="showCropper" class="cropper-buttons">
        <button @click="cropAndUpload" class="center-button">确定</button>
        <button @click="showCropper = false">取消</button>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, nextTick } from "vue";
import instance from "@/api/axios.js";
import Cropper from "cropperjs";
import "cropperjs/dist/cropper.css";

// 接收父组件传递的头像源
const props = defineProps({
  avatarSrc: {
    type: String,
    default: "",
  },
});


// 控制裁剪器的显示
const showCropper = ref(false);
// 选中的图片路径
const selectedImageSrc = ref("");
// Cropper 实例
let cropper;
// 控制模态框的显示
const emit = defineEmits(["close", "update:avatarSrc"]);

// 使用 ref 获取文件输入框
const fileInput = ref(null);

// 处理文件选择事件
const handleFileChange = async (e) => {
  const file = e.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (event) => {
      selectedImageSrc.value = event.target.result;
      showCropper.value = true;
      nextTick(() => {
        cropper = new Cropper(document.getElementById("image"), {
          aspectRatio: 1, // 正方形裁剪
          viewMode: 1,
          autoCropArea: 1,
          background: false,
          crop: () => {},
        });
      });
    };
    reader.readAsDataURL(file);
  }
};

// 裁剪并上传图片
const cropAndUpload = async () => {
  try {
    const jwtToken = localStorage.getItem("jwtToken");
    const canvas = cropper.getCroppedCanvas({
      width: 200,
      height: 200,
    });

    if (!canvas) {
      alert("裁剪失败，请重新选择图片");
      return;
    }

    // 强制使用JPEG格式解决类型问题
    canvas.toBlob(async (blob) => {
      if (!blob || blob.size === 0) {
        alert("无法生成有效图片，请重试");
        return;
      }

      // 创建文件对象（强制JPEG格式）
      const file = new File([blob], "avatar.jpg", {
        type: "image/jpeg"
      });

      const formData = new FormData();
      formData.append("file", file);

      try {
        // 上传请求
        const uploadResponse = await instance.post(
          "/upload",
          formData,
          {
            headers: {
              Authorization: jwtToken,
              "Content-Type": "multipart/form-data"
            }
          }
        );

        if (uploadResponse.status === 200) {
          const avatarUrl = uploadResponse.data.data;
          const patchResponse = await instance.patch(
            "/user/avatar",
            { avatarUrl: avatarUrl },
            { headers: { Authorization: `${jwtToken}` } }
          );

          if (patchResponse.status === 200) {
            // 更新本地头像显示并通知父组件
            emit("update:avatarSrc", avatarUrl);
            closeModal();
          } else {
            console.error("更新头像失败:", patchResponse);
          }
          closeModal();
        }
      } catch (uploadError) {
        console.error("上传失败:", uploadError.response?.data);
        alert(`上传失败: ${uploadError.response?.data?.msg || "服务器错误"}`);
      }
    }, "image/jpeg");
    
  } catch (error) {
    console.error("操作失败:", error);
    alert("图片处理失败，请重试");
  }
};

// 关闭模态框
const closeModal = () => {
  showCropper.value = false;
  emit("close");
};
</script>



<style scoped>
.avatar-editor-modal {
  position: fixed;
  top: 30%; /* 调整为中间偏上的位置 */
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(16, 16, 16, 0.923);
  color: white;
  padding: 20px;
  border: 1px solid #000000;
  border-bottom: 1px solid #ffffff;
  border-radius: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  width: 240px; /* 调整窗口宽度 */
  height: 200px; /* 调整窗口高度 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  padding-bottom: 10px;
}

.close-icon {
  cursor: pointer;
  font-size: 24px;
  transition: color 0.2s ease;
}

.close-icon:hover {
  color: #ccc;
}

.modal-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;
}

.modal-content img {
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid white;
}

.button-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
}

.custom-file-button {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 8px 15px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.custom-file-button:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

input[type="file"] {
  display: none;
}

.cropper-container {
  width: 100%;
  overflow: hidden;
}

.cropper-container img {
  width: 100%;
}

.cropper-buttons {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.center-button,
.cropper-buttons button {
  width: 80px; /* 可以根据需要调整按钮宽度 */
  margin: 0 auto; /* 使按钮水平居中 */
  background-color: black; /* 底色黑色 */
  color: white; /* 文字白色 */
  border: 1px solid #000000;
  border-radius: 5px;
  padding: 5px 10px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.center-button:hover,
.cropper-buttons button:hover {
  background-color: rgba(255, 255, 255, 0.1); /* 鼠标悬停时背景颜色变化 */
}
</style>
