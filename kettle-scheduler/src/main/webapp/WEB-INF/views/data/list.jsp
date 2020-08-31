<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<base href="<%=basePath %>">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>数据分类列表</title>
    <link href="static/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="static/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="static/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
    <link href="static/css/animate.css" rel="stylesheet">
    <link href="static/css/style.css?v=4.1.0" rel="stylesheet">
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
                <h5>数据分类列表</h5>
                <div class="ibox-tools">
                    <a class="collapse-link">
                        <i class="fa fa-chevron-up"></i>
                    </a>
                    <a class="close-link">
                        <i class="fa fa-times"></i>
                    </a>
                </div>
            </div>
            <div class="ibox-content">
                <div class="row">
                    <div class="col-sm-3 float-left">
                        <a href="view/data/addUI.shtml" class="btn btn-w-m btn-info" type="button">
                            <i class="fa fa-plus" aria-hidden="true"></i>&nbsp;新增数据分类
                        </a>
                    </div>
                    <div class="col-sm-9">
                        <div class="form-inline  text-right">
                            <div class="form-group ">
                                <label class="control-label m-b-xs" for="dataBigClass">资料大类：</label>
                                <select id="dataBigClass" name="dataBigClass" class="form-control m-b-xs"
                                        aria-required="true">
                                    <option value="无" selected>请选择资料大类</option>
                                </select>
                            </div>
                            <div class="form-group ">
                                <label class="control-label m-b-xs" for="dataSmallClass">资料小类：</label>
                                <select id="dataSmallClass" name="dataSmallClass" class="form-control m-b-xs"
                                        aria-required="true">
                                    <option value="无" selected>请选择资料小类</option>
                                </select>
                            </div>
                            <div class="form-group ">
                                <label class="control-label m-b-xs" for="dataName">数据分类名称：</label>
                                <input id="dataName" name="dataName" type="text" placeholder="请输入数据分类名称"
                                class="form-control m-b-xs" aria-required="true">
                            </div>
                            <div class="form-group ">
                                <label class="control-label m-b-xs" for="dataServiceObject">服务对象：</label>
                                <input id="dataServiceObject" name="dataServiceObject" type="text" placeholder="请输入服务对象"
                                       class="form-control m-b-xs" aria-required="true">
                            </div>
                            <div class="form-group">
                                <button onclick="search()" class="btn btn-w-m btn-info" type="button">
                                    <i class="fa fa-search" aria-hidden="true"></i>&nbsp;搜索
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <table id="dataList" data-toggle="table"
					data-url="data/getList.shtml"
					data-query-params=queryParams data-query-params-type="limit"
					data-pagination="true"
					data-side-pagination="server" data-pagination-loop="false">
					<thead>
						<tr>
							<th data-field="dataId">数据分类编号</th>
							<th data-field="dataName">数据分类名称</th>
							<th data-field="dataBigClass">资料大类</th>
							<th data-field="dataSmallClass">资料小类</th>
							<th data-field="dataServiceObject">服务对象</th>
							<th data-field="dataSize">单条数据量(字节)</th>
							<th data-field="addTime">创建时间</th>
<%--							<th data-field="editTime">更新时间</th>--%>
							<th data-field="action" data-formatter="actionFormatter"
								data-events="actionEvents">操作</th>
						</tr>
					</thead>
				</table>
            </div>
        </div>
	</div>
	<!-- 全局js -->
    <script src="static/js/jquery.min.js?v=2.1.4"></script>
    <script src="static/js/bootstrap.min.js?v=3.3.6"></script>
    <!-- layer javascript -->
    <script src="static/js/plugins/layer/layer.min.js"></script>
    <!-- 自定义js -->
    <script src="static/js/content.js?v=1.0.0"></script>
    <!-- Bootstrap table -->
    <script src="static/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
    <script src="static/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
    <script src="static/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
	<script>

        // 获取资料大类、小类
        $(document).ready(function () {
            $.ajax({
                type: 'POST',
                async: false,
                url: 'data/getDataBigClassList.shtml',
                data: {},
                success: function (data) {
                    for (var i = 0; i < data.length; i++) {
                        $("#dataBigClass").append('<option value="' + data[i] + '">' + data[i] + '</option>');
                    }
                },
                error: function () {
                    alert("请求失败！请刷新页面重试");
                },
                dataType: 'json'
            });
        });

        $("#dataBigClass").change(function(){
            var dataBigClass = $(this).val();
            $.ajax({
                type: 'POST',
                async: false,
                url: 'data/getDataSmallClassList.shtml',
                data: {
                    "dataBigClass": dataBigClass
                },
                success: function (data) {
                    $("#dataSmallClass ").find("option").remove();
                    $("#dataSmallClass").append('<option  value="无">请选择资料小类</option>');
                    for (var i = 0; i < data.length; i++) {
                        $("#dataSmallClass").append('<option  value="' + data[i] + '">' + data[i] + '</option>');
                    }
                },
                error: function () {
                    alert("请求失败！请刷新页面重试");
                },
                dataType: 'json'
            });
        });

        function search() {
            $('#dataList').bootstrapTable('refresh', "data/getList.shtml");
        }

	    function actionFormatter(value, row, index) {
	    	return ['<a class="btn btn-primary btn-xs" id="edit" type="button"><i class="fa fa-edit" aria-hidden="true"></i>&nbsp;编辑</a>',
    			'&nbsp;&nbsp;',
    			'<a class="btn btn-primary btn-xs" id="delete" type="button"><i class="fa fa-trash" aria-hidden="true"></i>&nbsp;删除</a>'
            ].join('');
	    };
	    window.actionEvents = {
	    		'click #edit' : function(e, value, row, index) {
	    			var dataId = row.dataId;
	    			location.href = "view/data/editUI.shtml?dataId=" + dataId;
	    		},
	    		'click #delete' : function(e, value, row, index) {
	    			layer.confirm('确定删除该单位？', {
	    				  btn: ['确定', '取消']
	    				},
	    				function(index){
	    				    layer.close(index);
	    				    $.ajax({
	    				        type: 'POST',
	    				        async: true,
	    				        url: 'data/delete.shtml',
	    				        data: {
	    				            "dataId": row.dataId
	    				        },
	    				        success: function (data) {
	    				            if(data.status == 'error'){
                                        alert(data.message);
                                    }else {
	    				        	    location.replace(location.href);
                                    }
	    				        },
	    				        error: function () {
	    				            alert("系统出现问题，请联系管理员");
	    				        },
	    				        dataType: 'json'
	    				    });
	    		  		},
	    		  		function(){
	    		  			layer.msg('取消操作');
    		  			}
    		  		);
	    		},
	    	};

		    function queryParams(params) {
		    	var temp = {
		    	    limit: 10,
                    offset: params.offset,
                    dataName: $("#dataName").val(),
                    dataBigClass: $("#dataBigClass").val(),
                    dataSmallClass: $("#dataSmallClass").val(),
                    dataServiceObject: $("#dataServiceObject").val()
		    	};
		        return temp;
		    };

    </script>
</body>
</html>