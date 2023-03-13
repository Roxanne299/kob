<template>
  <ContentCard>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="register">
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
import { ref } from 'vue';
import router from '@/router/index';
import $ from 'jquery';
export default {
  name: "UserAccountRegisterView",
  components: {
    ContentCard,
  },
  setup(){
    const username = ref('');
    const password = ref('');
    const confirmPassword = ref('');
    const error_message = ref('');
    const register = ()=>{
      $.ajax({
        url: "http://127.0.0.1:8081/user/account/register/",
        type: "post",
        data: {
          username: username.value,
          password: password.value,
          confirmPassword: confirmPassword.value,
        },
        success(resp){
          console.log(resp,username,password,confirmPassword);
          if(resp.error_message == "success"){
              router.push({name: 'user_account_login'});
          }else{
            error_message.value = resp.error_message;

          }
        },
        error(resp){
          console.log(2);
          error_message.value = resp.error_message;

        }
      });
    }

    return{
      username,
      password,
      confirmPassword,
      register,
      error_message,
    }
  },
};
</script>

  
  <style scoped>
  .error_message{
  color:red;
}
button{
  width: 100%;
}
</style>