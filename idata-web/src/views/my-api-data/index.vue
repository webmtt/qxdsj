<template>
  <div class="my-api-data">
    <div class="api-box">
      <div class="api-nav">
        <side-bar
          :nav-type-data="navTypeData"
          :nav-type-detail="navTypeDetail"
          :active-type-detail-id="activeTypeDetailId"
          :active-type-id="activeTypeId"
          @handleTypeList="handleTypeList"
          @handleTypeDetailList="handleTypeDetailList"
        />
      </div>
      <div class="api-content">
        <div class="special-header">
          <div>
            <span class="header-title-line" />
            <span class="header-title-text">API</span>
            <span class="header-sub-title">高效，及时，可靠，定制化服务</span>
          </div>
        </div>
        <div class="data-api-body">
          <div class="api-title">接口调用参数服务</div>
          <api-select-parameter
            :nav-type-data="navTypeData"
            :nav-type-detail="navTypeDetail"
            :api-list-data="apiListData"
            :active-type-id="activeTypeId"
            :active-type-detail-id="activeTypeDetailId"
            @handleTypeList="handleTypeList"
            @handleTypeDetailList="handleTypeDetailList"
            @handleChangeApi="handleChangeApi"
          />
          <div class="api-title">接口参数</div>
          <api-table
            :active-type-detail-id="activeTypeDetailId"
            :table-list="tableList"
            @handletableElementData="handletableElementData"
            @clearParamObjData="clearParamObjData"
          />
          <div class="select-body">
            <div>
              <span>返回类型</span>
              <el-select
                v-model="backTypeId"
                placeholder="请选择"
                @change="handleChangeSelect"
              >
                <el-option
                  v-for="item in backTypeData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </div>
          <div class="handle-cooper">
            <el-button
              type="warning"
              @click="handleUrlInit()"
            >生成并执行URL</el-button>
            <el-button
              type="success"
              @click="handleScriptInit()"
            >生成脚本</el-button>
            <el-button
              :disabled="!dataResult"
              type="primary"
              @click="handleDownFile()"
            >下载</el-button>
            <el-button
              type="danger"
              @click="handleShowChart()"
            >图表</el-button>
            <el-button
              type="success"
              @click="handleMap()"
            >地图</el-button>
          </div>
        </div>
        <div class="data-show-content">
          <div
            v-if="!dataScript"
            style="height: auto;
            min-height:100px;"
          >
            <span>返回url</span>
            <div>
              <div>
                {{ dataUrl }}
              </div>
            </div>
          </div>
          <div
            v-if="!dataScript"
            style="
            min-height: 300px;
            max-height: 500px;"
          >
            <span>返回结果</span>
            <div>
              <div
                v-if="backTypeId === 'HTML'"
                v-html="dataResult"
              ></div>
              <div v-else>{{dataResult }}</div>
            </div>
          </div>
          <div v-if="dataScript">
            <span>返回脚本</span>
            <div>
              <div>{{ dataScript }}</div>
            </div>
          </div>
        </div>
        <div
          v-if="showChart"
          style="width: 100%;height: 400px; margin-top:20px"
          ref="lineChart"
        ></div>
        <div
          v-if="showGis && gisData.length"
          style="width: 100%;height: 600px;"
          ref="gisMap"
        >
          <echarts-gis :data-list="gisData" />
        </div>
      </div>
    </div>

    <!-- my-api-data -->
  </div>
</template>
<script>
import echarts from "echarts";
import { LineOption } from "./apiConfig";
import FileDownload from "@/utils/fileDownload";
import SideBar from "./components/SideBar.vue";
import ApiSelectParameter from "./components/ApiSelectParameter.vue";
import ApiTable from "./components/ApiTable.vue";
import EchartsGis from "./components/EchartsGis.vue";
import { getUserinfo } from "@/utils/auth";
// 测试数据
// import data from './data'
import {
  getApiTypeList, // 资料类别
  getApiTypeDetailList, //  资料名称
  getApiDetail, // 请求接口
  getAllElements,  // 全部要素 
  postInitUrl, // 执行url
  postTheScript, // 执行脚本
  getEleForInter, // 请求接口要素列表
  getElements, // 要素列表
} from "@/api/dataApi.js";

export default {
  name: 'MyApiData',
  components: { SideBar, ApiSelectParameter, ApiTable, EchartsGis },
  data() {
    return {
      showChart: false, // 展示图标
      chartData: null, // 图表数据
      showGis: false, // 展示gis图
      gisData: null, // 数据
      apiId: null, // api 得 ID
      paramObj: null, // 请求参数对象
      activeTypeId: null, //资料类别
      activeTypeDetailId: null, // 资料名称
      navTypeData: null, // 资料类别数据
      navTypeDetail: null, // 资料名称数据
      apiListData: [], // 接口列表
      tableList: [], // 接口要素列表
      backTypeId: "", // 返回得数据类型
      backTypeData: [
        { value: "HTML", label: "HTML" },
        { value: "XML", label: "XML" },
        { value: "JSON", label: "JSON" },
        { value: "JSONP", label: "JSONP" },
        { value: "TEXT", label: "TEXT" },
        { value: "CSV", label: "CSV" },
        { value: "COMMATEXT", label: "COMMATEXT" },
        { value: "KML", label: "KML" },
        { value: "SPACETEXT", label: "SPACETEXT" }
      ], // 返回类型
      dataUrl: null, // 返回url
      dataResult: null, //  null, // 返回结果
      dataScript: null, // 返回得脚本
      tableElementData: null, // 要素列表
    }
  },
  created() {
    if (this.$route.query.parentId) {
      this.activeTypeId = this.$route.query.parentId;
      this.activeTypeDetailId = this.$route.query.id;
    }
    // 请求类型接口
    let userInfo = getUserinfo() ? JSON.parse(getUserinfo()) : null;
    // 如果没有登录，转home页
    if (!userInfo) this.$router.push({ path: `/home` })
    // 请求导航数据
    this.getNavTypeData(userInfo.iD)
  },
  methods: {
    // 请求导航数据
    getNavTypeData(id) {
      getApiTypeList(id)
        .then(res => {
          if (res.data && res.data.length) {
            this.navTypeData = res.data.map(item => {
              return {
                ...item,
                value: item.dataClassId,
                label: item.dataClassName,
                chName: `${item.dataClassId}(${item.dataClassName})`
              };
            });
            if (this.navTypeData.length) {
              this.activeTypeId = this.activeTypeId
                ? this.activeTypeId
                : this.navTypeData[0].dataClassId;
              this.getTypeDetail(this.activeTypeId);
            }
          }
        })
        .catch();
    },
    // 获取资料名称接口列表
    getTypeDetail(id) {
      this.navTypeDetail = [];
      // 获取用户的 userid
      let userInfo = getUserinfo() ? JSON.parse(getUserinfo()) : null;
      getApiTypeDetailList({
        userid: userInfo.iD,
        id: id
      }).then(res => {
        if (res.message === "查询为空") {
          this.$message({
            message: `该资料类别下暂无授权数据`,
            type: "warning"
          });
        }
        if (res.data && res.data.length) {
          this.navTypeDetail = res.data.map(item => {
            return {
              ...item,
              value: item.DATA_CODE,
              label: item.DATA_NAME,
              chName: `${item.DATA_CODE}(${item.DATA_NAME})`
            };
          });
          if (this.navTypeDetail.length) {
            this.activeTypeDetailId =
              this.$route.query.id || this.navTypeDetail[0].value;
            this.navSelectedDetail(this.activeTypeDetailId);
          }
        }
      });
    },
    // 切换资料类别
    handleTypeList(id) {
      this.activeTypeId = id;
      this.navTypeDetail = [];
      this.activeTypeDetailId = "";
      this.apiId = "";
      this.$router.push({ query: { parentId: this.activeTypeId } });
      // 获取资料名称接口列表
      this.getTypeDetail(this.activeTypeId);
      this.clearDataShow()
    },
    // 切换资料名称
    handleTypeDetailList(id) {
      this.activeTypeDetailId = id;
      this.$router.push({
        query: { id: this.activeTypeDetailId, parentId: this.activeTypeId }
      });
      this.navSelectedDetail(id);
      this.clearDataShow()
    },
    // 请求api接口
    navSelectedDetail(id) {
      // this.apiListData = [];
      getApiDetail(id).then(res => {
        if (res.data) {
          if (res.data.length && res.data[0]) {
            this.apiListData = res.data.map(item => {
              return {
                ...item,
                value: item.CUSTOM_API_ID,
                label: `${item.CUSTOM_API_ID}(${item.CUSTOM_API_NAME})`
              };
            });
          }
        }
      });
    },
    // 切换API, 请求接口要素详情
    handleChangeApi(id) {
      this.apiId = id
      getEleForInter(id).then(res => {
        this.tableList = res.data.map(item => {
          return { ...item, parameter: item.PARAM_ID !== 'dataCode' ? null : this.activeTypeDetailId }
        });
      });

      /* 测试
      // getAllElements().then(res => {
      // this.tableList = res.data.map(item => {
      //   return { ...item, parameter: item.PARAM_ID !== 'dataCode' ? null : this.activeTypeDetailId }
      // });
      // })
      */
    },
    // 切换返回类型
    handleChangeSelect() {
      this.clearParamObjData()
    },
    // 清空数据
    clearDataShow() {
      this.tableList = [];
    },
    clearParamObjData() {
      this.paramObj = null;
      this.dataUrl = null; // 返回url
      this.dataResult = null; // 返回结果
      this.dataScript = null; // 返回脚本
      this.showChart = false; // 隐藏echarts
      this.showGis = false; // 隐藏echarts
      this.chartData = null; // 图表数据
      this.gisData = null; // 数据
    },
    // 执行URL
    handleUrlInit() {
      const isPass = this.checkParamObj()
      if (!isPass) return
      postInitUrl(this.paramObj)
        .then(res => {
          const d = res
          this.dataUrl = d && d.data ? d.data.url : null
          this.dataResult = d && d.data ? d.data.urlData : null
        })
        .catch(e => {
          this.dataUrl = null
          this.dataResult = null
        });

      this.clearParamObjData()
    },
    // 执行脚本
    handleScriptInit() {
      if (!this.checkParamObj()) return
      postTheScript(this.paramObj)
        .then(res => {
          const d = res
          this.dataScript = d && d.data ? d.data : '暂无数据'
        })
        .catch(e => {
          this.dataUrl = '暂无数据'
        });

      this.clearParamObjData()
    },
    // 下载
    handleDownFile() {
      if (!this.checkParamObj()) return
      if (!this.dataResult) return
      const fileName =
        this.apiListData.find(item => item.value === this.apiId).label ||
        null;
      const timerlong = new Date().getTime();
      const downName = `${fileName}${timerlong}.${this.backTypeId}`;
      new FileDownload().fileBlodDownload(
        downName,
        this.dataResult
      );
    },
    // echarts图
    handleShowChart() {
      /* 测试
        // this.showChart = true
        // if (this.tableElementData) {
        //   this.showEchartsLineData(JSON.stringify(this.dataResult))
        // } else {
        //   getElements(this.activeTypeDetailId).then(res => {
        //     this.tableElementData = res.data;
        //     this.showEchartsLineData(JSON.stringify(this.dataResult))
        //   });
        // }
      */
      if (!this.checkParamObj()) return
      this.paramObj.dataFormat = 'JSON'
      this.showChart = true
      postInitUrl(this.paramObj).then(res => {
        if (res && res.data && res.data.urlData) {
          this.showEchartsLineData(res.data.urlData)
        }
      })
    },
    // 展示图标数据
    showEchartsLineData(data) {
      const arrData = JSON.parse(data).DS || []
      if (arrData.length) {
        const obj = arrData[0]
        if (!obj.Datetime) {
          this.$message({
            message: `没有时间标记，无法生成图表`,
            type: "warning"
          });
          return
        }
        if (arrData.length) {
          arrData.forEach(item => {
            Object.keys(item).forEach(key => {
              if (key === 'Datetime' || key === 'Station_Id_C' || key === 'Station_Name') return
              const oriObj = this.tableElementData.find(eleItem => eleItem.USER_ELE_CODE === key)
              if (oriObj) {
                const str = `${oriObj.ELE_NAME}-${oriObj.ELE_UNIT}`
                item[str] = item[key]
              }
              delete item[key]
            })
          })
        }
        let arr = []
        let xAxis = {
          type: 'category',
          boundaryGap: false,
          data: []
        };
        // 设置折线个数
        Object.keys(obj).forEach(key => {
          if (key === 'Datetime' || key === 'Station_Id_C' || key === 'Station_Name') return
          arr.push({
            name: key,
            type: 'line',
            stack: '总量',
            data: []
          })
          LineOption.legend.data.push(key)
        })

        arrData.forEach(item => {
          Object.keys(item).forEach(key => {
            const activeObj = arr.find(chartItem => chartItem.name === key) || null
            !!activeObj ? activeObj.data.push(item[key]) : ''
          })
          xAxis.data.push(item.Datetime.substring(8, 13).replace(' ', '日') + '时')
          xAxis.data = [...new Set(xAxis.data)]
        })
        this.chartData = new echarts.init(this.$refs.lineChart);
        LineOption.series = arr
        LineOption.xAxis = xAxis
        this.chartData.setOption(LineOption);
      }
    },
    //  要素列表
    handletableElementData(data) {
      this.tableElementData = data
    },
    // 地图
    handleMap() {
      /* 测试
      // this.showGis = true
      // this.handleGisData(this.dataResult)
      // return
      */
      if (!this.checkParamObj()) return
      this.paramObj.dataFormat = 'JSON'
      this.showGis = true
      postInitUrl(this.paramObj).then(res => {
        if (res && res.data && res.data.urlData) {
          this.handleGisData(res.data.urlData)
        }
      })
    },
    // 处理地图数据
    handleGisData(data) {
      if (typeof data === 'string') {
        const obj = JSON.parse(data)
        this.gisData = JSON.parse(data).DS
      }

    },
    // 检测请求参数
    checkParamObj() {
      if (this.paramObj) {
        return true
      }
      if (!this.backTypeId) {
        this.$message({
          message: `请选择返回类型`,
          type: "warning"
        });
        return false
      }
      const obj = this.tableList.find(item => item.IS_OPTIONAL === "必选参数" && !item.parameter) || null
      if (obj) {
        this.$message({
          message: `${obj.PARAM_ID}是必填参数，参数值不能为空`,
          type: "warning"
        });
        return false
      }
      //
      let userInfo = getUserinfo() ? JSON.parse(getUserinfo()) : null;
      this.paramObj = {
        interfaceId: this.apiId,
        dataFormat: this.backTypeId,
        userid: userInfo.userName,
        factor: [],
      }
      this.tableList.forEach(item => {
        item.parameter && this.paramObj.factor.push(
          {
            item: item.PARAM_ID,
            name: item.PARAM_NAME,
            value: item.parameter
          }
        )
      })
      return true
    }
  },
}
</script>
<style lang="scss" scoped>
.my-api-data {
  width: 100%;
  min-width: 1320px;
  min-height: 100vh;
  .api-box {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    .api-nav {
      width: 20%;
      height: 100%;
      min-height: 100vh;
      background-color: #31394b;
      overflow: hidden;
      position: absolute;
      top: 0;
      left: 0;
      bottom: 0;
    }
    .api-content {
      position: relative;
      width: 80%;
      margin-left: 20%;
      height: auto;
      padding: 0 30px;
      margin-bottom: 80px;
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
          // font-weight: bolder;
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
      .data-api-body {
        border: 1px solid #dbdbdb;
        width: 100%;
        min-height: 300px;
        padding: 0 40px;
        .api-title {
          height: 66px;
          line-height: 66px;
          color: #555;
          font-size: 16px;
          font-weight: 600;
        }
        .select-body {
          margin-top: 10px;
          & > div {
            display: flex;
            justify-content: flex-start;
            align-items: center;
            border: 1px solid #dbdbdb;
            & > span {
              display: inline-block;
              width: 160px;
              height: 40px;
              line-height: 40px;
              background-color: #eee;
              color: #333;
              font-size: 16px;
              text-align: center;
              margin-right: 40px;
            }
            & > div {
              flex: 1;
            }
          }
          & > div:nth-child(2) {
            margin-top: -1px;
          }
          & > div:last-child() {
            margin-top: -1px;
          }
        }
        .handle-cooper {
          margin: 20px 0;
          text-align: right;
        }
      }
      .data-show-content {
        width: 100%;
        min-height: 300px;
        box-sizing: border-box;
        margin-top: 20px;
        & > div {
          width: 100%;
          height: 100%;
          display: flex;
          flex: 1;
          align-items: center;
          max-height: 400px;
          border: 1px solid #ccc;
          box-sizing: border-box;
          position: relative;
          overflow: hidden;
          & > span {
            position: absolute;
            padding-top: 30px;
            top: 0;
            left: 0;
            bottom: 0;
            display: block;
            width: 120px;
            background-color: #f3f3f3;
            text-align: center;
            font-weight: bolder;
          }
          & > div {
            width: calc(100% - 120px); //  100%;
            position: relative;
            left: 120px;
            overflow: auto;
            padding: 10px;
            color: #999;
            & > div {
              width: 100%;
              height: 100%;
            }
          }
        }
      }
    }
  }
}
</style>