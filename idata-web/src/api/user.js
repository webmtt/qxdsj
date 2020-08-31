import request from '@/utils/request'
const url = '/userInfo'

export function getRoleList() {
  return request({
    url: `/dataRole/getDataRoleInfo`,
    method: 'get'
  })
}
// 注册
export function addUser(data) {
  const postdata = new FormData()
  Object.keys(data).forEach(key => {
    postdata.append(key, data[key])
  })
  return request({
    url: `${url}/addUser`,
    method: 'post',
    data: postdata
  })
}
// 用户登录
export function userLogin(data) {
  return request({
    url: `${url}/login`,
    method: 'get',
    params: data
  })
}
// 用户退出登录
export function existLogin() {
  return request({
    url: `${url}/existLogin`,
    method: 'get'
  })
}
// 忘记密码
export function findPasswordMail(name) {
  return request({
    url: `${url}/findPasswordMail?username=${name}`,
    method: 'get'
  })
}
// 修改密码
export function updatePassword(data) {
  return request({
    url: `${url}/updatePassword`,
    method: 'get',
    params: data
  })
}
// 用户名验证接口

export function checkUserName(name) {
  return request({
    url: `${url}/checkUserName?username=?${name}`,
    method: 'get'
  })
}

// 服务器地址

export function getServerUrl() {
  return request({
    url: '/subject/getPNGSourceUrl',
    method: 'get'
  })
}

export function insertDeskLog(data) {
  return request({
    url: '/desklog/insertDeskLog',
    method: 'post',
    data: data
  })
}
