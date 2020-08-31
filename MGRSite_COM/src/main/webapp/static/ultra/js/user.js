var pathName = window.document.location.pathname;
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
var root = window.location.href;

function page(n, s) {
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    var isActive = $("input[type='radio'][name='status']:checked").val();
    var userType = $("#selects option:selected").val();
    var searchName = $("#searchName").val();
    // alert(name);
    if ((isActive == null || isActive == undefined) || isActive == "") {
        isActive = "";
    }
    $("#form1").attr(
        "action",
        projectName + "/user/userList?searchName=" + searchName + "&IsActive=" + isActive + "&userType=" + userType);
    $("#form1").submit();
    return false;
}


function selectById(id) {
    $.ajax({
        url: projectName + '/user/getUserById',
        type: 'POST',
        data: {'id': id},
        dataType: 'html',
        success: function (result) {
            $(".modal-body").empty();
            $(".modal-body").html(result);
        }
    });
}

function submitnotice() {
    var id = $("#iD").val();
    var content = $("#content").val();
    var isActive = $("radio:checked").val();
    var areaId = $("input[type='checkbox'][id='r']:checked");
    var url = "";
    var i = 1;
    $(areaId).each(function () {
        if (i < areaId.length) {
            url += this.attributes['value'].value + "=" + this.attributes['value'].value + "&";
        } else {
            url += this.attributes['value'].value + "=" + this.attributes['value'].value;
        }
        i++;
    });
    $("#form").attr("action", projectName + "/user/activeUser?id=" + id + "&content=" + content + "&isActive=" + isActive + "&" + url);
    $("#form").submit();
}

function closemodel() {
    $('#myModal').modal('hide');
}
