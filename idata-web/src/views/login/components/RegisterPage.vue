<template>
  <div class="register-page">
    <div class="register-title">
      欢迎您
      <span>注册</span>
    </div>
    <div>
      <el-form
        ref="ruleForm"
        :model="ruleForm"
        status-icon
        :rules="rules"
        label-width="auto"
        label-position="right"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="姓名" prop="chName">
          <el-input v-model="ruleForm.chName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="ruleForm.password" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input v-model="ruleForm.checkPass" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="工作单位" prop="orgName">
          <el-input v-model="ruleForm.orgName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="手机" prop="mobile">
          <el-input v-model="ruleForm.mobile" autocomplete="off" />
        </el-form-item>
        <el-form-item label="办公电话" prop="phone">
          <el-input v-model="ruleForm.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="微信号" prop="wechatNumber">
          <el-input v-model="ruleForm.wechatNumber" autocomplete="off" />
        </el-form-item>
        <!-- <el-form-item label="邮箱" prop="emailName">
          <el-input v-model="ruleForm.emailName" autocomplete="off" />
        </el-form-item>-->
        <el-form-item label="权限服务" prop="dataInfo" style="width: 100%;margin-bottom:0px;">
          <el-input v-model="ruleForm.dataInfo" type="textarea" :rows="2" placeholder="请输入内容" />
        </el-form-item>

        <el-form-item label="服务条款" style="width: 100%;margin-bottom:0px;">
          <span class="empower-btn" @click="hanldeEmpower">
            <i class="el-icon-s-grid" />
          </span>
          <el-checkbox v-model="ruleForm.checked" class="role-name">我以看过并同意《气象业务内网服务条款》</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button :disabled="!ruleForm.checked" type="primary" @click="submitForm('ruleForm')">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="register-info">
      已有账号
      <span @click="toLogin">立即登录</span>？
    </div>
    <el-dialog
      class="form-el-dialog form-el-dialog-add"
      title="服务条款"
      :visible.sync="dialogVisible"
      width="1200px"
      top="5vh"
    >
      <register-empower />
    </el-dialog>
  </div>
</template>
<script>
import { addUser, checkUserName } from '@/api/user.js'
import RegisterEmpower from './RegisterEmpower.vue'

export default {
  name: 'RegisterPage',
  components: { RegisterEmpower },
  data() {
    var validatename = async(rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        const data = await checkUserName(value)
        if (data.code === '200' && data.data) {
          callback()
        } else {
          callback(new Error(data.message || '用户名不可用'))
        }
      }
    }
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
      dialogVisible: false, // 授权弹出框显示控制
      dialogRoleVisible: false, // 选择角色弹出框显示控制
      roleLists: [], // 角色的列表
      ruleForm: {
        username: '',
        chName: '',
        password: '',
        checkPass: '',
        orgName: '',
        phone: '',
        mobile: '',
        wechatNumber: '',
        // emailName: '',
        dataInfo: '',
        checked: false
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名称', trigger: 'blur' },
          { validator: validatename, trigger: 'blur' }
        ],
        chName: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { min: 2, max: 8, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validatePass2, trigger: 'blur' }
        ],
        orgName: [
          { required: true, message: '请输入工作单位', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: false, message: '请输入办公室电话', trigger: 'blur' }
        ],
        mobile: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { min: 11, max: 11, message: '长度只能为11位', trigger: 'blur' }
        ],
        wechatNumber: [
          { required: false, message: '请输入微信号', trigger: 'blur' }
        ],
        // emailName: [
        //   { required: true, message: '请输入邮箱号', trigger: 'blur' },
        //   {
        //     type: 'email',
        //     message: '请输入正确的邮箱地址',
        //     trigger: ['blur', 'change']
        //   }
        // ],
        dataInfo: [
          { required: true, message: '请输入数据内容', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {},
  methods: {
    // 打开用户选择的对话框
    hanldeRole() {
      if (this.roleLists.length) {
        this.dialogRoleVisible = true
      }
    },
    // 获取授权
    hanldeEmpower() {
      this.dialogVisible = true
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          const MD5 = require('md5')
          const postObj = {
            username: this.ruleForm.username,
            password: MD5(this.ruleForm.password),
            chName: this.ruleForm.chName,
            orgName: this.ruleForm.orgName,
            phone: this.ruleForm.phone,
            mobile: this.ruleForm.mobile,
            wechatNumber: this.ruleForm.wechatNumber,
            // emailName: this.ruleForm.emailName,
            dataInfo: this.ruleForm.dataInfo
          }
          addUser(postObj).then(res => {
            if (res.data && res.data.flag) {
              this.$message({
                showClose: true,
                message: '注册成功，审核通过后，电话通知！',
                type: 'success'
              })
              this.$router.push({ path: `/home` })
              // this.$confirm('审核通过后，电话通知！', res.data.message, {
              //   confirmButtonText: '确定',
              //   cancelButtonText: '取消',
              //   type: 'warning'
              // })
              //   .then(() => {
              //     this.$router.push({ path: `/home` })
              //   })
              //   .catch(() => {})
              //   .catch(() => {
              //     this.$message({
              //       showClose: true,
              //       message: '申请失败',
              //       type: 'error'
              //     })
              //   })
            } else {
              this.$message({
                showClose: true,
                message: res.data.message,
                type: 'success'
              })
            }
          })
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
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    toLogin() {
      this.$router.push({ path: `/login` })
    }
  }
}
</script>

<style scoped  lang="scss">
.register-page {
  width: 100%;
  height: 100%;
  min-width: 500px;
  max-width: 820px;
  background-color: #ffffff;
  padding: 30px;
  position: relative;
  .register-title {
    color: #999999;
    font-size: 16px;
    letter-spacing: 1px;
    margin-bottom: 10px;
    span {
      font-weight: bolder;
      font-size: 18px;
      color: #196bd5;
      position: relative;
      top: -1px;
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
  .empower-btn {
    display: inline-block;
    width: 30px;
    height: 30px;
    border: 1px solid #f2f2f2;
    font-size: 20px;
    text-align: center;
    line-height: 30px;
    cursor: pointer;
    border-radius: 4px;
    overflow: hidden;
    &:hover {
      background-color: #1890ff;
      color: #ffffff;
    }
  }
  .role-name {
    position: relative;
    top: 3px;
    display: inline-block;
    // width: 168px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .role-list {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    // span {
    //   display: inline-block;
    //   width: 150px;
    //   height: 40px;
    //   color: #999;
    //   margin: 0 10px 10px 0;
    //   text-align: center;
    //   line-height: 40px;
    //   background-color: #fff;
    //   border: 1px solid #f2f2f2;
    //   border-radius: 4px;
    //   cursor: pointer;
    //   overflow: hidden;
    //   text-overflow: ellipsis;
    //   white-space: nowrap;
    //   &:hover {
    //     color: #1890ff;
    //     font-weight: bold;
    //     border-color: #1890ff;
    //   }
    // }
  }
}
</style>
<style>
.demo-ruleForm {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
}
.demo-ruleForm > div {
  width: 48%;
}
.demo-ruleForm > div:nth-child(1n) {
  margin-right: 2%;
}
.register-page .el-form-item {
  margin-bottom: 15px;
  color: #9da1a4;
}
.register-page .el-form-item__label {
  color: #9da1a4;
}
.role-list label {
  display: inline-block;
  width: 150px;
  height: 40px;
  color: #999;
  margin: 0 10px 10px 0;
  /* text-align: center; */
  /* line-height: 40px; */
  background-color: #fff;
  border: 1px solid #f2f2f2;
  border-radius: 4px;
  cursor: pointer;
  line-height: 20px;
}
</style>
