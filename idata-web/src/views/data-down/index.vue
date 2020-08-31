<template>
  <div class="special-product-setail">
    <div class="special-product-setail-content">
      <div class="special-nav">
        <side-bar @select="navSelected" />
      </div>
      <div class="special-detail-content">
        <div class="special-header">
          <div>
            <span class="header-title-line" />
            <span class="header-title-text">数据产品下载服务</span>
            <span class="header-sub-title">数据下载， 便携只管</span>
          </div>
        </div>
        <div class="special-body">
          <template v-if="activeType === 'nav'">
            <default-info :data-list="dataList" />

          </template>
          <template v-else>
            <dialog-detail v-if="detailData" :detail-data="detailData" />
            <!-- <datadown-detail :detail-list="detailList" />
            <div class="page-box">
              <pagination
                v-show="metaData.total > 0"
                style="margin-top: 20px;"
                :total="metaData.total"
                :page.sync="metaData.page"
                :limit="metaData.size"
                @pagination="paginationChange"
              />
            </div> -->
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import { getfirInfoList, getSecInfoList,  getDetail } from '@/api/dataDown.js'
import SideBar from './components/SideBar.vue'
import DialogDetail from './components/DialogDetail.vue'
import DefaultInfo from './components/DefaultInfo.vue'
import datadownDetail from './components/datadownDetail.vue'
export default {
  name: 'DataDown',
  components: { SideBar, DefaultInfo, datadownDetail, DialogDetail },
  data() {
    return {
      detailData: null, // 详情
      activeType: 'nav',
      dataList: [],
      detailListDetail: {},
      detailList: [],
      id: null,
      metaData: {
        page: 1,
        size: 5,
        total: 0
      }
    }
  },
  created() {
  },
  methods: {
    navSelected(obj) {
      this.activeId = obj.data.id
      this.activeType = obj.type
      if (obj.type === 'nav') {
        getfirInfoList(obj.data.id).then(res => {
          this.dataList = res.data.list.map(item => {
            return {
              id: item.categoryid + '',
              title: item.chnname
            }
          })
        }).catch(() => {
          this.dataList = []
        })
      } else if (obj.type === 'navChildren') {
        this.id = obj.data.id
        this.getData(this.id)
      }
    },
    
    getData(id) {
       getDetail(id).then(res => {
         this.detailData  = null
        if (res.data && res.data.dataDef) {
          this.detailData = res.data.dataDef || null
        } else {
          this.$message({
            message: '暂无数据',
            type: 'success'
          })
        }
      }).catch(() => {
        this.$message({
          message: '暂无数据',
          type: 'success'
        })
      })
    },
    // 分页
    // paginationChange(p) {
    //   this.metaData.page = p.page
    //   this.getData()
    // }
  }
}
</script>

<style scoped  lang="scss">
.special-product-setail {
  width: 100%;
  height: auto;
  min-height: calc(100vh - 100px);
  background-color: #fbfaff;
  .special-product-setail-content {
    min-width: 1300px;
    max-width: 1920px;
    min-height: calc(100vh - 100px);
    display: flex;
    justify-content: flex-start;
    .special-nav {
      width: 20%;
      min-height: calc(100vh - 100px);
      background-color: #31394b;
      overflow: hidden;
    }
    .special-detail-content {
      width: 85%;
      height: auto;
      padding: 0 30px;
      .special-header {
        width: 100%;
        height: 70px;
        line-height: 70px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #bfbfbf;
        .header-title-line {
          display: inline-block;
          position: relative;
          top: 3px;
          width: 4px;
          height: 20px;
          background-color: #24d2f0;
          margin-right: 20px;
        }
        .header-title-text {
          color: #333;
          font-size: 20px;
          letter-spacing: 2px;
        }
        .header-sub-title {
          margin-left: 40px;
          margin-top: 4px;
          color: #666;
          font-size: 16px;
          letter-spacing: 2px;
        }
      }
      .special-body {
        width: 100%;
        min-height: 300px;
        background-color: #ffffff;
      }
    }
  }
  .page-box {
    width: 100%;
    margin: 0 auto 30px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    .pagination-container {
      padding: 0;
      border: 1px solid #f2f2f2;
      margin-top: 0 !important;
    }
  }
}
</style>
