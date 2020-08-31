import request from '@/utils/request'

const url = '/products'
// 1.获取所有制作单位
export function getAllUnit() {
  return request({
    url: `${url}/getAllUnit`,
    method: 'get'
  })
}
// 2.获取热门产品种类
export function getUnitSum(data) {
  return request({
    url: `${url}/getUnitSum`,
    method: 'get',
    params: data
  })
}
// 3.按时间区间获取热门产品种类
export function getUnitSumForTimes(data) {
  return request({
    url: `${url}/getUnitSumForTimes`,
    method: 'get',
    params: data
  })
}
// 4.按产品名称查询此产品下文件数量
export function getUnitCount(data) {
  return request({
    url: `${url}/getUnitCount`,
    method: 'get',
    params: data
  })
}
// 服务产品清单展示 products/getNewProduct PRODUCTS LIST DISPLAY
export function getDisplay() {
  return request({
    url: `${url}/getNewProduct`,
    method: 'get'
  })
}
// 最新展品展示
export function getNewUnits(data) {
  return request({
    url: `${url}/getNewUnits`,
    method: 'get',
    params: data
  })
}

// 创建产品
export function insertProducts(data) {
  return request({
    url: `${url}/insertProducts`,
    method: 'post',
    data: data
  })
}
// 编辑产品
export function updateProducts(data) {
  return request({
    url: `${url}/updateProducts`,
    method: 'post',
    data: data
  })
}
// 获取产品
export function getProducts(data) {
  return request({
    url: '/products/getProducts',
    method: 'get',
    params: data
  })
}

// 更新次数
export function statisticsUnits(data) {
  return request({
    url: '/products/statisticsUnits',
    method: 'get',
    params: data
  })
}
