/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/views/layout'

const industryApplicationRouter = {
  path: '/industryApplication',
  component: Layout,
  redirect: 'industryApplication',
  name: '行业应用',
  children: [
    {
      path: '',
      component: () => import('@/views/industry-application/index.vue'),
      name: 'Application',
      meta: { title: '行业应用', noCache: true }
    },
    {
      path: 'detail/:id',
      component: () => import('@/views/industry-application/detail.vue'),
      name: '/industryApplication',
      meta: { title: '行业应用详情', noCache: true }
    }
  ]
}

export default industryApplicationRouter
