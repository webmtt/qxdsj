<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-info">
        <div class="login-info-title">
          <img :src="loginImgurl" alt="" srcset="">
          <span>内蒙古气象</span>
        </div>
        <div class="login-title">气象大数据行业共享服务</div>
        <!-- <div class="login-sub-title">(面向行业用户共享服务)</div> -->
        <!-- <div class="login-info-list">
          <router-link to="/specialProduct" tag="span">热门产品</router-link>
          <router-link to="/dataDown" tag="span">数据下载</router-link>
          <router-link to="/industryApplication" tag="span">行业服务</router-link>
          <router-link to="/api" tag="span">API服务</router-link>
          <router-link to="/api" tag="span">最近接口</router-link>
        </div> -->
      </div>
      <div>
        <component :is="loginRole" />
      </div>
    </div>
    <div class="go-back-btn">
       <router-link to="/home" tag="i" class="el-icon-monitor" />
       <!-- <i class="el-icon-monitor"></i> -->
    </div>

    

  </div>
</template>

<script>
import LoginPage from './components/LoginPage.vue'
import RegisterPage from './components/RegisterPage.vue'

export default {
  name: 'Login',
  components: { LoginPage, RegisterPage },
  data() {
    return {
      loginRole: 'LoginPage',
      loginImgurl: require('@/assets/images/logoBig.png')
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        this.loginRole = query.type ? 'RegisterPage' : 'LoginPage'
      },
      immediate: true
    }
  },
  mounted() {
    this.loginRole = this.$route.query.type ? 'RegisterPage' : 'LoginPage'
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {}
}
</script>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  max-width: 1920px;
  margin: 0 auto;
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  .go-back-btn {
    position: absolute;
    top: 40px;
    right: 40px;
    font-size: 40px;
    color: white;
    cursor: pointer;
  }
  .login-box {
    min-width: 690px;
    max-width: 1130px;
    min-height: 500px;
    margin: 0 auto;
    display: flex;
    justify-content: center;
    align-self: center;
    transition: all 0.3s;
  }
  .login-info {
    padding: 40px;
    min-width: 480px;
    color: #ffffff;
    transition: all 0.3s;
    background: linear-gradient(to bottom, #121f40, #101d3d, #0c1429);
    .login-info-title {
      height: 44px;
      line-height: 44px;
      display: flex;
      align-items: center;
      img {
        width: 48px;
        height: 44px;
      }
    }
    .login-title {
      margin-top:30px;
      font-size: 30px;
    }
    .login-sub-title{
      margin-top:20px;
      font-size: 20px;
    }
    .login-info-list{
      margin-top:30px;
      font-size: 14px;
      // color: #6999ff;
      span {
        padding-right: 20px;
        position: relative;
        cursor: pointer;
      }
      &>span::before {
        content: '';
        position: absolute;
        top: 4px;
        right: 6px;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background-color: #fff;
      }
      &>span:last-child:before {
        content: '';
        position: absolute;
        top: 4px;
        right: 6px;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background-color: transparent;
      }
    }

  }
}
</style>
