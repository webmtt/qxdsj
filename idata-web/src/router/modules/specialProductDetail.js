/** When your routing table is too long, you can split it into small modules**/

// import Layout from '@/views/layout'
import LayoutOue from '@/views/layout/LayoutOne.vue'

const specialProductRouter = {
  path: '/specialProduct/detail',
  component: LayoutOue,
  meta: {
    title: '专题产品',
    icon: 'chart'
  },
  children: [
    {
      path: '',
      component: () => import('@/views/special-product/SpecialProductDetail.vue'),
      name: 'SpecialProductDetail',
      meta: { title: '专题产品', noCache: true }
    }
  ]

}

export default specialProductRouter
