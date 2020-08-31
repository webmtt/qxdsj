import request from '@/utils/request'

const url = '/industry'

export function getList() {
  return request({
    url: `${url}/findList`,
    method: 'get'
  })
}

export function getFindexample(id) {
  return request({
    url: `${url}/findexample?id=${id}`,
    method: 'get'
  })
}
