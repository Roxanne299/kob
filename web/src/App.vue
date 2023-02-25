<template>
  <nav>
    <NavBar></NavBar>
  </nav>
  <router-view />
</template>

<script>
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.min.js";
import NavBar from "./components/NavBar.vue";
import $ from "jquery";
import { ref } from "vue";
export default {
  name: "App",
  components: {
    NavBar,
  },
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

<style >
body{
  background-image: url("@/assets/images/background.png");
  background-size: cover;
}
</style>
