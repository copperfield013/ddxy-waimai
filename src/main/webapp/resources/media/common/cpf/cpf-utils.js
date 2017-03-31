define(function(require, exports){
	var CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
	$.extend(exports, {
		/**
		 * 判断一个是否是整数
		 */
		isInteger	: function(o){
			return (o | 0) == o;
		},
		trim		: function(str){
			if(typeof str === 'string'){
				return str.trim();
			}
			return str;
		},
		/**
		 * 获得dom的位置和尺寸
		 * 如果没有传入参数，则获取body的位置和尺寸
		 */
		getPageOffset: function($page){
			var page = document.body;
			if($page instanceof $){
				page = $page.get(0);
			}
			return {
				top		: page.offsetTop,
				left	: page.offsetLeft,
				width	: page.offsetWidth,
				height	: page.offsetHeight
			};
		},
		/**
		 * 获得随机字符串
		 * @param len 随机字符串长度，默认32
		 * @param radix 字符维度。如果传入10，则生成的字符串的每个字符都是0~9，如果传入16，则为0~F。默认16
		 * @return 随机字符串
		 */
		uuid		: function(len, radix){
			var chars = CHARS, uuid = [], i;
			len = len || 32;
			radix = radix || 16;
			if(radix > chars.length){
				radix = chars.length;
			}
			for (i = 0; i < len; i++){
				uuid[i] = chars[0 | Math.random() * radix];
			}
			return uuid.join('');
		},
		/**
		 * 遍历树形结构对象
		 * @param node 要遍历的树的根节点
		 * @param childrenGetter 从父节点获得子节点的方法
		 * @param func 遍历每个节点要执行的方法
		 * @return 调用遍历方法时，第一个return false的节点会被返回
		 * 			如果始终没有调用到return false，那么会返回一个包含所有元素的数组（先序遍历）
		 */
		iterateTree	: function(node, childrenGetter, func){
			var array = [node];
			if(node && typeof func === 'function'){
				try{
					result = func(node);
					if(result === false){
						return node;
					}
				}catch(e){
					console.error(e);
				}
				if(typeof childrenGetter === 'function'){
					var children = childrenGetter(node);
					if($.isArray(children)){
						for(var i in children){
							var itrResult = this.iterateTree(children[i]);
							if(typeof result === 'object'){
								return result;
							}else if($.isArray(itrResult)){
								$.merge(array, itrResult);
							}
						}
					}
				}
			}
			return array;
		},
		/**
		 * 如果值是一个函数，那么会根据参数计算之后得到返回值
		 * 否则会直接返回对象
		 * 如果还传入了checkFn，那么会在返回之前先检验是否符合，符合的话返回值，否则返回undefined
		 * @param val {any} 要返回的值或者要计算的函数
		 * @param args {Array<any>} 如果val是函数的话，那么会传入的参数
		 * @param checkFn {Function(any)} 用于检验值的函数，如果传入了函数，那么仅返回true的时候会校验成功并返回
		 */
		applyValue	: function(val, args, checkFn){
			var checkFn = typeof checkFn === 'function'? checkFn: returnTrue;
			var value = val;
			if(typeof val === 'function'){
				value = val.apply(this, args);
			}
			return checkFn(value) == true? value: undefined;
		},
		/**
		 * 切换jquery对象的class。
		 * 如果jquery对象本来都没有class1和class2,或者都有class1和class2，则不处理
		 * @param jqObj {jQuery}要切换的JqueryDom对象
		 * @param class1 {String}
		 * @param class2 {String}
		 * @param flag {Boolean} 为true的话，jqObj一定有class1没有class2；为false的话，jqObj有class2没有class1
		 */
		switchClass	: function(jqObj, class1, class2, flag){
			var hasClass1 = jqObj.is('.' + class1),
				hasClass2 = jqObj.is('.' + class2);
			if((hasClass1 ^ hasClass2) === 1){
				flag = flag || hasClass2;
				jqObj.toggleClass(class1, flag);
				jqObj.toggleClass(class2, !flag);
			}
			return this;
		},
		/**
		 * 将dom元素的内容设置为数字编辑器
		 */
		NumberEdit	: function(_param, whenEnter){
			var defaultParam = {
				$target		: null,
				scope		: document,
				whenEnter	: $.noop
			};
			var param = {};
			if(_param instanceof $){
				$.extend(param, defaultParam, {
					$target		: _param,
					whenEnter	: whenEnter
				});
			}else{
				$.extend(param, defaultParam, _param);
			}
			$(param.scope).keydown(function(e){
				if($(e.target).is(':text,textarea')){
					return;
				}
				console.log(e.keyCode);
				var $number = param.$target,
					oNumber = $number.text();
				if(e.keyCode >= 48 && e.keyCode <= 57){
					oNumber = oNumber === '0'? '': oNumber;
					oNumber += String(e.keyCode - 48);
					$number.text(oNumber);
				}else if(e.keyCode == 13){
					var _return = param.whenEnter(parseInt(oNumber));
					if(_return !== false){
						//回车时将数字置零
						$number.text(0);
					}
				}else if(e.keyCode == 8){
					if(oNumber.length > 1){
						oNumber = oNumber.substr(0, oNumber.length - 1);
					}else{
						oNumber = '0';
					}
					$number.text(oNumber);
				}
			});
		},
		/**
		 * 将滚动条滑动到指定元素的位置
		 */
		scrollTo	: function($container, position){
			var $position
			if(position instanceof $){
				$position = position;
			}else{
				$position = $container.children().last()
			}
			$container.scrollTop(
				$position.offset().top - $container.offset().top + $container.scrollTop()
			);
		},
		createPrintTemp	: function(){
			//创建打印模板，需要
			
		},
		/**
		 * 验证联系号码的格式
		 */
		testContactNumber	: function(contact){
			return /^1[34578]\d{9}$/.test(contact)
			|| /^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,8}$/.test(contact)
		},
		/**
		 * 格式化日期
		 */
		formatDate			: function(date, fmt){
			if(typeof date === 'string' && fmt === undefined){
				fmt = date;
				date = new Date();
			}
			if(date instanceof Date){
				var o = { 
						"M+" : date.getMonth()+1,                 //月份 
						"d+" : date.getDate(),                    //日 
						"h+" : date.getHours(),                   //小时 
						"m+" : date.getMinutes(),                 //分 
						"s+" : date.getSeconds(),                 //秒 
						"q+" : Math.floor((date.getMonth()+3)/3), //季度 
						"S"  : date.getMilliseconds()             //毫秒 
				}; 
				if(/(y+)/.test(fmt)) {
					fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
				}
				for(var k in o) {
					if(new RegExp("("+ k +")").test(fmt)){
						fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
					}
				}
				return fmt; 
			}
		},
		/**
		 * 获得当天时间的零点Date对象
		 * @param date {Date} 传入要获取零点的那天的某个时间对象，不传入时，取当天
		 * @return {Date} 零点时的时间对象
		 */
		getDate			: function(date, incDay){
			if(!(date instanceof Date)){
				date = new Date();
			}else{
				date = new Date(date);
			}
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			date.setMilliseconds(0);
			if(typeof incDay === 'number' && incDay > 0){
				date = new Date(Date.parse(date) + incDay * 86400000);
			}
			return date;
		}
		
	});
	
	function returnTrue(){return true;}
	function returnFalse(){return false;}
});