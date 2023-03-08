import { createRouter, createWebHistory } from 'vue-router'

import PkIndexView from "@/views/pk/PkIndexView";
import NotFoundView from "@/views/error/NotFoundView";
import RanklistIndexView from "@/views/ranklist/RanklistIndexView";
import RecordIndexView from "@/views/record/RecordIndexView";
import UserBotIndexView from "@/views/user/bot/UserBotIndexView";
import UserAccountLoginView from "@/views/user/account/UserAccountLoginView";
import UserAccountRegisterView from "@/views/user/account/UserAccountRegisterView";
 
const routes = [
  {
    path: '/pk/',
    name: 'pk_index',
    component: PkIndexView
  },
  {
    path: '/record/',
    name: 'record_index',
    component: RecordIndexView
  },
  {
    path: '/userbot/',
    name: 'userbot_index',
    component: UserBotIndexView
  },
  {
    path: '/ranklist/',
    name: 'ranklist_index',
    component: RanklistIndexView
  },
  {
    path: '/user/account/login/',
    name: 'user_account_login',
    component: UserAccountLoginView
  },
  {
    path: '/user/account/register/',
    name: 'user_account_register',
    component: UserAccountRegisterView
  },
  {
    path: '/404',
    name: '404',
    component: NotFoundView
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

export default router
