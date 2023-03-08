<template>
  <ContentCard>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form>
          <div class="mb-3">
            <label for="username" class="form-label"
              >用户名</label
            >
            <input
              type="text"
              class="form-control"
              id="username"
              placeholder="请输入用户名"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label"
              >密码</label
            >
            <input
              type="password"
              class="form-control"
              id="password"
              placeholder="请输入密码"
            />
          </div>
          <div class="error_message">密码错误</div>
          <button type="submit" class="btn btn-primary">登录</button>
        </form>
      </div>
    </div>
  </ContentCard>
</template>
  
  <script>
import ContentCard from "@/components/ContentCard.vue";
export default {
  name: "UserAccountLoginView",
  components: {
    ContentCard,
  },
};
</script>
  
  <style scoped>
  button{
    width:100%;
  }
  .error_message{
    color:red;
  }
</style>