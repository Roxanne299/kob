const ModulePk = {
     state: {
          status: "matching", //matching 表示匹配界面 playing 表示对战界面
          socket: null,
          opponent_username: "xxx",
          opponent_photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
          gamemap: null,
     },
     getters: {

     },
     mutations: {
          updateStatus(state, status) {
               state.status = status;
          },

          updateOpponent(state, opponent) {
               state.opponent_photo = opponent.opponent_photo;
               state.opponent_username = opponent.opponent_username;
               state.gamemap = opponent.gamemap;
          },
          updateSocket(state, socket) {
               state.socket = socket;
          },


     },
     actions: {

     },
     modules: {

     }

};
export default ModulePk;