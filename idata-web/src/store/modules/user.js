
import { getUserinfo, removeUserinfo } from '@/utils/auth'

const userInfo = getUserinfo() ? JSON.parse(getUserinfo()) : null
const state = {
  name: userInfo ? userInfo.userName : null,
  pass: userInfo ? userInfo.pass : null,
  chName: userInfo ? userInfo.chName : null,
  pId: userInfo ? userInfo.productId : null,
  pName: userInfo ? userInfo.productName : null,
  iD: userInfo ? userInfo.iD : null
}

const mutations = {
  SET_USERINFO: (state, obj) => {
    state.name = obj.userName
    state.pass = obj.pass
    state.chName = obj.chName
    state.pId = obj.productId
    state.pName = obj.productName
    state.iD = obj.iD
  },
  REMOVE_USERINFO: () => {
    state.name = null
    state.pass = null
    state.chName = null
    state.pId = null
    state.pName = null
    state.iD = null
    removeUserinfo()
  }
}

const actions = {
  userInfo({ commit }, userInfo) {
    if (userInfo) {
      commit('SET_USERINFO', userInfo)
    }
  },
  logout({ commit, state }) {
    commit('REMOVE_USERINFO')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
