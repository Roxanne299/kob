<template>
  <ContentCard>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="register">
          <div class="mb-3">
            <label for="formFile" class="form-label">头像</label>
            <input
              class="form-control"
              type="file"
              id="formFile"
              @change="getBase64"
            />
            <div class="upload_status">{{ upload_status }}</div>
          </div>
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input
              v-model="username"
              type="text"
              class="form-control"
              id="username"
              placeholder="请输入用户名"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
              v-model="password"
              type="password"
              class="form-control"
              id="password"
              placeholder="请输入密码"
            />
          </div>
          <div class="mb-3">
            <label for="confirmPassword" class="form-label">确认密码</label>
            <input
              v-model="confirmPassword"
              type="password"
              class="form-control"
              id="confirmPassword"
              placeholder="请输入确认密码"
            />
          </div>
          <div class="error_message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary">注册</button>
        </form>
      </div>
    </div>
  </ContentCard>
</template>
  
  <script>
import ContentCard from "@/components/ContentCard.vue";
import { ref } from "vue";
import router from "@/router/index";
import $ from "jquery";
import { Octokit } from "octokit";
export default {
  name: "UserAccountRegisterView",
  components: {
    ContentCard,
  },
  setup() {
    const username = ref("");
    const password = ref("");
    const img_url = ref("");
    const confirmPassword = ref("");
    const error_message = ref("");
    const upload_status = ref("");
    const octokit = new Octokit({
      auth: "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",
    });

    const register = () => {
      $.ajax({
        url: "http://127.0.0.1:8081/user/account/register/",
        type: "post",
        data: {
          username: username.value,
          password: password.value,
          confirmPassword: confirmPassword.value,
          imgUrl: img_url.value,
        },
        success(resp) {
          if (resp.error_message == "success") {
            router.push({ name: "user_account_login" });
          } else {
            error_message.value = resp.error_message;
          }
        },
        error(resp) {
          error_message.value = resp.error_message;
        },
      });
    };

    const getBase64 = (e) => {
      // 选择的文件
      let file = e.target.files[0];
      // 判断文件是否读取完毕，读取完毕后执行
      if (window.FileReader) {
        let reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = (e) => {
          let result = e.target.result;
          let a = result.split(",");
          let base64String = a[a.length - 1];
          // 此处可对该base64进行获取赋值传入后端
          let fileName = getDateString() + "." + file.name.split(".")[1];
          getDateString() + "." + file.name.split(".")[1];
          uploadPicToGit(base64String, fileName);
        };
      }
    };

    const getDateString = () => {
      let dt = new Date();
      var y = dt.getFullYear();
      var mt = (dt.getMonth() + 1).toString().padStart(2, "0");
      var day = dt.getDate().toString().padStart(2, "0");
      var h = dt.getHours().toString().padStart(2, "0");
      var m = dt.getMinutes().toString().padStart(2, "0");
      var s = dt.getSeconds().toString().padStart(2, "0");
      return y + mt + day + h + m + s;
    };

    const uploadPicToGit = async (base_str, fileName) => {
      upload_status.value = "上传中...";
      console.log("wait....");
      let response = await octokit.request(
        "PUT /repos/Roxanne299/PictureBed/contents/kob/user_photo/" + fileName,
        {
          owner: "OWNER",
          repo: "REPO",
          path: "kob/user_photo/",
          message: "save_image",
          committer: {
            name: "Monalisa Octocat",
            email: "octocat@github.com",
          },
          content: base_str,
          headers: {
            "X-GitHub-Api-Version": "2022-11-28",
          },
        }
      );
      img_url.value = response.data.content.download_url;
      upload_status.value = "上传成功！";
      return response;
    };

    return {
      username,
      password,
      confirmPassword,
      register,
      error_message,
      getBase64,
      upload_status,
    };
  },
};
</script>

  
  <style scoped>
.error_message,
.upload_status {
  color: red;
}
.upload_status {
  margin-top: 2px;
}
button {
  width: 100%;
}
</style>