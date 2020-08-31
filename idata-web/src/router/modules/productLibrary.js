/** When your routing table is too long, you can split it into small modules**/

import Layout from "@/views/layout";

const productLibraryRouter = {
  path: "/productLibrary",
  component: Layout,
  redirect: "productLibrary",
  // name: '产品库',
  meta: {
    title: "产品库",
    icon: "chart"
  },
  children: [
    {
      path: "",
      component: () => import("@/views/product-library/index"),
      name: "产品库",
      meta: { title: "产品库", noCache: true }
    },
    {
      path: "detail/:id",
      component: () => import("@/views/product/ProductDetail.vue"),
      name: "产品",
      meta: { title: "产品库详情", noCache: true }
    }
    // {
    //   path: 'list',
    //   component: () => import('@/views/product-library/ProductList'),
    //   name: 'KeyboardChart',
    //   meta: { title: 'Keyboard Chart', noCache: true }
    // },
    // {
    //   path: 'create',
    //   component: () => import('@/views/product-library/ProductCreate'),
    //   name: '创建产品',
    //   meta: { title: '创建', noCache: true }
    // },
    // {
    //   path: 'detail/:id',
    //   component: () => import('@/views/product-library/ProductDetail'),
    //   name: '产品详情',
    //   meta: { title: '详情', noCache: true }
    // }
  ]
};

export default productLibraryRouter;
