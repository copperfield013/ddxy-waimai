define(function(require, exports, module){
	var utils = require('utils'),
		cls = require('console');
	
	/**
	 * 微信支付类
	 */
	function WxPay(_param){
		var defaultParam = {
			//策略对象
			strategy	: null,
			ajaxSetting	: {
				//提交的地址
				url		: '',
				method	: 'POST',
				headers	: {
					'content-type'	: 'application/json;charset=utf-8'
				},
				data	: ''
			}
		};
		if($.isPlainObject(_param.strategy)){
			_param.strategy = new Strategy(_param.strategy);
		}
		var param = $.extend(true, {}, defaultParam, _param);
		var _this = this;
		/**
		 * 
		 */
		this.createOrder = function(){
			var order = param.strategy.createOrder.apply(_this, [Order]);
			if(order instanceof Order){
				//TODO: 执行预处理
				if(!order.getWxPay()){
					order.setWxPay(_this);
				}
			}else if(order != null){
				$.error('创建的对象不是Order对象');
			}
			return order;
		}
		/**
		 * 
		 */
		this.getAjaxSetting = function(){
			return $.extend(true, {}, param.ajaxSetting);
		}
		/**
		 * 获得策略对象
		 */
		this.getStrategy = function(){
			return param.strategy;
		}
		
	}
	
	/**
	 * 订单类
	 */
	function Order(_param, __wxPay){
		var defaultParam = {
			wxPay		: null,
			//订单介绍
			intro		: '',
			//订单总价
			totalFee	: 100,
			//订单详情
			detail		: {
				//商品列表
				goodsList	: 
				[
					 {
						//商品id，要求商品在后台有
						goodsId	: '',
						quantity: 0,
						comment	: ''
					 }
				 ],
				
			}
		};
		var param = $.extend(true, {}, defaultParam, _param);
		var wxPay = __wxPay;
		/**
		 * 提交订单到服务器
		 */
		this.commit = function(_setting){
			var setting;
			if(wxPay){
				setting = $.extend(true, wxPay.getAjaxSetting(), _setting);
			}
			setting.data = this.serialize();
			$.ajax(setting)
				.done(function(data, textStatus, jqXHR){
					cls.log(data);
					if(typeof data === 'string'){
						data = $.parseJSON(data);
					}
					
					if(data.persisted){
						//数据已经持久化
					}
					if(data.prepaied){
						//预付款订单已经提交
						//调用微信JS的接口
					}
					
					
					
				}).fail(function(jqXHR, textStatus){
					cls.log(jqXHR);
				}).always(function(data, textStatus, jqXHR){
					cls.log(data);
				});
		}
		/**
		 * 序列化订单，将其转换成json字符串
		 */
		this.serialize = function(){
			var temp = {};
			$.extend(true, temp, param);
			temp.wxPay = undefined;
			return JSON.stringify(temp);
		}
		/**
		 * 
		 */
		this.setWxPay = function(_wxPay){
			wxPay = _wxPay;
		}
		this.getWxPay = function(){
			return wxPay;
		}
	}
	/**
	 * 微信支付策略类
	 */
	function Strategy(_param){
		var defaultParam = {
			//根据页面中的数据构造订单对象，方法要返回一个Order对象
			createOrder	: $.noop	
		};
		
		var param = $.extend({}, defaultParam, _param);
		
		this.createOrder = function(){
			return param.createOrder.apply(this, arguments);
		}
	}
	
	module.exports = WxPay;
});
