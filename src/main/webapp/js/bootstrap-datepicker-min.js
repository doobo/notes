(function(d,s){function r(){return new Date(Date.UTC.apply(Date,arguments))}function u(){var a=new Date;return r(a.getFullYear(),a.getMonth(),a.getDate())}function C(a,b){return a.getUTCFullYear()===b.getUTCFullYear()&&a.getUTCMonth()===b.getUTCMonth()&&a.getUTCDate()===b.getUTCDate()}function z(a){return function(){return this[a].apply(this,arguments)}}function D(a,b){function c(i,o){return o.toLowerCase()}a=d(a).data();var e={},f,g=new RegExp("^"+b.toLowerCase()+"([A-Z])");b=new RegExp("^"+b.toLowerCase());
for(var h in a)if(b.test(h)){f=h.replace(g,c);e[f]=a[h]}return e}function E(a){var b={};if(!n[a]){a=a.split("-")[0];if(!n[a])return}var c=n[a];d.each(F,function(e,f){if(f in c)b[f]=c[f]});return b}var A=function(){var a={get:function(b){return this.slice(b)[0]},contains:function(b){b=b&&b.valueOf();for(var c=0,e=this.length;c<e;c++)if(this[c].valueOf()===b)return c;return-1},remove:function(b){this.splice(b,1)},replace:function(b){if(b){d.isArray(b)||(b=[b]);this.clear();this.push.apply(this,b)}},
clear:function(){this.length=0},copy:function(){var b=new A;b.replace(this);return b}};return function(){var b=[];b.push.apply(b,arguments);d.extend(b,a);return b}}(),t=function(a,b){this._process_options(b);this.dates=new A;this.viewDate=this.o.defaultViewDate;this.focusDate=null;this.element=d(a);this.isInline=false;this.isInput=this.element.is("input");this.hasInput=(this.component=this.element.hasClass("date")?this.element.find(".add-on, .input-group-addon, .btn"):false)&&this.element.find("input").length;
if(this.component&&this.component.length===0)this.component=false;this.picker=d(l.template);this._buildEvents();this._attachEvents();this.isInline?this.picker.addClass("datepicker-inline").appendTo(this.element):this.picker.addClass("datepicker-dropdown dropdown-menu");this.o.rtl&&this.picker.addClass("datepicker-rtl");this.viewMode=this.o.startView;this.o.calendarWeeks&&this.picker.find("tfoot .today, tfoot .clear").attr("colspan",function(c,e){return parseInt(e)+1});this._allow_update=false;this.setStartDate(this._o.startDate);
this.setEndDate(this._o.endDate);this.setDaysOfWeekDisabled(this.o.daysOfWeekDisabled);this.setDatesDisabled(this.o.datesDisabled);this.fillDow();this.fillMonths();this._allow_update=true;this.update();this.showMode();this.isInline&&this.show()};t.prototype={constructor:t,_process_options:function(a){this._o=d.extend({},this._o,a);var b=this.o=d.extend({},this._o);a=b.language;if(!n[a]){a=a.split("-")[0];if(!n[a])a=y.language}b.language=a;switch(b.startView){case 2:case "decade":b.startView=2;break;
case 1:case "year":b.startView=1;break;default:b.startView=0}switch(b.minViewMode){case 1:case "months":b.minViewMode=1;break;case 2:case "years":b.minViewMode=2;break;default:b.minViewMode=0}b.startView=Math.max(b.startView,b.minViewMode);if(b.multidate!==true){b.multidate=Number(b.multidate)||false;if(b.multidate!==false)b.multidate=Math.max(0,b.multidate)}b.multidateSeparator=String(b.multidateSeparator);b.weekStart%=7;b.weekEnd=(b.weekStart+6)%7;var c=l.parseFormat(b.format);if(b.startDate!==
-Infinity)b.startDate=b.startDate?b.startDate instanceof Date?this._local_to_utc(this._zero_time(b.startDate)):l.parseDate(b.startDate,c,b.language):-Infinity;if(b.endDate!==Infinity)b.endDate=b.endDate?b.endDate instanceof Date?this._local_to_utc(this._zero_time(b.endDate)):l.parseDate(b.endDate,c,b.language):Infinity;b.daysOfWeekDisabled=b.daysOfWeekDisabled||[];if(!d.isArray(b.daysOfWeekDisabled))b.daysOfWeekDisabled=b.daysOfWeekDisabled.split(/[,\s]*/);b.daysOfWeekDisabled=d.map(b.daysOfWeekDisabled,
function(f){return parseInt(f,10)});b.datesDisabled=b.datesDisabled||[];if(!d.isArray(b.datesDisabled)){a=[];a.push(l.parseDate(b.datesDisabled,c,b.language));b.datesDisabled=a}b.datesDisabled=d.map(b.datesDisabled,function(f){return l.parseDate(f,c,b.language)});a=String(b.orientation).toLowerCase().split(/\s+/g);var e=b.orientation.toLowerCase();a=d.grep(a,function(f){return/^auto|left|right|top|bottom$/.test(f)});b.orientation={x:"auto",y:"auto"};if(!(!e||e==="auto"))if(a.length===1)switch(a[0]){case "top":case "bottom":b.orientation.y=
a[0];break;case "left":case "right":b.orientation.x=a[0];break}else{e=d.grep(a,function(f){return/^left|right$/.test(f)});b.orientation.x=e[0]||"auto";e=d.grep(a,function(f){return/^top|bottom$/.test(f)});b.orientation.y=e[0]||"auto"}if(b.defaultViewDate){a=b.defaultViewDate.year||(new Date).getFullYear();b.defaultViewDate=r(a,b.defaultViewDate.month||0,b.defaultViewDate.day||1)}else b.defaultViewDate=u();b.showOnFocus=b.showOnFocus!==s?b.showOnFocus:true},_events:[],_secondaryEvents:[],_applyEvents:function(a){for(var b=
0,c,e,f;b<a.length;b++){c=a[b][0];if(a[b].length===2){e=s;f=a[b][1]}else if(a[b].length===3){e=a[b][1];f=a[b][2]}c.on(f,e)}},_unapplyEvents:function(a){for(var b=0,c,e,f;b<a.length;b++){c=a[b][0];if(a[b].length===2){f=s;e=a[b][1]}else if(a[b].length===3){f=a[b][1];e=a[b][2]}c.off(e,f)}},_buildEvents:function(){var a={keyup:d.proxy(function(b){d.inArray(b.keyCode,[27,37,39,38,40,32,13,9])===-1&&this.update()},this),keydown:d.proxy(this.keydown,this),paste:d.proxy(this.paste,this)};if(this.o.showOnFocus===
true)a.focus=d.proxy(this.show,this);if(this.isInput)this._events=[[this.element,a]];else if(this.component&&this.hasInput)this._events=[[this.element.find("input"),a],[this.component,{click:d.proxy(this.show,this)}]];else if(this.element.is("div"))this.isInline=true;else this._events=[[this.element,{click:d.proxy(this.show,this)}]];this._events.push([this.element,"*",{blur:d.proxy(function(b){this._focused_from=b.target},this)}],[this.element,{blur:d.proxy(function(b){this._focused_from=b.target},
this)}]);this.o.immediateUpdates&&this._events.push([this.element,{"changeYear changeMonth":d.proxy(function(b){this.update(b.date)},this)}]);this._secondaryEvents=[[this.picker,{click:d.proxy(this.click,this)}],[d(window),{resize:d.proxy(this.place,this)}],[d(document),{mousedown:d.proxy(function(b){this.element.is(b.target)||this.element.find(b.target).length||this.picker.is(b.target)||this.picker.find(b.target).length||d(this.picker).hide()},this)}]]},_attachEvents:function(){this._detachEvents();
this._applyEvents(this._events)},_detachEvents:function(){this._unapplyEvents(this._events)},_attachSecondaryEvents:function(){this._detachSecondaryEvents();this._applyEvents(this._secondaryEvents)},_detachSecondaryEvents:function(){this._unapplyEvents(this._secondaryEvents)},_trigger:function(a,b){b=this._utc_to_local(b||this.dates.get(-1));this.element.trigger({type:a,date:b,dates:d.map(this.dates,this._utc_to_local),format:d.proxy(function(c,e){if(arguments.length===0){c=this.dates.length-1;e=
this.o.format}else if(typeof c==="string"){e=c;c=this.dates.length-1}e=e||this.o.format;var f=this.dates.get(c);return l.formatDate(f,e,this.o.language)},this)})},show:function(){if(!(this.element.attr("readonly")&&this.o.enableOnReadonly===false)){this.isInline||this.picker.appendTo(this.o.container);this.place();this.picker.show();this._attachSecondaryEvents();this._trigger("show");if((window.navigator.msMaxTouchPoints||"ontouchstart"in document)&&this.o.disableTouchKeyboard)d(this.element).blur();
return this}},hide:function(){if(this.isInline)return this;if(!this.picker.is(":visible"))return this;this.focusDate=null;this.picker.hide().detach();this._detachSecondaryEvents();this.viewMode=this.o.startView;this.showMode();if(this.o.forceParse&&(this.isInput&&this.element.val()||this.hasInput&&this.element.find("input").val()))this.setValue();this._trigger("hide");return this},remove:function(){this.hide();this._detachEvents();this._detachSecondaryEvents();this.picker.remove();delete this.element.data().datepicker;
this.isInput||delete this.element.data().date;return this},paste:function(a){var b;if(a.originalEvent.clipboardData&&a.originalEvent.clipboardData.types&&d.inArray("text/plain",a.originalEvent.clipboardData.types)!==-1)b=a.originalEvent.clipboardData.getData("text/plain");else if(window.clipboardData)b=window.clipboardData.getData("Text");else return;this.setDate(b);this.update();a.preventDefault()},_utc_to_local:function(a){return a&&new Date(a.getTime()+a.getTimezoneOffset()*6E4)},_local_to_utc:function(a){return a&&
new Date(a.getTime()-a.getTimezoneOffset()*6E4)},_zero_time:function(a){return a&&new Date(a.getFullYear(),a.getMonth(),a.getDate())},_zero_utc_time:function(a){return a&&new Date(Date.UTC(a.getUTCFullYear(),a.getUTCMonth(),a.getUTCDate()))},getDates:function(){return d.map(this.dates,this._utc_to_local)},getUTCDates:function(){return d.map(this.dates,function(a){return new Date(a)})},getDate:function(){return this._utc_to_local(this.getUTCDate())},getUTCDate:function(){var a=this.dates.get(-1);return typeof a!==
"undefined"?new Date(a):null},clearDates:function(){var a;if(this.isInput)a=this.element;else if(this.component)a=this.element.find("input");a&&a.val("").change();this.update();this._trigger("changeDate");this.o.autoclose&&this.hide()},setDates:function(){this.update.apply(this,d.isArray(arguments[0])?arguments[0]:arguments);this._trigger("changeDate");this.setValue();return this},setUTCDates:function(){var a=d.isArray(arguments[0])?arguments[0]:arguments;this.update.apply(this,d.map(a,this._utc_to_local));
this._trigger("changeDate");this.setValue();return this},setDate:z("setDates"),setUTCDate:z("setUTCDates"),setValue:function(){var a=this.getFormattedDate();if(this.isInput)this.element.val(a).change();else this.component&&this.element.find("input").val(a).change();return this},getFormattedDate:function(a){if(a===s)a=this.o.format;var b=this.o.language;return d.map(this.dates,function(c){return l.formatDate(c,a,b)}).join(this.o.multidateSeparator)},setStartDate:function(a){this._process_options({startDate:a});
this.update();this.updateNavArrows();return this},setEndDate:function(a){this._process_options({endDate:a});this.update();this.updateNavArrows();return this},setDaysOfWeekDisabled:function(a){this._process_options({daysOfWeekDisabled:a});this.update();this.updateNavArrows();return this},setDatesDisabled:function(a){this._process_options({datesDisabled:a});this.update();this.updateNavArrows()},place:function(){if(this.isInline)return this;var a=this.picker.outerWidth(),b=this.picker.outerHeight(),
c=d(this.o.container).width(),e=d(this.o.container).height(),f=d(this.o.container).scrollTop(),g=d(this.o.container).offset(),h=[];this.element.parents().each(function(){var j=d(this).css("z-index");j!=="auto"&&j!==0&&h.push(parseInt(j))});var i=Math.max.apply(Math,h)+10,o=this.component?this.component.parent().offset():this.element.offset(),k=this.component?this.component.outerHeight(true):this.element.outerHeight(false),q=this.component?this.component.outerWidth(true):this.element.outerWidth(false),
m=o.left-g.left;g=o.top-g.top;this.picker.removeClass("datepicker-orient-top datepicker-orient-bottom datepicker-orient-right datepicker-orient-left");if(this.o.orientation.x!=="auto"){this.picker.addClass("datepicker-orient-"+this.o.orientation.x);if(this.o.orientation.x==="right")m-=a-q}else if(o.left<0){this.picker.addClass("datepicker-orient-left");m-=o.left-10}else if(m+a>c){this.picker.addClass("datepicker-orient-right");m=o.left+q-a}else this.picker.addClass("datepicker-orient-left");a=this.o.orientation.y;
if(a==="auto"){a=-f+g-b;e=f+e-(g+k+b);a=Math.max(a,e)===e?"top":"bottom"}this.picker.addClass("datepicker-orient-"+a);if(a==="top")g+=k;else g-=b+parseInt(this.picker.css("padding-top"));this.o.rtl?this.picker.css({top:g,right:c-(m+q),zIndex:i}):this.picker.css({top:g,left:m,zIndex:i});return this},_allow_update:true,update:function(){if(!this._allow_update)return this;var a=this.dates.copy(),b=[],c=false;if(arguments.length){d.each(arguments,d.proxy(function(e,f){if(f instanceof Date)f=this._local_to_utc(f);
b.push(f)},this));c=true}else{b=(b=this.isInput?this.element.val():this.element.data("date")||this.element.find("input").val())&&this.o.multidate?b.split(this.o.multidateSeparator):[b];delete this.element.data().date}b=d.map(b,d.proxy(function(e){return l.parseDate(e,this.o.format,this.o.language)},this));b=d.grep(b,d.proxy(function(e){return e<this.o.startDate||e>this.o.endDate||!e},this),true);this.dates.replace(b);if(this.dates.length)this.viewDate=new Date(this.dates.get(-1));else if(this.viewDate<
this.o.startDate)this.viewDate=new Date(this.o.startDate);else if(this.viewDate>this.o.endDate)this.viewDate=new Date(this.o.endDate);if(c)this.setValue();else b.length&&String(a)!==String(this.dates)&&this._trigger("changeDate");!this.dates.length&&a.length&&this._trigger("clearDate");this.fill();return this},fillDow:function(){var a=this.o.weekStart,b="<tr>";if(this.o.calendarWeeks){this.picker.find(".datepicker-days thead tr:first-child .datepicker-switch").attr("colspan",function(c,e){return parseInt(e)+
1});b+='<th class="cw">&#160;</th>'}for(;a<this.o.weekStart+7;)b+='<th class="dow">'+n[this.o.language].daysMin[a++%7]+"</th>";b+="</tr>";this.picker.find(".datepicker-days thead").append(b)},fillMonths:function(){for(var a="",b=0;b<12;)a+='<span class="month">'+n[this.o.language].monthsShort[b++]+"</span>";this.picker.find(".datepicker-months td").html(a)},setRange:function(a){if(!a||!a.length)delete this.range;else this.range=d.map(a,function(b){return b.valueOf()});this.fill()},getClassNames:function(a){var b=
[],c=this.viewDate.getUTCFullYear(),e=this.viewDate.getUTCMonth(),f=new Date;if(a.getUTCFullYear()<c||a.getUTCFullYear()===c&&a.getUTCMonth()<e)b.push("old");else if(a.getUTCFullYear()>c||a.getUTCFullYear()===c&&a.getUTCMonth()>e)b.push("new");this.focusDate&&a.valueOf()===this.focusDate.valueOf()&&b.push("focused");this.o.todayHighlight&&a.getUTCFullYear()===f.getFullYear()&&a.getUTCMonth()===f.getMonth()&&a.getUTCDate()===f.getDate()&&b.push("today");this.dates.contains(a)!==-1&&b.push("active");
if(a.valueOf()<this.o.startDate||a.valueOf()>this.o.endDate||d.inArray(a.getUTCDay(),this.o.daysOfWeekDisabled)!==-1)b.push("disabled");this.o.datesDisabled.length>0&&d.grep(this.o.datesDisabled,function(g){return C(a,g)}).length>0&&b.push("disabled","disabled-date");if(this.range){a>this.range[0]&&a<this.range[this.range.length-1]&&b.push("range");d.inArray(a.valueOf(),this.range)!==-1&&b.push("selected")}return b},fill:function(){var a=new Date(this.viewDate),b=a.getUTCFullYear(),c=a.getUTCMonth();
a=this.o.startDate!==-Infinity?this.o.startDate.getUTCFullYear():-Infinity;var e=this.o.startDate!==-Infinity?this.o.startDate.getUTCMonth():-Infinity,f=this.o.endDate!==Infinity?this.o.endDate.getUTCFullYear():Infinity,g=this.o.endDate!==Infinity?this.o.endDate.getUTCMonth():Infinity,h=n[this.o.language].today||n.en.today||"",i=n[this.o.language].clear||n.en.clear||"",o;if(!(isNaN(b)||isNaN(c))){this.picker.find(".datepicker-days thead .datepicker-switch").text(n[this.o.language].months[c]+" "+b);
this.picker.find("tfoot .today").text(h).toggle(this.o.todayBtn!==false);this.picker.find("tfoot .clear").text(i).toggle(this.o.clearBtn!==false);this.updateNavArrows();this.fillMonths();h=r(b,c-1,28);c=l.getDaysInMonth(h.getUTCFullYear(),h.getUTCMonth());h.setUTCDate(c);h.setUTCDate(c-(h.getUTCDay()-this.o.weekStart+7)%7);i=new Date(h);i.setUTCDate(i.getUTCDate()+42);i=i.valueOf();c=[];for(var k;h.valueOf()<i;){if(h.getUTCDay()===this.o.weekStart){c.push("<tr>");if(this.o.calendarWeeks){k=new Date(+h+
(this.o.weekStart-h.getUTCDay()-7)%7*864E5);k=new Date(Number(k)+(11-k.getUTCDay())%7*864E5);var q=new Date(Number(q=r(k.getUTCFullYear(),0,1))+(11-q.getUTCDay())%7*864E5);c.push('<td class="cw">'+((k-q)/864E5/7+1)+"</td>")}}k=this.getClassNames(h);k.push("day");if(this.o.beforeShowDay!==d.noop){var m=this.o.beforeShowDay(this._utc_to_local(h));if(m===s)m={};else if(typeof m==="boolean")m={enabled:m};else if(typeof m==="string")m={classes:m};m.enabled===false&&k.push("disabled");if(m.classes)k=k.concat(m.classes.split(/\s+/));
if(m.tooltip)o=m.tooltip}k=d.unique(k);c.push('<td class="'+k.join(" ")+'"'+(o?' title="'+o+'"':"")+">"+h.getUTCDate()+"</td>");o=null;h.getUTCDay()===this.o.weekEnd&&c.push("</tr>");h.setUTCDate(h.getUTCDate()+1)}this.picker.find(".datepicker-days tbody").empty().append(c.join(""));var j=this.picker.find(".datepicker-months").find("th:eq(1)").text(b).end().find("span").removeClass("active");d.each(this.dates,function(w,v){v.getUTCFullYear()===b&&j.eq(v.getUTCMonth()).addClass("active")});if(b<a||
b>f)j.addClass("disabled");b===a&&j.slice(0,e).addClass("disabled");b===f&&j.slice(g+1).addClass("disabled");if(this.o.beforeShowMonth!==d.noop){var p=this;d.each(j,function(w,v){d(v).hasClass("disabled")||p.o.beforeShowMonth(new Date(b,w,1))===false&&d(v).addClass("disabled")})}c="";b=parseInt(b/10,10)*10;o=this.picker.find(".datepicker-years").find("th:eq(1)").text(b+"-"+(b+9)).end().find("td");b-=1;q=d.map(this.dates,function(w){return w.getUTCFullYear()});for(g=-1;g<11;g++){e=["year"];if(g===
-1)e.push("old");else g===10&&e.push("new");d.inArray(b,q)!==-1&&e.push("active");if(b<a||b>f)e.push("disabled");c+='<span class="'+e.join(" ")+'">'+b+"</span>";b+=1}o.html(c)}},updateNavArrows:function(){if(this._allow_update){var a=new Date(this.viewDate),b=a.getUTCFullYear();a=a.getUTCMonth();switch(this.viewMode){case 0:this.o.startDate!==-Infinity&&b<=this.o.startDate.getUTCFullYear()&&a<=this.o.startDate.getUTCMonth()?this.picker.find(".prev").css({visibility:"hidden"}):this.picker.find(".prev").css({visibility:"visible"});
this.o.endDate!==Infinity&&b>=this.o.endDate.getUTCFullYear()&&a>=this.o.endDate.getUTCMonth()?this.picker.find(".next").css({visibility:"hidden"}):this.picker.find(".next").css({visibility:"visible"});break;case 1:case 2:this.o.startDate!==-Infinity&&b<=this.o.startDate.getUTCFullYear()?this.picker.find(".prev").css({visibility:"hidden"}):this.picker.find(".prev").css({visibility:"visible"});this.o.endDate!==Infinity&&b>=this.o.endDate.getUTCFullYear()?this.picker.find(".next").css({visibility:"hidden"}):
this.picker.find(".next").css({visibility:"visible"});break}}},click:function(a){a.preventDefault();a=d(a.target).closest("span, td, th");var b,c,e;if(a.length===1)switch(a[0].nodeName.toLowerCase()){case "th":switch(a[0].className){case "datepicker-switch":this.showMode(1);break;case "prev":case "next":a=l.modes[this.viewMode].navStep*(a[0].className==="prev"?-1:1);switch(this.viewMode){case 0:this.viewDate=this.moveMonth(this.viewDate,a);this._trigger("changeMonth",this.viewDate);break;case 1:case 2:this.viewDate=
this.moveYear(this.viewDate,a);this.viewMode===1&&this._trigger("changeYear",this.viewDate);break}this.fill();break;case "today":a=new Date;a=r(a.getFullYear(),a.getMonth(),a.getDate(),0,0,0);this.showMode(-2);this._setDate(a,this.o.todayBtn==="linked"?null:"view");break;case "clear":this.clearDates();break}break;case "span":if(!a.hasClass("disabled")){this.viewDate.setUTCDate(1);if(a.hasClass("month")){e=1;c=a.parent().find("span").index(a);b=this.viewDate.getUTCFullYear();this.viewDate.setUTCMonth(c);
this._trigger("changeMonth",this.viewDate);if(this.o.minViewMode===1){this._setDate(r(b,c,e));this.showMode()}else this.showMode(-1)}else{e=1;c=0;b=parseInt(a.text(),10)||0;this.viewDate.setUTCFullYear(b);this._trigger("changeYear",this.viewDate);this.o.minViewMode===2&&this._setDate(r(b,c,e));this.showMode(-1)}this.fill()}break;case "td":if(a.hasClass("day")&&!a.hasClass("disabled")){e=parseInt(a.text(),10)||1;b=this.viewDate.getUTCFullYear();c=this.viewDate.getUTCMonth();if(a.hasClass("old"))if(c===
0){c=11;b-=1}else c-=1;else if(a.hasClass("new"))if(c===11){c=0;b+=1}else c+=1;this._setDate(r(b,c,e))}break}this.picker.is(":visible")&&this._focused_from&&d(this._focused_from).focus();delete this._focused_from},_toggle_multidate:function(a){var b=this.dates.contains(a);a||this.dates.clear();if(b!==-1){if(this.o.multidate===true||this.o.multidate>1||this.o.toggleActive)this.dates.remove(b)}else{this.o.multidate===false&&this.dates.clear();this.dates.push(a)}if(typeof this.o.multidate==="number")for(;this.dates.length>
this.o.multidate;)this.dates.remove(0)},_setDate:function(a,b){if(!b||b==="date")this._toggle_multidate(a&&new Date(a));if(!b||b==="view")this.viewDate=a&&new Date(a);this.fill();this.setValue();if(!b||b!=="view")this._trigger("changeDate");var c;if(this.isInput)c=this.element;else if(this.component)c=this.element.find("input");c&&c.change();if(this.o.autoclose&&(!b||b==="date"))this.hide()},moveMonth:function(a,b){if(!a)return s;if(!b)return a;var c=new Date(a.valueOf());a=c.getUTCDate();var e=c.getUTCMonth(),
f=Math.abs(b),g;b=b>0?1:-1;if(f===1){f=b===-1?function(){return c.getUTCMonth()===e}:function(){return c.getUTCMonth()!==g};g=e+b;c.setUTCMonth(g);if(g<0||g>11)g=(g+12)%12}else{for(var h=0;h<f;h++)c=this.moveMonth(c,b);g=c.getUTCMonth();c.setUTCDate(a);f=function(){return g!==c.getUTCMonth()}}for(;f();){c.setUTCDate(--a);c.setUTCMonth(g)}return c},moveYear:function(a,b){return this.moveMonth(a,b*12)},dateWithinRange:function(a){return a>=this.o.startDate&&a<=this.o.endDate},keydown:function(a){if(this.picker.is(":visible")){var b=
false,c,e,f=this.focusDate||this.viewDate;switch(a.keyCode){case 27:if(this.focusDate){this.focusDate=null;this.viewDate=this.dates.get(-1)||this.viewDate;this.fill()}else this.hide();a.preventDefault();break;case 37:case 39:if(!this.o.keyboardNavigation)break;c=a.keyCode===37?-1:1;if(a.ctrlKey){this.moveYear(this.dates.get(-1)||u(),c);e=this.moveYear(f,c);this._trigger("changeYear",this.viewDate)}else if(a.shiftKey){this.moveMonth(this.dates.get(-1)||u(),c);e=this.moveMonth(f,c);this._trigger("changeMonth",
this.viewDate)}else{e=new Date(this.dates.get(-1)||u());e.setUTCDate(e.getUTCDate()+c);e=new Date(f);e.setUTCDate(f.getUTCDate()+c)}if(this.dateWithinRange(e)){this.focusDate=this.viewDate=e;this.setValue();this.fill();a.preventDefault()}break;case 38:case 40:if(!this.o.keyboardNavigation)break;c=a.keyCode===38?-1:1;if(a.ctrlKey){this.moveYear(this.dates.get(-1)||u(),c);e=this.moveYear(f,c);this._trigger("changeYear",this.viewDate)}else if(a.shiftKey){this.moveMonth(this.dates.get(-1)||u(),c);e=this.moveMonth(f,
c);this._trigger("changeMonth",this.viewDate)}else{e=new Date(this.dates.get(-1)||u());e.setUTCDate(e.getUTCDate()+c*7);e=new Date(f);e.setUTCDate(f.getUTCDate()+c*7)}if(this.dateWithinRange(e)){this.focusDate=this.viewDate=e;this.setValue();this.fill();a.preventDefault()}break;case 32:break;case 13:f=this.focusDate||this.dates.get(-1)||this.viewDate;if(this.o.keyboardNavigation){this._toggle_multidate(f);b=true}this.focusDate=null;this.viewDate=this.dates.get(-1)||this.viewDate;this.setValue();this.fill();
if(this.picker.is(":visible")){a.preventDefault();if(typeof a.stopPropagation==="function")a.stopPropagation();else a.cancelBubble=true;this.o.autoclose&&this.hide()}break;case 9:this.focusDate=null;this.viewDate=this.dates.get(-1)||this.viewDate;this.fill();this.hide();break}if(b){this.dates.length?this._trigger("changeDate"):this._trigger("clearDate");var g;if(this.isInput)g=this.element;else if(this.component)g=this.element.find("input");g&&g.change()}}else if(a.keyCode===40||a.keyCode===27)this.show()},
showMode:function(a){if(a)this.viewMode=Math.max(this.o.minViewMode,Math.min(2,this.viewMode+a));this.picker.children("div").hide().filter(".datepicker-"+l.modes[this.viewMode].clsName).css("display","block");this.updateNavArrows()}};var B=function(a,b){this.element=d(a);this.inputs=d.map(b.inputs,function(c){return c.jquery?c[0]:c});delete b.inputs;x.call(d(this.inputs),b).on("changeDate",d.proxy(this.dateUpdated,this));this.pickers=d.map(this.inputs,function(c){return d(c).data("datepicker")});
this.updateDates()};B.prototype={updateDates:function(){this.dates=d.map(this.pickers,function(a){return a.getUTCDate()});this.updateRanges()},updateRanges:function(){var a=d.map(this.dates,function(b){return b.valueOf()});d.each(this.pickers,function(b,c){c.setRange(a)})},dateUpdated:function(a){if(!this.updating){this.updating=true;var b=d(a.target).data("datepicker").getUTCDate();a=d.inArray(a.target,this.inputs);var c=a-1,e=a+1,f=this.inputs.length;if(a!==-1){d.each(this.pickers,function(g,h){h.getUTCDate()||
h.setUTCDate(b)});if(b<this.dates[c])for(;c>=0&&b<this.dates[c];)this.pickers[c--].setUTCDate(b);else if(b>this.dates[e])for(;e<f&&b>this.dates[e];)this.pickers[e++].setUTCDate(b);this.updateDates();delete this.updating}}},remove:function(){d.map(this.pickers,function(a){a.remove()});delete this.element.data().datepicker}};var G=d.fn.datepicker,x=function(a){var b=Array.apply(null,arguments);b.shift();var c;this.each(function(){var e=d(this),f=e.data("datepicker"),g=typeof a==="object"&&a;if(!f){f=
D(this,"date");var h=d.extend({},y,f,g);h=E(h.language);g=d.extend({},y,h,f,g);if(e.hasClass("input-daterange")||g.inputs){f={inputs:g.inputs||e.find("input").toArray()};e.data("datepicker",f=new B(this,d.extend(g,f)))}else e.data("datepicker",f=new t(this,g))}if(typeof a==="string"&&typeof f[a]==="function"){c=f[a].apply(f,b);if(c!==s)return false}});return c!==s?c:this};d.fn.datepicker=x;var y=d.fn.datepicker.defaults={autoclose:false,beforeShowDay:d.noop,beforeShowMonth:d.noop,calendarWeeks:false,
clearBtn:false,toggleActive:false,daysOfWeekDisabled:[],datesDisabled:[],endDate:Infinity,forceParse:true,format:"mm/dd/yyyy",keyboardNavigation:true,language:"en",minViewMode:0,multidate:false,multidateSeparator:",",orientation:"auto",rtl:false,startDate:-Infinity,startView:0,todayBtn:false,todayHighlight:false,weekStart:0,disableTouchKeyboard:false,enableOnReadonly:true,container:"body",immediateUpdates:false},F=d.fn.datepicker.locale_opts=["format","rtl","weekStart"];d.fn.datepicker.Constructor=
t;var n=d.fn.datepicker.dates={en:{days:["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"],daysShort:["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],daysMin:["Su","Mo","Tu","We","Th","Fr","Sa"],months:["January","February","March","April","May","June","July","August","September","October","November","December"],monthsShort:["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],today:"Today",clear:"Clear"},"zh-CN":{days:["\u661f\u671f\u65e5","\u661f\u671f\u4e00","\u661f\u671f\u4e8c","\u661f\u671f\u4e09","\u661f\u671f\u56db","\u661f\u671f\u4e94","\u661f\u671f\u516d"],
daysShort:["\u5468\u65e5","\u5468\u4e00","\u5468\u4e8c","\u5468\u4e09","\u5468\u56db","\u5468\u4e94","\u5468\u516d"],daysMin:["\u65e5","\u4e00","\u4e8c","\u4e09","\u56db","\u4e94","\u516d"],months:["\u4e00\u6708","\u4e8c\u6708","\u4e09\u6708","\u56db\u6708","\u4e94\u6708","\u516d\u6708","\u4e03\u6708","\u516b\u6708","\u4e5d\u6708","\u5341\u6708","\u5341\u4e00\u6708","\u5341\u4e8c\u6708"],monthsShort:["\u4e00\u6708","\u4e8c\u6708","\u4e09\u6708","\u56db\u6708","\u4e94\u6708","\u516d\u6708","\u4e03\u6708","\u516b\u6708","\u4e5d\u6708","\u5341\u6708","\u5341\u4e00\u6708","\u5341\u4e8c\u6708"],today:"\u4eca\u65e5",format:"yyyy\u5e74mm\u6708dd\u65e5",weekStart:1,clear:"\u6e05\u7a7a"}},l={modes:[{clsName:"days",navFnc:"Month",navStep:1},{clsName:"months",navFnc:"FullYear",navStep:1},{clsName:"years",navFnc:"FullYear",navStep:10}],isLeapYear:function(a){return a%4===0&&a%100!==0||a%400===0},getDaysInMonth:function(a,
b){return[31,l.isLeapYear(a)?29:28,31,30,31,30,31,31,30,31,30,31][b]},validParts:/dd?|DD?|mm?|MM?|yy(?:yy)?/g,nonpunctuation:/[^ -\/:-@\[\u3400-\u9fff-`{-~\t\n\r]+/g,parseFormat:function(a){var b=a.replace(this.validParts," ").split(" ");a=a.match(this.validParts);if(!b||!b.length||!a||a.length===0)throw new Error("Invalid date format.");return{separators:b,parts:a}},parseDate:function(a,b,c){function e(){var j=this.slice(0,g[i].length),p=g[i].slice(0,j.length);return j.toLowerCase()===p.toLowerCase()}
if(!a)return s;if(a instanceof Date)return a;if(typeof b==="string")b=l.parseFormat(b);var f=/([\-+]\d+)([dmwy])/,g=a.match(/([\-+]\d+)([dmwy])/g),h,i;if(/^[\-+]\d+[dmwy]([\s,]+[\-+]\d+[dmwy])*$/.test(a)){a=new Date;for(i=0;i<g.length;i++){b=f.exec(g[i]);h=parseInt(b[1]);switch(b[2]){case "d":a.setUTCDate(a.getUTCDate()+h);break;case "m":a=t.prototype.moveMonth.call(t.prototype,a,h);break;case "w":a.setUTCDate(a.getUTCDate()+h*7);break;case "y":a=t.prototype.moveYear.call(t.prototype,a,h);break}}return r(a.getUTCFullYear(),
a.getUTCMonth(),a.getUTCDate(),0,0,0)}g=a&&a.match(this.nonpunctuation)||[];a=new Date;f={};var o=["yyyy","yy","M","MM","m","mm","d","dd"];h={yyyy:function(j,p){return j.setUTCFullYear(p)},yy:function(j,p){return j.setUTCFullYear(2E3+p)},m:function(j,p){if(isNaN(j))return j;for(p-=1;p<0;)p+=12;p%=12;for(j.setUTCMonth(p);j.getUTCMonth()!==p;)j.setUTCDate(j.getUTCDate()-1);return j},d:function(j,p){return j.setUTCDate(p)}};var k;h.M=h.MM=h.mm=h.m;h.dd=h.d;a=r(a.getFullYear(),a.getMonth(),a.getDate(),
0,0,0);var q=b.parts.slice();if(g.length!==q.length)q=d(q).filter(function(j,p){return d.inArray(p,o)!==-1}).toArray();if(g.length===q.length){var m;i=0;for(m=q.length;i<m;i++){k=parseInt(g[i],10);b=q[i];if(isNaN(k))switch(b){case "MM":k=d(n[c].months).filter(e);k=d.inArray(k[0],n[c].months)+1;break;case "M":k=d(n[c].monthsShort).filter(e);k=d.inArray(k[0],n[c].monthsShort)+1;break}f[b]=k}for(i=0;i<o.length;i++){b=o[i];if(b in f&&!isNaN(f[b])){c=new Date(a);h[b](c,f[b]);isNaN(c)||(a=c)}}}return a},
formatDate:function(a,b,c){if(!a)return"";if(typeof b==="string")b=l.parseFormat(b);c={d:a.getUTCDate(),D:n[c].daysShort[a.getUTCDay()],DD:n[c].days[a.getUTCDay()],m:a.getUTCMonth()+1,M:n[c].monthsShort[a.getUTCMonth()],MM:n[c].months[a.getUTCMonth()],yy:a.getUTCFullYear().toString().substring(2),yyyy:a.getUTCFullYear()};c.dd=(c.d<10?"0":"")+c.d;c.mm=(c.m<10?"0":"")+c.m;a=[];for(var e=d.extend([],b.separators),f=0,g=b.parts.length;f<=g;f++){e.length&&a.push(e.shift());a.push(c[b.parts[f]])}return a.join("")},
headTemplate:'<thead><tr><th class="prev">&#171;</th><th colspan="5" class="datepicker-switch"></th><th class="next">&#187;</th></tr></thead>',contTemplate:'<tbody><tr><td colspan="7"></td></tr></tbody>',footTemplate:'<tfoot><tr><th colspan="7" class="today"></th></tr><tr><th colspan="7" class="clear"></th></tr></tfoot>'};l.template='<div class="datepicker"><div class="datepicker-days"><table class=" table-condensed">'+l.headTemplate+"<tbody></tbody>"+l.footTemplate+'</table></div><div class="datepicker-months"><table class="table-condensed">'+
l.headTemplate+l.contTemplate+l.footTemplate+'</table></div><div class="datepicker-years"><table class="table-condensed">'+l.headTemplate+l.contTemplate+l.footTemplate+"</table></div></div>";d.fn.datepicker.DPGlobal=l;d.fn.datepicker.noConflict=function(){d.fn.datepicker=G;return this};d.fn.datepicker.version="1.4.1-dev";d(document).on("focus.datepicker.data-api click.datepicker.data-api",'[data-provide="datepicker"]',function(a){var b=d(this);if(!b.data("datepicker")){a.preventDefault();x.call(b,
"show")}});d(function(){x.call(d('[data-provide="datepicker-inline"]'))})})(window.jQuery);
