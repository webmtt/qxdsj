import request from '@/utils/request'

const url = '/subject'

// const http='http://192.168.223.157:8085'

// 获取列表
// export function getProductList(data) {
//   return request({
//     url: `${url}/getIndexProduct`,
//     method: 'get',
//     params: data
//   })
// }

export function getProductList(data) {
  return request({
    url: `${url}/getHomeProduct`,
    method: 'get',
    params: data
  })
}

//  获取导航
export function getTreeProduct(data) {
  return request({
    url: `${url}/getTreeProduct`,
    method: 'get',
    params: data
  })
}

//  获取产品
export function getProductImg(data) {
  return request({
    url: `${url}/getProductImg`,
    method: 'get',
    params: data
  })
}

// 获取访问权限
export function getPermissions(params) {
  return request({
    url: `${url}/ispermissions`,
    method: 'get',
    params
  })
}
