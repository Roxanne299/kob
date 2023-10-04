<template>
  <PlayGround v-if="$store.state.pk.status === 'playing'"></PlayGround>
  <GameMatch v-if="$store.state.pk.status === 'matching'"></GameMatch>
  <ResultBoard v-if="$store.state.pk.loser != 'none'"></ResultBoard>
</template>

<script>
import PlayGround from "@/components/PlayGround.vue";
import { useStore } from "vuex";
import GameMatch from "@/components/GameMatch.vue";
import { onMounted, onUnmounted } from "vue";
import ResultBoard from "@/components/ResultBoard.vue";
export default {
  name: "PkIndexView",
  components: {
    PlayGround,
    GameMatch,
    ResultBoard,
  },
  setup() {
    const store = new useStore();
    const socketUrl = `ws://127.0.0.1:8081/websocket/${store.state.user.token}/`;
    let socket = null;

    onMounted(() => {
      socket = new WebSocket(socketUrl);
      socket.onopen = () => {
        console.log("开始匹配......");
      };
      socket.onmessage = (msg) => {
        console.log(msg);
        let data = JSON.parse(msg.data);
        if (data.msg == "macthing") {
          store.commit("updateOpponent", {
            opponent_username: data.opponent_username,
            opponent_photo: data.opponent_photo,
          });
          setTimeout(() => {
            store.commit("updateStatus", "playing");
          }, 200);

          store.commit("updateGame", data.game_map);
        } else if (data.msg == "move") {
          const game = store.state.pk.gameObject;
          const [snake0, snake1] = game.snakes;
          snake0.set_direction(data.a_direction);
          snake1.set_direction(data.b_direction);
        } else if (data.msg == "result") {
          const game = store.state.pk.gameObject;
          const [snake0, snake1] = game.snakes;

          if (data.loser === "all" || data.loser === "A") {
            snake0.status = "die";
          }
          if (data.loser === "all" || data.loser === "B") {
            snake1.status = "die";
          }
          store.commit("updateLoser", data.loser);
        }
      };
      store.commit("updateSocket", socket);
    });

    onUnmounted(() => {
      socket.close();
      store.commit("updateStatus", "matching");
    });
    return {};
  },
};
</script>

<style scoped>
</style>