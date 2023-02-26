<template>
    <GameMap class="playground"></GameMap>
</template>
  
<script>
import GameMap from './GameMap.vue';
export default {
name: "PlayGround",
components:{
    GameMap,
}
};
</script>

<style scoped>
.playground{
    width: 60vw;
    height: 70vh;
    background-color: aqua;
    margin: 0 auto;
    margin-top: 10px;
}
</style>