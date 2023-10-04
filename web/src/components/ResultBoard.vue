<template>
  <div class="result_board">
    <div
      class="content"
      v-if="
        $store.state.pk.a_id == $store.state.user.id &&
        $store.state.pk.loser == 'A'
      "
    >
      you lose!
    </div>
    <div
      class="content"
      v-else-if="
        $store.state.pk.b_id == $store.state.user.id &&
        $store.state.pk.loser == 'B'
      "
    >
      you lose!
    </div>
    <div class="content" v-else-if="$store.state.pk.loser == 'all'">draw!</div>
    <div class="content" v-else>you win!</div>
    <div class="button">
      <button type="button" class="btn btn-light" @click="restart">重开</button>
    </div>
  </div>
</template>
    
<script>
import { useStore } from "vuex";

export default {
  name: "ResultBoardVue",
  setup() {
    let store = new useStore();

    let restart = () => {
      store.commit("updateStatus", "matching");
      store.commit("updateOpponent", {
        opponent_photo:
          "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
        opponent_username: "xxx",
      });
      store.commit("updateLoser", "none");
    };
    return {
      store,
      restart,
    };
  },
};
</script>

<style scoped>
.result_board {
  width: 30vw;
  height: 30vh;
  background-color: rgb(50, 50, 50, 0.5);
  position: absolute;
  top: 17vh;
  left: 35vw;
  border-radius: 1%;
}
.content {
  padding-top: 7vh;
  text-align: center;
  font-size: 60px;
  font-weight: 600;
  font-style: italic;
  color: white;
}

.button {
  margin-top: 5vh;
  text-align: center;
}
.btn {
  font-size: 14px;
  font-weight: bold;
  width: 7vw;
}
</style>