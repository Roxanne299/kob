<template>
  <ContentCard
    ><div class="container">
      <div class="row">
        <div class="col-3">
          <div class="card">
            <div class="card-body">
              <img :src="$store.state.user.photo" alt="用户头像无法加载" />
            </div>
          </div>
        </div>
        <div class="col-9">
          <div class="card">
            <div class="card-header">
              <span style="font-size: 150%; text-align: center">我的Bot</span>
              <!-- Button trigger modal -->
              <button
                type="button"
                class="btn btn-secondary float-end"
                data-bs-toggle="modal"
                data-bs-target="#bot_add_modal"
                @click="clear_error"
              >
                创建bot
              </button>
              <!-- Modal -->
              <div
                class="modal fade"
                id="bot_add_modal"
                data-bs-backdrop="static"
                data-bs-keyboard="false"
                tabindex="-1"
                aria-labelledby="staticBackdropLabel"
                aria-hidden="true"
              >
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h1 class="modal-title fs-5" id="staticBackdropLabel">
                        创建Bot
                      </h1>
                      <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Close"
                      ></button>
                    </div>
                    <div class="modal-body">
                      <div class="mb-3">
                        <label for="title_input" class="form-label">标题</label>
                        <input
                          type="text"
                          class="form-control"
                          id="title_input"
                          v-model="title"
                          placeholder="请输入bot标题"
                        />
                      </div>
                      <div class="mb-3">
                        <label for="description_textarea" class="form-label"
                          >介绍</label
                        >
                        <textarea
                          class="form-control"
                          id="description_textarea"
                          v-model="description"
                          rows="3"
                          placeholder="请输入bot相关介绍"
                        ></textarea>
                      </div>
                      <div class="mb-3">
                        <label for="content_textarea" class="form-label"
                          >内容</label
                        >
                        <VAceEditor
                          v-model:value="content"
                          @init="editorInit"
                          lang="java"
                          theme="textmate"
                          style="height: 300px"
                          :options="{
                            enableBasicAutocompletion: true, //启用基本自动完成
                            enableSnippets: true, // 启用代码段
                            enableLiveAutocompletion: true, // 启用实时自动完成
                            fontSize: 18, //设置字号
                            tabSize: 4, // 标签大小
                            showPrintMargin: false, //去除编辑器里的竖线
                            highlightActiveLine: true,
                          }"
                          >{{ add_bot_code_content }}</VAceEditor
                        >
                      </div>
                    </div>
                    <div class="modal-footer">
                      <div class="error_message">
                        {{ error_message }}
                      </div>
                      <button
                        type="button"
                        class="btn btn-secondary"
                        data-bs-dismiss="modal"
                      >
                        取消
                      </button>
                      <button
                        type="button"
                        class="btn btn-primary"
                        @click="add_bot"
                      >
                        确认
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="card-body">
              <table class="table table-striped table-hover">
                <thead>
                  <tr>
                    <th scope="col">标题</th>
                    <th scope="col">创建时间</th>
                    <th scope="col">修改时间</th>
                    <th scope="col">操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="bot in bots" :key="bot.id">
                    <td>{{ bot.title }}</td>
                    <td>{{ bot.createtime }}</td>
                    <td>{{ bot.modifytime }}</td>
                    <td>
                      <!-- Button trigger modal -->
                      <button
                        type="button"
                        style="margin-right: 10px"
                        class="btn btn-secondary"
                        data-bs-toggle="modal"
                        :data-bs-target="'#update_modal' + bot.id"
                        @click="clear_error"
                      >
                        修改
                      </button>

                      <!-- Modal -->
                      <div
                        class="modal fade"
                        :id="'update_modal' + bot.id"
                        data-bs-backdrop="static"
                        data-bs-keyboard="false"
                        tabindex="-1"
                        aria-labelledby="staticBackdropLabel"
                        aria-hidden="true"
                      >
                        <div class="modal-dialog">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h1
                                class="modal-title fs-5"
                                id="staticBackdropLabel"
                              >
                                修改Bot
                              </h1>
                              <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                              ></button>
                            </div>
                            <div class="modal-body">
                              <div class="mb-3">
                                <label for="title_input" class="form-label"
                                  >标题</label
                                >
                                <input
                                  type="text"
                                  class="form-control"
                                  id="title_input"
                                  v-model="bot.title"
                                />
                              </div>
                              <div class="mb-3">
                                <label
                                  for="description_textarea"
                                  class="form-label"
                                  >介绍</label
                                >
                                <textarea
                                  class="form-control"
                                  id="description_textarea"
                                  v-model="bot.description"
                                  rows="3"
                                ></textarea>
                              </div>
                              <div class="mb-3">
                                <label for="content_textarea" class="form-label"
                                  >内容</label
                                >
                                <VAceEditor
                                  v-model:value="bot.content"
                                  @init="editorInit"
                                  lang="java"
                                  theme="textmate"
                                  style="height: 300px"
                                  :options="{
                                    enableBasicAutocompletion: true, //启用基本自动完成
                                    enableSnippets: true, // 启用代码段
                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                    fontSize: 18, //设置字号
                                    tabSize: 4, // 标签大小
                                    showPrintMargin: false, //去除编辑器里的竖线
                                    highlightActiveLine: true,
                                  }"
                                />
                              </div>
                            </div>
                            <div class="modal-footer">
                              <div class="error_message">
                                {{ error_message }}
                              </div>
                              <button
                                type="button"
                                class="btn btn-secondary"
                                data-bs-dismiss="modal"
                              >
                                取消
                              </button>
                              <button
                                type="button"
                                class="btn btn-primary"
                                @click="update_bot(bot)"
                              >
                                确认
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button
                        type="button"
                        class="btn btn-danger"
                        @click="delete_bot(bot.id)"
                      >
                        删除
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div></ContentCard
  >
</template>

<script>
import ContentCard from "@/components/ContentCard.vue";
import $ from "jquery";
import { ref } from "vue";
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap.min.js";
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds";
import "ace-builds/src-noconflict/mode-c_cpp";
import "ace-builds/src-noconflict/mode-java";
import "ace-builds/src-noconflict/mode-json";
import "ace-builds/src-noconflict/theme-chrome";
import "ace-builds/src-noconflict/ext-language_tools";

export default {
  name: "UserBotIndexView",
  components: {
    ContentCard,
    VAceEditor,
  },
  setup() {
    const store = new useStore();
    let bots = ref([]);
    let title = ref("");
    let description = ref("");
    let content = ref(
      "package com.kob.botrunningsystem.utils;\npublic class Bot implements BotInterface{\n    //重写这个方法，返回值分别是上下左右（0，1，2，3）\n@Override\n    public Integer nextMove(String input) {\n        return 0;\n    }\n}"
    );
    let error_message = ref("");
    let bot_id = ref("");

    // 编辑框
    ace.config.set(
      "basePath",
      "https://cdn.jsdelivr.net/npm/ace-builds@" +
        require("ace-builds").version +
        "/src-noconflict/"
    );

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

    const clear_error = () => {
      error_message.value = "";
    };
    const add_bot = () => {
      $.ajax({
        url: "http://127.0.0.1:8081/user/bot/add/",
        type: "post",
        data: {
          title: title.value,
          description: description.value,
          content: content.value,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message == "success") {
            title.value = "";
            content.value = "";
            description.value = "";
            error_message.value = "";
            refresh_bots();
            Modal.getInstance("#bot_add_modal").hide();
          } else {
            error_message.value = resp.error_message;
            console.log(
              title.value + " " + description.value + " " + content.value
            );
          }
        },
      });
    };

    const update_bot = (bot) => {
      error_message.value = "";
      $.ajax({
        url: "http://127.0.0.1:8081/user/bot/update/",
        type: "post",
        data: {
          bot_id: bot.id,
          title: bot.title,
          description: bot.description,
          content: bot.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message == "success") {
            refresh_bots();
            Modal.getInstance("#update_modal" + bot.id).hide();
          } else {
            error_message.value = resp.error_message;
          }
        },
      });
    };

    const delete_bot = (bot_id) => {
      $.ajax({
        url: "http://127.0.0.1:8081/user/bot/remove/",
        type: "post",
        data: {
          bot_id: bot_id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message == "success") {
            refresh_bots();
          }
        },
      });
    };

    return {
      store,
      refresh_bots,
      bots,
      title,
      description,
      content,
      add_bot,
      error_message,
      delete_bot,
      bot_id,
      update_bot,
      clear_error,
    };
  },
};
</script>

<style scoped>
.error_message {
  color: red;
}
</style>
