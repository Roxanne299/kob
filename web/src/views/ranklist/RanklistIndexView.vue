<template>
  <ContentCard
    ><table class="table table-striped table-hover">
      <thead>
        <tr>
          <th scope="col">玩家</th>
          <th scope="col">天梯分数</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="rating in rating_list" :key="rating.id">
          <td>
            <img :src="rating.photo" alt="" class="rating-user-photo" />
            &nbsp;
            <span class="rating-user-username">{{ rating.username }}</span>
          </td>

          <td>
            <span>{{ rating.rating }}</span>
          </td>
        </tr>
      </tbody>
    </table>
    <nav aria-label="Page navigation example" style="float: right">
      <ul class="pagination">
        <li class="page-item" @click="click_page(-2)">
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li
          v-for="page in pages"
          :key="page.num"
          :class="'page-item ' + page.is_active"
          @click="click_page(page.num)"
        >
          <a class="page-link" href="#">{{ page.num }}</a>
        </li>
        <li class="page-item" @click="click_page(-1)">
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav></ContentCard
  >
</template>

<script>
import ContentCard from "@/components/ContentCard.vue";
import $ from "jquery";
import { useStore } from "vuex";
import { ref } from "vue";
export default {
  name: "RanklistIndexView",
  components: {
    ContentCard,
  },
  setup() {
    let store = new useStore();
    let current_page = 1;
    let rating_list = ref([]);
    let rating_total = 0;
    let pages = ref([]);

    let update_page = (page) => {
      let list = [];
      let max = Math.ceil(rating_total / 2);
      for (let i = page - 2; i <= page + 2; i++) {
        if (i >= 1 && i <= max)
          list.push({
            num: i,
            is_active: i == current_page ? "active" : "",
          });
      }
      pages.value = list;
    };

    let click_page = (page) => {
      let max = Math.ceil(rating_total / 2);
      let target = page;
      if (page == -2) target = current_page - 1;
      if (page == -1) target = current_page + 1;
      if (target >= 1 && target <= max) query_page(target);
    };
    let query_page = (page) => {
      $.ajax({
        url: "http://127.0.0.1:8081/ranklist/getlist/",
        data: {
          page: page,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        type: "get",
        success(resp) {
          rating_list.value = resp.users;
          rating_total = resp.count;
          current_page = page;
          update_page(page);
          console.log(resp);
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    query_page(current_page);
    return {
      query_page,
      store,
      current_page,
      rating_list,
      rating_total,
      update_page,
      click_page,
      pages,
    };
  },
};
</script>

<style scoped>
.rating-user-photo {
  width: 4vh;
  border-radius: 50%;
}
</style>
