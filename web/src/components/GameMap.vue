<template>
  <div ref="parent" class="gamemap">
    <canvas ref="canvas" tabindex="0"> </canvas>
  </div>
</template>

<script>
import { GameMap } from "@/assets/scripts/GameMap";
import { ref, onMounted } from "vue";
import { useStore } from "vuex";
export default {
  name: "GameMap",
  setup() {
    let parent = ref(null);
    let canvas = ref(null);
    let store = new useStore();
    onMounted(() => {
      store.commit(
        "updateGameObject",
        new GameMap(canvas.value.getContext("2d"), parent.value, store)
      );
    });
    return {
      parent,
      canvas,
    };
  },
};
</script>

<style scoped>
.gamemap {
  /*同父元素等长宽*/
  width: 100%;
  height: 100%;
  /* 全部居中 */
  display: flex;
  /* 从中间开始排列 控制横轴 */
  justify-content: center;
  /* 从中间开始排列 控制纵轴 */
  align-items: center;
}
</style>