<template>
  <ContentCard
    ><table class="table table-striped table-hover">
      <thead>
        <tr>
          <th scope="col">玩家A</th>
          <th scope="col">玩家B</th>
          <th scope="col">对战结果</th>
          <th scope="col">对战时间</th>
          <th scope="col">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="record in record_list" :key="record.id">
          <td>
            <img :src="record.a_photo" alt="" class="record-user-photo" />
            &nbsp;
            <span class="record-user-username">{{ record.a_username }}</span>
          </td>
          <td>
            <img :src="record.b_photo" alt="" class="record-user-photo" />
            &nbsp;
            <span class="record-user-username">{{ record.b_username }}</span>
          </td>
          <td>
            <span>{{ record.result }}</span>
          </td>
          <td>
            <span>{{ record.record.createtime }}</span>
          </td>
          <td>
            <!-- Button trigger modal -->
            <button
              type="button"
              style="margin-right: 10px"
              class="btn btn-secondary"
              @click="open_record(record)"
            >
              查看录像
            </button>
          </td>
        </tr>
      </tbody>
    </table></ContentCard
  >
</template>

<script>
import ContentCard from "@/components/ContentCard.vue";
import $ from "jquery";
import { useStore } from "vuex";
import { ref } from "vue";
import router from "@/router";
export default {
  name: "RecordIndexView",
  components: {
    ContentCard,
  },
  setup() {
    let store = new useStore();
    let current_page = 1;
    let record_list = ref([]);
    let records_total = 0;

    let query_page = (page) => {
      $.ajax({
        url: "http://127.0.0.1:8081/record/getlist/",
        data: {
          page: page,
          user_id: store.state.user.id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        type: "get",
        success(resp) {
          record_list.value = resp.records;
          records_total = resp.records_count;
          console.log(resp);
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    let stringToMap = (map) => {
      let g = [];
      for (let i = 0, k = 0; i < 14; i++) {
        let line = [];
        for (let j = 0; j < 13; j++, k++) {
          if (map[k] == "0") line.push(0);
          else line.push(1);
        }
        g.push(line);
      }
      return g;
    };
    let open_record = (record) => {
      store.commit("updateIsRecord", true);
      store.commit("updateGame", {
        map: stringToMap(record.record.map),
        a_id: record.record.aid,
        asx: record.record.asx,
        asy: record.record.asy,
        b_id: record.record.bid,
        bsx: record.record.bsx,
        bsy: record.record.bsy,
      });
      store.commit("updateSteps", {
        a_step: record.record.asteps,
        b_step: record.record.bsteps,
      });
      store.commit("updateRecordLoser", record.record.loser);
      router.push({
        name: "record_view",
        params: {
          recordId: record.id,
        },
      });
    };
    query_page(current_page);
    return {
      query_page,
      store,
      current_page,
      record_list,
      records_total,
      open_record,
    };
  },
};
</script>

<style scoped>
.record-user-photo {
  width: 4vh;
  border-radius: 50%;
}
</style>
