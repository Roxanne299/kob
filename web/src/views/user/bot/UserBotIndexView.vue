<template>
  <ContentCard>我的Bot</ContentCard>
</template>

<script>
import ContentCard from "@/components/ContentCard.vue";
import $ from "jquery";
import { useStore } from "vuex";

export default {
  name: "UserBotIndexView",
  components: {
    ContentCard,
  },
  setup() {
    const store = new useStore();
    // $.ajax({
    //     url: "http://127.0.0.1:8081/user/bot/add/",
    //     type: "post",
    //     data: {
    //         title: "标题",
    //         content: "内容",
    //         description: "描述",
    //     },
    //     headers:{
    //         'Authorization': 'Bearer ' + store.state.user.token,
    //     },
    //     success(resp){
    //         console.log(resp);
    //     },
    //     error(resp){
    //         console.log(resp);
    //     }
    // });

    // $.ajax({
    //     url: "http://127.0.0.1:8081/user/bot/remove/",
    //     type: "post",
    //     data: {
    //         bot_id: "6",
    //     },
    //     headers:{
    //         'Authorization': 'Bearer ' + store.state.user.token,
    //     },
    //     success(resp){
    //         console.log(resp);
    //     },
    //     error(resp){
    //         console.log(resp);
    //     }
    // });

    // $.ajax({
    //   url: "http://127.0.0.1:8081/user/bot/update/",
    //   type: "post",
    //   data: {
    //     bot_id: "7",
    //     title: "标题2",
    //     content: "内容2",
    //     description: "描述2",
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });

    $.ajax({
      url: "http://127.0.0.1:8081/user/bot/getlist/",
      type: "get",
      headers: {
        Authorization: "Bearer " + store.state.user.token,
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      },
    });
  },
};
</script>

<style scoped>
</style>
