<template>
  <div class="gamematch">
    <div class="row">
      <div class="col-4">
        <div class="pk_img_div">
          <img
            :src="$store.state.user.photo"
            alt="用户头像无法加载"
            class="pk_photo me"
          />
        </div>
        <div class="pk_username me">{{ $store.state.user.username }}</div>
      </div>
      <div class="col-4 user-bot-select">
        <div class="form-floating select">
          <select
            class="form-select"
            id="floatingSelect"
            aria-label="Floating label select example"
            v-model="select_bot"
          >
            <option value="-1">亲自上阵</option>

            <option v-for="bot in bots" :key="bot.id" :value="bot.id">
              {{ bot.title }}
            </option>
          </select>
          <label for="floatingSelect">选择你的对战模式：</label>
        </div>
      </div>
      <div class="col-4">
        <div class="pk_img_div">
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
import $ from "jquery";
export default {
  name: "GameMatch",
  components: {},
  setup() {
    let store = new useStore();
    let match_btn = ref("开始匹配");
    let socket = null;
    let bots = ref([]);
    let select_bot = ref("-1");

    const macth_click = () => {
      if (match_btn.value == "开始匹配") {
        socket = store.state.pk.socket;
        if (socket.readyState == 1) {
          socket.send(
            JSON.stringify({
              msg: "matching",
              bot_id: select_bot.value,
            })
          );
        }

        match_btn.value = "取消";
      } else if (match_btn.value == "取消") {
        socket = store.state.pk.socket;
        if (socket.readyState == 1) {
          socket.send(
            JSON.stringify({
              msg: "cancel_matching",
            })
          );
        }
        match_btn.value = "开始匹配";
      }
    };

    const refresh_bots = () => {
      $.ajax({
        url: "http://127.0.0.1:8081/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          bots.value = resp;
        },
        error(resp) {
          console.log(resp);
        },
      });
    };
    refresh_bots();
    return {
      store,
      match_btn,
      macth_click,
      socket,
      refresh_bots,
      bots,
      select_bot,
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
.user-bot-select {
  padding-top: 13vh;
}
.user-bot-select > select {
  font-size: 20px;
  font-weight: bold;
  width: 60%;
  height: 150%;
  margin: 0 auto;
}
</style>