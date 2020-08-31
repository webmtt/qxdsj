// import Cookies from 'js-cookie'
import Cookies from 'js-cookie'

const UserInfo = 'DSJ-NM-USER'

const getExpires = () => {
  return new Date(new Date().getTime() + 20 * 60 * 1000)
}
export function getUserinfo() {
  return Cookies.get(UserInfo)
}

export function setUserinfo(data) {
  return Cookies.set(UserInfo, JSON.stringify(data), { expires: getExpires() })
}

export function removeUserinfo() {
  return Cookies.remove(UserInfo)
}
