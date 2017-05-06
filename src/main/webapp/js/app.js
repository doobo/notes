document.write("<script language='javascript' src='../js/ImageCropper/dist/lrz.bundle.js'></script>");
//添加代码
var CodeMirrorButton = function(context) {
	var ui = $.summernote.ui;
	// create button
	var button = ui.button({
		contents: '<span class="fa fa-file-code-o"></span>',
		tooltip: '添加代码',
		click: function() {
			qiao.bs.dialog({
			    url : './../js/CodeMirror/index.html',
			    title :'输入或粘贴代码',
                okbtn:'插入到编辑器',
                callback: function(){

                }
			},function(){
                var codeTxt = codeMirrorEditor.getValue();
				codeTxt = $("#editor").text(codeTxt).html();
				codeTxt = $('#summernote').summernote('code')
					+'<pre class="prettyprint linenums Lang-js">'+codeTxt+'</pre><br />';
                //console.log(codeTxt);
                context.invoke('code', codeTxt);
                PR.prettyPrint();
			    return true;
			});
		}
	});
	return button.render(); // return button as jquery object 
}
//换行
var tobr = function(context) {
	var ui = $.summernote.ui;
	// create button
	var button = ui.button({
		contents: '<span class="fa fa-arrow-down"></span>',
		tooltip: '未尾换行/下一行',
		click: function() {
		 codeTxt = $('#summernote').summernote('code')+'<br />';
         context.invoke('code', codeTxt);
		}
	});
	return button.render(); // return button as jquery object 
}

var noteConf = {
	height: 300,
	focus: true,
	lang: 'zh-CN',
	fontSizes: ['12', '14', '16', '18', '20', '22', '26', '28', '36'],
	fontNames: ['Microsoft YaHei', 'Arial', 'Arial Black', 'SimSun', 'SimHei', 'NSimSun', 'FangSong', 'KaiTi', 'STXingkai', 'Times'],
	toolbar: [
		['style', ['bold', 'italic', 'underline', 'clear','strikethrough', 'hr']],
		['br',['br', 'codemirror']],
		['fontsize', ['style', 'fontname', 'fontsize', 'color', 'height']],
		['para', ['ul', 'ol', 'paragraph']],
		['Insert', ['table', 'link', 'picture']],
		['Misc', ['undo', 'redo',  'fullscreen']],
	],
    codemirror: {
        mode: 'text/html',
        htmlMode: true,
        lineNumbers: true,
        theme: 'monokai'
    },
	callbacks: {
		onImageUpload:function(files,editor, $editable){
			// console.log('image upload:', files, editor, $editable);
            lrz(files[0],
                {
                    width:'960',
                    height:'260',
                    quality:'0.6'
                })
                .then(function (rst) {
                    // 处理成功会执行
                    console.log(rst.base64.length);
                    // console.log(rst.base64Len);
                    // console.log(rst.fileLen);
                    // console.log(rst.origin);
                    uploadNoteImage(rst.base64);
                })
                .catch(function (err) {
                    // 处理失败会执行
                    alert('图片上传失败！');
                })
                .always(function () {
                    // 不管是成功失败，都会执行
                });
		}
	},
	buttons: {
		codemirror: CodeMirrorButton,
		br: tobr
	},
    hintDirection: 'top',
    hint:[]
};

var uploadNoteImage = function (dataUrl) {
    var type = dataUrl
        .substring(dataUrl.indexOf('/')+1,dataUrl.indexOf(';'));
    dataUrl = dataUrl
        .substring(dataUrl.indexOf(',')+1,dataUrl.length);
    var data = {};
    data.imgData = dataUrl;
    data.base64Len = dataUrl.length;
    data.type = type;
    $.ajax({
        url: "./uploadNoteImage",
        data: data,
        type: "POST",
        success: function(result) {
            if(!result){
                $('#summernote')
                    .summernote('insertImage','data:image/jpeg;base64,'
                        +dataUrl);
            }else{
                $('#summernote')
                    .summernote('insertImage','../images/notes/'
                        + result);
            }
        }
    });
}

var getImages = function (el,options) {
    var summer = $(el).summernote('code');
    var images = $(summer).find('img');
    options = '';
    images.each(function () {
        var tmp = this['style'].cssText;
        tmp = tmp.substring(tmp.indexOf(":")+1,tmp.indexOf("px"));
        if(!isEmpty(this.src) && !isEmpty(tmp)){
            options = options + this.src + '>'+$.trim(tmp)+"~";
        }
    });
    return options;
}

var addMType = function () {
    qiao.bs.dialog({
        id : "addMType",
        url : 'addMainTypeStatic.htm',
        title : '添加笔记主分类',
        okbtn : '提交',
        keyboard : false,
        backdrop : false
    },function () {
        var $form = $('#addMType').find('form');
        var data = {
            typeName:$.trim($form.find("input[name=typename]").val()),
            description:$.trim($form.find("textarea[name=description]").val())
        };
        var is_exit = false;
        exitSelectValue('#mainType',data.typeName,function (text) {
            is_exit = true;
            alert('已经存在分类信息：'+text);
            return;
        });
        if(is_exit) return false;
        if(!verifyStringLength(data.typeName,3,20)){
            alert('添加类别标题必须在3-20之内');
            return false;
        }
        if(!verifyStringLength(data.description,5,100)){
            alert('类别描述应该在5-100之间');
            return false;
        }else
            $.ajax({
                type: "post",
                url: "addMainType",
                data:data,
                async: true,
                xhrFields: {
                    withCredentials: true
                },
                success: function(result) {
                    if(typeof result.id == "undefined"){
                        alert('分类名已经存在，添加失败！')
                    }else
                    changSelect1(result.id,function () {
                        changSelect2(result.id);
                    });
                }
            });
        return true;
    });
}

var addCHType = function () {
    var mid=$('#mainType').select2("val");
    qiao.bs.dialog({
        id : "addCHType",
        url : 'addChildType.htm',
        title : '添加笔记子分类',
        okbtn : '提交',
        keyboard : false,
        backdrop : false
    },function () {
        var $form = $('#addCHType').find('form');
        var data = {
            typeName:$.trim($form.find("input[name=typename]").val()),
            description:$.trim($form.find("textarea[name=description]").val()),
            mid:mid
        };

        var is_exit = false;
        exitSelectValue('#childType',data.typeName,function (text) {
            is_exit = true;
            alert('已经存在分类信息：'+text);
            return;
        });
        if(is_exit) return false;

        if(!verifyStringLength(data.typeName,3,20)){
            alert('添加类别标题必须在3-20之内');
            return false;
        }
        if(!verifyStringLength(data.description,5,100)){
            alert('类别描述应该在5-100之间');
            return false;
        }
        if(!verify(data.mid,'int')){
        	alert('主类别不存在');
			return false;
		}else
            $.ajax({
                type: "post",
                url: "addChild",
                data:data,
                async: true,
                xhrFields: {
                    withCredentials: true
                },
                success: function(result) {
                    if(typeof result.id == 'undefined'){
                        alert('分类名已经存在，添加失败！')
                    }
                    changSelect2(mid,result.id);
                }
            });
        return true;
    });
}

var changSelect1 = function (chid,callback) {
    $("#mainType").html('');
    mainData = [];
    $.ajax({
        type:'post',
        url:'getMainType',
        data:null,
        success:function (data) {
            $.each(data,function () {
                var tmp = {id:this.id,text:this.typename};
                mainData.push(tmp);
            });
        },
        complete:function () {
            $("#mainType").select2({
                data: mainData,
            });
            if(typeof(chid) !== "undefined"){
                $("#mainType").select2().
                val(chid).trigger("change");
            }
            if (typeof callback === "function"){
                //alert(callback);
                callback();
            }
        }
    });
}

/**
 * 根据主id实现二级联动
 * @param id
 */
var changSelect2  = function (id,chid) {
    $("#childType").html('');
    childData = [];
    $.ajax({
        type:'post',
        url:'getChildType',
        data:{mid:id},
        success:function (data) {
            $.each(data,function () {
                var tmp = {id:this.id,text:this.typename};
                childData.push(tmp);
            });
        },
        complete:function () {
            $("#childType").select2({
                data: childData,
            });
            if(typeof(chid) !== 'undefined'){
                $("#childType").select2().
                val(chid).trigger("change");
            }
        }
    });
}

var exitSelectValue = function (select2_el,value,callback) {
    var options = $($(select2_el).select2()[0]).find("option");
    $.each(options,function () {
        if($.trim($(this).text()) == value){
            if(typeof callback == "function"){
                callback($.trim($(this).text()));
            }
            return false;
        }
    });
    return true;
}

//select2的选项更改事件，只有选项值改变时才触发事件
$('#mainType').on('select2:select', function (evt) {
    //获取选中值的id
    var id =$('#mainType').select2("val");
    changSelect2(id);
});

//去除字符串里面的html标签
function removeHTMLTag(str) {
    str = str.replace(/<\/?[^>]*>/g, ''); //去除HTML tag
    str = str.replace(/[ | ]*\n/g, ''); //去除行尾空白\n
    str = str.replace(/\n[\s| | ]*\r/g, ''); //去除多余空行\n
    str = str.replace(/&nbsp;/ig, ''); //去掉空格
    str = str.replace(/&gt;/ig, ">"); //替换成'>'
    str = str.replace(/&lt;/ig, "<"); //替换成'<'
    str = str.replace(/&quot;/ig, "\""); //替换成'"'
    str = str.replace(/&#39;/ig, "\""); //替换成'"'
    str = str.replace(/&#9;/ig, ""); //替换tab
    str = str.replace(/&amp;/ig, "&"); //替换&
    str = str.replace(/&times;/ig, "×"); //替换*
    str = str.replace(/&divide;/ig, "÷"); //替换除号
    str = str.replace(/&copy;/ig, "©"); //替换版权
    str = str.replace(/&reg;/ig, "®"); //替换商标注册
    str = str.replace(/(^\s+)|(\s+$)/g,""); //去除行尾和行首空白
    str = str.replace(/\s+/g, ' ');//多个空格改为一个空格
    //str = str.replace(/\s/g,"");//去除行内所有空白
    return str;
}

/*表情符号输入，要联网获取github的表情包，使用 ‘：’+表情英文
var self = this;
var self = this;
// load github's emoji list
$.ajax({
    url: 'https://api.github.com/emojis'
}).then(function (data) {
    var emojis = Object.keys(data);
    var emojiUrls = data;
    noteConf['hint'] = [{
        search: function (keyword, callback) {
            callback($.grep(emojis, function (item) {
                return item.indexOf(keyword)  === 0;
            }));
        },
        match: /\B:([\-+\w]+)$/,
        template: function (item) {
            var content = emojiUrls[item];
            return '<img src="' + content + '" width="20" /> :' + item + ':';
        },
        content: function (item) {
            var url = emojiUrls[item];
            if (url) {
                return $('<img />').attr('src', url).css('width', 20)[0];
            }
            return '';
        }
    }];
    $('.summernote').summernote(noteConf);
});
*/