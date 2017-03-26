/**
 * 表单提交模块
 * 表单提交有以下几种情况
 * 	1.查询
 * 		该情况下，表单提交之后依然会返回html代码，此时需要把html代码放到标签页或者弹出框中
 * 	2.修改
 * 		该情况下，表单提交之后会对数据库进行操作。处理结束之后只会返回处理的状态json。此时需要前后台统一该状态json，并让js直接根据状态信息处理回调
 * 	3.
 */
define(function(require, exports, module){
	var $CPF = require('$CPF'),
		Page = require('page'),
		Ajax = require('ajax')
		;
	
	$CPF.addDefaultParam({
		
	});
	
	$CPF.putPageInitSequeue(4, function($page){
		//阻止跳转
		$('form', $page).submit(function(e){
			  e.preventDefault();
		});
		$('form', $page).submit(function(e){
			var $this = $(this),
				page = $this.getLocatePage(),
				formData = new FormData(this)
			;
			var url = $this.attr('action')
				;
			try{
				page.loadContent(url, undefined, formData);
			}catch(e){
				console.error(e);
			}finally{
				return false;
			}
		});
		//绑定在文本框的回车事件
		$('form :text', $page).keypress(function(e){
			if(e.keyCode === 13){
				$(this).closest('form').trigger('submit');
			}
		});
	});
	
	function bindSubmit($page){
		
	}
});