
import LayoutOne from '@/views/layout/LayoutOne.vue'

const dataApiRouter = {
  path: '/api',
  component: LayoutOne,
  redirect: 'api',
  name: 'API',
  meta: {
    title: 'API接口',
    icon: 'chart'
  },
  children: [
    {
      path: '',
      component: () => import('@/views/my-api-data/index.vue'),
      name: 'api',
      meta: { title: 'API接口', noCache: true }
    }
  ]
}

export default dataApiRouter
