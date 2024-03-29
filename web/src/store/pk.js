const ModulePk = {
     state: {
          status: "matching", //matching 表示匹配界面 playing 表示对战界面
          socket: null,
          opponent_username: "xxx",
          opponent_photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
          gamemap: null,
          a_id: 0,
          a_sx: 0,
          a_sy: 0,
          b_id: 0,
          b_sx: 0,
          b_sy: 0,
          gameObject: null,
          loser: "none",  // none、all、A、B

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
          },
          updateSocket(state, socket) {
               state.socket = socket;
          },
          updateGame(state, game) {
               state.gamemap = game.map;
               state.a_id = game.a_id;
               state.a_sx = game.a_sx;
               state.a_sy = game.a_sy;
               state.b_id = game.b_id;
               state.b_sx = game.b_sx;
               state.b_sy = game.b_sy;
          },
          updateGameObject(state, gameObject) {
               state.gameObject = gameObject;
          },
          updateLoser(state, loser) {
               state.loser = loser;
          },



     },
     actions: {

     },
     modules: {

     }

};
export default ModulePk;