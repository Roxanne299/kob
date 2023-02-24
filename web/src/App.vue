<template>
  <nav>
    <div>{{user_name}}</div>
    <div>{{user_age}}</div>
  </nav>
  <router-view />
</template>

<script>
import $ from "jquery";
import { ref } from "vue";
export default {
  name: "App",
  setup() {
    let user_name = ref("");
    let user_age = ref("");
    $.ajax({
      url: "http://localhost:8081/pk/getbotinfo/",
      type: "get",
      success(resp) {
        user_name.value = resp.Bot_Name;
        user_age.value = resp.Bot_age;
      },
    });
    return {
      user_age,
      user_name,
    };
  },
};
</script>

<style>
</style>
