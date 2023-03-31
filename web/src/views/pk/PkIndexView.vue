<template>
  <PlayGround>PK</PlayGround>
</template>

<script>
import PlayGround from "@/components/PlayGround.vue";
import { onMounted,onUnmounted} from "vue";
import { useStore } from "vuex";
export default {
  name: "PkIndexView",
  components: {
    PlayGround,
  },
  setup(){
    const store = new useStore();
    const socketUrl = `ws://127.0.0.1:8081/websocket/${store.state.user.id}/`;
    let socket = null;

    onMounted(()=>{
      socket = new WebSocket(socketUrl);
      
      socket.onopen = ()=>{
          console.log("connected!");
      };
      socket.onmessage = msg =>{
        //spring框架返回的消息定义在msg.data里面
        console.log(msg.data);
      };
      socket.onclose = ()=>{
        console.log("closed");
      };
    });

    //当组件关闭之后就关掉连接
    onUnmounted(()=>{
      socket.close();
    });

  }
};
</script>

<style scoped>
</style>