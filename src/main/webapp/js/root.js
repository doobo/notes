/**封装好的分页工具-jquery.twbsPagination.js
 * options{el:'#id'---显示分页的元素，div的id等
 * ,visiblePages:7--可见页码
 * ,pageCount:1--页总数
 * ,isOne:true,只有一页时不显示页码，false,只有一页时显示页码，默认false;
 * ,callback:function(page)},初始化和下一页执行的方法
 * -->page为下一页页码，从1开始
 * @param options
 */
var resetPageNum = function (options) {
    var options = options || {};
    var el = options.el ||  '#pagination';
    var pageCount = options.pageCount || 1;
    var isOne = options.isOne || false;
    var visiblePages = options.visiblePages || 7;
    var callback = options.callback || function(){};
    $(el).html('');
    if(isOne && pageCount < 2) return;
    var t_id = 'page_id'+new Date().getTime();
    $(el).html(
        '<ul id="'+t_id+'" class="pagination-sm"></ul>');
    $("#"+t_id).twbsPagination({
        totalPages: pageCount,
        visiblePages: visiblePages,
        first:'首页',
        last:'末页',
        prev:'上一页',
        next:'下一页',
        onPageClick: function (event, page) {
            var el_li =
                '<li class="disabled">' +
                '<a class="page-link">总页数:' +
                pageCount+'</a></li>';
            $("#"+t_id).append(el_li);
            if(typeof callback == "function"){
                callback(page);
            }
        }
    });
}

//判读是否登录，没登录，重新加载
var is_login = function (url,callback) {
    var url = url || '../isRoot';
    $.ajax({
        type:'post',
        url:url,
        success:function (result) {
            if(!result){
                location.reload();
                return false;
            }else {
                if(typeof callback == "function"){
                    callback();
                }
                return true;
            }
        }
    });
}

//截取字符串，并在超出长度后用户...代替
var subStr = function (str,max) {
    var max = max || 20;
    if(str.length > max){
        str = str.substring(0,max)+'...';
    }
    return str;
}

/**
 * 复选框全选或全不选的函数
 * @param {Object} allName 全选框的name，字符参数-'name'
 * @param {Object} childName 子复选框的name，字符参数-'name'
 * callback(isChecked,child) isChecked 选中状态（true,false）,child 所有子checkbox
 */
function checkAll(allName, childName,callback) {
    var allName = allName || 'checkAll';
    var childName = childName|| 'childBox';
    var isChecked = $("input[name='" + allName + "']").prop("checked");
    if(typeof callback == "function"){
        var child = $("input[name='" + childName + "']");
        callback(isChecked,child);
    }
    // $("input[name='" + childName + "']").each(function() {
    //     $(this).prop("checked", isChecked);
    // });
}

//通过get方式获取页面代码，并且通过html()添加到el里面去
var getPageByUrl = function (url, data,el) {
    var url = url || '';
    var data = data || {};
    var el = el || '#mainContent';
    $.ajax({
        url: url,
        global: false,
        type: "get",
        data: data,
        cache:false,
        dataType: "html",
        success: function (msg) {
            $(el).html(msg);
        }
    });
}