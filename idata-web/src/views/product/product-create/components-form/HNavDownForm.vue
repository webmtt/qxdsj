<template>
  <div class="components-form-data">
    <!-- <div
      v-if="componentActive.title"
      class="compontent-header"
    >
      <el-input
        v-model="title"
        placeholder="请输入内容的标题"
        class="input-with-select"
      />
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
          <el-form-item label="制作单位">
            <el-input
              v-model="formData.unitVal"
              placeholder="请输入制作单位,中间逗号隔开"
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
          <el-tabs v-model="activeName">
            <el-tab-pane
              v-for=" (item, index) in dataList"
              :key="index"
              :label="item.title"
              :name="item.id"
            >
              <div
                v-if="item.children && item.children.length"
                class="citem-content"
              >
                <span
                  v-for="(citem, cindex) in item.children"
                  :key="cindex"
                >{{ citem.PRODUCTNAME || citem.TYPENAME }}</span>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CrearteNavData from '../navDate'
import { getTreeData } from '../createApi.js'
import NavLine from '../nav-line/NavDown.vue'
import HSetHeaderStyle from './HSetHeaderStyle.vue'
import PromptDetail from './PromptDetail.vue'
export default {
  name: 'HNavDownForm',
  components: {
    NavLine,
    HSetHeaderStyle,
    PromptDetail
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
      activeName: '0',
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
      metaData: {
        pageNum: 1,
        pageSize: 20,
        totalNum: 0
      }, // 数据分页
      searchObj: null,
      navList: [] // 导航数据
    }
  },
  async mounted() {
    // const arr = await new CrearteNavData().getProductsTree()
    // const index = arr.findIndex(item => item.title === "产品子类")
    // index > -1 && arr.splice(index, 1)
    // const index1 = arr.findIndex(item => item.title === "业务门类")
    // index1 > -1 && arr.splice(index1, 1)
    // this.defaultList = arr
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
        params: this.searchObj,
        formData: this.formData,
        style: this.style ? { ...this.style } : null,
        list: this.dataList
      })
    },
    // 导航数据
    navSelected(data) {
      this.navList = data
    },
    searchFormData() {
      if (!this.formData['unitVal']) {
        this.$message({
          message: '请输入查询条件',
          type: 'warning'
        })
        return
      }
      const obj = {
        product: this.formData.unitVal
      }
      this.searchObj = obj
      getTreeData(obj).then(res => {
        if (res.data && res.data) {
          this.dataList = []
          Object.keys(res.data).forEach((key, index) => {
            if (key === 'product') return
            if (index === 0) {
              this.activeName = index + 'dfasd'
            }
            this.dataList.push({
              id: index + 'dfasd',
              title: key,
              children: res.data[key]
            })
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
      width: 100%;
      .data-list {
        width: 100%;
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        & > div {
          width: 100%;
        }
        .data-list-item {
          width: 100%;
          min-height: 50px;
        }
        .citem-content {
          width: 100%;
          span {
            padding: 4px 6px;
            margin: 10px;
          }
        }
      }
    }
  }
}
</style>
