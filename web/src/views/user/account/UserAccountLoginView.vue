<template>
  <ContentCard v-if="!$store.state.user.polling">
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
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
          <div class="error_message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary">登录</button>
        </form>
      </div>
    </div>
  </ContentCard>
</template>
  
<script>
import ContentCard from "@/components/ContentCard.vue";
import { useStore } from "vuex";
import { ref } from "vue";
import router from "@/router";

export default {
  name: "UserAccountLoginView",
  components: {
    ContentCard,
  },
  setup() {
    const store = new useStore();
    const username = ref("");
    const password = ref("");
    const error_message = ref("");

    const img_name = ref("");
    let token = localStorage.getItem("token");

    store.commit("updatePolling", true);
    if (token == null) {
      store.commit("updatePolling", false);
    } else {
      //更新全局信息token
      store.commit("updateToken", token);
      store.dispatch("getInfo", {
        success() {
          //验证成功跳转到首页
          router.push({ name: "pk_index" });
          store.commit("updatePolling", false);
        },
        error() {
          localStorage.removeItem("token");
          store.commit("updatePolling", false);
        },
      });
    }

    const login = () => {
      error_message.value = "";
      store.dispatch("login", {
        username: username.value,
        password: password.value,
        success(resp) {
          console.log(resp);
          store.dispatch("getInfo", {
            success(resp) {
              router.push({ name: "pk_index" });
              console.log(resp);
            },
            error(resp) {
              error_message.value = "用户名或密码错误";
              console.log(resp);
            },
          });
        },
        error() {
          error_message.value = "用户名或密码错误";
        },
      });
    };

    return {
      username,
      password,
      error_message,
      login,

      img_name,
    };
  },
};
</script>
  
<style scoped>
button {
  width: 100%;
}
.error_message,
.upload_status {
  color: red;
}
</style>