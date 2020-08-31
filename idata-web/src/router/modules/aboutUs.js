/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/views/layout'

const aboutUsRouter = {
  path: '/aboutUs',
  component: Layout,
  redirect: 'aboutUs',
  name: '关于我们',
  meta: {
    title: '关于我们',
    icon: 'chart'
  },
  children: [
    {
      path: '',
      component: () => import('@/views/about-us/index.vue'),
      name: 'aboutUs',
      meta: { title: '关于我们', noCache: true }
    }
  ]
}

export default aboutUsRouter
