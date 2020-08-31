/** When your routing table is too long, you can split it into small modules**/

// import Layout from '@/views/layout'
import LayoutOue from '@/views/layout/LayoutOne.vue'

const dataDownRouter = {
  path: '/dataDown',
  component: LayoutOue,
  meta: {
    title: '数据下载',
    icon: 'chart'
  },
  children: [
    {
      path: '',
      component: () => import('@/views/data-down/index.vue'),
      name: 'dataDown',
      meta: { title: '数据下载', noCache: true }
    }
  ]
}

export default dataDownRouter
