<template>
  <div class="forgot-password">
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      :rules="rules"
      label-width="160px"
      class="demo-ruleForm"
    >
      <el-form-item label="请输入用户名" prop="username">
        <el-input v-model="ruleForm.username" />
      </el-form-item>
      <!-- <el-form-item v-if="step === 1" label="请输入验证码" prop="code">
        <el-input v-model="ruleForm.code" />
      </el-form-item>-->
      <el-form-item label="旧密码" prop="password">
        <el-input v-model="ruleForm.password" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="新密码" prop="checkPass">
        <el-input v-model="ruleForm.checkPass" type="password" autocomplete="off" />
      </el-form-item>
      <br>
      <el-form-item>
        <!-- <el-button type="primary" @click="submitForm('ruleForm')">{{ step === 1?'提交':'获取验证码' }}</el-button> -->
        <el-button @click="updatePasswordForm('ruleForm')">修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { findPasswordMail, updatePassword } from '@/api/user.js'

export default {
  name: 'ForgotPassword',
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      step: 0,
      ruleForm: {
        username: '',
        password: '',
        checkPass: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { required: true, message: '请再次输入密码', trigger: 'blur' }
          // { validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          findPasswordMail(this.ruleForm.username)
            .then(() => {
              this.$message({
                message: `已发送至您注册邮箱，请查收`,
                type: 'success'
              })
              this.step = 1
            })
            .catch(() => {
              this.$message({
                message: `发送失败`,
                type: 'error'
              })
            })
        } else {
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    updatePasswordForm(formName) {
      const MD5 = require('md5')
      this.$refs[formName].validate(valid => {
        if (valid) {
          updatePassword({
            username: this.ruleForm.username,
            oldpassword: MD5(this.ruleForm.password),
            password: MD5(this.ruleForm.checkPass)
          })
            .then(res => {
              this.$message({
                message: res.message,
                type: 'success'
              })
              if (res.message === '密码修改成功！') {
                this.$emit('sendMessage')
                this.$refs[formName].resetFields()
              }
            })
            .catch(() => {})
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style scoped  lang="scss">
.forgot-password {
  width: 100%;
  padding: 20px 40px;
  background-color: #ffffff;
}
</style>
<style>
.forgot-password .el-form-item {
  width: 100%;
}
</style>

