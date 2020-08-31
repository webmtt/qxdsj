<template>
  <div class="app-header">
    <div class="app-header-content">
      <div class="logo-title">
        <img :src="loginImgurl" alt="" srcset="">
        <span>气象大数据行业共享服务</span>
      </div>
      <div v-if="!username" class="content-right">
        <span @click="toLogin">登录</span>
        <span @click="toRegister">注册</span>
      </div>
      <div v-else class="content-right">
        <el-dropdown :trigger="'click'" type="warning" @command="loginOut">
          <span>{{ username }}</span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="out">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script>

import { existLogin } from '@/api/user.js'

// import { getUserinfo } from '@/utils/auth'
export default {
  name: 'HeaderPage',
  data() {
    return {
      loginImgurl: require('@/assets/images/logo.png')
    }
  },
  computed: {
    username() {
      return this.$store.state.user.chName || null
    }
  },
  methods: {
    toLogin() {
      this.$router.push({ path: `/login` })
    },
    toRegister() {
      this.$router.push({ path: `/login`, query: { type: 'register' }})
    },
    loginOut() {
      existLogin().then(res => {
        if (res.data && res.data.flag) {
          this.$store.dispatch('user/logout')
          if (this.$route.path === '/home') {
            location.reload()
          } else {
            this.$router.push({ path: `/home` })
          }
        }
      })
    }
  }
}
</script>

<style scoped  lang="scss">
.app-header {
  width: 100%;
  height: 50px;
  background-color: #242a37;
  border-bottom: 1px solid #3c4559;
  .app-header-content {
    width: 1300px;
    height: 100%;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .logo-title {
      height: 31px;
      line-height: 31px;
      display: flex;
      align-items: center;
      color: #ffffff;
      font-size: 18px;
      font-weight: bolder;
      img {
        width: 34px;
        height: 31px;
        margin-right: 10px;
      }
    }
    .content-right {
      // height:100%;
      color: #ffffff;
      span {
        padding-left: 10px;
        font-size: 14px;
        color: #ffffff;
        cursor: pointer;
        &:hover {
          color: rgb(13, 149, 255);
        }
      }
    }
  }
}
</style>
<style>
.content-right .el-dropdown {
  color: #ffffff;
}
</style>

