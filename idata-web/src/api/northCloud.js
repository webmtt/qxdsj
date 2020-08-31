import request from '@/utils/request'

const url = '/cloud'
// 1.数据目录交换情况 (累计值)
export function directoryExchange(data) {
  return request({
    url: `${url}/directoryExchange.shtml`,
    method: 'get',
    params: data
  })
}
// 2.数据资源交换情况 (统计值)
export function resourceExchange(data) {
  return request({
    url: `${url}/resourceExchange.shtml`,
    method: 'get',
    params: data
  })
}
// 3.文件资源交换详情
export function fileResourceDetails(data) {
  return request({
    url: `${url}/fileResourceDetails.shtml`,
    method: 'get',
    params: data
  })
}
// 4.库表资源交换详情
export function tableResourceDetails(data) {
  return request({
    url: `${url}/tableResourceDetails.shtml`,
    method: 'get',
    params: data
  })
}
// 5.接口资源交换详情
export function interfaceResourceDetails(data) {
  return request({
    url: `${url}/interfaceResourceDetails.shtml`,
    method: 'get',
    params: data
  })
}
