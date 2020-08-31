/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/views/layout'

const testRouter = {
  path: '/test',
  component: Layout,
  redirect: 'test',
  name: '测试',
  meta: {
    title: '测试',
    icon: 'chart'
  },
  children: [
    {
      path: '',
      component: () => import('@/views/test/index.vue'),
      name: 'aboutUs',
      meta: { title: '测试', noCache: true }
    }
  ]
}

export default testRouter
