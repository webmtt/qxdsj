<template>
  <div class="components-form-data">
    <!-- <div v-if="componentActive.title" class="compontent-header">
      <el-input v-model="title" placeholder="请输入内容的标题" class="input-with-select" />
    </div> -->
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
          <el-form-item label="">
            <el-button
              type="primary"
              @click="searchFormData"
            >搜索</el-button>
            <el-button
              :disabled='!dataList.length'
              type="success"
              @click="getFormData"
            >确定</el-button>
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
            {{ item.fileshowname }}
          </div>
        </div>
      </div>
    </div>
    <div
      v-if="dataList.length"
      style="padding: 0 0 0px 0;text-align: right"
    >
      <!-- <el-button type="success" @click="getFormData">确定</el-button> -->
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
import CrearteNavData from '../navDate'
import { getMoreVersion } from '../createApi.js'
import HSetHeaderStyle from './HSetHeaderStyle.vue'

import PromptDetail from './PromptDetail.vue'
import NavLine from '../nav-line/index.vue'
export default {
  name: 'HTextListForm',
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
      formData: {
        title: null,
        typeVal: null,
        productVal: null,
        unitVal: null,
        imgNUm: null,
        imgType: []
      },
      title: null, // 标题
      style: null, // 标题
      dataList: [], // 数据列表
      initId: 0,
      defaultList: [],
      searchObj: {},
      metaData: {
        pageNum: 1,
        pageSize: 10,
        totalNum: 0
      }, // 数据分页
      navList: [] // 导航数据
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
        params: this.searchObj,
        formData: this.formData,
        style: this.style ? { ...this.style } : null,
        list: []
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
      if (!this.formData['typeVal'] && !this.formData['productVal'] && !this.formData['unitVal']) {
        this.$message({
          message: '请输入查询条件',
          type: 'warning'
        })
        return
      }
      const obj = {
        unit: this.formData.unitVal,
        typs: this.formData.typeVal,
        product: this.formData.productVal
      }
      this.searchObj = obj
      getMoreVersion({
        ...obj,
        ...this.metaData
      }).then(res => {
        if (res.data && res.data.list && res.data.list.length) {
          this.metaData.page = parseInt(res.data.pageNum)
          this.metaData.totalNum = parseInt(res.data.total)
          this.dataList = res.data.list
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
    border-radius: 6px;
    padding: 0 20px 0px;
    & > div {
      display: flex;
      width: 500px;
      & > div {
        margin-right: 10px;
      }
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
        flex-direction: column;
        border: 1px solid #f2f2f2;
        .data-list-item-type-1 {
          width: 100%;
          height: 40px;
          line-height: 40px;
          border-bottom: 1px solid #f2f2f2;
          cursor: pointer;
          transition: all 0.3s;
          position: relative;
          padding: 0 20px;
        }
        & > .data-list-item-type-1:nth-child(2n + 1) {
          background-color: #f2f2f2;
        }
      }
    }
  }
}
</style>
