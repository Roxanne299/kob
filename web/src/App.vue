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
export default {
  name: "App",
  components: {
    NavBar,
  },
  setup() {

    return {

    };
  },
};
</script>

<style >
body {
  background-image: url("@/assets/images/background.png");
  background-size: cover;
}
</style>