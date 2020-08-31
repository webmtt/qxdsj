<template>
  <div class="side-bar compontent-box product-border">
    <div v-if="title" class="h-carousel-title" :style="headerStyle">
      <i class="el-icon-s-grid" />
      <span>{{ title }}</span>
      <!-- <span v-if="false" class="show-more">更多</span> -->
    </div>
    <div ref="textListRef" class="compontent-body">
      <div class="side-bar-nav">
        <div v-for="item in navData" :key="item.id">
          <div
            class="bar-header"
            :class="{'active-bar-header':activeheader === item.id}"
            @click="selectHeader(item)"
          >
            <span>{{ item.title }}</span>
            <i class="el-icon-arrow-down" />
          </div>
          <div
            v-if="item.children && item.children.length"
            class="bar-children"
            :style="{ height: isOpen && activeheader === item.id ? item.height: 0}"
          >
            <div
              v-for="innerItem in item.children"
              :ref="item.id+'ref'"
              :key="innerItem.id"
              class="bar-children-item"
              @click="selectChildren(innerItem)"
            >
              <div
                :style="{color: activeChild === innerItem.id ? '#28cce9':'#ffffff'}"
              >{{ innerItem.title }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="side-bar-content">
        <div v-if="activeObj.pTitle" class="list-content-header">{{ activeObj.pTitle }}</div>
        <div v-if="dataListContent.length" class="list-content">
          <div
            v-for="(item, index) in dataListContent"
            :key="index"
            @click="dataDown(item)"
          >{{ item.fileshowname }}</div>
        </div>
        <div v-else class="no-data">暂无数据</div>
        <div class="text-list-page">
          <pagination-mini
            v-show="metaData.totalNum > 0"
            :total="metaData.totalNum"
            :page.sync="metaData.pageNum"
            :limit.sync="metaData.pageSize"
            @pagination="paginationChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import FileDownload from '@/utils/fileDownload'
import { getTreeData, getAlltypsFile } from '../createApi.js'
export default {
  name: 'HNavDown',
  props: {
    valueData: {
      required: false,
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      initID: 1,
      title: null,
      headerStyle: null,
      activeheader: '',
      activeChild: '',
      activeObj: {},
      navData: [],
      dataList: [],
      defaultList: [
        {
          id: '1',
          title: '这里欢迎您',
          icon: '',
          children: []
        },
        {
          id: '2',
          title: '这里欢迎您',
          icon: '',
          children: []
        }
      ],
      dataListContent: [],
      isOpen: false,
      metaData: {
        pageNum: 1,
        pageSize: 10,
        totalNum: 0
      },
      product: null
    }
  },
  watch: {
    valueData: {
      handler(newVal, oldVal) {
        if (this.valueData.length && this.valueData[0].params) {
          this.title = this.valueData[0].formData.title
          this.product = this.valueData[0].formData.unitVal
          this.headerStyle = this.valueData[0].style
          this.getList()
        }
      },
      deep: true
    }
  },

  mounted() {
    if (this.valueData.length && this.valueData[0].params) {
      this.title = this.valueData[0].title
      this.headerStyle = this.valueData[0].style
      // 计算高度
      const oh = this.$refs['textListRef'].offsetHeight
      const h = parseInt(oh) - 90 - 40
      this.metaData.pageSize = parseInt(h / 40)
      this.getList()
    } else {
      this.navData = this.defaultList
    }
  },
  methods: {
    getList() {
      getTreeData({ ...this.valueData[0].params }).then(res => {
        this.dataList = []
        Object.keys(res.data).forEach((key, index) => {
          if (key === 'product') return
          this.dataList.push({
            id: this.initID++,
            title: key,
            height: res.data[key].length ? res.data[key].length * 50 + 'px' : 0,
            children: res.data[key].length
              ? res.data[key].map((item, cindex) => {
                return {
                  id: this.initID++,
                  title: item.TYPENAME || item.PRODUCTNAME,
                  pTitle: key,
                  ...item
                }
              })
              : []
          })
        })
        if (this.dataList.length) {
          this.navData = this.dataList
          this.activeheader = this.navData[0].id
          if (this.navData[0].children && this.navData[0].children.length) {
            this.activeObj = this.navData[0].children[0]
            this.activeChild = this.activeObj.id
            this.isOpen = true
            this.getListData()
          }
        } else {
          this.navData = this.defaultList
        }
      })
    },
    selectHeader(data) {
      this.isOpen = this.activeheader === data.id ? !this.isOpen : true
      this.activeheader = data.id
    },
    selectChildren(data) {
      this.activeChild = data.id
      this.activeObj = data
      this.getListData()
    },
    getListData() {
      const obj = {}
      Object.keys(this.activeObj).forEach(key => {
        if (key === 'TYPENAME') {
          obj['typscode'] = this.activeObj['TYPECODE']
        }
        if (key === 'PRODUCTNAME') {
          obj['prodctcode'] = this.activeObj['PRODUCTCODE']
        }
      })
      getAlltypsFile({ ...obj, ...this.metaData }).then(res => {
        if (res.data && res.data.list) {
          this.dataListContent = res.data.list || []
          this.metaData.pageNum = res.data.pageNum
          this.metaData.totalNum = res.data.total
        }
      })
    },
    dataDown(data) {
      new FileDownload().zipDownload(data.url)
    },
    paginationChange(d) {
      this.metaData.pageNum = d.page
      this.getListData()
    },
    handleEmit(obj) {
      this.$emit('select', obj)
    }
  }
}
</script>

<style scoped  lang="scss">
@import "../components-header.scss";
@media screen and (max-width: 600px) {
  .side-bar-nav {
    width: 120px;
  }
}

.side-bar {
  width: 100%;
  height: 100%;
  margin-bottom: 20px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  .compontent-body {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: flex-start;
    overflow: hidden;
    .side-bar-nav {
      width: 220px;
      background-color: #31394b;
      overflow-y: auto;
      @media screen and (min-width: 310px) and (max-width: 600px) {
        & {
          width: 120px;
        }
      }

      &::-webkit-scrollbar {
        width: 0px;
      }
      .bar-header {
        width: 100%;
        height: 50px;
        display: flex;
        align-items: center;
        color: #ffffff;
        border-bottom: 1px solid #1a1f32;
        position: relative;
        cursor: pointer;
        span {
          margin-left: 15px;
        }
        i {
          color: #ffffff;
          position: absolute;
          right: 10px;
        }
        &:hover {
          background-image: url("../../../../assets/images/sidebarbg.png");
          background-origin: content-box;
          background-position: center;
          background-repeat: no-repeat;
          background-clip: content-box;
          background-size: cover;
          &::after {
            position: absolute;
            content: "";
            top: 0;
            left: 0;
            width: 4px;
            height: 100%;
            background-color: #28cce9;
          }
        }
      }
      .active-bar-header {
        background-image: url("../../../../assets/images/sidebarbg.png");
        background-origin: content-box;
        background-position: center;
        background-repeat: no-repeat;
        background-clip: content-box;
        background-size: cover;
        &::after {
          position: absolute;
          content: "";
          top: 0;
          left: 0;
          width: 4px;
          height: 100%;
          background-color: #28cce9;
        }
      }
      .bar-children {
        width: 100%;
        transition: all 0.3s;
        height: 0;
        overflow: hidden;
        .bar-children-item {
          height: 50px;
          line-height: 50px;
          color: #ffffff;
          padding-left: 20px;
          font-size: 14px;
          border-bottom: 1px solid #1a1f32;
          cursor: pointer;
          & > div {
            width: 100%;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
      }
    }
    .side-bar-content {
      flex: 1;
      padding: 0 20px;
      position: relative;
      .list-content-header {
        width: 100%;
        height: 50px;
        color: #333;
        font-size: 16px;
        line-height: 50px;
        border-bottom: 1px solid #f2f2f2;
      }
      .list-content {
        width: 100%;
        & > div {
          width: 100%;
          height: 40px;
          line-height: 40px;
          font-size: 14px;
          color: #999;
          position: relative;
          padding-left: 20px;
          cursor: pointer;
          &::after {
            content: "";
            position: absolute;
            width: 4px;
            height: 4px;
            border-radius: 50%;
            background-color: #ccc;
            top: 17px;
            left: 0;
          }
        }
      }
      .text-list-page {
        position: absolute;
        right: 0;
        bottom: 0;
      }
    }
  }
  .no-data {
    width: 100%;
    text-align: center;
    padding: 30px 0;
    color: #999;
    font-size: 30px;
  }
}
</style>

