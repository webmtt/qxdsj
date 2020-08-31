<template>
  <div class="server-products-list">
    <div class="hot-content">
      <div class="hot-content-header">
        <span>文件资源交换详情</span>
        <!-- <span>FILE RESOURCE EXCHANGE DETAILS</span> -->
      </div>
      <div class="fileResource">
        <el-form class="dataFilter">
          <el-form-item style="float: right;">
            <el-date-picker
              v-model="startDate"
              value-format="yyyy-MM-dd"
              style="width: 140px;"
              type="date"
              placeholder="选择日期"
            />至
            <el-date-picker
              v-model="endDate"
              value-format="yyyy-MM-dd"
              style="width: 140px;"
              type="date"
              placeholder="选择日期"
              @change="handleChangePicker"
            />
            <el-button @click="getData">查询</el-button>
          </el-form-item>
        </el-form>
        <div id="main1" style="height:450px;" />
      </div>
    </div>
  </div>
</template>

<script>
import echarts from "echarts";
import { fileResourceDetails } from "@/api/northCloud.js";

import moment from "moment";
export default {
  data() {
    return {
      chart: null,
      option: "",
      startDate: moment()
        .subtract(7, "days")
        .format("YYYY-MM-DD"),
      endDate: moment().format("YYYY-MM-DD"),
      gainDataList: "",
      pushDataList: "",
      dateList: ""
    };
  },
  created() {},
  mounted() {
    this.getData();
  },
  methods: {
    handleChangePicker() {},
    initEcharts() {
      this.$nextTick(() => {
        this.chart = echarts.init(document.getElementById("main1"));
        this.option = {
          // title: {
          //   text: '目录累计访问量'
          // },
          tooltip: {
            trigger: "axis"
          },
          legend: {
            data: [
              {
                name: "接收量",
                icon: "circle"
              },
              {
                name: "推送量",
                icon: "circle"
              }
            ],
            x: "right",
            padding: [10, 50, 0, 50]
          },
          grid: {
            left: "3%",
            right: "4%",
            bottom: "6%",
            top: "10%"
          },
          toolbox: {
            feature: {}
          },
          xAxis: {
            type: "category",
            boundaryGap: false,
            data: this.dateList
          },
          yAxis: {
            type: "value"
          },
          series: [
            {
              name: "接收量",
              type: "line",
              symbol: "circle",
              symbolSize: 14, // 设定实心点的大小
              stack: "总量",
              data: this.gainDataList,
              itemStyle: {
                // 设置折线折点颜色
                normal: {
                  color: "#FFBC01",
                  lineStyle: {
                    // 设置折线线条颜色
                    color: "#FFBC01"
                  }
                }
              }
            },
            {
              name: "推送量",
              type: "line",
              stack: "总量",
              symbol: "circle",
              symbolSize: 14, // 设定实心点的大小
              data: this.pushDataList,
              itemStyle: {
                normal: {
                  color: "#16B8BE",
                  lineStyle: {
                    color: "#16B8BE"
                  }
                }
              }
            }
          ]
        };
        this.chart.setOption(this.option);
      });
    },
    getData() {
      fileResourceDetails({
        startDate: this.startDate,
        endDate: this.endDate
      }).then(res => {
        if (res.data) {
          this.pushDataList = res.data.pushDataList;
          this.gainDataList = res.data.gainDataList;
          this.dateList = res.data.dateList;
          this.initEcharts();
        }
      });
    }
  }
};
</script>

<style scoped  lang="scss">
.dataFilter .el-button {
  background: #16b8be;
  border: 1px solid #16b8be;
  border-color: #16b8be;
  color: #fff;
}
.dataFilter {
  position: relative;
}
.dataFilter .el-form-item.el-form-item--medium {
  position: absolute;
  top: -5px;
  left: 35px;
  z-index: 2000;
}
.fileResource {
  margin: 0 20px;
  border: 1px solid #dcdcdc;
  padding: 21px;
  border-radius: 5px;
}
.server-products-list {
  width: 100%;
  // padding-bottom: 100px;
  background-color: #ffffff;
  .hot-content {
    width: 1300px;
    margin: 0 auto;
    .hot-content-header {
      width: 100%;
      height: 100px;
      display: flex;
      justify-content: center;
      align-items: center;
      & > span:first-child {
        font-size: 20px;
        color: #666;
        // margin-top: 67px;
        font-weight: bolder;
        // padding-bottom: 15px;
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
      & > span:nth-child(2) {
        font-size: 20px;
        color: #666;
      }
      & > span:nth-child(3) {
        margin-top: 25px;
        font-size: 16px;
        color: #666;
      }
    }
    .hot-contnet-box {
      width: 100%;
      display: flex;
      margin-top: 50px;
      justify-content: space-between;
      .service-list {
        width: 390px;
        height: 200px;
        border: 1px solid #dadde6;
        border-radius: 4px;
        padding: 20px;
        display: flex;
        align-items: flex-start;
        img {
          width: 32px;
          height: 32px;
        }
        & > div {
          flex: 1;
        }
        .hot-inner {
          padding: 0 20px;
          .hot-title {
            font-size: 14px;
            color: #202020;
            // font-weight: bold;
            padding: 15px 0;
          }
          .hot-item-box {
            display: flex;
            display: -webkit-flex;
            justify-content: flex-start;
            flex-direction: row;
            flex-wrap: wrap;
            height: 90px;
            overflow: hidden;
            font-size: 14px;
            span {
              width: 33%;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              margin: 6px 0;
            }
          }
          .service-btn {
            text-align: right;
          }
        }
      }
    }
  }
}
</style>

