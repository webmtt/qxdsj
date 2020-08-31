<template>
  <div class="components-form-data">
    <el-collapse v-model="activeNames">
      <el-collapse-item
        title="根据条件查询内容"
        name='0'
      >
        <el-form label-width="80px">
          <el-form-item
            v-if="componentActive.title"
            label="标题名称"
          >
            <el-input
              v-model="formData.title"
              placeholder="请输入内容的标题"
            ></el-input>
          </el-form-item>
          <el-form-item label="业务门类">
            <el-input
              v-model="formData.typeVal"
              placeholder="请输入业务门类,中间逗号隔开"
            ></el-input>
          </el-form-item>
          <el-form-item label="制作单位">
            <el-input
              v-model="formData.unitVal"
              placeholder="请输入制作单位,中间逗号隔开"
            ></el-input>
          </el-form-item>
          <el-form-item label="产品子类">
            <el-input
              v-model="formData.productVal"
              placeholder="请输入产品子类,中间逗号隔开"
            ></el-input>
          </el-form-item>
          <el-form-item label="图片格式">
            <el-select
              v-model="formData.imgType"
              multiple
              collapse-tags
              style="width:100%"
              placeholder="请选择"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="图片数量">
            <el-input
              v-model="formData.imgNUm"
              placeholder="请输入图片个数"
            />
          </el-form-item>
          <el-form-item label="">
            <el-button
              type="primary"
              @click="searchFormData"
            >搜索</el-button>
            <el-button
              :disabled='!dataList.length'
              type="success"
              @click="getFormData"
            >保存数据</el-button>
          </el-form-item>
        </el-form>
      </el-collapse-item>
    </el-collapse>
    <div v-if="componentActive.title">
      <h-set-header-style @handleChangeStyle="handleChangeStyle" />
    </div>
    <div>
      <prompt-detail />
    </div>
    <div class="data-content">
      <div class="data-page">
        <div class="data-list">
          <div
            v-for="item in dataList"
            :key="item.id"
            class="data-list-item-type-1"
          >
            <img
              :src="item.url"
              alt
            >
            <span>{{ item.fileshowname }}</span>
          </div>
        </div>
      </div>
    </div>
    <div
      v-if="dataList.length"
      style="padding: 0 0 20px 0;text-align: right"
    >
      <pagination
        v-show="metaData.totalNum > 0"
        style="margin-top: 20px;"
        :total="metaData.totalNum"
        :page.sync="metaData.pageNum"
        :limit.sync="metaData.pageSize"
        @pagination="paginationChange"
      />
    </div>
  </div>
</template>

<script>
import { getMenuPhoto } from '../createApi.js'

import HSetHeaderStyle from './HSetHeaderStyle.vue'
import PromptDetail from './PromptDetail.vue'
export default {
  name: 'HImgTextListForm',
  components: {
    PromptDetail,
    HSetHeaderStyle
  },
  props: {
    componentActive: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      activeNames: '0',
      options: [
        {
          value: 'jpg',
          label: 'jpg'
        },
        {
          value: 'png',
          label: 'png'
        },
        {
          value: 'gif',
          label: 'gif'
        },
        {
          value: 'svg',
          label: 'svg'
        },
        {
          value: 'psd',
          label: 'psd'
        }
      ],
      formData: {
        title: null,
        typeVal: null,
        productVal: null,
        unitVal: null,
        imgNUm: null,
        imgType: []
      },
      // title: null, // 标题
      style: null, // 标题
      dataList: [], // 数据列表
      // initId: 0,
      metaData: {
        pageNum: 1,
        pageSize: 20,
        totalNum: 0
      }, // 数据分页
      navList: [], // 导航数据
      // searchObj: {
      //   imgNUm: null,
      //   imgType: null
      // },
      paramsObj: {}
    }
  },
  mounted() {
    if (this.componentActive && this.componentActive.compontentData.length) {
      this.formData = this.componentActive.compontentData[0].formData
    }
  },
  methods: {
    // 样式设置
    handleChangeStyle(d) {
      this.style = d
    },
    // 获取点击数据
    getFormData() {
      this.$emit('getCompontentData', {
        title: this.title,
        params: this.paramsObj,
        formData: this.formData,
        style: this.style ? { ...this.style } : null,
        list: this.dataList
      })
    },
    // 分页
    paginationChange(d) {
      this.metaData.page = d.page
      this.searchFormData()
    },
    // 导航数据
    navSelected(data) {
      this.navList = data
    },
    searchFormData() {
      if (!this.formData['imgType'].length) {
        this.$message({
          message: '请选择图片格式',
          type: 'warning'
        })
        return
      }
      if (!this.formData['imgNUm']) {
        this.$message({
          message: '请输入轮播图片个数',
          type: 'warning'
        })
        return
      }
      if (!this.formData['typeVal'] && !this.formData['productVal'] && !this.formData['unitVal']) {
        this.$message({
          message: '请输入查询条件',
          type: 'warning'
        })
        return
      }

      const obj = {
        photoType: String(this.formData.imgType),
        unit: this.formData.unitVal,
        typs: this.formData.typeVal,
        product: this.formData.productVal,
        pageNum: this.metaData.pageNum,
        pageSize: this.metaData.pageSize
      }
      this.paramsObj = obj
      getMenuPhoto(obj).then(res => {
        if (res.data) {
          this.metaData.totalNum = res.data.total
          if (res.data.list && res.data.list.length) {
            this.dataList = res.data.list.map((item, index) => {
              return {
                filename: item.filename,
                fileshowname: item.fileshowname,
                url: item.url, // require(`@/assets/create/${index + 1}.jpg`), // item.url,
                id: item.id
              }
            })
          }
        } else {
          this.$message({
            message: '暂无数据',
            type: 'warning'
          })
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.components-form-data {
  width: 100%;
  padding: 20px;
  min-height: 90vh;
  .compontent-header {
    width: 100%;
    height: 60px;
    padding: 20px 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .compontent-title {
      font-size: 20px;
      color: #333;
    }
  }
  .compontent-header-search {
    // background-color: #f2f2f2;
    // height: 100px;
    border-radius: 6px;
    padding: 0 20px 0px;
    // justify-content: flex-start;
    // margin-bottom: 20px;
    & > div {
      display: flex;
      width: 500px;
      & > div {
        margin-right: 10px;
      }
      // padding: 0 20px;
      .search-label {
        padding: 0 0 15px 0;
        font-size: 14px;
        color: #333;
        font-weight: bold;
      }
    }
  }
  .search-content {
    transition: 0.3s;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
    margin-bottom: 20px;
  }
  .data-content {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    .side-bar-form {
      width: 200px;
    }
    .data-page {
      flex: 1;
      .data-list {
        width: 100%;
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        margin-bottom: 20px;
        .data-list-item-type-1 {
          width: 50%;
          height: 50px;
          margin: 0;
          // cursor: pointer;
          border: 2px dashed transparent;
          transition: all 0.3s;
          position: relative;
          display: flex;
          justify-content: flex-start;
          align-items: center;
          border-bottom: 1px solid #f2f2f2;
          // &:hover::after {
          //   content: '+';
          //   position: absolute;
          //   top: 0;
          //   left: 0;
          //   width: 100%;
          //   height: 100%;
          //   font-size: 60px;
          //   background-color: rgba(0, 0, 0, 0.1);
          //   text-align: center;
          //   line-height: 200px;
          //   // z-index: 3000;
          //   color: #f2f2f2;
          // }
          img {
            width: 50px;
            height: 40px;
            transition: all 0.3s;
          }
          span {
            flex: 1;
            padding-left: 15px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          &:hover {
            // border-color: salmon;
            // border-radius: 4px;
            img {
              // transform: scale(1.1,1.1);
            }
          }
        }
        & > .data-list-item-type-1:nth-child(4n) {
          margin-right: 0;
        }
      }
    }
  }
}
</style>
