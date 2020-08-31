<template>
  <div class="api-table">
    <el-table
      ref="multipleTable"
      :data="tableList"
      border
      fit
      stripe
      highlight-current-row
      style="width: 100%;"
    >
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
          <el-date-picker
            v-if="row.PARAM_ID==='time'|| row.PARAM_ID==='times'|| row.PARAM_ID==='tenRangeOfYear'"
            v-model="row.parameter"
            value-format="yyyyMMddHHmmss"
            type="datetime"
            placeholder="选择日期时间"
          />
          <el-input
            v-else
            v-model="row.parameter"
            placeholder="请输入内容"
            @input="handleChangeInput"
          />
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
            v-if="row.PARAM_ID==='fcstEle'||row.PARAM_ID==='elements'||row.PARAM_ID==='fcstLevel'||row.PARAM_ID==='netCodes'||row.PARAM_ID==='adminCodes'||row.PARAM_ID==='soilDepths'"
            type="success"
            size="mini"
            style="width: auto;"
            @click="handleDetails(row)"
          >选择参数</el-button>
          <el-button
            v-else
            type="warning"
            size="mini"
            style="width: auto;"
            @click="handleDetailsInfo(row)"
          >赋值说明</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <table>选择类</table> -->
    <el-dialog
      :visible.sync="dialogTableEle"
      title="请选中参数"
      width="70%"
      top="5vh"
      class="map-dialog"
    >
      <el-table
        :data="tableListData"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          align="center"
          width="55"
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
      <el-button
        style="margin-top:20px;"
        type="success"
        @click="handleSelectData"
      >确定</el-button>
    </el-dialog>
    <el-dialog
      :visible.sync="cascaderShow"
      title="请选中参数"
      width="70%"
      top="5vh"
      class="map-dialog"
    >

      <!-- v-model="activeRowData.parameter" -->
      <div style="min-height: 300px">
        <el-cascader
          :key="isResourceShow"
          :options="cascaderOptions"
          :props="props"
          collapse-tags
          clearable
          @change="cascaderChange"
          style="width: 100%"
        />

        <el-button
          style="margin-top:20px;"
          type="success"
          @click="handleSelectData"
        >确定</el-button>

      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  getElements, // 要素
  getFcstLevel,
  // getInterElement,
  getNetCodes,
  getSoilDepths,
  adminCode,
  getFcstEle,
  // getTimeSted
  // uploadApiData
  // getUrlData
} from "@/api/dataApi.js";

export default {
  name: 'ApiTable',
  props: {
    tableList: {
      type: Array,
      default: () => []
    },
    activeTypeDetailId: {
      type: String,
      default: () => ''
    }
  },
  data() {
    return {
      backTypeId: null, // 接口返回烈性 
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
      ],
      activeRowData: {}, // 当前操作项
      // table类型
      tableListData: [], // table数据列表
      dialogTableEle: false, // 对话框银川显示
      tableElement: [], // table lable
      // 多级显示，选中框
      cascaderShow: false, // 隐藏与现实
      cascaderOptions: [],
      props: { multiple: true },
      isResourceShow: 0,
      // 返回类型
      value: 0, //  限制
      tableMeta: [
        { key: "PARAM_ID", label: "参数ID" },
        { key: "PARAM_NAME", label: "参数名称" },
        { key: "IS_OPTIONAL", label: "参数类型" }
      ],
      options: [
        { value: "5", label: "5" },
        { value: "10", label: "10" },
        { value: "20", label: "20" },
        { value: "30", label: "30" },
        { value: "无限制", label: "无限制" }
      ],
    }
  },
  methods: {
    // 改变输入框内容
    handleChangeInput() {
      this.$emit('clearParamObjData')
    },
    // 选择得要素
    handleSelectionChange(val) {
      if (!val.length) return
      const arr = val.map(item => {
        if (this.activeRowData.PARAM_ID === "elements") {
          return item.USER_ELE_CODE
        } else if (this.activeRowData.PARAM_ID === 'fcstEle') {
          return item.USER_FCST_ELE
        } else if (this.activeRowData.PARAM_ID === 'fcstLevel') {
          return item.level
        } else if (this.activeRowData.PARAM_ID === 'soilDepths') {
          return item.names
        } else if (this.activeRowData.PARAM_ID === 'netCodes') {
          return item.names
        }
      }) || []
      // USER_ELE_CODE
      const str = arr.toString()
      this.activeRowData.parameter = str; // JSON.stringify(arr).replace(/\[/, '').replace(/\]/, '') //
      this.$emit('clearParamObjData')
    },
    // 关闭对话框
    handleSelectData() {
      this.dialogTableEle = false
      this.cascaderShow = false
    },
    handleDetails(row) {
      this.$emit('clearParamObjData')
      // 将当前值保存起来
      this.activeRowData = row;
      this.tableElement = [];
      this.tableListData = []
      this.dialogTableEle = false
      if (row.PARAM_ID === "elements") {
        getElements(this.activeTypeDetailId).then(res => {
          this.dialogTableEle = true
          this.tableElement = [
            { key: "USER_ELE_CODE", label: "要素代码" },
            { key: "ELE_NAME", label: "要素名称" },
            { key: "ELE_UNIT", label: "单位" }
          ];
          this.tableListData = res.data;
          this.$emit('handletableElementData', this.tableListData)
        });
      } else if (row.PARAM_ID === "fcstLevel") {
        getFcstLevel(this.activeTypeDetailId).then(res => {
          this.dialogTableEle = true
          this.tableElement = [{ key: "level", label: "预报层次" }];
          if (res.data && res.data.length) {
            res.data.sort((a, b) => {
              return a - b;
            });
            this.tableListData = res.data.map(item => {
              return { ...item, level: item }
            });
          }
        });
      } else if (row.PARAM_ID === "netCodes") {
        getNetCodes(this.activeTypeDetailId).then(res => {
          this.dialogTableEle = true
          this.tableElement = [{ key: "names", label: "名称" }];
          this.tableListData = res.data;
        });
      } else if (row.PARAM_ID === "soilDepths") {
        getSoilDepths(this.activeTypeDetailId).then(res => {
          this.dialogTableEle = true
          this.tableElement = [{ key: "names", label: "名称" }];
          this.tableListData = res.data;
        });
      } else if (row.PARAM_ID === "fcstEle") {
        getFcstEle(this.activeTypeDetailId).then(res => {
          this.dialogTableEle = true
          this.tableElement = [
            { key: "USER_FCST_ELE", label: "预报要素名称" },
            { key: "DB_ELE_UNIT", label: "单位" }
          ];
          this.tableListData = res.data;
        });
      } else if (row.PARAM_ID === "adminCodes") {
        this.cascaderOptions = [];
        this.cascaderData = [];
        ++this.isResourceShow;
        adminCode(this.activeTypeDetailId).then(res => {

          this.cascaderShow = true;
          res.data.map((item, i) => {
            item.label = item.name;
            item.children.map(ix => {
              ix.value = ix.adcode;
              ix.label = ix.name;
            });
          });
          this.cascaderOptions = res.data;
        });
      }
    },
    //  级联选择器
    cascaderChange(val) {
      this.activeRowData.parameter = val.toString()
    },
    // 提示信息
    handleDetailsInfo(row) {
      this.$notify({
        type: 'success',
        title: row.PARAM_ID + '提示',
        message: row.VALUE_SAMPLE,
        duration: 5000
      });

    }
  }
}
</script>
<style>
.el-table--medium th,
.el-table--medium td {
  padding: 8px 0;
}
.map-dialog {
  max-height: 92vh;
  overflow: auto;
  padding-bottom: 20px;
}
.el-dialog__body {
  margin-bottom: 20px;
  max-height: 86vh;
  overflow: auto;
}
</style>
<style lang="scss" scoped>
.api-table {
  width: 100%;
  max-height: 500px;
  overflow: auto;
  border: 1px solid #f2f2f2;
}
</style>