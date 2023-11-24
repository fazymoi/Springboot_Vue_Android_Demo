import Vue from 'vue'
import Router from 'vue-router'
// 导入刚才编写的组件
import AppIndex from '@/components/home/AppIndex'
import Login from '@/views/Login'
import Login2 from '@/views/Login2'
import Register from '@/views/Register'
import Main from '@/views/AppMain'
import adminmain from '@/views/adminMain'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/adminmain',
      name: 'adminMain',
      component: adminmain,
      redirect: '/check',
      children: [
        {
          path: '/check',
          name: 'check',
          component: () => import('@/views/check')
        }
      ]
    },
    {
      path: '/main',
      name: 'Main',
      component: Main,
      redirect: '/searchstore',
      children: [
        {
          path: '/searchstore',
          name: 'searchstore',
          component: () => import('@/views/searchstore')
        },
        {
          path: '/searchfood',
          name: 'searchfood',
          component: () => import('@/views/searchfood')
        },
        {
          path: '/personal',
          name: 'personal',
          component: () => import('@/views/personal')
        },
        {
          path: '/comment',
          name: 'comment',
          component: () => import('@/views/comment')
        },
        {
          path: '/store',
          name: 'store',
          component: () => import('@/views/store')
        },
        {
          path: '/food',
          name: 'food',
          component: () => import('@/views/food')
        },
        {
          path: '/storepage',
          name: 'storepage',
          component: () => import('@/views/StorePage')
        },
        {
          path: '/foodpage',
          name: 'foodpage',
          component: () => import('@/views/FoodPage')
        }
      ]
    },
    // 下面都是固定的写法
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/login2',
      name: 'Login2',
      component: Login2
    },
    {
      path: '/index',
      name: 'AppIndex',
      component: AppIndex
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    }
  ]
})
