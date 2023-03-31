const ModulePk = {
    state: {
        status: "matching", //matching 表示匹配界面 playing 表示对战界面
        socket: null,
        opponent_username: "",
        opponent_photo: "",
    },
    getters: {

    },
    mutations:{
       updateStatus(state,status){
            state.status = status;
       },

       updateOpponent(state,opponent){
            state.opponent_photo = opponent.opponent_photo;
            state.opponent_username = opponent.opponent_username;
       },
       updateSocket(state,socket){
            state.socket = socket;
       },
       

    },
    actions: {
    
    },
    modules: {

    }

};
export default ModulePk;