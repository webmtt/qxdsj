<template>
  <div class="prompt-detail">
    <el-collapse>
      <el-collapse-item
        title="输入提示信息展示"
        name="1"
      >
        <div>
          <el-tabs
            v-model="tabPaneVal"
            type="border-card"
            @tab-click="handleTabClick"
            style="font-size:12px"
          >
            <el-tab-pane label="业务门类">
            </el-tab-pane>
            <el-tab-pane label="制作单位">
            </el-tab-pane>
            <el-tab-pane label="产品子类">
            </el-tab-pane>
            <el-form label-width="0px">
              <el-form-item label="">
                <el-input
                  placeholder="模糊查询"
                  prefix-icon="el-icon-search"
                  v-model="searchInputVal"
                  @input="handleInputValChange"
                >
                </el-input>
              </el-form-item>
            </el-form>

            <div
              v-if="tabPaneVal !== '2' "
              class="tab-pane-box"
            >
              <span
                v-for="(item, index) in dataList"
                :key="index"
              > {{ item.name }} （{{item.code}}）</span>
            </div>
            <div v-if="tabPaneVal === '2'">
              <el-table
                :data="dataList"
                border
                style="width: 100%"
              >
                <el-table-column
                  v-for="(item, index) in tableMeta"
                  :key="index"
                  align="center"
                  :prop="item.key"
                  :label="item.label"
                  :width="item.width"
                />
              </el-table>

            </div>
            <el-pagination
              style="text-align: right;"
              v-if="total > 0"
              background
              :page-size="pageSize"
              layout="prev, pager, next"
              :total="total"
              @current-change="handlePageChange"
            >
            </el-pagination>
          </el-tabs>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script>

import { getFactor } from '../createApi.js'
import PromptDetailData from './promptDetail.js'
export default {
  name: 'PromptDetail',
  data() {
    return {
      tabPaneVal: '0',
      typeList: [], // 业务门类
      unitList: [],  // 制作单位
      productList: [], // 产品子类
      page: 1,
      pageSize: 100,
      total: 0,
      dataList: [],
      tableMeta: [
        { key: 'PRODUCTSUBCODE', label: '产品编码', width: 200 },
        { key: 'name', label: '产品名称', width: 200 },
        { key: 'TOKENSNAME', label: '产品标识', width: 120 },
        { key: 'UNITNAME', label: '制作单位', width: 100 },
        { key: 'TYPENAME', label: '业务门类', width: 100 },
        { key: 'CONTENTNAME', label: '产品内容', width: 100 },
        { key: 'COVERAREANAME', label: '覆盖区域', width: 150 },
        { key: 'UPDATED', label: '创建时间', width: 100 },
        { key: 'PUBLISHFREQ', label: '发布频次', width: 80 },
        { key: 'fileext', label: '产品状态', width: 'auto' }
      ],
      searchInputVal: null
    }
  },
  created() {
    this.tabPaneVal = '0';
    ({ types: this.typeList, units: this.unitList, products: this.productList } = new PromptDetailData().getPromptData())
    if (!this.typeList.length || !this.unitList.length || !this.productList.length) {
      getFactor().then(res => {
        if (res.data) {
          this.typeList = res.data['typeList'].map((item, index) => {
            return {
              id: index,
              name: item.ENUMNAME,
              code: item.ENUMCODE
            }
          })
          this.unitList = res.data['unitList'].map(item => {
            return {
              id: item.ENUMID,
              name: item.ENUMNAME, // 产品名称
              code: item.ENUMCODE,
            }
          })
          this.productList = res.data['productList'].map((item, index) => {
            return {
              id: index,
              name: item.PRODUCTNAME,
              code: item.PRODUCTCODE,
              PRODUCTSUBCODE: item.PRODUCTSUBCODE, // 产品编码
              TOKENSNAME: item.TOKENSNAME, // 产品标识
              UNITNAME: item.UNITNAME, // 制作单位
              TYPENAME: item.TYPENAME, // 业务门类
              CONTENTNAME: item.CONTENTNAME, // 产品内容
              COVERAREANAME: item.COVERAREANAME, // 覆盖区域
              UPDATED: item.UPDATED, // 创建时间
              PUBLISHFREQ: item.PUBLISHFREQ, // 发布频次
            }
          })
          new PromptDetailData().setPromptData(this.typeList, this.unitList, this.productList)
        }
        this.handleTabClick('0')
      })
    } else {
      this.handleTabClick('0')
    }
  },
  methods: {
    handleTabClick() {
      this.pageSize = 100
      this.searchInputVal = null
      if (this.tabPaneVal === '0') {
        if (this.typeList.length > this.pageSize) {
          this.setPage(this.typeList.length, this.page, this.typeList.slice((this.page - 1) * this.pageSize, (this.page) * this.pageSize))
        } else {

          this.setPage(0, 1, this.typeList)
        }
      } else if (this.tabPaneVal === '1') {

        if (this.unitList.length > this.pageSize) {
          this.setPage(this.unitList.length, this.page, this.unitList.slice((this.page - 1) * this.pageSize, (this.page) * this.pageSize))
        } else {
          this.setPage(0, 1, this.unitList)
        }
      } else if (this.tabPaneVal === '2') {
        this.pageSize = 25
        if (this.productList.length > this.pageSize) {
          this.setPage(this.productList.length, this.page, this.productList.slice((this.page - 1) * this.pageSize, (this.page) * this.pageSize))
        } else {
          this.setPage(0, 1, this.productList)
        }
      }
    },
    setPage(t, p, d) {
      this.total = t
      this.page = p
      this.dataList = d
    },
    handlePageChange(p) {
      this.page = p
      this.handleTabClick()
    },
    /*
    * 模糊查询
    */
    handleInputValChange() {
      if (!this.searchInputVal) {
        this.handleTabClick()
      } else {
        let arr = [];
        if (this.tabPaneVal === '0') {
          arr = this.typeList
        } else if (this.tabPaneVal === '1') {
          arr = this.unitList
        } else if (this.tabPaneVal === '2') {
          arr = this.productList
        }
        const filterArr = arr.filter(item => item.name.indexOf(this.searchInputVal) >= 0)
        this.setPage(0, 1, filterArr)
      }
    }
  }
}
</script>
<style>
.prompt-detail .el-collapse-item__header {
  font-weight: bold;
  color: #333;
  font-size: 14px;
}
.prompt-detail .tab-pane-box > span {
  display: inline-block;
  margin: 4px 10px;
}
/* .demo-form-inline .el-form-item__content {
  display: flex;
}
.prompt-detail {
} */
</style>
