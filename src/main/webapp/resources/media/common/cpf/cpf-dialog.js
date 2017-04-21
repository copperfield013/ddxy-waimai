define(function(require, exports, module){
	var $CPF = require('$CPF'),
		utils = require('utils'),
		Page = require('page'),
		Ajax = require('ajax');
	
	$CPF.addDefaultParam({
		dialogEventSelector	: '.dialog',
		dialogIdGenerate	: function(id){
			return 'dialog_' + id;
		}
	});
	
	$CPF.putPageInitSequeue(3, function($page){
		bindDialogEvent($page);
	});
	
	var CLASS_MODAL = 'modal',
		CLASS_DIALOG = 'modal-dialog',
		CLASS_CONTENT = 'modal-content',
		CLASS_HEADER = 'modal-header',
		CLASS_TITLE = 'modal-title',
		CLASS_CLOSE = 'close',
		CLASS_SUBMIT = 'modal-submit',
		CLASS_BODY = 'modal-body',
		CLASS_BODY_WRAPPER = 'modal-body-wrapper',
		CLASS_FOOTER = 'modal-footer'
		;
	var dialogMap = {};
	function Dialog(_param){
		var _this = this,
			bodySize = utils.getPageOffset()
			;
		var defaultParam = {
			id			: utils.uuid(5, 32),
			title		: '标题',
			content		: '',
			onClose		: $.noop,
			onSubmit	: undefined,
			isModal		: true,
			width		: bodySize.width * 3/5,
			height		: bodySize.height * 3/5,
			top			: 0,//bodySize.top + bodySize.Height * 1/5,
			left		: bodySize.left + bodySize.Width * 1/5
		};
		var param = $.extend({}, defaultParam, _param);
		var id = String(param.id),
			title = param.title,
			url, formData
			;
		dialogMap[id] = this;
		var model =
			'<div class="fade ' + CLASS_MODAL + '" role="dialog" id="' + $CPF.getParam('dialogIdGenerate')(id) + '" aria-hidden="true">'
				+ '<div class="' + CLASS_DIALOG + '">' 
					+ '<div class="' + CLASS_CONTENT + '">'
						+ '<div class="' + CLASS_HEADER + '">'
							+ '<button type="button" class="close" data-dismiss="modal">×</button>'
							+ '<h4 class="' + CLASS_TITLE + '">' + param.title + '</h4>'
						+ '</div>'
						+ '<div class="' + CLASS_BODY + '"><div class="' + CLASS_BODY_WRAPPER + '"></div></div>'
					+ "</div>"
				+ '</div>'
			+ '</div>',
			$model = $(model)
			;
		//使窗口可拖动
		$model.draggable({   
		    handle: ".modal-header",   
		    cursor: 'move',
		    refreshPositions: false
		});  
		//调整弹出框的尺寸
		$model.on('show.bs.modal', function () {
			setModalSize($model, param);
			//将弹出框的滚动条设置为自动显示
			$wrapper.parent().css({
				overflowX	: 'auto',
				overflowY	: 'auto'
			});
			
		});
		//模态框显示之后
		$model.on('shown.bs.modal', function () {
			$wrapper.find(':input:first').focus();
		});
		//关闭弹出框时
		$model.on('hidden.bs.modal', function () {
			if(typeof param.onClose === 'function'){
				var result = param.onClose.call(this);
				if(result === false){
					return;
				}
			}
			$model.remove();
			dialogMap[id] = undefined;
			_this.destruct();
		});
		
		var $wrapper = $('.' + CLASS_BODY_WRAPPER, $model),
			$content = $('.' + CLASS_CONTENT, $model),
			$title = $('.' + CLASS_TITLE, $model)
			;
		var page = new Page({
			$content 	: $content,
			id			: param.id,
			pageObj		: this,
			type		: 'dialog',
			$container	: $model
		});
		this.getPage = function(){
			return page;
		};
		this.getId = function(){
			return id;
		};
		this.getFooter = function(){
			return $content.children('.' + CLASS_FOOTER).last();
		};
		/**
		 * 动态加载内容的方法
		 * @param url 页面的链接或者页面的dom对象
		 */
		this.loadContent = function(content, _title, _formData){
			if(typeof content === 'string'){
				url = content;
				formData = _formData;
				$CPF.showLoading();
				//根据url打开弹出框页面
				Ajax.ajax(url, formData, {
					page	: page,
					whenSuc	: function(data, dataType){
						if(dataType === 'html'){
							_this.loadContent($('<div>').html(data), _title);
						}
					},
					afterLoad: function(){
						$CPF.closeLoading();
					}
				});
			}else if(content instanceof $){
				//直接加载content
				$wrapper.html(content.html());
				var $title = $wrapper.children('title').first();
				if($title.length > 0 && !_title){
					$title.remove();
					var __title = $title.text();
					if(__title){
						_title = __title;
					}
				}
				//将页面内的页脚部分移动到弹出框的外面
				var $footer = $wrapper.children('.' + CLASS_FOOTER).last();
				var existFooter = $content.children('.' + CLASS_FOOTER);
				if(existFooter.length > 0){
					existFooter.last().replaceWith($footer);
				}else{
					$footer.appendTo($content);
				}
				//处理底部框中的按钮的事件
				dealFooterButton($footer, $content);
				//绑定关闭事件
				if(typeof param.onClose === 'function'){
					$('button.' + CLASS_CLOSE, $model).each(function(){
						$(this).off('click', hideModal)
								.on('click', hideModal)
					});
				}
				if(typeof _title === 'string' && _title != ''){
					this.setTitle(_title);
				}
				$CPF.initPage($wrapper);
			}
			return this;
		};
		/**
		 * 刷新页面，必须从url获得内容
		 */
		this.refresh = function(){
			this.loadContent(url, title, formData);
		};
		/**
		 * 隐藏弹出框
		 */
		function hideModal(){
			$model.modal('hide');
		}
		
		this.getDom = function(){
			return $model;
		};
		//关闭弹出框，并移除弹出框的dom
		this.close = function(){
			hideModal();
		};
		//显示弹出框
		this.show = function(){
			Page.putPage(id, page);
			this.getDom().modal();
		};
		//最小化弹出框
		this.minimize = function(){
			
		};
		//最大化弹出框
		this.maximize = function(){
			
		};
		/**
		 * 设置弹出框的标题
		 */
		this.setTitle = function(_title){
			title = _title;
			$title.text(title);
		};
		this.getTitle = function(){
			return title;
		};
		/**
		 * 析构
		 */
		this.destruct = function(){
			Page.remove(id);
		};
		/**
		 * 内部函数，用于处理弹出框底部框中的按钮的事件
		 */
		function dealFooterButton($footer, $content){
			var submitSelector = 'button.submit,button[type=submit],input[type="submit"],input.submit[type="button"]',
				resetSelector = 'button.reset,button[type=reset],input[type="reset"],input.reset[type="button"]'
			$(submitSelector, $footer).click(function(){
				var forForm = $($(this).attr('for'), $content);
				forForm.submit();
			});
			$(resetSelector, $footer).click(function(){
				var forForm = $($(this).attr('for'), $content);
				if(forForm.length > 0){
					forForm[0].reset();
				}
			});
		}
	}
	/**
	 * 自定义模态框的尺寸
	 */
	function setModalSize($modal, param){
		var top	 	= param.top,
			left 	= param.left,
			width	= param.width,
			height	= param.height
			;
		//先把最外面一层设置为绝对定位
		$modal.css({
			position	: 'absolute',
			overflow	: 'hidden',
			marginTop	: top,
			marginLeft	: left
		});
		//dialog层设置尺寸
		$modal.children('.' + CLASS_DIALOG).css({
			width	: width,
			height	: height
		})
		.children('.' + CLASS_CONTENT).css({})
		.children('.' + CLASS_BODY).css({
			height		: function(){
				return height - $modal.find('.' + CLASS_HEADER).height()
			},
			overflowY	: 'scroll',
	    	overflowX	: 'hidden'
		})
		;
		
	}
	
	
	function bindDialogEvent($page){
		//遍历当前页面中要打开弹出框的标签，并绑定点击事件
		$($CPF.getParam('dialogEventSelector'), $page).each(function(){
			var $this = $(this),
				title = $this.attr('title'),
				url = $this.attr('href')
				;
			$(this).click(function(e){
				e.preventDefault();
				var dialog = new Dialog({
					title	: title,
					url		: url
				});
				dialog.getDom().modal();
				return false;
			});
		});
	}
	
	$.extend(Dialog, {
		openDialog	: function(url, title, id, param){
			var dialog = new Dialog($.extend({
				title	: title,
				id		: id
			}, param));
			//直接加载弹出框
			dialog.loadContent(url)
				.show();
			return dialog;
		},
		getDialog	: function(dialogId){
			return dialogMap[dialogId];
		},
		closeDialog	: function(dialogId){
			var dialog = this.getDialog(dialogId);
			if(dialog){
				dialog.close();
			}
		},
		notice		:  function(msg, type){
			if(!msg || !type){
				return;
			}
			//Notify的参数说明
			//1.提示语
			//2.显示位置，由样式控制，前缀为toast-
			//3.隐藏时间
			//4.提示类型(danger,warning,info,success,blue,palegreen)，由样式控制，前缀为toast-
			//5.图标类型
			//6.是否显示关闭按钮
			var nTypeMap = {
				'error'		: {
					type	: 'danger',
					icon	: 'fa-bolt'
				},
				'warning'	: {
					type	: 'warning',
					icon	: 'fa-warning'
				},
				'success'	: {
					type	: 'success',
					icon	: 'fa-check'
				},
				'info'		: {
					type	: 'info',
					icon	: 'fa-envelope'
				}
			};
			var nTypeObj = nTypeMap[type];
			if(nTypeObj){
				Notify(msg, 'center', '3000', nTypeObj.type, nTypeObj.icon, false);
			}
		},
		/**
		 * 弹出确认框
		 */
		confirm		: function(msg, callback, _param){
			var defaultParam = {
				domHandler	: $.noop,
				width		: '300px',
				height		: '200px',
				top			: '100px'
			};
			var param = $.extend({}, defaultParam, _param)
			var confirmId = utils.uuid(5, 62),
				title = '操作提示',
				confirmModel = 
					'<div>' +
						'<div class="dialog-confirm-msg">' + msg + '</div>' + 
						'<div class="' + CLASS_FOOTER + '">' +
							'<button class="confirm-btn-yes btn btn-default"> 是 </button>' +
							'<button class="confirm-btn-no btn btn-primary"> 否 </button>' +
						'</div>' + 
						    
					'</div>',
				$confirm = $(confirmModel)
					;
			param.domHandler.apply(this, [$('div.dialog-confirm-msg:first', $confirm), $confirm]);
			var dialog = Dialog.openDialog($confirm, title, confirmId, {
				width	: param.width,
				height	: param.height,
				top		: param.top
			});
			dialog.getFooter().find('button.confirm-btn-yes,button.confirm-btn-no').click(function(){
				var isYes = $(this).is('.confirm-btn-yes');
				var page = dialog.getPage();
				if(page instanceof Page){
					var result = callback(isYes, page);
					if(result !== false){
						page.close();
					}
				}
			});
		}
	});
	
	
	module.exports = Dialog;
	
});