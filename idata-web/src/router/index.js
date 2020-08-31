import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout'

/* Router Modules */
import productLibraryRouter from './modules/productLibrary'
// import northCloudRouter from './modules/northCloud'
import specialProductRouter from './modules/specialProduct'
import specialProductDetail from './modules/specialProductDetail'
import dataDownRouter from './modules/dataDown'
import industryApplicationRouter from './modules/industryApplication'
import aboutUsRouter from './modules/aboutUs'
import dataApiRouter from './modules/dataApi'
import testRouter from './modules/test'

export const constantRoutes = [
  productLibraryRouter,
  // northCloudRouter,
  specialProductRouter,
  specialProductDetail,
  dataDownRouter,
  industryApplicationRouter,
  aboutUsRouter,
  dataApiRouter,
  testRouter,
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true,
    meta: { title: '登录', noCache: true }
  },
  // {
  //   path: '/myApi',
  //   component: () => import('@/views/my-api-data/index'),
  //   name: 'MyApi',
  //   hidden: true,
  //   meta: { title: 'API', noCache: true }
  // },
  {
    path: '/product',
    component: () => import('@/views/product/index'),
    hidden: true,
    meta: { title: '自定义产品', noCache: true },
    children: [
      {
        path: 'detail/:id',
        component: () => import('@/views/product/ProductDetail.vue'),
        name: '自定义产品详情'
      },
      {
        path: 'create',
        component: () => import('@/views/product/ProductCreate.vue'),
        name: '创建产品'
      }
    ]
  },
  {
    path: '/northCloud',
    component: () => import('@/views/northCloud/index'),
    name: '云上北疆',
    meta: { title: '云上北疆', noCache: true }
  },
  {
    path: 'northCloud/detail/:id',
    component: () => import('@/views/product/ProductDetail.vue'),
    name: '产品',
    meta: { title: '云上北疆详情', noCache: true }
  },
  {
    path: '/test',
    component: () => import('@/views/test/index'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    meta: { title: '首页', noCache: true },
    children: [
      {
        path: 'home',
        component: () => import('@/views/home/index.vue'),
        name: 'Home',
        meta: { title: '首页', icon: 'home', affix: true }
      }
    ]
  },
  {
    path: '/self-special-product',
    component: () => import('@/views/special-product/SpecialProductDetail.vue'),
    hidden: true,
    meta: { title: '专题产品', icon: 'home',}
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = []

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  // mode: 'history',
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
