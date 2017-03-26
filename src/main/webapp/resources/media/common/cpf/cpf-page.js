define(function(require, exports, module){
	var $CPF = require('$CPF');
	
	$CPF.addDefaultParam({
		//用于将Page对象放到容器的jQuery对象的data当中
		pageDataKey			: 'PAGE_DATA_KEY',
		//页面类
		pageContentClass	: 'cpf-page-content',
	});
	
	
	$.fn.extend({
		/**
		 * 获得当前节点的所在页面的页面对象
		 */
		getLocatePage	: function(){
			var $container = $(this).closest('.' + $CPF.getParam('pageContentClass'));
			if($container.length > 0){
				var page = $container.first().data($CPF.getParam('pageDataKey'));
				if(page instanceof Page){
					return page;
				}
			}
		}
	});
	
	$CPF.putPageInitSequeue(5, function($page){
		var page = $($page).getLocatePage();
		if(page instanceof Page){
			if(page.getType() === 'dialog'){
				$('a[href],button[href]', page.getContainer()).click(function(e){
					var href = $(this).attr('href');
					if(href !== '#'){
						goPage(this, page);
					}
				});
			}else{
				$('a[href],button[href]', $page).click(function(){
					var href = $(this).attr('href');
					if(href !== '#'){
						goPage(this, page);
					}
				});
			}
			
			function goPage(dom, targetPage){
				var target = $(dom).attr('target'),
					href = $(dom).attr('href'),
					title = $(dom).attr('title'),
					pageType = $(dom).attr('page-type') || 'dialog'
					;
				if(target && target.startsWith('@')){
					var pageId = target.substr(1);
					//根据id获得获得对应的Page对象
					targetPage = Page.getPage(pageId);
					if(!(targetPage instanceof Page)){
						//没有找到Page对象，那么就创建一个
						if(pageType === 'dialog'){
							var dialog = new Dialog({
								id		: pageId,
								title	: title
							});
							targetPage = dialog.getPage();
						}else if(pageType === 'tab'){
							var tab = new Tab({
								id		: pageId,
								title	: title
							});
							tab.insert();
							targetPage = tab.getPage();
						}
					}
				}
				targetPage.loadContent(href, title);
				var tPageType = targetPage.getType();
				if(tPageType === 'dialog'){
					targetPage.getPageObj().show();
				}else if(tPageType === 'tab'){
					targetPage.getPageObj().activate();
				}
			}
			
		}
	});
	
	
	
	var pageMap = {};
	
	/**
	 * 页面类
	 * 页面类型包括标签页(tab)、弹出框(dialog)，以及主页(home)
	 */
	function Page(_param){
		var $content = _param.$content,
			type = _param.type,
			id = _param.id,
			pageObj = _param.pageObj,
			$container = _param.$container
			;
		$content
			.addClass($CPF.getParam('pageContentClass'))
			.data($CPF.getParam('pageDataKey'), this);
		
		this.getContent = function(){
			return $content;
		};
		this.getType = function(){
			return type;
		};
		this.getPageObj = function(){
			return pageObj;
		};
		this.getContainer = function(){
			return $container;
		};
		this.refresh = function(){
			if(typeof pageObj.refresh === 'function'){
				pageObj.refresh();
			}
		};
		this.loadContent = function(url, _title, formData, afterLoadFunc){
			if(typeof pageObj.loadContent === 'function'){
				pageObj.loadContent(url, _title, formData, afterLoadFunc);
			}
		};
		this.close = function(){
			if(typeof pageObj.close === 'function'){
				pageObj.close();
			}
		}
	}
	
	$.extend(Page, {
		putPage		: function(pageId, page){
			pageMap[pageId] = page;
		},
		getPage		: function(pageId){
			return pageMap[pageId];
		},
		remove		: function(pageId){
			pageMap[pageId] = undefined;
		}
	});
	
	
	//将整个Page类作为对外接口
	module.exports = Page;
	
});