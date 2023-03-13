import { createRouter, createWebHistory } from 'vue-router'

import PkIndexView from "@/views/pk/PkIndexView";
import NotFoundView from "@/views/error/NotFoundView";
import RanklistIndexView from "@/views/ranklist/RanklistIndexView";
import RecordIndexView from "@/views/record/RecordIndexView";
import UserBotIndexView from "@/views/user/bot/UserBotIndexView";
import UserAccountLoginView from "@/views/user/account/UserAccountLoginView";
import UserAccountRegisterView from "@/views/user/account/UserAccountRegisterView";
import store from '@/store';
const routes = [
  {
    path: '/pk/',
    name: 'pk_index',
    component: PkIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/record/',
    name: 'record_index',
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/userbot/',
    name: 'userbot_index',
    component: UserBotIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/ranklist/',
    name: 'ranklist_index',
    component: RanklistIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/user/account/login/',
    name: 'user_account_login',
    component: UserAccountLoginView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/user/account/register/',
    name: 'user_account_register',
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: '/404',
    name: '404',
    component: NotFoundView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: '/:catchAll(.*)',
    redirect: '/404',
  }


]

const router = createRouter({
  history: createWebHistory(),
  routes
})


//每次跳转页面之前会检查一下
router.beforeEach((to,from,next)=>{
  if(to.meta.requestAuth && !store.state.user.is_login && to.name !== 'user_account_login'){
    console.log(1);
    next({name: "user_account_login"});
  }else{
    //跳转到本来要的页面
    next();
  }
})

export default router
