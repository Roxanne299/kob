<template>
  <PlayGround v-if="$store.state.pk.status === 'playing'"></PlayGround>
  <GameMatch v-if="$store.state.pk.status === 'matching'"></GameMatch>
</template>

<script>
import PlayGround from "@/components/PlayGround.vue";
import { useStore } from "vuex";
import GameMatch from "@/components/GameMatch.vue";
import { onMounted, onUnmounted } from "vue";
export default {
  name: "PkIndexView",
  components: {
    PlayGround,
    GameMatch,
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
        store.commit("updateOpponent", {
          opponent_username: msg.data.opponent_username,
          opponent_photo: msg.data.opponent_photo,
        });
        setInterval(() => {
          store.state.pk.status = "playing";
        }, 1000);
      };
      store.commit("updateSocket", socket);
    });

    onUnmounted(() => {});
    return {};
  },
};
</script>

<style scoped>
</style>