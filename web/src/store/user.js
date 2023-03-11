import $ from 'jquery';
const ModuleUser = {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login:"",
    },
    getters: {

    },
    mutations:{
        updateUser(state,user){
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state,token){
            state.token = token;
        },
        logout(state){
            state.id = "";
            state.username = "";
            state.photo = "";
            state.token = "";
            state.is_login = false;
        }

    },
    actions: {
        login(context,data){
            $.ajax({
                url: "http://127.0.0.1:8081/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp){
                    if(resp.error_message == "success"){

                        context.commit("updateToken",resp.token);
                        data.success(resp);
                    }
                },
                error(){
                    data.error();
                }
            });
        },
        getInfo(context,data){
            $.ajax({
                url: "http://127.0.0.1:8081/user/account/info/",
                type: "get",
                headers:{
                    'Authorization': 'Bearer ' + context.state.token,
                },
                success(resp){
                    if(resp.error_message == "success"){
                        context.commit("updateUser",{
                            id: resp.id,
                            photo: resp.photo,
                            username: resp.username,
                            is_login: true,
                        })
                        data.success(data);
                    }
                },
                error(resp){
                    data.error(resp);
                }
            });
        },
        logout(context){
            context.commit("logout");
        }

    },
    modules: {

    }

};
export default ModuleUser;