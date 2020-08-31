<template>
  <div class="add-compontents">
    <!-- <div class="add-compontents-header">
      <div class="header-left">
        <div class="create-symbol" @click="addNewCompontent">+</div>
      </div>
      <div class="header-right">
        <el-button type="warning" size="mini" >预览</el-button>
        <el-button type="success" size="mini" >完成</el-button>
      </div>
    </div> -->
    <div class="add-control">
      <div title="添加" @click="addNewCompontent">
        <i class="el-icon-circle-plus-outline" />
      </div>
      <div title="预览" @click="handlePreview">
        <i class="el-icon-view" />
      </div>
      <div title="保存" @click="handleSave">
        <i class="el-icon-circle-check" />
      </div>
    </div>
    <el-dialog class="form-el-dialog form-el-dialog-add" title="组件库" :visible.sync="dialogVisible" width="1000px">
      <components-list v-if="dialogVisible" @selectedComponent="handleComponentDialog" />
    </el-dialog>
    <div v-if="dialogPreviewVisible " class="icon-close" @click="handleColse">
      <i class="el-dialog__close el-icon el-icon-close" />
    </div>
    <el-dialog v-if="dialogPreviewVisible" class="form-el-dialog form-el-dialog-preview" :fullscreen="true" top="0" :modal="true" :destroy-on-close="false" :visible.sync="dialogPreviewVisible" width="1200px">
      <preview-compontent :dialog-visible.sync="dialogPreviewVisible" :components-list="componentsList" />
    </el-dialog>
  </div>
</template>

<script>
// import ToolLibrary from '@/utils/toolLibrary.js'
import { insertProducts, updateProducts } from '@/api/productLibrary.js'
import { getServerUrl } from '@/api/user.js'
import ComponentsList from './ComponentsList.vue'
import PreviewCompontent from './PreviewCompontent.vue'

export default {
  name: 'AddComponents',
  components: {
    ComponentsList,
    PreviewCompontent
  },
  // inject: ['componentsList'],
  props: {
    componentsList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      username: null,
      productId: null,
      baseUrl: null,
      dialogVisible: false,
      dialogPreviewVisible: false
    }
  },
  created() {
    // this.username = this.$route.query.username || null
    this.productId = this.$route.query.id || null
    getServerUrl().then(res => {
      if (res.code === '200') {
        this.baseUrl = res.data.replace('pic', '')
      }
    })
  },
  methods: {
    // 完成
    handleSave() {
      if (!this.productId) {
        this.$prompt('请输入产品名称', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^[\s\S]*.*[^\s][\s\S]*$/,
          inputErrorMessage: '产品名称不能为空'
        }).then(({ value }) => {
          const url = 'http://172.18.112.51:8085'
          // const url = ToolLibrary.h_randomString(8) + new Date().getTime()
          const obj = {
            product: JSON.stringify(this.componentsList),
            // username: this.username,
            url: `${url}/#/product/detail/`,
            prodname: value
          }
          // 新增
          insertProducts(obj).then(res => {
            if (res.code === '200') {
              this.$message({
                type: 'success',
                message: res.message
              })
            } else {
              this.$message({
                type: 'info',
                message: res.message
              })
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '创建失败'
          })
        })
      } else {
        const obj = {
          id: this.productId,
          product: JSON.stringify(this.componentsList)
        }
        updateProducts(obj).then(res => {
          if (res.code === '200') {
            this.$message({
              type: 'success',
              message: res.message
            })
          } else {
            this.$message({
              type: 'info',
              message: '编辑失败'
            })
          }
        })
      }
    },
    // 预览
    handlePreview() {
      if (this.componentsList.length) {
        this.dialogPreviewVisible = true
      } else {
        this.$message({
          showClose: true,
          message: '没有页面可以预览'
        })
      }
    },
    // 增加组件
    addNewCompontent() {
      this.dialogVisible = true
    },
    // 获取选择的组件
    handleComponentDialog(item) {
      this.dialogVisible = false
      this.$emit('handleGetComponent', item)
    },
    // 关闭
    handleColse() {
      this.dialogPreviewVisible = false
    }
  }
}
</script>

<style scoped lang="scss">
.add-compontents {
  width: 100%;
  padding-top: 10px;
  position: relative;
  // height: 60px;
  .add-compontents-header {
    height: 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    .header-left {
      justify-content: flex-start;
      display: flex;
      align-items: center;
    }
    .header-right {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
  }
  .add-control {
    position: fixed;
    z-index: 1000;
    right: 100px;
    width: 60px;
    height: 300px;
    top: 50%;
    transform:translateY(-50%) ;
    // border:1px solid red;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    &>div {
      width: 50px;
      height: 50px;
      border-radius: 80px;
      border:2px solid transparent;
      background-color:#f2f2f2 ;
      display: flex;
      justify-content: center;
      align-items: center;
      margin: 10px 0;
      cursor: pointer;
      color: #000;
      opacity: 0.6;
      box-shadow: 0px 4px 8px rgba(0,0,0,0.8);
      i {
        font-size: 30px;
      }
      &:hover {
        background-color: #196bd5;
        color: #ffffff;
        opacity: 1;
      }
    }
  }
  .create-symbol {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: blueviolet;
    text-align: center;
    line-height: 40px;
    font-size: 30px;
    color: #fff;
    cursor: pointer;
  }
  .icon-close {
    position: fixed;
    z-index: 3000;
    top: 50%;
    right: 0;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    text-align: center;
    line-height: 40px;
    background-color: salmon;
    color: #ffffff;
    cursor: pointer;
  }
}
</style>
<style>
.form-el-dialog-preview .el-dialog__header {
  display: none;
}
.form-el-dialog-preview .el-dialog__body {
  padding: 0;
  background-color: #f2f2f2;
}
.form-el-dialog-add .el-dialog__body {
  padding: 20px;
}
</style>

