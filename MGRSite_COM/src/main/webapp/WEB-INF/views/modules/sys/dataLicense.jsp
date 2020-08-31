<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/16
  Time: 15:04
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>MUSIC - 接口清单</title>

    <%--<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">--%>


    <link rel="stylesheet" href="${ctxStatic}/element/element-ui.css">

    <script src="https://cdn.jsdelivr.net/npm/vue"></script>

    <%--<script src="https://unpkg.com/element-ui/lib/index.js"></script>--%>

    <script src="${ctxStatic}/element/elemeng.js"></script>


    <script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
    <style>
        #apiPage{ width: 100%; min-height: 100vh;}
        .api-body { width: 100%; min-height: 100%; position: relative; display: flex; justify-content: space-between;}
        .api-body .side-bar { height: 100%; width: 260px;border: 1px solid #f1f1f1; }
        .api-body .api-content { flex: 1; height: 100%;}
        .side-body .side-bar-title { height: 40px; line-height: 38px; background-color: #45aeea; color: #ffffff; font-size: 16px; padding: 0 10px;border-bottom: 1px solid #f2f2f2;}
        .side-body .side-bar-title>i { float: right; font-size: 16px; position: relative; top: 14px; transform: rotate(0deg); transition: all 0.3s; cursor: pointer; }
        .side-body .side-bar-title>.rotate-box { transform: rotate(-90deg) !important; }
        .side-nav-data-list { overflow: hidden;}
        .side-nav-data-list .list-item { padding: 10px; display: flex; justify-content: flex-start; flex-wrap: wrap; }
        .side-nav-data-list .list-item > span { display: inline-block; width: 50%; margin-bottom: 10px; text-align: center; cursor: pointer; color: #999999; font-size: 14px; }
        .side-nav-data-list .list-item > span:hover { color: #45aeea; font-weight: bold; }
        .side-nav-data-list .list-item .list-item-active { color: #45aeea; font-weight: bold; }
        .side-nav-data-list .table-bar-style { height:20px; font-size: 12px;overflow: hidden; }
        .side-nav-data-list .el-table .cell { padding-right: 0; padding-left: 0px;overflow: hidden; }
        .side-nav-data-list .el-table-column--selection .cell { padding-left: 6px; padding-right: 6px; }
        .api-content { padding: 0 10px; }
        .api-content .content-select-box { margin-top:10px; margin-bottom: 30px; }
        .api-content .content-title { font-size: 16px; color: #333; border-bottom: 1px solid #cccccc; height: 40px; line-height: 40px; font-weight: bolder; }
        .content-select-box .el-table td, .el-table th { padding: 4px 0;}
        .content-api { margin-top:10px;  }
        .content-api>div { margin: 10px 0; border-bottom: 1px dashed #f1f1f1;}
        .content-api .el-collapse-item__header { font-weight: bold; font-size: 14px; }
        .content-api .table-bar-style{ font-size: 12px;overflow: hidden; }
        .dialog-style .el-cascader-menu__wrap { height:300px; }
        .dialog-style { padding: 0 20px 20px}
        .table-dialog-element { font-size: 14px; color: #999; }
        .table-dialog-element .content-select-box .el-table td, .el-table th { padding: 4px 4px; }
        .api-select-box .el-tag__close {
            display: none;
        }

    </style>
</head>
<body>
<div id="apiPage">
    <div class="api-body">
        <%--// 左侧导航--%>
        <div class="side-bar">
            <%--上部清单展示--%>
            <div class="side-body">
                <div class="side-bar-title">
                    <span>资料清单</span>
                    <i class="el-icon-arrow-left" :class="{'rotate-box':isNavShow}" @click="isNavShow = !isNavShow"></i>
                </div>
                <div class="side-nav-data-list" :style="{ height: isNavShow?'auto':'0' }">
                    <div class="list-item">
                            <span
                                    v-for="item in navList"
                                    :key="item.dataClassId"
                                    :class="{'list-item-active':navActive.dataClassId === item.dataClassId}" @click= "getBarList(item)">
                                {{ item.dataClassName }}
                            </span>
                    </div>
                </div>
            </div>
            <%--下部清单详情列表--%>
            <div class="side-body">
                <div class="side-bar-title">
                    <span>{{ navActive.dataClassName || '清单列表' }}</span>
                    <i class="el-icon-arrow-left" :class="{'rotate-box':isListShow}" @click="isListShow = !isListShow"></i>
                </div>
                <div class="side-nav-data-list" style="font-size: 12px"  :style="{ height: isListShow?'auto':'0' }">
                    <el-table
                            ref="multipleTable"
                            :data="navbarList"
                            :show-header="false"
                            size="mini"
                            tooltip-effect="dark"
                            :filter-multiple="false"
                            style="width: 100%;"
                            row-class-name="table-bar-style"
                            @selection-change="handleSelectionChange"
                    >
                        <el-table-column
                                type="selection"
                                width="30">
                        </el-table-column>
                        <el-table-column
                                prop="dataName"
                                label="名称">
                        </el-table-column>
                    </el-table>
                </div>

            </div>
        </div>
        <%--右侧信息--%>
        <div class="api-content">
            <div class="content-title">接口服务</div>
            <%--选择条--%>
            <div class="content-select-box">
                <el-table
                        :data="dataList"
                        border
                        :show-header="false"
                        style="width: 100%">
                    <el-table-column
                            fixed
                            prop="name"
                            width="150">
                    </el-table-column>
                    <el-table-column>
                        <template slot-scope="scope">
                            <el-select  v-if="scope.row.id === 1" v-model="navActive.dataClassId" placeholder="请选择资料类别" @change="selectChangeNav" style="width: 100%;">
                                <el-option
                                        v-for="item in navList"
                                        :key="item.dataClassId"
                                        :label="item.dataClassName"
                                        :value="item.dataClassId">
                                </el-option>
                            </el-select>
                            <el-select v-if="scope.row.id === 2" v-model="multipleSelection" placeholder="请选择资料名称"
                                       @change="selectChangeList"
                                       style="width: 100%;">
                                <el-option
                                        v-for="item in navbarList"
                                        :key="item.dataCode"
                                        :label="item.dataName"
                                        :value="item.dataCode">
                                </el-option>
                            </el-select>
                            <%--@blur="aipChangeList"--%>
                            <el-select v-if="scope.row.id === 3" v-model="apiSelection" multiple placeholder="请选择接口"
                                       class="api-select-box"
                                       @visible-change="visibleChangeApi"
                                       style="width: 100%;">
                                <el-option
                                        v-for="item in apiList"
                                        :key="item.activeId"
                                        :label="item.nameShow"
                                        :value="item.activeId">
                                </el-option>
                            </el-select>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="content-title">要素详情
            <el-button type="primary" size="mini" style="float: right;" @click="submit">授权</el-button>
            </div>
            <%--// 接口信息列表--%>
            <div class="content-api">
                <el-table
                        ref="apiTable"
                        border
                        :data="elementTableList"
                        size="mini"
                        tooltip-effect="dark"
                        style="width: 100%;"
                        row-class-name="table-bar-style"
                >

                    <el-table-column
                            prop="PARAM_NAME"
                            label="名称">
                    </el-table-column>
                    <el-table-column
                            prop="PARAM_ID"
                            label="参数id">
                    </el-table-column>
                    <el-table-column
                            prop="IS_SELF_DEFINE"
                            label="参数类型">
                    </el-table-column>
                    <el-table-column
                            prop="VALUE_FORMAT"
                            label="参数说明">
                    </el-table-column>
                    <el-table-column
                            label="操作">
                        <template slot-scope="scope">
                            <%--v-if="cooperObj[item.PARAM_NAME]"--%>
                            <el-button v-if="cooperObj[scope.row.PARAM_ID]"  type="success" size="small" @click="getDialogSelectData(cooperObj[scope.row.PARAM_ID])">
                                {{cooperObj[scope.row.PARAM_ID].name }}
                            </el-button>
                            <el-input v-if="cooperInput[scope.row.PARAM_ID] " v-model="cooperInput[scope.row.PARAM_ID].val"  :placeholder="cooperInput[scope.row.PARAM_ID].placeholder"></el-input>
                                <%--v-model="input"--%>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>

    <el-dialog class="dialog-style" :title="dialogActive.name" :visible.sync="dialogVisible" width="70%" top="1vh">
        <div v-if="dialogActive.type === 'adminCodes'"  style="min-height: 60vh">
            <el-cascader-panel v-model="dialogActive.val" :options="dialogActive.data" :props="props" style="width: 100%"></el-cascader-panel>
        </div>
        <div v-show="dialogActive.type === 'elements'"  style="min-height: 60vh">
            <el-table
                    ref="eleTable"
                    :data="dialogActive.data"
                    tooltip-effect="dark"
                    style="width: 100%"
                    height="380"
                    size="mini"
                    row-class-name="table-dialog-element"
                    @selection-change="dialogElementsChange">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="user_ele_code"
                        label="要素代码">
                </el-table-column>
                <el-table-column
                        prop="ele_name"
                        label="要素名称">
                </el-table-column>
                <el-table-column
                        prop="ele_unit"
                        label="单位">
                </el-table-column>
            </el-table>
        </div>
        <div v-show="dialogActive.type === 'soilDepths'"  style="min-height: 60vh">
            <el-table
                    ref="soilDepthsTable"
                    :data="dialogActive.data"
                    tooltip-effect="dark"
                    style="width: 100%"
                    height="380"
                    size="mini"
                    row-class-name="table-dialog-element"
                    @selection-change="dialogSoilDepthsChange">
                <el-table-column type="selection" width="55"> </el-table-column>
                <el-table-column prop="names" label="土壤深度"> </el-table-column>
                <el-table-column prop="id" label="id"> </el-table-column>
            </el-table>
        </div>
        <div v-show="dialogActive.type === 'timeRange' || dialogActive.type === 'time' || dialogActive.type === 'times'">
            <el-date-picker
                    style="width: 100%;margin-bottom: 20px;"
                    v-model="dialogActive.val"
                    type="daterange"
                    value-format="yyyy-MM-dd"
                    :picker-options="pickerOptions"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right">
            </el-date-picker>
        </div>
        <div v-show="dialogActive.type === 'tenRangeOfYear'">
            <el-date-picker
                    style="width: 100%;margin-bottom: 20px;"
                    v-model="dialogActive.val"
                    type="daterange"
                    value-format="yyyy-MM-dd"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right">
            </el-date-picker>
        </div>




        <div v-show="dialogActive.type === 'netCodes'"  style="min-height: 60vh">
            <el-table
                    ref="netCodesTable"
                    :data="dialogActive.data"
                    tooltip-effect="dark"
                    style="width: 100%"
                    height="380"
                    size="mini"
                    row-class-name="table-dialog-element"
                    @selection-change="dialogSoilDepthsChange">
                <el-table-column type="selection" width="55"> </el-table-column>
                <el-table-column prop="names" label="台站站网名称"> </el-table-column>
                <el-table-column prop="type" label="类型"> </el-table-column>
            </el-table>
        </div>
        <div v-show="dialogActive.type === 'fcstLevel'"  style="min-height: 60vh">
            <el-table
                    ref="fcstLevelTable"
                    :data="dialogActive.data"
                    tooltip-effect="dark"
                    style="width: 100%"
                    height="380"
                    size="mini"
                    row-class-name="table-dialog-element"
                    @selection-change="dialogSoilDepthsChange">
                <el-table-column type="selection" width="55"> </el-table-column>
                <el-table-column prop="names" label="层次等级"> </el-table-column>
            </el-table>
        </div>
        <div v-show="dialogActive.type === 'fcstEle'"  style="min-height: 60vh">
            <el-table
                    ref="fcstEleTable"
                    :data="dialogActive.data"
                    tooltip-effect="dark"
                    style="width: 100%"
                    height="380"
                    size="mini"
                    row-class-name="table-dialog-element"
                    @selection-change="dialogSoilDepthsChange">
                <el-table-column type="selection" width="55"> </el-table-column>
                <el-table-column prop="USER_FCST_ELE" label="要素"> </el-table-column>

                <el-table-column prop="DB_ELE_UNIT" label="单位"> </el-table-column>
            </el-table>
        </div>

        <div class="dialog-btn">
            <%--<el-button size="medium"  type="danger" @click="btnElementCls()">取消</el-button>--%>
            <el-button size="medium" type="success" @click="btnElementData(dialogActive.val)">确定</el-button>
        </div>
    </el-dialog>
</div>
</body>
<script>
    new Vue({
        el: '#apiPage',
        data: function () {
            return {
                dataRoleId: null,
                props: { multiple: true },
                dialogVisible: false, // 弹出框
                dialogActive: {}, // 对话框激活状态的信息
                isNavShow: false, // 资料清单是否展示
                isListShow: false, // 资料清单是否展示
                navList: [], // 清单数据
                navbarList: [], // 清单列表数据
                navActive: {}, // nav选中得值id
                multipleSelection: '', // 清单列表数据多选数据
                dataList:[
                    { id: 1, name:'资料类别' },
                    { id: 2, name:'资料名称' },
                    { id: 3, name:'访问接口' }
                ], // 清单表格选项数据
                apiList: [] , // 接口列表
                apiSelection: [], // 接口多选数据
                elementTableList: [], // 接口展示要素列表
                timer: null, // 定时器
                apiActiveData: {}, // 激活状态的api
                cooperInput:{ // input输入框
                    minLon: {id: 13,  type:'minLon', name:'起始经度' ,val: '',placeholder:'请输入起始经度'},
                    /*dataCode: {id: 14,  type:'dataCode', name:'资料代码' ,val: '',placeholder:'请输入资料代码'}, */
                    maxLon: {id: 15,  type:'maxLon', name:'终止经度' ,val: '',placeholder:'请输入终止经度'},
                    minLat: {id: 16,  type:'minLat', name:'起始纬度' ,val: '',placeholder:'请输入起始纬度'},
                    maxLat: {id: 17,  type:'maxLat', name:'终止纬度' ,val: '',placeholder:'请输入终止纬度'},
                    staIds: {id: 18,  type:'staIds', name:'站号' ,val: '',placeholder:'请输入站号'},
                    maxStaId: {id: 19,  type:'maxStaId', name:'终止站号' ,val: '',placeholder:'请输入终止站号'},
                    minStaId: {id: 20,  type:'minStaId', name:'起始站号' ,val: '',placeholder:'请输入起始站号'},
                    month: {id: 21,  type:'month', name:'月' ,val: '',placeholder:'请输入月'},
                    day: {id: 22,  type:'day', name:'日' ,val: '',placeholder:'请输入日'},
                    maxYear: {id: 23,  type:'maxYear', name:'截至年（历年同期）' ,val: '',placeholder:'请输入截至年(YYYY)'},
                    minYear: {id: 24,  type:'minYear', name:'起始年（历年同期）' ,val: '',placeholder:'请输入起始年(YYYY)'},
                    maxMD: {id: 25,  type:'maxMD', name:'截止月日（历年同期）' ,val: '',placeholder:'请输入截止月日(MMDD)'},
                    minMD: {id: 26,  type:'minMD', name:'起始月日（历年同期）' ,val: '',placeholder:'请输入起始月日(MMDD)'},
                },
                cooperObj: {
                    elements: {id: 1,  type:'elements', name:'选择要素' ,val: [], data:[], api:'/interdata/interfaceData/getElements', defaultData:false},
                    time: {id: 2,  type:'time', name:'选择时间' ,val: [],  data:[],api:'', defaultData:true},
                    times: {id: 2,  type:'times', name:'选择时间' ,val: [],  data:[],api:'', defaultData:true},
                    timeRange: {id: 2,  type:'timeRange', name:'选择时间' ,val: [],  data:[],api:'', defaultData:true},
                    tenRangeOfYear: {id: 2,  type:'tenRangeOfYear', name:'选择时间' ,val: [],  data:[],api:'', defaultData:true},
                    hourSeparate: {id: 3,  type:'hourSeparate', name:'选择小时' ,val: [], data:[], api:'', defaultData:false},
                    minSeparate: {id: 4,  type:'minSeparate', name:'选择分钟' ,val: [],  data:[],api:'', defaultData:false},
                    staLevels: {id: 5,  type:'staLevels', name:'选择台站级别' ,val: [],  data:[],api:'', defaultData:false},
                    eleValueRanges: {id: 6,  type:'eleValueRanges', name:'选择要素范围' ,val: [], data:[], api:'', defaultData:false},
                    orderBy: {id: 7,  type:'orderBy', name:'选择排序字段' ,val: [],  data:[],api:'', defaultData:false},
                    limitCnt: {id: 8,  type:'limitCnt', name:'选择返回录数' ,val: [],  data:[],api:'', defaultData:false},
                    netCodes: {id: 9,  type:'netCodes', name:'台站站网' ,val: [],  data:[],api:'/interdata/interfaceData/getNetCodes', defaultData:false},
                    fcstLevel: {id: 10,  type:'fcstLevel', name:'预报层次' ,val: [],  data:[],api:'/interdata/interfaceData/getFcstLevel', defaultData:false},
                    adminCodes: {id: 11,  type:'adminCodes', name:'国内行政编码' ,val: [],  data:[],api:'/interdata/interfaceData/getAdminCodes', defaultData:false},
                    soilDepths: {id: 12,  type:'soilDepths', name:'土壤深度' ,val: [],  data:[],api:'/interdata/interfaceData/getSoilDepths', defaultData:false},
                    fcstEle: {id: 12,  type:'fcstEle', name:'数值预报' ,val: [],  data:[],api:'/interdata/interfaceData/getFcstEle', defaultData:false},

                },
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    },
                        {
                            text: '最近二个月',
                            onClick(picker) {
                                const end = new Date();
                                const start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 60);
                                picker.$emit('pick', [start, end]);
                            }
                        }, {
                            text: '最近三个月',
                            onClick(picker) {
                                const end = new Date();
                                const start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                                picker.$emit('pick', [start, end]);
                            }
                        }]
                },
                message: 'huhuhuhu'
            }
        },
        watch: {
            dialogVisible: function () {
                if(!this.dialogVisible) {
                    this.dialogActive = {}
                    this.dialogVisible = false;
                }
            }
        },
        created: function() {
            this.dataRoleId = window.location.search.replace('?id=', '') || null
        },
        mounted: function() {
            this.getNavList()
        },
        methods: {
            getNavList: function () { // 获取顶部导航
                const that = this;
                $.ajax({
                    type: 'post',
                    url: '${ctx}/interdata/interfaceData/interfams',
                    dataType: 'json',
                    success: function (data) {
                        if(data && data.length) {
                            that.navList = data
//                            that.navActive = JSON.parse(JSON.stringify(data[0])) || null
                            if(that.navActive) {
                                that.isNavShow = true
                                that.getBarList(data[0])
                            }
                        }
                    },
                    error: function () {
                        //alert('暂无数据!');
                    }
                })
            },
            getBarList: function (data) { // 获取清单列表详情列表
                this.navActive =  JSON.parse(JSON.stringify(data)) || null
                const that = this;
                $.ajax({
                    type:"post",
                    async : false,
                    url:"${ctx}/interdata/interfaceData/interface",
                    data:{id:this.navActive.dataClassId},
                    dataType:"json",
                    success: function (data) {
                        if(data && data.length) {
                            that.navbarList = data
                            that.apiList = [] // 清单列表
                            that.elementTableList = [] // api 列表
                            that.isListShow= true
                            that.multipleSelection = null
                        }
                    },
                    error: function () {
                        that.navbarList = []
                        that.apiList = [] // 清单列表
                        that.elementTableList = [] // api 列表
                        that.isListShow= true
                        that.multipleSelection = null
                        //alert('暂无数据!');
                    }
                })
            },
            toggleSelection: function() { // 设置table选中项
                const that = this
                const arr = []
                this.navbarList.forEach(function (t2) {
                    if(t2.dataCode === that.multipleSelection) {
                        arr.push(t2)
                    }
                })
                if (arr) {
                    that.$refs.multipleTable.clearSelection();
                    arr.forEach(function (row) {
                        that.$refs.multipleTable.toggleRowSelection(row);
                    })
                }
            },
            handleSelectionChange: function(val) { // 点击选择框
                const that = this
                if (val.length > 1) {
                    this.$refs.multipleTable.clearSelection()
                    const d = val.pop() || null
                    this.$refs.multipleTable.toggleRowSelection(d?d:{})
                    that.multipleSelection =d ? d['dataCode']: null
                }else if(val.length === 1) {
                    const d = val.pop() || null
                    that.multipleSelection =d ? d['dataCode']: null
                }
                if(that.timer) {
                    window.clearTimeout(that.timer)
                    that.timer = null
                }
                that.timer = setTimeout(function () {
                    that.getApiList() // 请求接口列表
//                    that.getElementList() // 请求要素信息列表
                }, 1000)
            },
            selectChangeNav: function(d) { // 选择导航信息
                this.clearcooperObjData()
                const  that = this
                this.navList.forEach(function (item) {
                    if(item.dataClassId === d) {
                        that.navActive =  JSON.parse(JSON.stringify(item)) || null
                        that.getBarList(item)
                    }
                })
            },
            selectChangeList:function () { // 选择清单列表
                this.clearcooperObjData()
                this.toggleSelection();
//                this.getApiList() // 请求接口列表
//
            },
            aipChangeList: function() {},
            visibleChangeApi: function(e) {
                this.clearcooperObjData()
                if(!e && this.apiSelection.length) {
                    this.getElementList() // 请求要素信息列表
                }
            },
            getApiList: function() { // 接口数据请求
                const that = this;
                that.apiList = []
                that.elementTableList = [] // api 列表
                that.apiSelection= []
                if(!that.multipleSelection) {
                    return
                }
                const loading = that.$loading({
                    lock: true,
                    text: 'Loading-APi',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.2)'
                });
                $.ajax({
                    type: 'post',
                    data:{"id": this.multipleSelection},
                    url: '${ctx}/interdata/interfaceData/projectcdinter',
                    dataType: 'json',
                    success: function (data) {
                        if(data && data.length) {
                            loading.close();
                            that.apiList = []
                            data.forEach(function (item) {
                                const a = item.name+"("+item.CUSTOM_API_ID+")"
                                const b = item.CUSTOM_API_ID
                                that.apiList.push(Object.assign(item, {nameShow:a, activeId:b}))
                            });
                        }
                    },
                    error: function () {
                        loading.close();
                        that.apiList = []
                        //alert('暂无数据!');
                    }
                })
            },
            getElementList: function () { // 获取接口详细信息列表
                const that = this;
                const loading = that.$loading({
                    lock: true,
                    text: 'Loading-Element',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.2)'
                });


                $.ajax({
                    type: 'post',
                    data:{"id": this.apiSelection.join(',')},
//                    data:{"id": this.multipleSelection},
                    <%--url: '${ctx}/interdata/interfaceData/getAllInterElement',*/--%>
                    url: '${ctx}/interdata/interfaceData/getInterElement',
                    dataType: 'json',
                    success: function (data) {
                        that.elementTableList = data
                        loading.close();
                    },
                    error: function () {
                        loading.close();
                        that.elementTableList= []
                        //alert('暂无数据!');
                    }
                })
            },
            getDialogSelectData: function(data) { // 获取要素等信息 that.navbarList
                console.log('data=', data)
                const that = this;
                // 不需要请求数据的
                if(data.defaultData) {
                    that.dialogActive = data
                    that.dialogVisible = true;
                    return
                }
                // 根据类型，判断是否存在data值，如果存在 直接显示
                if(data.data.length) {
                    that.dialogActive = data
                    that.dialogVisible = true;
                    //  如果是要素，设置默认选择
                    if(data.type === 'elements') {
                        that.elements()
                    }else if(data.type === 'netCodes') {
                        that.netCodes()
                    }else if(data.type === 'fcstLevel') {
                        that.fcstLevel()
                    }else if(data.type === 'soilDepths') {
                        that.soilDepths()
                    }else  if(data.type === 'fcstEle') {
                        that.fcstEle()
                    }

                }else {
                    const  url = '${ctx}' + data.api;
                    const param = null;
                    // 设置请求参数
                    if(data.type === 'elements') {
                        that.navbarList.forEach(function (t) {
                            if(t.dataCode === that.multipleSelection) {
                                that.param = t.dDataId
                            }
                        })
                    } else if(data.type === 'fcstEle') {
                        that.param = that.multipleSelection
                        <%--url = '${ctx}' + data.api + '?id='+ that.multipleSelection;--%>
                    }
                    if(data.api) {
                        $.ajax({
                            type: 'post',
                            data: {id: that.param},
                            url: url,
                            dataType: 'json',
                            success: function (res) {
                                if(!res) {
                                    alert('暂无数据')
                                }
                                that.dialogActive = data
                                if(data.type === 'adminCodes') {
                                    that.adminCodes(res.mitList)
                                } else if(data.type === 'elements') {
                                    that.dialogActive.data = res
                                } else if(data.type === 'soilDepths') {
                                    that.dialogActive.data = res
                                }else if(data.type === 'netCodes') {
                                    that.dialogActive.data = res
                                }else if(data.type === 'fcstLevel') {
                                    that.dialogActive.data = res
                                }else if(data.type === 'fcstEle') {
                                    that.dialogActive.data = res
                                }
                                that.dialogVisible = true;
                            },
                            error: function () {
                                alert('暂无数据')
                            }
                        })
                    }

                }
            },
            // 清空选得值
            clearcooperObjData: function() {
                this.dialogVisible = false;
                const  that = this
                Object.keys(this.cooperObj).forEach(function (key) {
                    that.cooperObj[key].data = []
                    that.cooperObj[key].val = []
                })
            },
            btnElementData: function (data) { // 要素选中确定事件
                this.dialogVisible = false;
            },
            adminCodes: function (data) { // 国内行政编码
                this.dialogActive.data = data.map(function(item) {
                    return {
                        value: item.id,
                        label: item.name,
                        children: item.districtList && item.districtList ? item.districtList.map(function (citem) {
                            return {
                                value: citem.id,
                                label: citem.name,
                            }
                        }): []
                    }
                })
            },
            elements: function () { // 选择要素
                const that = this
                setTimeout(function () {
                    that.dialogActive.val.length && that.dialogActive.val.forEach(function (row) {
                        that.$refs.eleTable.toggleRowSelection(row)
                    });
                }, 100)
            },
            soilDepths: function () {
                const that = this
                setTimeout(function () {
                    that.dialogActive.val.length && that.dialogActive.val.forEach(function (row) {
                        that.$refs.soilDepthsTable.toggleRowSelection(row)
                    });
                }, 100)
            },
            fcstEle: function () {
                const that = this
                setTimeout(function () {
                    that.dialogActive.val.length && that.dialogActive.val.forEach(function (row) {
                        that.$refs.fcstEleTable.toggleRowSelection(row)
                    });
                }, 100)
            },
            netCodes: function () {
                const that = this
                setTimeout(function () {
                    that.dialogActive.val.length && that.dialogActive.val.forEach(function (row) {
                        that.$refs.netCodesTable.toggleRowSelection(row)
                    });
                }, 100)
            },
            fcstLevel: function () {
                const that = this
                setTimeout(function () {
                    that.dialogActive.val.length && that.dialogActive.val.forEach(function (row) {
                        that.$refs.fcstLevelTable.toggleRowSelection(row)
                    });
                }, 100)
            },
            dialogElementsChange: function (val) { // 要素选中
                this.dialogActive.val = val
            },
            dialogSoilDepthsChange: function (val) { // 要素选中
                this.dialogActive.val = val
            },
            submit: function() {

                const that = this;
                const tableList =  this.elementTableList.filter(function (t) {
                    if(t.PARAM_ID !== "dataCode") {
                        return { type: t.PARAM_ID }
                    }
                }).map(function (value) { return {
                    type: value.PARAM_ID
                } })

                if(!this.dataRoleId) {
                    this.$message({
                        message: '请选择刷新页面',
                        type: 'warning'
                    });
                    return
                }else if(!this.multipleSelection) {
                    this.$message({
                        message: '请选择资料名称',
                        type: 'warning'
                    });
                    return
                } else if(!this.apiSelection.length) {
                    this.$message({
                        message: '请选择api接口',
                        type: 'warning'
                    });
                    return
                }

                tableList.forEach(function (item) {
                    Object.keys(that.cooperInput).forEach(function (k) {
                        if(k === item.type) {
                            item['val'] = that.cooperInput[k].val
                            item['name'] = that.cooperInput[k].name
                        }
                    })
                    Object.keys(that.cooperObj).forEach(function (k) {
                        if(k === item.type) {
                            item['val'] = that.cooperObj[k].val || null
                            item['name'] = that.cooperObj[k].name
                        }
                    })
                })
                // // 验证input输入框是否有值
                // const inputaArr = Object.keys(this.cooperInput)
//                for(let j = 0; j < tableList.length; j++) {
//                    if(!tableList[j].val) {
//                        that.$message({
//                            message: tableList[j].name + '值不能为空',
//                            type: 'warning'
//                        });
//                        return
//                    }
//                }



                //
                // const arr = []
                // Object.keys(this.cooperObj).forEach(function (key) {
                //     that.cooperObj[key].val.length && arr.push({
                //         type: that.cooperObj[key].type,
                //         val:that.cooperObj[key].val
                //     })
                // })
                // arr.length && arr.forEach(function (item) {
                //     tableList.forEach(function (innerItem) {
                //         if(item.type === innerItem.type) {
                //             innerItem['val'] = item.val || []
                //         }
                //     })
                // })

                console.log('tableList=', tableList)
                const obj = {
                    dataRoleId: this.dataRoleId,
                    dataClassId: this.navActive.dataClassId,
                    otherID:this.multipleSelection,
                    data:JSON.stringify(tableList),
                    apiList: JSON.stringify(this.apiSelection),
                }
                $.ajax({
                    type: 'post',
                    data: JSON.stringify(obj),
                    headers: {
                        "Content-Type": "application/json"
                    },
                    url: '${ctx}/interdata/interfaceData/addInterValue',
                    dataType: 'json',
                    success: function () {
                        that.$message({
                            message: '恭喜您，授权成功',
                            type: 'success'
                        });
                    },
                    error: function () {
                        //alert('授权失败!');
                    }
                })
            }
        }
    })
</script>
</html>



