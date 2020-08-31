<template>
  <div class="data-api">
    <div class="data-api-content">
      <div class="special-nav">
        <side-bar
          :nav-type-data="navTypeData"
          :nav-type-detail="navTypeDetail"
          :active-type-detail-id="activeTypeDetailId"
          :active-type-id="activeTypeId"
          @handleTypeList="handleTypeList"
          @handleTypeDetailList="handleTypeDetailList"
        />
      </div>
      <div class="data-api-content-a">
        <div class="special-header">
          <div>
            <span class="header-title-line" />
            <span class="header-title-text">API</span>
            <span class="header-sub-title">高效，及时，可靠，定制化服务</span>
          </div>
        </div>
        <div class="data-api-body">
          <div class="api-title">接口调用参数服务</div>
          <div class="select-body">
            <div>
              <span>资料类别</span>
              <el-select
                v-model="activeTypeId"
                placeholder="请选择"
                @change="activeTypeEvent"
              >
                <el-option
                  v-for="item in navTypeData"
                  :key="item.value"
                  :label="item.chName"
                  :value="item.value"
                />
              </el-select>
            </div>
            <div>
              <span>资料名称</span>
              <el-select
                v-model="activeTypeDetailId"
                placeholder="请选择"
                @change="activeTypeDetailEvent"
              >
                <el-option
                  v-for="item in navTypeDetail"
                  :key="item.value"
                  :label="item.chName"
                  :value="item.value"
                />
              </el-select>
            </div>
            <div>
              <span>访问接口</span>
              <el-select
                v-model="apiId"
                placeholder="请选择"
                @change="activeApiListEvent"
              >
                <el-option
                  v-for="item in apiListData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </div>
          <div class="api-title">接口参数</div>

          <!-- 数据显示部分 -->
          <div>
            <el-table
              ref="multipleTable"
              :data="tableList"
              border
              fit
              stripe
              highlight-current-row
              style="width: 100%;"
            >
              <!-- @selection-change="handleSelectionChange"
              @sort-change="sortChange"-->
              <el-table-column
                v-for="(item, index) in tableMeta"
                :key="index"
                align="center"
                :prop="item.key"
                :label="item.label"
                :width="item.width"
              />
              <el-table-column
                label="赋值"
                align="center"
                width="230"
                class-name="small-padding fixed-width"
              >
                <template
                  slot-scope="{ row }"
                  class="tableButtom"
                >
                  <el-input
                    v-if="row.PARAM_ID==='dataCode'?row.assignment=activeTypeDetailId: ''"
                    v-model="row.assignment"
                    placeholder="请输入内容"
                  />
                  <el-input
                    v-if="row.PARAM_ID!=='dataCode'&&row.PARAM_ID!=='time'&&row.PARAM_ID!=='times'&&row.PARAM_ID!=='timeRange'&&row.PARAM_ID!=='tenRangeOfYear'&&row.PARAM_ID!=='limitCnt'"
                    v-model="row.assignment"
                    placeholder="请选择内容"
                  />
                  <el-date-picker
                    v-if="row.PARAM_ID==='time'|| row.PARAM_ID==='times'|| row.PARAM_ID==='tenRangeOfYear'"
                    v-model="row.assignment"
                    value-format="yyyyMMddHHmmss"
                    type="datetime"
                    placeholder="选择日期时间"
                    @change="assignmentEvent(row)"
                  />
                  <el-input
                    v-if="row.PARAM_ID==='timeRange'"
                    v-model="row.assignment"
                    placeholder="请输入内容"
                  />

                  <el-select
                    v-if="row.PARAM_ID==='limitCnt'"
                    v-model="value"
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                width="100"
                class-name="small-padding fixed-width"
              >
                <template
                  slot-scope="{ row }"
                  class="tableButtom"
                >
                  <el-button
                    v-if="row.PARAM_ID!=='dataCode' && row.PARAM_ID!=='times'"
                    type="success"
                    size="mini"
                    style="width: auto;"
                    @click="handleDetails(row)"
                  >{{ row.PARAM_ID==='dataCode'?operationName='':(row.PARAM_ID==='fcstEle'||row.PARAM_ID==='elements'||row.PARAM_ID==='fcstLevel'||row.PARAM_ID==='netCodes'||row.PARAM_ID==='adminCodes'||row.PARAM_ID==='soilDepths'?operationName='选择要素':operationName='赋值说明') }}</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="select-body">
            <div>
              <span>返回类型</span>
              <el-select
                v-model="backTypeId"
                placeholder="请选择"
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
          <div class="handleCooper">
            <el-button
              type="warning"
              @click="handleInit('url')"
            >生成并执行URL</el-button>
            <el-button
              type="success"
              @click="handleInit('script')"
            >生成脚本</el-button>

            <el-button
              v-if="getDataObj.data && getDataObj.data.url"
              type="primary"
              @click="handleDownFile()"
            >下载</el-button>

            <!-- :disabled="!showCharts"  -->
            <el-button
              
              type="danger"
              @click="handleShowChart()"
            >图表</el-button>
            <el-button
              :disabled="!dialogMap"
              type="success"
              @click="handleMap()"
            >地图</el-button>
          </div>
          <div
            v-if="getDataObj.message === 'url生成成功'"
            class="show-data"
          >
            <div class="show-data-title">URL</div>
            <div class="show-data-value">{{ getDataObj.data.url }}</div>
          </div>

          <div
            v-if="getDataObj.message === 'url生成成功'"
            class="show-data"
          >
            <div class="show-data-title">返回结果</div>
            <div
              v-if="this.backTypeId === 'HTML'"
              class="show-data-value"
            >
              <div style="width:100%;height:100%;overflow-x: auto;max-height:500px;overflow-y: auto">
                <div v-html="getDataObj.data.urlData"></div>
              </div>
            </div>
            <div
              v-else
              style="white-space:pre-wrap"
              class="show-data-value"
            >{{ getDataObj.data.urlData }}</div>
          </div>
          <div
            v-if="getDataObj.message === '脚本执行成功'"
            class="show-data"
          >
            <div class="show-data-title">脚本</div>
            <div
              style="white-space:pre-wrap"
              class="show-data-value"
            >{{ getDataObj.data }}</div>
          </div>
          <div
            ref="apiFileChart"
            style="width: 100%; height:500px; margin-top: 50px;"
          />
        </div>
      </div>
    </div>
    <el-dialog
      width="40%"
      title="详情"
      :visible.sync="dialogTableVisible"
    >
      <el-table
        v-if="tableShow"
        :data="gridData"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          align="center"
          width="55"
        />
        <el-table-column
          type="index"
          align="center"
          label="序号"
          width="70"
        />
        <el-table-column
          v-for="(item, index) in tableElement"
          :key="index"
          align="center"
          :prop="item.key"
          :label="item.label"
          :width="item.width"
        />
      </el-table>
      <el-cascader
        v-if="cascaderShow"
        :key="isResourceShow"
         v-model="cascaderData"
        :options="cascaderOptions"
        :props="props"
        collapse-tags
        clearable
      />
      <el-button
        v-if="cascaderShow"
        type="success"
        @click="toggleSelection()"
      >确定</el-button>
      <div
        v-if="tableShow"
        style="margin-top: 20px"
      >
        <el-button
          type="success"
          @click="toggleSelection()"
        >确定</el-button>
      </div>
      <div
        v-if="!tableShow"
        style="text-align: center;"
      >{{ assignmentContent }}</div>
    </el-dialog>
    <el-dialog
      :visible.sync="dialogMap"
      width="70%"
      top="5vh"
      class="map-dialog"
    >
      <echarts-gis :data-list="respData" />

    </el-dialog>
  </div>
</template>

<script>
import echarts from "echarts";
import { LineOption } from "./apiConfig";
import { getUserinfo } from "@/utils/auth";
import FileDownload from "@/utils/fileDownload";
import SideBar from "./components/SideBar.vue";
import EchartsGis from "./components/EchartsGis.vue";
import {
  getApiTypeList,
  getApiTypeDetailList,
  getApiDetail,
  postInitUrl,
  postTheScript,
  getEleForInter,
  getElements,
  getFcstLevel,
  // getInterElement,
  getNetCodes,
  getSoilDepths,
  adminCode,
  getFcstEle,
  getTimeSted
  // uploadApiData
  // getUrlData
} from "@/api/dataApi.js";

export default {
  name: "DataApi",
  components: {
    SideBar,
    EchartsGis
  },
  data() {
    return {
      dialogMap: false,
      showCharts: false,
      isResourceShow: 0,
      cascaderData: [],
      props: { multiple: true },
      cascaderOptions: [],
      cascaderShow: false,
      timeValue: "",
      assignment: "",
      assignmentContent: "",
      tableShow: true,
      options: [
        {
          value: "5",
          label: "5"
        },
        {
          value: "10",
          label: "10"
        },
        {
          value: "20",
          label: "20"
        },
        {
          value: "30",
          label: "30"
        },
        {
          value: "无限制",
          label: "无限制"
        }
      ],
      value: "30",
      gridData: [],
      dialogTableVisible: false,
      operationName: "",
      assignmentValue: "",
      navTypeData: [], // 类型列表
      activeTypeId: null, // 类型列表选中id
      navTypeDetail: [], // 类型详情列表
      activeTypeDetailId: "", // 类型列表选中id
      apiListData: [], // 访问接口列表
      apiId: "",
      tableMeta: [
        { key: "PARAM_ID", label: "参数ID" },
        { key: "PARAM_NAME", label: "参数名称" },
        { key: "IS_OPTIONAL", label: "参数类型" }
      ],
      tableElement: [],
      tableList: [],
      backTypeId: "",
      backTypeData: [
        {
          value: "HTML",
          label: "HTML"
        },
        {
          value: "XML",
          label: "XML"
        },
        {
          value: "JSON",
          label: "JSON"
        },
        {
          value: "JSONP",
          label: "JSONP"
        },
        {
          value: "TEXT",
          label: "TEXT"
        },
        {
          value: "CSV",
          label: "CSV"
        },
        {
          value: "COMMATEXT",
          label: "COMMATEXT"
        },
        {
          value: "KML",
          label: "KML"
        },
        {
          value: "SPACETEXT",
          label: "SPACETEXT"
        }
      ], // 返回类型
      getDataObj: {}, // 返回结果
      resultData: null,
      multipleSelection: [],
      rowContent: " ",
      clickRow: "",
      tableListData: [],
      respData: [
        {
          "Station_Name": "漠河",
          "Datetime": "2020-08-11 06:00:00",
          "Station_Id_C": "50136",
          "Lat": "40.855099",
          "Lon": "111.760676",
          "Alti": "438.5"
        },
        {
          "Station_Name": "漠河",
          "Datetime": "2020-08-11 01:00:00",
          "Station_Id_C": "50136",
          "Lat": "52.97",
          "Lon": "122.51",
          "Alti": "438.5"
        },
        {
          "Station_Name": "漠河",
          "Datetime": "2020-08-11 05:00:00",
          "Station_Id_C": "50136",
          "Lat": "52.97",
          "Lon": "122.51",
          "Alti": "438.5"
        },
        {
          "Station_Name": "漠河",
          "Datetime": "2020-08-11 02:00:00",
          "Station_Id_C": "50136",
          "Lat": "52.97",
          "Lon": "122.51",
          "Alti": "438.5"
        },
        {
          "Station_Name": "漠河",
          "Datetime": "2020-08-11 04:00:00",
          "Station_Id_C": "50136",
          "Lat": "52.97",
          "Lon": "122.51",
          "Alti": "438.5"
        },
        {
          "Station_Name": "漠河",
          "Datetime": "2020-08-11 00:00:00",
          "Station_Id_C": "50136",
          "Lat": "52.97",
          "Lon": "122.51",
          "Alti": "438.5"
        },
        {
          "Station_Name": "北极村",
          "Datetime": "2020-08-11 05:00:00",
          "Station_Id_C": "50137",
          "Lat": "53.47",
          "Lon": "122.38",
          "Alti": "296"
        },
        {
          "Station_Name": "北极村",
          "Datetime": "2020-08-11 04:00:00",
          "Station_Id_C": "50137",
          "Lat": "53.47",
          "Lon": "122.38",
          "Alti": "296"
        },
        {
          "Station_Name": "北极村",
          "Datetime": "2020-08-11 06:00:00",
          "Station_Id_C": "50137",
          "Lat": "53.47",
          "Lon": "122.38",
          "Alti": "296"
        },
        {
          "Station_Name": "北极村",
          "Datetime": "2020-08-11 02:00:00",
          "Station_Id_C": "50137",
          "Lat": "53.47",
          "Lon": "122.38",
          "Alti": "296"
        },
        {
          "Station_Name": "北极村",
          "Datetime": "2020-08-11 01:00:00",
          "Station_Id_C": "50137",
          "Lat": "53.47",
          "Lon": "122.38",
          "Alti": "296"
        },
        {
          "Station_Name": "北极村",
          "Datetime": "2020-08-11 00:00:00",
          "Station_Id_C": "50137",
          "Lat": "53.47",
          "Lon": "122.38",
          "Alti": "296"
        },
        {
          "Station_Name": "北极村",
          "Datetime": "2020-08-11 03:00:00",
          "Station_Id_C": "50137",
          "Lat": "53.47",
          "Lon": "122.38",
          "Alti": "296"
        },
        {
          "Station_Name": "塔河",
          "Datetime": "2020-08-11 00:00:00",
          "Station_Id_C": "50246",
          "Lat": "52.35",
          "Lon": "124.72",
          "Alti": "361.9"
        },
        {
          "Station_Name": "塔河",
          "Datetime": "2020-08-11 03:00:00",
          "Station_Id_C": "50246",
          "Lat": "52.35",
          "Lon": "124.72",
          "Alti": "361.9"
        },
        {
          "Station_Name": "塔河",
          "Datetime": "2020-08-11 06:00:00",
          "Station_Id_C": "50246",
          "Lat": "52.35",
          "Lon": "124.72",
          "Alti": "361.9"
        },
        {
          "Station_Name": "塔河",
          "Datetime": "2020-08-11 05:00:00",
          "Station_Id_C": "50246",
          "Lat": "52.35",
          "Lon": "124.72",
          "Alti": "361.9"
        },
        {
          "Station_Name": "塔河",
          "Datetime": "2020-08-11 04:00:00",
          "Station_Id_C": "50246",
          "Lat": "52.35",
          "Lon": "124.72",
          "Alti": "361.9"
        },
        {
          "Station_Name": "塔河",
          "Datetime": "2020-08-11 02:00:00",
          "Station_Id_C": "50246",
          "Lat": "52.35",
          "Lon": "124.72",
          "Alti": "361.9"
        },
        {
          "Station_Name": "塔河",
          "Datetime": "2020-08-11 01:00:00",
          "Station_Id_C": "50246",
          "Lat": "52.35",
          "Lon": "124.72",
          "Alti": "361.9"
        },
        {
          "Station_Name": "呼中",
          "Datetime": "2020-08-11 03:00:00",
          "Station_Id_C": "50247",
          "Lat": "52.04",
          "Lon": "123.57",
          "Alti": "514.5"
        },
        {
          "Station_Name": "呼中",
          "Datetime": "2020-08-11 06:00:00",
          "Station_Id_C": "50247",
          "Lat": "52.04",
          "Lon": "123.57",
          "Alti": "514.5"
        },
        {
          "Station_Name": "呼中",
          "Datetime": "2020-08-11 02:00:00",
          "Station_Id_C": "50247",
          "Lat": "52.04",
          "Lon": "123.57",
          "Alti": "514.5"
        },
        {
          "Station_Name": "呼中",
          "Datetime": "2020-08-11 04:00:00",
          "Station_Id_C": "50247",
          "Lat": "52.04",
          "Lon": "123.57",
          "Alti": "514.5"
        },
        {
          "Station_Name": "呼中",
          "Datetime": "2020-08-11 01:00:00",
          "Station_Id_C": "50247",
          "Lat": "52.04",
          "Lon": "123.57",
          "Alti": "514.5"
        },
        {
          "Station_Name": "呼中",
          "Datetime": "2020-08-11 05:00:00",
          "Station_Id_C": "50247",
          "Lat": "52.04",
          "Lon": "123.57",
          "Alti": "514.5"
        },
        {
          "Station_Name": "呼中",
          "Datetime": "2020-08-11 00:00:00",
          "Station_Id_C": "50247",
          "Lat": "52.04",
          "Lon": "123.57",
          "Alti": "514.5"
        },
        {
          "Station_Name": "新林",
          "Datetime": "2020-08-11 00:00:00",
          "Station_Id_C": "50349",
          "Lat": "51.67",
          "Lon": "124.39",
          "Alti": "501.5"
        },
        {
          "Station_Name": "新林",
          "Datetime": "2020-08-11 03:00:00",
          "Station_Id_C": "50349",
          "Lat": "51.67",
          "Lon": "124.39",
          "Alti": "501.5"
        },
        {
          "Station_Name": "新林",
          "Datetime": "2020-08-11 06:00:00",
          "Station_Id_C": "50349",
          "Lat": "51.67",
          "Lon": "124.39",
          "Alti": "501.5"
        }
      ],
      pramasObj: {}, // 请求参数
      myChart: null,
      tableElementData: [], // 要素列表
    };
  },
  created() {
    if (this.$route.query.parentId) {
      this.activeTypeId = this.$route.query.parentId;
      this.activeTypeDetailId = this.$route.query.id;
    }

    // 请求类型接口
    const userInfo = getUserinfo() ? JSON.parse(getUserinfo()) : null;

    getApiTypeList(userInfo.iD)
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
  mounted() { },
  methods: {
    // 详情复选框
    toggleSelection() {
      this.rowContent = "";
      if (this.clickRow === "adminCodes") {
        this.cascaderData.map(item => {
          this.rowContent += item[1] + ",";
        });
      } else {
        this.multipleSelection.map(item => {
          if (item.USER_ELE_CODE) {
            this.rowContent += item.USER_ELE_CODE + ",";
          } else if (item.names) {
            if (this.clickRow === "soilDepths") {
              this.rowContent +=
                item.names.slice(0, item.names.length - 2) + ",";
            } else {
              this.rowContent += item.id + ",";
            }
          } else if (item.level) {
            this.rowContent += item.level + ",";
          } else {
            this.rowContent += item.USER_FCST_ELE + ",";
          }
        });
      }
      getEleForInter(this.apiId).then(res => {
        this.tableList = res.data;
        this.tableListData.map((item, i) => {
          if (item.hasOwnProperty("assignment")) {
            this.tableList[i].assignment = this.tableListData[i].assignment;
          }
        });
        this.tableList.map(item => {
          if (item.PARAM_ID === this.clickRow) {
            item.assignment = this.rowContent.slice(
              0,
              this.rowContent.length - 1
            );
          }
        });
        this.tableListData = this.tableList;
      });
      this.dialogTableVisible = false;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 赋值离开触发事件
    assignmentEvent(row) {
      if (
        row.PARAM_ID === "time" ||
        row.PARAM_ID === "times" ||
        row.PARAM_ID === "timeRange" ||
        row.PARAM_ID === "tenRangeOfYear"
      ) {
        getTimeSted({ id: this.activeTypeDetailId, time: row.assignment }).then(
          res => {
            if (res.message === "时间不符合要求") {
              this.$message({
                message: `时间不在授权范围内,请重新选择`,
                type: "warning"
              });
            }
          }
        );
      }
    },
    // 点击详情
    handleDetails(row) {
      this.clickRow = row.PARAM_ID;
      this.tableShow = true;
      this.cascaderShow = false;
      this.dialogTableVisible = true;
      this.tableElement = [];
      this.gridData = [];
      if (row.PARAM_ID === "elements") {
        getElements(this.activeTypeDetailId).then(res => {
          this.tableElement = [
            { key: "USER_ELE_CODE", label: "要素代码", width: 120 },
            { key: "ELE_NAME", label: "要素名称" },
            { key: "ELE_UNIT", label: "单位" }
          ];
          this.gridData = res.data;
          this.tableElementData = JSON.parse(JSON.stringify(this.gridData))
        });
      } else if (row.PARAM_ID === "fcstLevel") {
        getFcstLevel(this.activeTypeDetailId).then(res => {
          this.tableElement = [{ key: "level", label: "预报层次" }];
          res.data.sort((a, b) => {
            return a - b;
          });
          res.data.map(item => {
            this.gridData.push({
              level: item
            });
          });
        });
      } else if (row.PARAM_ID === "netCodes") {
        getNetCodes(this.activeTypeDetailId).then(res => {
          this.tableElement = [{ key: "names", label: "名称" }];
          this.gridData = res.data;
        });
      } else if (row.PARAM_ID === "soilDepths") {
        getSoilDepths(this.activeTypeDetailId).then(res => {
          this.tableElement = [{ key: "names", label: "名称" }];
          this.gridData = res.data;
        });
      } else if (row.PARAM_ID === "adminCodes") {
        this.cascaderOptions = [];
        this.cascaderData = [];
        ++this.isResourceShow;
        this.tableShow = false;
        this.cascaderShow = true;
        adminCode(this.activeTypeDetailId).then(res => {
          res.data.map((item, i) => {
            item.label = item.name;
            item.children.map(ix => {
              ix.value = ix.adcode;
              ix.label = ix.name;
            });
            // delete item.level
          });
          this.cascaderOptions = res.data;
        });
      } else if (row.PARAM_ID === "fcstEle") {
        getFcstEle(this.activeTypeDetailId).then(res => {
          this.tableElement = [
            { key: "USER_FCST_ELE", label: "预报要素名称" },
            { key: "DB_ELE_UNIT", label: "单位" }
          ];
          this.gridData = res.data;
        });
      } else {
        this.tableShow = false;
        this.assignmentContent = row.VALUE_SAMPLE + "," + row.VALUE_FORMAT;
      }
    },
    // 切换资料类别
    activeTypeEvent() {
      this.timeValue = "";
      this.navTypeDetail = [];
      this.activeTypeDetailId = "";
      this.apiId = "";
      this.tableList = [];
      this.tableListData = [];
      this.getTypeDetail(this.activeTypeId);
      this.showCharts = false

      this.clearDataShow()
    },
    // 切换资料名称
    activeTypeDetailEvent() {
      this.tableListData = [];
      this.showCharts = false
      this.navSelectedDetail(this.activeTypeDetailId);

      this.clearDataShow()
    },
    // 切换访问接口
    activeApiListEvent() {
      this.tableListData = [];
      this.tableList = [];
      this.showCharts = false
      getEleForInter(this.apiId).then(res => {
        this.tableList = res.data;
        this.clearDataShow()
      });
    },
    // 保存文件
    handleDownFile() {
      if (this.getDataObj.data && this.getDataObj.data.urlData) {
        const fileName =
          this.apiListData.find(item => item.value === this.apiId).label ||
          null;
        const timerlong = new Date().getTime();
        const downName = `${fileName}${timerlong}.${this.backTypeId}`;
        new FileDownload().fileBlodDownload(
          downName,
          this.getDataObj.data.urlData
        );
      }
    },
    // 图表
    handleShowChart() {
      this.myChart = null
      if (this.pramasObj.dataFormat = 'JSON') {
        this.showCharts = true
        this.showEchartsLineData()
      } else {
        this.pramasObj.dataFormat = 'JSON'
        postInitUrl(this.pramasObj).then(res => {
          this.getDataObj = res;
          this.showCharts = true
          this.showEchartsLineData()
        })
      }
    },
    // 展示图标数据
    showEchartsLineData() {
      if (this.getDataObj && this.getDataObj.data && this.getDataObj.data.urlData) {
        const arrData = JSON.parse(this.getDataObj.data.urlData).DS || []
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
                if (key === 'Datetime' || key === 'Station_Id_C') return
                const oriObj = this.tableElementData.find(eleItem => eleItem.USER_ELE_CODE === key)
                const str = `${oriObj.ELE_NAME}-${oriObj.ELE_UNIT}`
                item[str] = item[key]
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
            if (key === 'Datetime' || key === 'Station_Id_C') return
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
          this.myChart = new echarts.init(this.$refs.apiFileChart);
          LineOption.series = arr
          LineOption.xAxis = xAxis
          this.myChart.setOption(LineOption);
        }
      }
    },
    // 生成并执行URL
    handleInit(type) {
      this.showCharts = false
      const userInfo = getUserinfo() ? JSON.parse(getUserinfo()) : null;
      if (!userInfo) return;
      if (!this.apiListData[0]) return;
      const n = this.tableList.length;
      for (let i = 0; i < n; i += 1) {
        if (this.tableList[i].IS_OPTIONAL === "必选参数") {
          if (this.tableList[i].PARAM_ID === 'timeRange' && Object.prototype.toString.call(this.tableList[i].assignment) === "[object Array]") {
            this.tableList[i].assignment = '[' + this.tableList[i].assignment.join(',') + ']'
          }
          if (!this.tableList[i].assignment) {
            this.$message({
              message: `${this.tableList[i].PARAM_ID}是必填参数，参数值不能为空`,
              type: "warning"
            });
            return;
          }
        }
      }
      if (!this.backTypeId) {
        this.$message({
          message: "请选择返回类型",
          type: "warning"
        });
        return;
      }
      const obj = {
        interfaceId: this.apiId ? this.apiId : this.apiListData[0].value,
        dataFormat: this.backTypeId,
        userid: userInfo.userName, // 'BEXA_YGZX_sxnqzx',
        factor: []
      };
      this.tableList.forEach(item => {
        if (item.assignment) {
          if (type === "url") {
            obj.factor.push({
              item: item.PARAM_ID,
              name: item.PARAM_NAME,
              value: item.assignment
            });
          } else {
            obj.factor.push({
              item: item.PARAM_ID,
              name: item.PARAM_NAME,
              value: item.assignment
            });
          }
        }
      });
      if (this.value && this.value !== "无限制") {
        if (type === "url") {
          obj.factor.push({
            item: "limitCnt",
            name: '最大返回记录数',
            value: this.value
          });
        } else if (type === "script") {
          obj.factor.push({
            item: "limitCnt",
            name: "最大返回记录数",
            value: this.value
          });
        }
      }
      this.pramasObj = obj;
      if (type === "url") {
        postInitUrl(obj)
          .then(res => {
            this.getDataObj = res;
          })
          .catch(e => {
            this.getDataObj = {};
          });
      } else if (type === "script") {
        postTheScript(obj)
          .then(res => {
            this.getDataObj = res;
          })
          .catch(() => {
            this.getDataObj = {};
          });
      }
    },
    // 获取类型的详细接口列表
    getTypeDetail(id) {
      this.navTypeDetail = [];
      // 获取用户的 userid
      const userInfo = getUserinfo() ? JSON.parse(getUserinfo()) : null;
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
    // 选中类型返回函数
    handleTypeList(id) {
      this.activeTypeId = id;
      this.timeValue = "";
      this.navTypeDetail = [];
      this.activeTypeDetailId = "";
      this.apiId = "";
      this.tableList = [];
      this.tableListData = [];
      this.$router.push({ query: { parentId: this.activeTypeId } });
      this.getTypeDetail(this.activeTypeId);
      
      this.clearDataShow()
    },
    // 选中类型详情返回最
    handleTypeDetailList(id) {
      this.activeTypeDetailId = id;
      this.$router.push({
        query: { id: this.activeTypeDetailId, parentId: this.activeTypeId }
      });
      this.navSelectedDetail(id);
      
      this.clearDataShow()
    },
    // 获取接口详细信息
    getTypeDetailContent(data) {
      if (this.activeTypeDetailId !== data.id) {
        this.activeTypeDetailId = data.id;
        this.$emit("navSelected", this.activeTypeDetailId);
      }
    },
    // 获取接口详细信息
    navSelectedDetail(id) {
      getApiDetail(id).then(res => {
        if (res.data) {
          if (res.data.length && res.data[0]) {
            this.apiId = res.data[0].CUSTOM_API_ID;
            this.apiListData = res.data.map(item => {
              return {
                ...item,
                value: item.CUSTOM_API_ID,
                label: `${item.CUSTOM_API_ID}(${item.CUSTOM_API_NAME})`
              };
            });
            getEleForInter(this.apiId).then(res => {
              this.tableList = res.data;
            });
          }
        }
      });
    },
    // 清空数据
    clearDataShow() {
      if (!this.getDataObj.data) {
        this.getDataObj.data.url = null;
        this.getDataObj.data.urlData = null;
        this.getDataObj.data = null
      }
      this.getDataObj = {}
      this.myChart = null
    },
    // 展示地图
    handleMap() {
      // 判断  如果有经纬度
      this.dialogMap = true
      // 没有

    }
  }
};
</script>
<style>
.map-dialog .el-dialog {
  background-color: #f2f2f2;
}
</style>
<style scoped  lang="scss">
.data-api {
  width: 100%;
  .data-api-content {
    min-width: 1300px;
    max-width: 1920px;
    margin: 0 auto;
    min-height: calc(100vh - 100px);
    display: flex;
    justify-content: flex-start;
    .special-nav {
      width: 20%;
      min-height: calc(100vh - 100px);
      background-color: #31394b;
      overflow: hidden;
    }
    .data-api-content-a {
      width: 80%;
      height: auto;
      // background-color: #f1f6fa;
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
        .handleCooper {
          text-align: right;
          margin: 20px 0;
        }
        .show-data {
          display: flex;
          justify-content: flex-start;
          min-height: 100px;
          align-items: center;
          border: 1px solid #dbdbdb;
          background-color: #eee;
          .show-data-title {
            display: inline-block;
            width: 160px;
            height: 100%;
            color: #333;
            font-size: 16px;
            text-align: center;
            padding-right: 40px;
          }
          .show-data-value {
            // flex: 1;
            width: 1217px;
            min-height: 100px;
            padding: 15px 20px;
            background-color: #ffffff;
            word-wrap: break-word;
            word-break: break-all;
          }
        }
      }
    }
  }
}
</style>
