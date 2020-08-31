import request from '@/utils/request'

const url = '/interfacedata'

export function getApiTypeList(id) {
  return request({
    // url: `${url}/allinter`,
    url: `${url}/allinter?userid=${id}`,
    // url: `http://192.168.3.4:8085${url}/allinter`,
    method: 'get'
  })
}
// 2.查询当前资料下的所有接口
export function getApiTypeDetailList(obj) {
  return request({
    url: `${url}/getInterface?userid=${obj.userid}&id=${obj.id}`,
    method: 'get'
  })
}
// 3.查询接口详细信息
// http://tszh75.natappfree.cc/interfacedata/interfaces?id=11
export function getApiDetail(id) {
  return request({
    url: `${url}/interfaces?id=${id}`,
    method: 'get'
  })
}
// 4.列表查询接口信息
export function getEleForInter(id) {
  return request({
    url: `${url}/getEleForInter?id=${id}`,
    method: 'get'
  })
}
// 5.要素详情
export function getElements(id) {
  return request({
    url: `${url}/getElements?id=${id}`,
    method: 'get'
  })
}
// 6.其他要素详情
export function getInterElement(id) {
  return request({
    url: `${url}/getInterElement?id=${id}`,
    method: 'get'
  })
}
// 7.getFcstLevel详情
export function getFcstLevel(id) {
  return request({
    url: `${url}/getFcstLevel?id=${id}`,
    method: 'get'
  })
}
// 8.getNetCodes详情
export function getNetCodes(id) {
  return request({
    url: `${url}/getNetCodes?id=${id}`,
    method: 'get'
  })
}
// 9.getSoilDepths详情
export function getSoilDepths(id) {
  return request({
    url: `${url}/getSoilDepths?id=${id}`,
    method: 'get'
  })
}
// 10.adminCode详情
export function adminCode(id) {
  return request({
    url: `${url}/getAdminCodes?id=${id}`,
    method: 'get'
  })
}
// 11.getFcstEle详情
export function getFcstEle(id) {
  return request({
    url: `${url}/getFcstEle?id=${id}`,
    method: 'get'
  })
}
// 11.赋值时间触发事件
export function getTimeSted(obj) {
  return request({
    url: `${url}/getTimeSted?id=${obj.id}&time=${obj.time}`,
    method: 'get'
  })
}
// 生成并执行URL
export function postInitUrl(obj) {
  return request({
    // url: `${url}/createurl`,
    url: `${url}/createurl`,
    method: 'post',
    data: obj
  })
}
// 生成脚本
export function postTheScript(obj) {
  return request({
    // url: `${url}/script`,
    url: `${url}/script`,
    method: 'post',
    data: obj
  })
}
// const url = '/interfacedata'
// // api 下载
export function uploadApiData(url1) {
  return request({
    url: `${url}/uploadApiData?url=${url1}`,
    method: 'get'
  })
}
export function getAllElements() {
  return request({
    url: '/interfacedata/getAllElements',
    method: 'get'
  })
}


