import Vue from 'vue'
import VueRouter from 'vue-router'

//懒加载
// const Login = () => import('../views/Login.vue')
const Home = () => import('../views/Home.vue')
const Welcome = () => import('../views/Welcome.vue')
const Users = () => import('../views/user/Users.vue')
const Rights = () => import('../views/power/Rights.vue')
const Roles = () => import('../views/power/Roles.vue')
const Orders = () => import('../views/orders/Orders.vue')
const Personnel = () => import('../views/Personnel/Personnel.vue')
const application = () => import('../views/application/application.vue')
const sellOut = () => import('../views/sellOut/sellOut.vue')
const User = () => import('../views/Recharge/User.vue')
const Reports = () => import('../views/reports/reports.vue')

Vue.use(VueRouter)

const routes = [

  { path: '/', redirect: '/login' },
  { path: '/login',
    name:'login',
    component: () => import('@/views/Login')
  },
  {
    path: '/home',
    component: Home,
    redirect: '/Personnel',
    children: [
      { path: '/welcome', component: Welcome },
      { path: '/application', component: application },
      { path: '/Personnel', component: Personnel },
      { path: '/sellOut', component: sellOut },
      { path: '/User', component: User },
      { path: '/users', component: Users },
      { path: '/rights', component: Rights },
      { path: '/roles', component: Roles },
      { path: '/orders', component: Orders },
      { path: '/reports', component: Reports }
    ]
  }
]

const router = new VueRouter({
    // mode: 'hash',
  routes
})

export default router

router.beforeEach((to, from, next) => {
    const token = window.localStorage.getItem('token')
    if (token) {
      //登陆
      if (to.path === '/login'|| to.path === '/register') {
        next()
      } else {
        next()
      }
    } else {
      //未登陆
      if (routes.indexOf(to.path) >= 0) {
          console.log('weidenglu')
        next('/login')
      } else {
        next()
      }
    }
  })

  // 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}


