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
				codeTxt = '<pre class="prettyprint linenums Lang-js">'+codeTxt+'</pre><br /><br/>';
                // console.log(codeTxt);
                codeTxt = $('#summernote').summernote('code') + codeTxt;
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
		tooltip: '换行/下一行',
		click: function() {
		 codeTxt = $('#summernote').summernote('code') + '<br />';
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
		//onImageUpload:function(files,editor, $editable){
		//	console.log('image upload:', files, editor, $editable);
		//}
	},
	buttons: {
		codemirror: CodeMirrorButton,
		br: tobr
	},
    hintDirection: 'top',
    hint:[]
};

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
