var params = new Object();

//验证IP
params.ip = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

//验证EMAIL
params.email = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;

//验证日期格式2009-07-13
params.date = /^\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2]\d|3[0-1])$/;

//验证时间格式16:55:39
params.time = new RegExp("^([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$");

//是否由英文数字字母下划线组成的字符串
params.username = /^[a-zA-Z0-9_]+$/;

//验证字符是否为整数
params.int = /^[-+]?\d*$/;

//验证双精度字符数字
params.double = /^[-\+]?\d+(\.\d+)?$/;

//验证英文字符
params.englist = /^[a-zA-Z]+$/;

//验证中文字符,含中文标点符号等
params.chinese = /^[\u0391-\uFFE5]+$/;

//验证用户昵称，字母、数字、下划线、空格、+、中文(不含中文标点符号)
params.nickname = /^[a-zA-Z0-9(\.)(\+)(\_)(-)(\ )\u4E00-\uFA29\uE7C7-\uE7F3]+$/;

//验证大于或等于1小于等于20位长度连续的字符串
params.isnull = /^\S{1,20}$/ig;

//验证函数
function verify(str,pat)
{
    var thePat;
    thePat = params[pat];
    if(thePat.test(str)){
        return true;
    }else{
        return false;
    }
}

//验证字符串长度范围:[min,max],只能验证连续的字符串
function verifyStrLength(str,min,max) {
    var min = min || 1;
    var max = max || '';
    var thePat = new RegExp("^\\S{"+min+","+max+"}$");
    if(thePat.test(str)){
        return true;
    }else{
        return false;
    }
}

function  verifyStringLength(str,min,max) {
    var min = min || 1;
    var max = max || 1;
    if(str.length >= min && str.length <= max){
        return true;
    }else {
        return false;
    }
}

//字符串是否为空
function  isEmpty(str) {
    var str = ''+str || '';
    if(str.length == 0){
        return true;
    }else{
        return false;
    }
}

//解码url里面编码字符，并且去掉多余的空格(首页空格去掉，行内多个空格改一个空格)
function decodeUrlKey(key) {
    var key = key || '';
    key = key.replace(/\+/g, ' ');//还原+为空格
    key = $.trim(key);
    key = key.replace(/\s+/g, ' ');//多个空格改为一个空格
    key = decodeURIComponent(key);
    key = key.replace(/\s+/g, ' ');//多个空格改为一个空格
    key = $.trim(key);
    return key;
}

//替换单个特殊正则字符
function privateReplace(str) {
    if(/\~/.test(str))
        str = str.replace(/\~/,'(\\~)');
    else if(/\!/.test(str))
        str = str.replace(/\!/,'(\\!)');
    else if(/\@/.test(str))
        str = str.replace(/\@/,'(\\@)');
    else if(/\#/.test(str))
        str = str.replace(/\#/,'(\\#)');
    else if(/\$/.test(str))
        str = str.replace(/\$/,'(\\$)');
    else if(/\%/.test(str))
        str = str.replace(/\%/,'(\\%)');
    else if(/\^/.test(str))
        str = str.replace(/\^/,'(\\^)');
    else if(/\&/.test(str))
        str = str.replace(/\&/,'(\\&)');
    else if(/\*/.test(str))
        str = str.replace(/\*/,'(\\*)');
    else if(/\(/.test(str))
        str = str.replace(/\(/,'(\\()');
    else if(/\)/.test(str))
        str = str.replace(/\)/,'(\\))');
    else if(/\_/.test(str))
        str = str.replace(/\_/,'(\_)');
    else if(/\+/.test(str))
        str = str.replace(/\+/,'(\\+)');
    else if(/\-/.test(str))
        str = str.replace(/\-/,'(\\-)');
    else if(/\ /.test(str))
        str = str.replace(/\ /,'(\\ )');
    else if(/\=/.test(str))
        str = str.replace(/\=/,'(\\=)');
    else if(/\[/.test(str))
        str = str.replace(/\[/,'(\\[)');
    else if(/\]/.test(str))
        str = str.replace(/\]/,'(\\])');
    else if(/\{/.test(str))
        str = str.replace(/\{/,'(\\{)');
    else if(/\}/.test(str))
        str = str.replace(/\}/,'(\\})');
    else if(/\|/.test(str))
        str = str.replace(/\|/,'(\\|)');
    else if(/\\/.test(str))
        str = str.replace(/\\/,'(\\\)');
    else if(/\;/.test(str))
        str = str.replace(/\;/,'(\\;)');
    else if(/\:/.test(str))
        str = str.replace(/\:/,'(\\:)');
    else if(/\'/.test(str))
        str = str.replace(/\'/,'(\\\')');
    else if(/\"/.test(str))
        str = str.replace(/\"/g,'(\\")');
    else if(/\,/.test(str))
        str = str.replace(/\,/,'(\\,)');
    else if(/\./.test(str))
        str = str.replace(/\./,'(\\.)');
    else if(/\//.test(str))
        str = str.replace(/\//,'(\\/)');
    else if(/\</.test(str))
        str = str.replace(/\</,'(\\<)');
    else if(/\>/.test(str))
        str = str.replace(/\>/,'(\\>)');
    else if(/\?/.test(str))
        str = str.replace(/\?/,'(\?)');
    return str;
}
//替换特殊正则字符~!@#$%^&*()-_+=[]{}|\;:'",./<>?
function replaceSpecialChar(str) {
    var str = str || '';
    var tmp = '';
    for(var i = 0;i<str.length;i++){
        tmp = tmp + privateReplace(str.substring(i,i+1));
    }
    return tmp;
}

//清楚特殊正则字符~!@#$%^&*()-_+=[]{}|\;:'",./<>?
function clearSpecialChar(str) {
    var str = str || '';
    return str.replace(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/)(\<)(\>)(\?)(\)]+/g, '');
}

function superTrim(str) {
    str = str || '';
    str = str.replace(/\s+/g, ' ')
    str = str.replace(/^(\ )/,'');
    return str.replace(/(\ )$/,'');
}

//清楚常用的特殊字符,~!@#$%^&*()-_+=[]{}|\;:'",./<>?`！@#￥……&*（）&;|{}【】‘；：”“'。，、？
function clearCommonChar(str){
    var str = str || '';
    var pattern = /[(`)(‘)(！)(￥)(……)(（)(）)(【)(】)(；)(：)(”)(“)(。)(，)(、)(？)(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/)(\<)(\>)(\?)(\)]+/g;
    str = str.replace(pattern,'');
    return superTrim(str);
}

//判断str里面是否存在param,不区分大小写
function isExitStringParam(str,param) {
    var myReg;
    param = replaceSpecialChar(param);
    eval("var myReg = /[" + param + "]/ig;");
    return myReg.test(str);
}

//随机获取指定范围的随机整数
function randomBy(under, over){
    if(!verify(under,'int')) under = false;
    if(!verify(over,'int')) over = false;
    var under = under || 0;
    var over = over || 10;
    switch(arguments.length){
        case 1: return parseInt(Math.random()*under+1);
        case 2: return parseInt(Math.random()*(over-under+1) + under);
        default: return 0;
    }
}
//跳转到错误页面，并把原页面url作为参数传递过去
function toErrorPage(url) {
    var url = url || '404.html';
    window.location.href=url+'?url='
        +encodeURIComponent(window.location.href);
}