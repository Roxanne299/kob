<template>
  <div class="gamematch">
    <div class="row">
      <div class="col-6">
        <div class="pk_img_div">
          <!-- <img :src="$store.state.user.photo" alt="" class="" /> -->
          <img
            src="https://cdn.acwing.com/media/user/profile/photo/80738_lg_e91297d1fd.jpg"
            alt="用户头像无法加载"
            class="pk_photo me"
          />
        </div>
        <div class="pk_username me">{{ $store.state.user.username }}</div>
      </div>
      <div class="col-6">
        <div class="pk_img_div">
          <!-- <img :src="$store.state.user.photo" alt="" class="" /> -->
          <img
            :src="$store.state.pk.opponent_photo"
            alt="对手图片无法加载"
            class="pk_photo me"
          />
        </div>
        <div class="pk_username me">
          {{ $store.state.pk.opponent_username }}
        </div>
      </div>
      <div class="row">
        <div class="match_button">
          <button type="button" @click="macth_click" class="btn btn-light">
            {{ match_btn }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
import { useStore } from "vuex";
import { ref } from "vue";
export default {
  name: "GameMatch",
  components: {},
  setup() {
    let store = new useStore();
    let match_btn = ref("开始匹配");
    let socket = null;

    const macth_click = () => {
      if (match_btn.value == "开始匹配") {
        socket = store.state.pk.socket;
        if (socket.readyState == 1) {
          socket.send(
            JSON.stringify({
              msg: "matching",
            })
          );
        }

        match_btn.value = "取消";
      } else if (match_btn.value == "取消") {
        socket.close();
        match_btn.value = "开始匹配";
      }
    };
    return {
      store,
      match_btn,
      macth_click,
      socket,
    };
  },
};
</script>

<style scoped>
.gamematch {
  width: 60vw;
  height: 50vh;
  margin: 0 auto;
  padding-top: 5vh;
  margin-top: 10px;
  background-color: rgb(50, 50, 50, 0.5);
}
.pk_photo {
  margin: 1vh;
  width: 12vw;
  border-radius: 50%;
  border: 2px solid white;
}
.pk_img_div,
.pk_username {
  text-align: center;
}
.pk_username {
  font-size: 20px;
  font-weight: bold;
  color: white;
}
.match_button {
  margin-top: 5vw;
  text-align: center;
}
.btn {
  font-size: 14px;
  font-weight: bold;
  width: 7vw;
}
</style>