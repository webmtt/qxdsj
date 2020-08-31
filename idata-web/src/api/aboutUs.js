import request from '@/utils/request'

const url = '/aboutus'

export function getdetail() {
  return request({
    url: `${url}/findList`,
    method: 'get'
  })
}
