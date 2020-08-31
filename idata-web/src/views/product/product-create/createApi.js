import request from '@/utils/request'

// 用户自定义页面 数据接口
export function getProductsPhoto(data) {
  return request({
    url: `/products/getProductsPhoto`,
    method: 'post',
    data: data
  })
}
// 单图轮播
export function getMorePhoto(data) {
  return request({
    url: `/products/getMorePhoto`,
    method: 'get',
    params: data
  })
}
export function getMenuPhoto(data) {
  return request({
    url: `/products/getMenuPhoto`,
    method: 'get',
    params: data
  })
}
//  用户自定义页面  数据导航
export function getUserNavData() {
  return request({
    url: `/products/getProductsFileType`,
    method: 'post'
  })
}

// 标题列表模块
export function getMoreVersion(data) {
  return request({
    url: `/products/getMoreVersion`,
    method: 'get',
    params: data
  })
}

//  用户自定义页面  树形结构
export function getTree() {
  return request({
    url: `products/getTree`,
    method: 'get'
  })
}

//  用户自定义页面  树形结构数据内容
export function getTreeData(data) {
  return request({
    url: `products/getTreeData`,
    method: 'get',
    params: data
  })
}
//  用户自定义页面  树形结构数据详情列表
export function getAlltypsFile(data) {
  return request({
    url: `products/getAlltypsFile`,
    method: 'get',
    params: data
  })
}

// 实况请求地址

export function getActuallyHappening(id) {
  const timer = new Date().getTime()
  return request({
    url: `http://10.62.89.55/cimiss-web/api?userId=user_imwb&pwd=symantec5227&interfaceId=getSurfEleByTimeAndStaID&dataCode=SURF_CHN_MUL_HOR&elements=Station_Id_C,Hour,TEM,TEM_Max,TEM_Min,PRS,PRE_6h,PRE_12h,PRE_24h,RHU,WIN_D_Avg_10mi,WIN_S_Avg_10mi&times=${timer}&staIDs=${id}&dataFormat=jsonp`,
    method: 'get',
  })
}

// 类型提示信息的接口
export function getFactor() {
  return request({
    url: `products/getFactor`,
    method: 'get'
  })
}

// // 导航树
// export function getTreeData(data) {
//   return request({
//     url: `products/getTreeData`,
//     method: 'get',
//     params: data
//   })
// }