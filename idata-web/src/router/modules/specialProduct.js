/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/views/layout/LayoutOne.vue'

const specialProductRouter = {
  path: '/specialProduct',
  component: Layout,
  redirect: 'specialProduct',
  name: '专题产品',
  meta: {
    title: '专题产品',
    icon: 'chart'
  },
  children: [
    {
      path: '',
      component: () => import('@/views/special-product/index.vue'),
      name: 'specialProduct',
      meta: { title: '专题产品', noCache: true }
    },
    {
      path: 'list',
      component: () => import('@/views/special-product/SpecialProductList.vue'),
      name: 'specialProductList',
      meta: { title: '专题产品', noCache: true }
    }
  ]
}

export default specialProductRouter
