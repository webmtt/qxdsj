<template>
  <div class="login-page">
    <div class="login-flag">Hello!</div>
    <div class="login-title">
      欢迎您
      <span>登录</span>数据共享子系统
    </div>
    <div>
      <el-form
        ref="ruleForm"
        :model="ruleForm"
        status-icon
        :rules="rules"
        label-width="auto"
        label-position="top"
      >
        <el-form-item label="账号" prop="usernamne">
          <el-input v-model="ruleForm.usernamne" autocomplete="off" placeholder="请输入您的账号" />
        </el-form-item>
        <el-form-item label="密码" prop="pass">
          <el-input
            v-model="ruleForm.pass"
            type="password"
            autocomplete="off"
            placeholder="请输入您的密码"
          />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="ruleForm.checked">记住密码</el-checkbox>
          <span class="forgot-pass" @click="handleforgotPassword">忘记密码</span>
          <span class="forgot-pass" style="margin-right:10px;" @click="handleChangePassword">修改密码</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="register-info">
      还没有账号
      <span @click="toRegister">立即注册</span>？
    </div>
    <el-dialog
      class="form-el-dialog form-el-dialog-add"
      title="修改密码"
      :visible.sync="dialogVisible"
      width="1000px"
    >
      <forgot-password @sendMessage="sendMessage" />
    </el-dialog>
  </div>
</template>

<script>
import ForgotPassword from './ForgotPassword.vue'

import { getUserinfo, setUserinfo, removeUserinfo } from '@/utils/auth'
// import { validUsername } from '@/utils/validate'
// import SocialSign from './components/SocialSignin'

import { userLogin, findPasswordMail } from '@/api/user.js'
export default {
  name: 'Login',
  components: { ForgotPassword },
  data() {
    const validataUsernamne = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入账号'))
      } else {
        callback()
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    return {
      dialogVisible: false,
      ruleForm: {
        usernamne: '',
        pass: '',
        checked: true
      },
      rules: {
        pass: [{ validator: validatePass, trigger: 'blur' }],
        usernamne: [{ validator: validataUsernamne, trigger: 'blur' }]
      }
    }
  },

  created() {
    const userInfo = getUserinfo() ? JSON.parse(getUserinfo()) : null
    if (userInfo) {
      this.ruleForm.usernamne = userInfo.userName || null
      this.ruleForm.pass = userInfo.pass || null
      if (this.ruleForm.usernamne && this.ruleForm.pass) {
        this.toLogin(this.ruleForm)
      }
    }
  },
  mounted() {
    // if (this.loginForm.username === '') {
    //   this.$refs.username.focus()
    // } else if (this.loginForm.password === '') {
    //   this.$refs.password.focus()
    // }
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    sendMessage() {
      this.dialogVisible = false
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.toLogin(this.ruleForm)
        } else {
          this.$message({
            showClose: true,
            message: '请补全信息',
            type: 'warning'
          })
          return false
        }
      })
    },
    toLogin(obj) {
      const MD5 = require('md5')
      const postObj = {
        username: obj.usernamne,
        password: MD5(obj.pass)
      }
      userLogin(postObj).then(res => {
        if (res.data && res.data.flag) {
          // 存入 Cookies
          if (this.ruleForm.checked) {
            setUserinfo({
              ...res.data.userinfo,
              pass: this.ruleForm.pass,
              productId: res.data.userproduct ? res.data.userproduct.id : null,
              productName: res.data.userproduct
                ? res.data.userproduct.prodname
                : null
            })
          } else {
            removeUserinfo()
          }
          this.$store.dispatch('user/userInfo', {
            ...res.data.userinfo,
            pass: this.ruleForm.pass,
            productId: res.data.userproduct ? res.data.userproduct.id : null,
            productName: res.data.userproduct
              ? res.data.userproduct.prodname
              : null
          })
          this.$message({
            showClose: true,
            message: '登陆成功',
            type: 'success'
          })
          this.$router.push({ path: `/home` })
        } else {
          this.$message({
            showClose: true,
            message: res.data.message || res.message,
            type: 'warning'
          })
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    toRegister() {
      this.$router.push({ path: `/login`, query: { type: 'register' }})
    },
    // 忘记密码
    handleforgotPassword() {
      if (!this.ruleForm.usernamne) {
        this.$message({
          message: `请输入账号`,
          type: 'warning'
        })
      } else {
        findPasswordMail(this.ruleForm.usernamne).then(res => {
          this.$message({
            message: res.data,
            type: 'success'
          })
          this.step = 1
        }).catch(() => {
        })
      }
      // this.dialogVisible = true
    },
    // 修改密码
    handleChangePassword() {
      this.dialogVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  width: 370px;
  height: 100%;
  background-color: #ffffff;
  padding: 30px;
  position: relative;

  .login-flag {
    margin-top: 20px;
    color: #0e1c37;
    font-size: 30px;
  }
  .login-title {
    margin-top: 10px;
    color: #d0d0d0;
    font-size: 14px;
    span {
      color: #196bd5;
    }
  }
  .register-info {
    position: absolute;
    bottom: 20px;
    font-size: 14px;
    color: #a4abb3;
    span {
      cursor: pointer;
      color: #196bd5;
    }
  }
  .forgot-pass {
    float: right;
    cursor: pointer;
    color: #9da1a4;
    &:hover {
      color: #1890ff;
    }
  }
}
</style>
<style>
.login-page .el-form--label-top .el-form-item__label {
  padding: 0;
  color: #9da1a4;
}
.login-page .el-form-item {
  margin-bottom: 10px;
  color: #9da1a4;
}

.login-page .el-checkbox__label {
  color: #9da1a4;
}
</style>

