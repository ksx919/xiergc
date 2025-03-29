<template>
  <div class="markdown-editor">
    <!-- 文章标题框 -->
    <input
      v-model="articleTitle"
      placeholder="请输入文章标题"
      class="article-title-input"
    />
    <div class="button-group">
      <button @click="addBold" title="加粗">
        <i class="fas fa-bold"></i>
      </button>
      <button @click="addItalic" title="斜体">
        <i class="fas fa-italic"></i>
      </button>
      <div
        class="dropdown"
        @mouseenter="dropdownOpen = true"
        @mouseleave="dropdownOpen = false"
      >
        <button title="标题">
          <i class="fas fa-heading"></i>
        </button>
        <div class="dropdown-content" v-if="dropdownOpen">
          <div @click="setHeadingLevel(1)" title="一级标题">
            <i class="fas fa-heading"></i> 一级标题
          </div>
          <div @click="setHeadingLevel(2)" title="二级标题">
            <i class="fas fa-heading"></i> 二级标题
          </div>
          <div @click="setHeadingLevel(3)" title="三级标题">
            <i class="fas fa-heading"></i> 三级标题
          </div>
          <div @click="setHeadingLevel(4)" title="四级标题">
            <i class="fas fa-heading"></i> 四级标题
          </div>
          <div @click="setHeadingLevel(5)" title="五级标题">
            <i class="fas fa-heading"></i> 五级标题
          </div>
          <div @click="setHeadingLevel(6)" title="六级标题">
            <i class="fas fa-heading"></i> 六级标题
          </div>
        </div>
      </div>
      <button @click="addList" title="列表">
        <i class="fas fa-list-ul"></i>
      </button>
      <button @click="addCodeBlock" title="代码块">
        <i class="fas fa-code"></i>
      </button>
      <button @click="addLink" title="插入链接">
        <i class="fas fa-link"></i>
      </button>
      <label for="image-input" title="插入图片">
        <i class="fas fa-image"></i>
      </label>
      <input
        type="file"
        id="image-input"
        @change="handleImageSelect"
        style="display: none"
      />
    </div>
    <div class="input-preview-container">
      <textarea
        v-model="input"
        placeholder="请输入Markdown内容"
        class="markdown-input"
      ></textarea>
      <div class="markdown-preview" v-html="renderedMarkdown"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import MarkdownIt from "markdown-it";

const input = ref("");
const renderedMarkdown = ref("");
const selectedHeadingLevel = ref(1);
const dropdownOpen = ref(false);
const articleTitle = ref("");

const md = new MarkdownIt();

watch(input, (newValue) => {
  renderedMarkdown.value = md.render(newValue);
});

// 加粗功能
const addBold = () => {
  const selectionStart = document.querySelector("textarea").selectionStart;
  const selectionEnd = document.querySelector("textarea").selectionEnd;
  const selectedText = input.value.slice(selectionStart, selectionEnd);
  const newText = `**${selectedText}**`;
  input.value =
    input.value.slice(0, selectionStart) +
    newText +
    input.value.slice(selectionEnd);
};

// 斜体功能
const addItalic = () => {
  const selectionStart = document.querySelector("textarea").selectionStart;
  const selectionEnd = document.querySelector("textarea").selectionEnd;
  const selectedText = input.value.slice(selectionStart, selectionEnd);
  const newText = `*${selectedText}*`;
  input.value =
    input.value.slice(0, selectionStart) +
    newText +
    input.value.slice(selectionEnd);
};

// 设置标题级别并添加标题
const setHeadingLevel = (level) => {
  selectedHeadingLevel.value = level;
  addHeading();
};

// 添加标题功能
const addHeading = () => {
  const selectionStart = document.querySelector("textarea").selectionStart;
  const selectionEnd = document.querySelector("textarea").selectionEnd;
  const selectedText = input.value.slice(selectionStart, selectionEnd);
  const headingPrefix = "#".repeat(selectedHeadingLevel.value);
  const newText = `${headingPrefix} ${selectedText}`;
  input.value =
    input.value.slice(0, selectionStart) +
    newText +
    input.value.slice(selectionEnd);
};

// 添加列表功能
const addList = () => {
  const selectionStart = document.querySelector("textarea").selectionStart;
  const selectionEnd = document.querySelector("textarea").selectionEnd;
  const selectedText = input.value.slice(selectionStart, selectionEnd);
  const lines = selectedText.split("\n");
  const newLines = lines.map((line) => `- ${line}`);
  const newText = newLines.join("\n");
  input.value =
    input.value.slice(0, selectionStart) +
    newText +
    input.value.slice(selectionEnd);
};

// 添加代码块功能
const addCodeBlock = () => {
  const selectionStart = document.querySelector("textarea").selectionStart;
  const selectionEnd = document.querySelector("textarea").selectionEnd;
  const selectedText = input.value.slice(selectionStart, selectionEnd);
  const newText = "```\n" + selectedText + "\n```";
  input.value =
    input.value.slice(0, selectionStart) +
    newText +
    input.value.slice(selectionEnd);
};

// 插入链接功能
const addLink = () => {
  const linkUrl = prompt("请输入链接地址");
  if (linkUrl) {
    const linkText = prompt("请输入链接文本");
    const newText = `[${linkText || "链接"}](${linkUrl})`;
    const selectionStart = document.querySelector("textarea").selectionStart;
    const selectionEnd = document.querySelector("textarea").selectionEnd;
    input.value =
      input.value.slice(0, selectionStart) +
      newText +
      input.value.slice(selectionEnd);
  }
};

// 处理图片选择事件
const handleImageSelect = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      const imageUrl = e.target.result;
      const imageAlt = prompt("请输入图片描述");
      const newText = `![${imageAlt || "图片"}](${imageUrl})`;
      const selectionStart = document.querySelector("textarea").selectionStart;
      const selectionEnd = document.querySelector("textarea").selectionEnd;
      input.value =
        input.value.slice(0, selectionStart) +
        newText +
        input.value.slice(selectionEnd);
    };
    reader.readAsDataURL(file);
  }
  // 清空文件输入框，以便下次选择同一文件也能触发 change 事件
  event.target.value = "";
};

defineExpose({
  articleTitle,
  input
})
</script>

<style scoped>
.markdown-editor {
  display: flex;
  flex-direction: column;
  height: 600px;
  color: white; /* 设置整个编辑器内文字颜色为白色 */
}

.article-title-input {
  padding: 10px;
  font-size: 24px;
  border: none;
  border-bottom: 1px solid #4d4d4d;
  margin-bottom: 10px;
  background: transparent;
  outline: none;
  border-radius: 20px;
  color: white; /* 标题输入框文字颜色为白色 */
}

.article-title-input::placeholder {
  color: white; /* 标题输入框占位符文字颜色为白色 */
}

.button-group {
  margin-bottom: 10px;
  display: flex;
  gap: 10px;
}

.button-group button,
.button-group label {
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 8px;
  font-size: 18px;
  color: white; /* 按钮文字颜色为白色 */
  transition: background-color 0.3s ease;
}

.button-group button:hover,
.button-group label:hover {
  background-color: rgba(255, 255, 255, 0.1); /* 鼠标悬停时背景颜色 */
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #222; /* 下拉菜单背景颜色 */
  font-size: 10px;
  min-width: 180px;
  box-shadow: 0 8px 16px 0 rgba(255, 255, 255, 0.2);
  z-index: 1;
  border-radius: 4px;
  overflow: hidden;
}

.dropdown-content div {
  color: white; /* 下拉菜单选项文字颜色为白色 */
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.dropdown-content div:hover {
  background-color: #444; /* 下拉菜单选项鼠标悬停时背景颜色 */
}

.dropdown:hover .dropdown-content {
  display: block;
}

.input-preview-container {
  display: flex;
  flex: 1;
  gap: 10px;
}

.markdown-input {
  width: 50%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #4d4d4d; /* 输入框边框颜色 */
  border-radius: 4px;
  background: transparent;
  resize: none;
  color: white; /* 输入框文字颜色为白色 */
  height: 450px; /* 设置输入框的高度，可根据需求调整 */
}

.markdown-input::placeholder {
  color: white; /* 输入框占位符文字颜色为白色 */
}

.markdown-preview {
  width: 50%;
  padding: 10px;
  font-size: 16px;
  height: 450px;
  border: 1px solid #4d4d4d; /* 预览框边框颜色 */
  border-radius: 4px;
  overflow-y: scroll;
  background: transparent;
  color: white; /* 预览框文字颜色为白色 */
}
</style>
