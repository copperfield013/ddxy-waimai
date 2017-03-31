/**
 * 
 */
define(function(require, exports, module){
	var $CPF = require('$CPF'),
		Page = require('page'),
		Dialog = require('dialog'),
		utils = require('utils')
		;
	
	$CPF.addDefaultParam({
		//是否在ajax请求时检测返回session状态
		ajaxSessionValid	: true,
		//当session无效的时候，ajax请求返回后需要跳转的地址
		sessionInvalidURL	: ''
	});
	
	/**
	 * 返回json数据时，将其转换成JsonResponse类对象
	 */
	function JsonResponse(_data){
		var defaultResponseData = {
			//当前页面的处理方式(close:关闭;refresh:重新加载;redirect:url：跳转)
			localPageAction	: '',
			//要处理响应数据的页面id
			targetPageId	: '',
			//处理方式(refresh/redirect,默认refresh，传入url时则为redirect）
			targetPageAction: '',
			//打开的页面标题
			targetPageTitle	: '',
			//如果没有找到targetPageId对应的Page，将以什么方式打开页面(redirect下)（dialog/tab,默认dialog）
			targetPageType	: '',
			//响应状态
			status			: '',
			//响应的提示语
			notice			: '',
			//响应提示语的类型
			noticeType		: ''
			
		};
		var data = $.extend({}, defaultResponseData, _data);
		
		this.getLocalPageAction = function(){
			return data.localPageAction;
		};
		
		this.getTargetPageId = function(){
			return data.targetPageId;
		};
		
		this.getTargetPageAction = function(){
			return data.targetPageAction;
		};
		
		this.getTargetPageTitle = function(){
			data.targetPageTitle;
		};
		
		this.getTargetPageType = function(){
			return data.targetPageType
		};
		this.getNotice = function(){
			return data.notice;
		};
		this.getNoticeType = function(){
			return data.noticeType;
		};
		
		this.doAction = function(page){
			if(page instanceof $){
				page = page.getLocatePage();
			}
			var localPageAction = this.getLocalPageAction();
			if(localPageAction && page instanceof Page){
				//处理当前页面
				_doAction(localPageAction, page);
			}
			var tPageId = this.getTargetPageId(),
				tPageAction = this.getTargetPageAction()
				;
			if(tPageId){
				var tPage = Page.getPage(tPageId);
				if(tPage instanceof Page){
					_doAction(tPageAction, tPage, this.getTargetPageTitle());
				}
			}
			var notice = this.getNotice(),
				noticeType = this.getNoticeType()
				;
			if(notice && noticeType){
				Dialog.notice(notice, noticeType);
			}
		}
		
	}
	var REDIRECT_KEY = 'redirect:';
	function _doAction(action, page, title){
		if(action === 'refresh'){
			page.refresh();
		}else if(action.startsWith(REDIRECT_KEY)){
			var url = action.substr(REDIRECT_KEY.length)
			page.loadContent(url, title);
		}else if(action === 'close'){
			page.close();
		}
	}
	
	
	
	function ajax(url, formData, _param){
		var defaultParam = {
			//提交类型
			method		: 'POST',
			//当提交请求成功
			whenSuc		: $.noop,
			//当提交请求失败
			whenErr		: $.noop,
			//提交请求无论成功或者失败都会执行
			afterLoad	: $.noop,
			//当前页面
			page		: undefined
		};
		var param = {};
		if(typeof _param === 'function'){
			_param = {
				whenSuc	: _param
			};
		}
		//继承获得参数
		$.extend(param, defaultParam, _param);
		
		var fData = new FormData();
		if($.isPlainObject(formData)){
			for(var key in formData){
				fData.append(key, formData[key]);
			}
		}else if(formData instanceof FormData){
			fData = formData;
		}
		
		$.ajax({
		    url: 		url,
		    type: 		param.method,
		    cache: 		false,
		    data: 		fData,
		    processData: false,
		    contentType: false,
		    success		: function(data, status, jqXHR){
		    	commonHandleSucAjax(data, status, jqXHR);
		    	var resContentType = utils.trim(jqXHR.getResponseHeader("Content-Type"));
		    	if(/^.+\/json;.+$/.test(resContentType)){
		    		//返回的数据是Json格式的数据
		    		try{
		    			var json = data;
		    			if(typeof json === 'string'){
		    				json = $.parseJSON(json)
		    			}
		    			var jRes = new JsonResponse(json);
		    			var result = param.whenSuc(jRes, 'json');
		    			if(result !== false){
		    				jRes.doAction(param.page);
		    			}
		    		}catch(e){
		    			console.error(e);
		    		}
		    	}else if(/^.+\/html;.+$/.test(resContentType)){
		    		//返回的数据是html
		    		param.whenSuc(data, 'html');
		    	}
		    },
		    error		: function(jqXHR, textStatus, errorThrown){
		    	console.error(textStatus);
		    	var errResult = param.whenErr();
		    	if(errResult !== false){
		    		Dialog.notice('请求时发生错误', 'error');
		    	}
		    }
		}).always(function(res) {
			param.afterLoad(res);
		});
	}
	
	/**
	 * 以post的方式将obj转换成json字符串，并发送到后台
	 * 后台的控制器需要支持application/json;charset=utf-8的头信息
	 * @param url {String} 请求地址
	 * @param obj {PlainObject} 请求的数据对象，会被转换成json，因此必须是纯粹对象
	 * @param done {Function} 请求成功后的回调函数，有一个参数，是已经对象化的json对象
	 */
	function postJson(url, obj, done){
		$.ajax({
			//提交的地址
			url		: url,
			method	: 'POST',
			headers	: {
				'content-type'	: 'application/json;charset=utf-8'
			},
			data	: JSON.stringify(obj)
		}).done(function(data, textStatus, jqXHR){
			commonHandleSucAjax(data, textStatus, jqXHR);
			var json = data;
			if(typeof json === 'string'){
				try{
					json = $.parseJSON(json);
				}catch(e){}
			}
			done.apply(this, [json, 'done', arguments]);
		}).fail(function(jqXHR, textStatus, errorThrown){
			done.apply(this, [null, 'fail', arguments]);
		});
	}
	
	function commonHandleSucAjax(data, textStatus, jqXHR){
		if($CPF.getParam('ajaxSessionValid')){
			var sessionStatus = jqXHR.getResponseHeader('cpf-session-status');
			if(sessionStatus === 'invalid'){
				location.href = $CPF.getParam('sessionInvalidURL');
			}
		}
	}
	exports.ajax = ajax;
	exports.postJson = postJson;
});