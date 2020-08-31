<template>
  <div class="hot-product">
    <div class="hot-content">
      <div class="hot-content-header">
        <span>热门产品</span>
      </div>
      <div class="hot-contnet-box">
        <div class="box-title">热门产品种类TOP10</div>
        <div class="box-tab-time">
          <div class="box-tab">
            <img
              :src="quyuImgurl"
              alt
            >
            <span @click="handleDialogClick">{{ unitSelected ? unitSelected.ENUMNAME: '区域' }}</span>
            <div>
              <span
                v-for="item in dataTypeList"
                :key="item.id"
                :class="{'active-datatype':item.id === dataType}"
                @click="handleDatatype(item)"
              >{{ item.value }}</span>
            </div>
          </div>
          <div>
            <el-date-picker
              v-model="datePacker"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              @change="handleChange"
            />
          </div>
        </div>

        <div class="hot-echart">
          <div
            ref="hotEchart"
            class="echart-box"
          />
          <div class="echart-info">
            <div class="info-title">详细信息</div>
            <div class="info-list">
              <div>
                <span>类型编码：</span>
                <span>{{ detailData ? detailData.TOKENSCODE : '-' }}</span>
              </div>
              <div>
                <span>类型名称</span>
                <span>{{ detailData ? detailData.unitname : '-' }}</span>
              </div>
              <div>
                <span>定义时间</span>
                <span>{{ detailData ? detailData.created : '-' }}</span>
              </div>
              <div>
                <span>发布单位</span>
                <span>{{ detailData ? detailData.unit : '-' }}</span>
              </div>
              <div>
                <span>发布数量</span>
                <span>{{ detailData ? detailData.counts : '-' }}</span>
              </div>
              <div>
                <span>总阅读量</span>
                <span>{{ detailData ? detailData.num : '-' }}</span>
              </div>
              <div>
                <span>平均阅读量</span>
                <span>{{ detailData ? detailData.mean : '-' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      class="form-el-dialog form-el-dialog-add"
      top="5vh"
      :close-on-click-modal="false"
      title="区域名称"
      :visible.sync="dialogVisible"
      width="1200px"
    >
      <div>
        <div
          v-for="item in AllUnitData"
          :key="item.title"
          class="select-line"
        >
          <label>{{ item.title }}:</label>
          <div class="dialog-list">
            <span
              v-for="itemInter in item.list"
              :key="itemInter.ENUMNAME"
              :title="itemInter.ENUMNAME"
              @click="getSelectUnit(itemInter)"
            >{{ itemInter.ENUMNAME }}</span>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import echarts from 'echarts'
import {
  getAllUnit,
  getUnitSum,
  getUnitSumForTimes,
  getUnitCount
} from '@/api/productLibrary.js'
import { HotOption } from './optionsComfig'

import moment from 'moment'

export default {
  name: 'HotProducts',
  data() {
    return {
      datePacker: [moment().subtract(365, 'days').format('YYYY-MM-DD'), moment().format('YYYY-MM-DD')],
      dialogVisible: false,
      AllUnitData: [], // 有所制作单位
      unitSelected: null, // 选中的区域
      echartData: [], // 图标数据
      dataTypeList: [
        {
          id: '1',
          value: '24小时',
          timeList: [moment().subtract(1, 'days').format('YYYY-MM-DD'), moment().format('YYYY-MM-DD')]
        },
        {
          id: '2',
          value: '过去2天',
          timeList: [moment().subtract(2, 'days').format('YYYY-MM-DD'), moment().format('YYYY-MM-DD')]
        },
        {
          id: '3',
          value: '过去一周',
          timeList: [moment().subtract(7, 'days').format('YYYY-MM-DD'), moment().format('YYYY-MM-DD')]
        },
        {
          id: '4',
          value: '过去一月',
          timeList: [moment().subtract(30, 'days').format('YYYY-MM-DD'), moment().format('YYYY-MM-DD')]
        },
        {
          id: '5',
          value: '过去一年',
          timeList: [moment().subtract(365, 'days').format('YYYY-MM-DD'), moment().format('YYYY-MM-DD')]
        },
        {
          id: '6',
          value: '时间区间',
          timeList: [moment().subtract(1, 'days').format('YYYY-MM-DD'), moment().format('YYYY-MM-DD')]
        }
      ],
      dataType: '5',
      chartDom: null,
      detailData: null, // 产品详情
      quyuImgurl: require('@/assets/images/quyu.png')
    }
  },
  created() {
    // 获取有所制作单位
    this.getUnit()
  },
  mounted() { },
  methods: {
    // 时间选择器
    handleChange() {
      if (this.datePacker) {
        const obj = {
          startTime: this.datePacker[0],
          endTime: this.datePacker[1],
          unit: this.unitSelected.ENUMCODE || ''
        }
        getUnitSumForTimes(obj)
          .then(res => {
            this.echartInit(res)
          })
          .catch(() => {
            this.echartData = []
            this.echartInit()
          })
      }
      this.dataType = '6'
    },
    // 获取有所制作单位
    getUnit() {
      getAllUnit()
        .then(res => {
          if (res.data) {
            Object.keys(res.data).forEach(keys => {
              this.AllUnitData.push({
                title: keys,
                list: res.data[keys]
              })
            })
            // this.AllUnitData = res.data
            this.unitSelected = this.AllUnitData[0].list[0] || null
            this.getUnittypeNumber()
          }
        })
        .catch(() => {
          this.unitSelected = null
        })
    },
    // 选择制作单位
    getSelectUnit(data) {
      this.unitSelected = data || null
      this.dialogVisible = false
      // 获取产品种类
      this.getUnittypeNumber()
    },
    // 区域选中
    handleDialogClick() {
      if (this.AllUnitData.length) {
        this.dialogVisible = true
      }
    },
    // 获取类型
    handleDatatype(data) {
      if (data.id !== '6') {
        this.datePacker = data.timeList
        this.dataType = data.id
        // 获取产品种类
        this.getUnittypeNumber()
      } else {
        this.dataType = data.id

        this.datePacker = data.timeList
        const obj = {
          startTime: this.datePacker[0],
          endTime: this.datePacker[1],
          unit: this.unitSelected.ENUMCODE || ''
        }
        getUnitSumForTimes(obj)
          .then(res => {
            this.echartInit(res)
          })
          .catch(() => {
            this.echartData = []
            this.echartInit()
          })
      }
    },
    // 获取产品种类
    getUnittypeNumber() {
      const obj = {
        unit: this.unitSelected.ENUMCODE || '',
        dataType: this.dataType
      }
      getUnitSum(obj)
        .then(res => {
          this.echartInit(res)
        })
        .catch(() => {
          this.echartInit()
          this.detailData = null
        })
    },
    // 获取详情
    getDetailData(name) {
      const obj = {
        unit: this.unitSelected.ENUMCODE,
        productname: name
      }
      const data = this.echartData.find(item => item.unitname === name)
      getUnitCount(obj)
        .then(res => {

          if (res.data ) {
            this.detailData = res.data
            this.detailData = { ...this.detailData, ...data }
          } else {
            this.detailData = null
          }
        })
        .catch(() => {
          this.detailData = null
        })
    },
    echartInit(res) {
      this.chartDom = echarts.init(this.$refs.hotEchart)
      if (res && res.data && res.data.length) {
        this.echartData = res.data || []
        HotOption.xAxis[0].data = this.echartData.map(item => item.unitname)
        HotOption.series[0].data = this.echartData.map(item => item.num)
        this.chartDom.setOption(HotOption)
        this.getDetailData(HotOption.xAxis[0].data[0])
      } else {
        HotOption.xAxis[0].data = ['-', '-', '-', '-']
        HotOption.series[0].data = [0, 0, 0, 0]
        this.chartDom.setOption(HotOption)
      }
      const that = this
      this.chartDom.on('click', function(params) {
        if (params.value) {
          that.getDetailData(params.value)
        }
      })
    }
  }
}
</script>
<style>
.hot-product .el-date-editor--daterange.el-input,
.hot-product .el-date-editor--daterange.el-input__inner,
.hot-product .el-date-editor--timerange.el-input,
.hot-product .el-date-editor--timerange.el-input__inner {
  width: 260px;
}
</style>

<style scoped  lang="scss">
.hot-product {
  width: 100%;
  height: 810px;
  background-color: #f5f6fa;
  .hot-content {
    width: 1300px;
    margin: 0 auto;
    .hot-content-header {
      width: 100%;
      height: 100px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      & > span:first-child {
        color: #333;
        font-size: 24px;
        margin-top: 10px;
        font-weight: bolder;
        padding-bottom: 15px;
        position: relative;
        &::after {
          content: "";
          position: absolute;
          left: -22px;
          top: 2px;
          width: 4px;
          height: 24px;
          background-color: #24d2f0;
        }
      }
      // & > span:last-child {
      //   font-size: 20px;
      //   color: #666;
      // }
    }
    .hot-contnet-box {
      width: 100%;
      height: 650px;
      // display: flex;
      // justify-content: space-between;
      border-radius: 6px;
      border: 1px solid #e3e3e5;
      padding: 0 40px;
      .box-title {
        height: 70px;
        line-height: 70px;
        color: #333;
        font-size: 22px;
      }
      .box-tab-time {
        height: 100px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .box-tab {
          display: flex;
          justify-content: flex-start;
          align-items: center;
          img {
            width: 30px;
            height: 30px;
            margin-right: 10px;
          }
          & > span {
            font-size: 16px;
            color: #f7b84f;
            margin-right: 50px;
            cursor: pointer;
          }
          & > div {
            span {
              margin: 0 30px;
              font-size: 14px;
              color: #666666;
              font-weight: bolder;
              cursor: pointer;
              position: relative;

              &:hover {
                color: #3d7dea;
              }
              &:hover::after {
                position: absolute;
                content: "";
                width: 45px;
                height: 2px;
                background-color: #3d7dea;
                top: 25px;
                left: 50%;
                transform: translateX(-50%);
              }
            }
            .active-datatype {
              color: #3d7dea;
              &::after {
                position: absolute;
                content: "";
                width: 45px;
                height: 2px;
                background-color: #3d7dea;
                top: 25px;
                left: 50%;
                transform: translateX(-50%);
              }
            }
          }
        }
      }
      .hot-echart {
        width: 100%;
        height: 422px;
        display: flex;
        justify-content: space-between;
        .echart-box {
          width: 960px;
          height: 422px;
        }
        .echart-info {
          height: 100%;
          flex: 1;
          .info-title {
            font-size: 18px;
            color: #333;
            padding-left: 13px;
            position: relative;
            margin-top: 10px;
            &::after {
              position: absolute;
              top: 0;
              left: 0;
              content: "";
              width: 3px;
              height: 18px;
              background-color: #0099cc;
            }
          }
          .info-list {
            margin-top: 20px;
            width: 272px;
            height: 280px;
            border: 1px solid #e5e5e5;
            div {
              padding: 0 10px;
              height: 40px;
              display: flex;
              justify-content: space-between;
              align-items: center;
              font-size: 14px;
              color: #333;
              span {
                // flex: 1;
                overflow: hidden;
              }
              & > span:first-child {
                width: 80px;
                text-align: left;
              }
              & > span:last-child {
                flex: 1;
                text-align: right;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
            }
            & > div:nth-child(odd) {
              background-color: #f4f8f9;
            }
          }
        }
      }
    }
  }
  .select-line {
    display: flex;
    justify-self: start;
    align-items: center;
    margin: 10px 0;
    border-bottom: 1px solid #f2f2f2;
    & > label {
      display: inline-block;
      height: 100%;
      width: 100px;
    }
    .dialog-list {
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
      flex: 1;
      span {
        display: inline-block;
        width: 160px;
        cursor: pointer;
        columns: #999;
        border: 1px dashed transparent;
        padding: 10px;
        text-align: center;
        border-radius: 4px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        &:hover {
          color: #fff;
          background-color: #24d2f0;
          border: 1px dashed #f2f2f2;
        }
      }
    }
  }
}
</style>
