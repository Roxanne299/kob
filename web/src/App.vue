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
<<<<<<< HEAD
body {
  background-image: url("@/assets/background.png");
=======
body{
  background-image: url("@/assets/images/background.png");
>>>>>>> c8f4f7a06b2aa5845a8a4bdb4eb37057ac877360
  background-size: cover;
}
</style>