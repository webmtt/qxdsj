import request from '@/utils/request'

const url = '/interfacedata'

export function getNewInterface() {
  return request({
    url: `${url}/newInterface`,
    method: 'get'
  })
}
export function getHotProduct() {
  return request({
    url: `/subject/getHotProduct`,
    method: 'get'
  })
}

export function getHomeProduct() {
  return request({
    url: `/subject/getHomeProduct`,
    method: 'get'
  })
}

export function getFeaturesProduct() {
  return request({
    url: `/subject/getFeaturesProduct`,
    method: 'get'
  })
}
// 数据服务
export function getDataSortCount() {
  return request({
    url: `/data/getDataSortCount`,
    method: 'get'
  })
}
