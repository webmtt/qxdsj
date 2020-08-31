import request from '@/utils/request'

const url = '/data'

export function getNavList(data) {
  return request({
    url: `${url}/getTree`,
    method: 'get',
    params: data
  })
}
// 3)一级菜单点击返回JSON数据：
// http://localhost:8085/data/firInfo?id=2
export function getfirInfoList(id) {
  return request({
    url: `${url}/firInfo?id=${id}`,
    method: 'get'
  })
}

// 4)二级节点返回JSON数据格式：/
// http://localhost:8085/data/secInfo?pageNum=1&id=201

export function getSecInfoList(obj) {
  return request({
    url: `${url}/secInfo?pageNum=${obj.page}&id=${obj.id}`,
    method: 'get'
  })
}

// http://localhost:8081/data/detail?dataCode=UPAR_ARD_G_MUT_AMD
//

export function getDetail(data) {
  return request({
    url: `${url}/detail?dataCode=${data}`,
    method: 'get'
  })
}
