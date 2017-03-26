/**
 * 
 */
define(function(require, exports, module){
	var $CPF = require('$CPF'),
		Page = require('page'),
		Dialog = require('dialog'),
		utils = require('utils')
		;
	
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
		    	console.log(jqXHR);
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
	
	exports.ajax = ajax;
});