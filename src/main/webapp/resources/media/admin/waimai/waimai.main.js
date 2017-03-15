/**
 * 外卖点单系统
 */
define(function(require, exports, module){
	var utils = require('utils');
	function WaiMai(){
		var defaultParam = {
			//订单生成算法
			generateOrderCode	: function(){
				return "123456";
			},
			//所有饮料的对象
			$drink				: null,
		};
		
		var param = null;
		var currentOrder = null,
			currentOrderItem = null
			;
		
		/**
		 * 初始化
		 */
		this.init = function(_param){
			$.extend({}, defaultParam, _param);
			this.buildNewOrder();
		};
		
		/**
		 * 创建新的订单，之前的订单将会被替换
		 */
		this.buildNewOrder = function(){
			var order = new Order(param.generateOrderCode());
			currentOrder = order;
		}
		
		/**
		 * 返回当前正在处理的订单
		 */
		this.getCurrentOrder = function(){
			return currentOrder;
		};
		
		/**
		 * 
		 */
		this.bindAll = function(){
			//饮料列表的事件绑定
			if(param.$drink instanceof $){
				var $drinks = param.$drink;
				$drinks.click(function(){
					//如果当前没有正在编辑的订单条目的话，那么可以创建新的订单条目
					if(currentOrderItem == null){
						var $this = $(this);
						var drink = createInfo('drink', $this);
						if(drink instanceof Drink){
							var orderItem = createInfo('orderItem', drink);
							
							//替换全局唯一的当前处理的订单条目
							currentOrderItem = orderItem;
							//着重显示当前选中的元素
							$drinks.removeClass('selected');
							$this.addClass('selected');
							//订单对象放置到订单中
							currentOrder.addItem(orderItem);
							//将订单条目的显示dom放置到容器当中
							appendOrderItem(orderItem);
							
						}
					}
				});
			}
		}
		
		/**
		 * 工厂方法，用于创建POJO
		 */
		function createInfo(infoType, param){
			if(infoType === 'drink'){
				if(param instanceof $){
					var obj = {
						id : param.attr('data-id'),
						name : param.attr('data-name'),
						uPrice : param.attr('data-price'),
						cupSize : param.attr('data-size'),
						tagsStr : param.attr('data-tags'),
						tags	: []
					}
					if(!obj.id){
						return null;
					}
					if(obj.tagsStr){
						obj.tags = obj.tagsStr.split(',');
					}
					if(obj.uPrice){
						obj.uPrice = parseInt(obj.uPrice);
					}
					if(obj.cupSize){
						obj.cupSize = parseInt(obj.cupSize);
					}
					return new Drink(obj);
				}
			}else if(infoType === 'addition'){
				if(param instanceof $){
					
				}
			}else if(infoType === 'sweetness'){
				if(param instanceof $){
					
				}
			}else if(infoType === 'heat'){
				if(param instanceof $){
					
				}			
			}else if(infoType === 'receiver'){
				if(param instanceof $){
					
				}
			}else if(infoType === 'orderItem'){
				if(param instanceof Drink){
					var orderItem = new OrderItem();
					orderItem.init({
						drink	: param,
						order	: currentOrder
					});
					return orderItem;
				}
				return null;
			}
		}
		
		var $orderWrapper = $('#order-list-wrapper');
		/**
		 * 
		 */
		function appendOrderItem(orderItem){
			if(orderItem instanceof OrderItem){
				var $item = $('<div class="order-item">'),
					$row = $('<div class="ydd-wm-order-row">'),
					$selected = $('<span class="order-item-selected">'),
					$number = $('<span class="order-item-number>'),
					$count = $('<span class="order-item-count>').text(1)
				;
				$item.append($row.append($selected).append($number).append($count));
				var drink = orderItem.getDrink();
				$selected.text(drink.getName());
				$number.text($('.order-item', $orderWrapper).length);
				$orderWrapper.append($item);
			}
		}
		
	}
	
	
	/**
	 * 外卖订单类
	 */
	function Order(orderCode){
		
		var
			//订单号
			orderCode = orderCode || null,
			//条目
			itemList = [],
			//收货人
			receiver = null,
			//外卖平台
			takeawayPlatform = null,
			//现金
			cash = 0
		;
		
		/**
		 * 创建。订单号必须
		 */
		this.init = function(){
			
		};
		/**
		 * 添加订单条目
		 * @param item {OrderItem}要添加的订单条目对象
		 * @return this
		 */
		this.addItem = function(item){
			if(item instanceof OrderItem){
				itemList.push(item);
			}else{
				$.error('item参数必须是OrderItem对象');
			}
			return this;
		};
		/**
		 * 移除订单条目
		 * @param item {OrderItem|Integer} 要移除的订单条目对象或者订单条目的索引
		 * @return this
		 */
		this.removeItem = function(item){
			if(item instanceof OrderItem){
				for(var i in itemList){
					if(itemList[i] === item){
						itemList.splice(i, 1);
						return this;
					}
				}
			}else{
				if(utils.isInteger(item)){
					itemList.splice(item, 1);
					return this;
				}
			}
			$.error('传入的参数只能是OrderItem对象或者要移除的item的索引');
		};
		/**
		 * 获得订单条目
		 * @param index 要获得的订单条目的索引
		 * @return {OrderItem}
		 */
		this.getItem = function(index){
			return itemList[index];
		};
		/**
		 * 获得订单条目的条数
		 * @return {Integer} 条数
		 */
		this.getItemCount = function(){
			return itemList.length;
		}
		/**
		 * 计算订单总价
		 * @return {Integer} 订单总价，单位分
		 */
		this.accountPrice = function(){
			var result = 0;
			for(var i in itemList){
				var item = itemList[i];
				result += item.accountPrice();
			}
			return result;
		};
		/**
		 * 统计订单信息
		 * @return {Object} 统计信息对象，包括以下信息
		 * 			总杯数：cupCount
		 * 			现金： cash
		 * 			总价： price
		 * 			找零： change
		 * 			已优惠：promotion
		 */
		this.makeStatistics = function(){
			var result = {
					cupCount	: 0,
					cash		: cash,
					price		: this.accountPrice,
					change		: 0,
					promotion	: 0
			};
			for(var i in itemList){
				result.cupCount += itemList.getCount();
			}
			if(result.cash > result.price){
				result.change = result.cash - result.price;
			}
			return result;
		};
		/**
		 * 设置收货人信息
		 * @param receiver {Receiver} 收件人对象
		 * @return this
		 */
		this.setReceiver = function(_receiver){
			receiver = _receiver;
		};
		/**
		 * 根据模板打印当前订单
		 * @param printTemp {PrintTemp}打印模板
		 * @return this
		 */
		this.print = function(printTemp){
			//TODO: 
		};
		/**
		 * 设置外卖平台信息
		 */
		this.setTakeawayPlatform = function(platform){
			takeawayPlatform = platform;
		};
		/**
		 * 设置现金数
		 * @param cash {Integer} 现金金额，单位为分
		 */
		this.setCash = function(_cash){
			if(utils.isInteger(_cash)){
				cash = _cash;
			}else{
				$.error('现金参数必须是整数');
			}
		}
		
	}
	
	/**
	 * 订单条目类
	 */
	function OrderItem(){
		var defaultParam = {
			//所选饮料
			drink	: null,
			//条目所在的订单
			order	: null,
		};
		//参数
		var param = null;
		
		var 
			//加料
			additionMap = {},
			//热度
			heat = null,
			//甜度
			sweetness = null,
			//杯数
			count = 1
			;
		
		
		/**
		 * 订单条目初始化，饮料信息必需
		 */
		this.init = function(_param){
			param = $.extend({}, defaultParam, _param);
			if(!(param.drink instanceof Drink)){
				$.error('传入的drink参数不是Drink对象');
			}
			if(!(param.order instanceof Order)){
				$.error('传入的order参数不是Order对象');
			}
		}
		
		/**
		 * 添加加料
		 * @param additionInfo {AdditionInfo} 要添加的加料对象
		 * @return this
		 */
		this.addAddition = function(additionInfo){
			if(additionInfo instanceof AdditionInfo){
				var key = additionInfo.getId();
				additionMap[String(key)] = additionInfo;
			}else{
				$.error('additionInfo参数对象不是AdditionInfo对象');
			}
		};
		
		/**
		 * 移除加料
		 * @param additionInfo {AdditionInfo|Long|String} 要移除的加料对象，或者加料对象的标识
		 * @return this
		 */
		this.removeAddition = function(additionInfo){
			if(additionInfo instanceof AdditionInfo){
				for(var key in additionMap){
					if(additionMap[key] === additionInfo){
						additionMap[key] = undefined;
					}
				}
			}else{
				var type = typeof additionInfo;
				if(type === 'string' || type === 'number'){
					additionMap[String(additionInfo)] = undefined;
				}
			}
		};
		/**
		 * 设置热度
		 * @param heat {HeatInfo} 热度对象
		 * @return this
		 */
		this.setHeat = function(_heat){
			if(_heat instanceof HeatInfo){
				heat = _heat;
			}else{
				$.error('传入的heat参数不是heatInfo对象[' + _heat + ']');
			}
		};
		/**
		 * 设置甜度
		 * @param sweetness {Sweetness} 甜度对象
		 * @return this
		 */
		this.setSweetness = function(_sweetness){
			if(_sweetness instanceof Sweetness){
				sweetness = _sweetness;
			}else{
				$.error('传入的sweetness参数不是Sweetness对象[' + _sweetness + ']');
			}
		};
		/**
		 * 设置数量
		 * @param count {Integer} 杯数
		 * @return this
		 */
		this.setCount = function(_count){
			if(utils.isInteger(_count)){
				count = _count;
			}else{
				$.error('count参数不是整数');
			}
		};
		/**
		 * 获得数量
		 * @return {Integer} 杯数
		 */
		this.getCount = function(){
			return count;
		}
		/**
		 * 计算条目价格
		 * @return {Integer} 条目价格，单位分 
		 */
		this.accountPrice = function(){
			var result = this.getDrink().getPrice();
			//遍历所有加料，添加加价
			for(var key in additionMap){
				result += additionMap[key].getAdditionPrice(this);
			}
			//温度是否加价
			if(heat != null){
				result += heat.getAdditionPrice(this);
			}
			//甜度是否加价
			if(sweeetness != null){
				result += sweetness.getAdditionPrice(this);
			}
			return result;
		}
		/**
		 * 获得当前订单条目所选的饮料
		 * @return {Drink} 所选的饮料信息对象
		 */
		this.getDrink = function(){
			
		}
	}
	/**
	 * 饮料类
	 */
	function Drink(_param){
		var defaultParam = {
				//标识
				id		: '',
				//饮料名称
				name	: '',
				//规格
				cupSize	: 0,
				//单价
				uPrice	: 0,
				//标签
				tags	: []
		};
		var param = $.extend({}, defaultParam, _param);
		/**
		 * 获得id
		 * @return {String}
		 */
		this.getId = function(){
			return param.id;
		};
		this.getName = function(){
			return param.name;
		};
		/**
		 * 获得规格
		 * @return {Integer} (2中杯，3大杯)
		 */
		this.getCupSize = function(){
			return parseInt(param.cupSize);
		};
		/**
		 * 获得饮料单价
		 * @return {Integer} 饮料单价，单位分 
		 */
		this.getPrice = function(){
			return parseInt(param.uPrice);
		};
		/**
		 * 获得所有标签
		 * @return {Array[String]} 标签数组
		 */
		this.getTags = function(){
			return param.tags;
		};
		/**
		 * 判断饮料是否包含tag标签
		 * @return {Boolean} 包含返回true，不包含返回false
		 */
		this.hasTag = function(tag){
			return $.inArray(tag, this.getTags()) >= 0;
		}
	}
	/**
	 * 加料信息类
	 */
	function AdditionInfo(_param){
		var defaultParam = {
				//标识
				id			: '',
				//名称
				name		: '',
				//基础价格
				basePrice	: 0,
				//加价策略(函数this为当前AdditionInfo对象，参数为搭配的OrderItem对象)
				strategy	: $.noop
		}
		
		var param = $.extemd({}, defaultParam, _param);
		
		/**
		 * 获得标识
		 * @return {String}
		 */
		this.getId = function(){
			return param.id;
		};
		
		/**
		 * 获得加料名称
		 * @return {String} 加料名称
		 */
		this.getName = function(){
			return param.name;
		};
		
		/**
		 * 获得加料的基础价格
		 * @return {Integer} 该加料的基础价格
		 */
		this.getBasePrice = function(){
			return parseInt(param.basePrice);
		};
		
		/**
		 * 传入订单条目对象，根据策略计算该饮料加料时该加多少钱
		 * @return {Integer} 需加价的价格，单位为分
		 */
		this.getAdditionPrice = function(orderItem){
			if(drink instanceof OrderItem && param.strategy !== $.noop && typeof param.strategy === 'function'){
				return parseInt(param.strategy.apply(this, [orderItem]));
			}
		};
	}
	
	/**
	 * 热度信息类
	 */
	function HeatInfo(_param){
		var defaultParam = {
			//标识
			id			: '',
			//热度名称
			name		: '',
			//加价基础价格
			basePrice	: 0,
			//加价策略
			strategy	: function(){
				return 0;
			}
		};
		
		var param = $.extend({}, defaultParam, _param);
		
		/**
		 * 获得标识
		 * @return {String} 标识字符串
		 */
		this.getId = function(){
			return param.id;
		};

		/**
		 * 获得热度名称
		 * @return {String} 热度名称
		 */
		this.getName = function(){
			return param.name;
		};
		
		/**
		 * 该热度需要另外加价的基础价格
		 * @return {Integer} 加价
		 */
		this.getBasePrice = function(){
			return parseInt(param.basePrice);
		};
		/**
		 * 传入订单条目对象，根据策略计算在该热度下该加多少钱
		 * @return {Integer} 需加价的价格，单位为分
		 */
		this.getAdditionPrice = function(orderItem){
			if(orderItem instanceof OrderItem && typeof param.strategy === 'function'){
				return parseInt(param.strategy.apply(this, [orderItem]));
			}
			return 0;
		};
	}
	
	/**
	 * 甜度信息类
	 */
	function Sweetness(_param){
		var defaultParam = {
				//标识
				id			: '',
				//甜度名称
				name		: '',
				//加价基础价格
				basePrice	: 0,
				//加价策略
				strategy	: function(){
					return 0;
				}
			};
			
			var param = $.extend({}, defaultParam, _param);
			
			/**
			 * 获得标识
			 * @return {String} 标识字符串
			 */
			this.getId = function(){
				return param.id;
			};

			/**
			 * 获得甜度名称
			 * @return {String} 甜度名称
			 */
			this.getName = function(){
				return param.name;
			};
			
			/**
			 * 该甜度需要另外加价的基础价格
			 * @return {Integer} 加价
			 */
			this.getBasePrice = function(){
				return parseInt(param.basePrice);
			};
			/**
			 * 传入订单条目对象，根据策略计算在该热甜度下该加多少钱
			 * @return {Integer} 需加价的价格，单位为分
			 */
			this.getAdditionPrice = function(orderItem){
				if(orderItem instanceof OrderItem && param.strategy !== $.noop && typeof param.strategy === 'function'){
					param.strategy.apply(this, [orderItem]);
				}
			};
	}
	
	/**
	 * 外卖平台类
	 */
	function TakeawayPlatform(_param){
		var defaultParam = {
				id		: '',
				name	: ''
		};
		
		var param = $.extend({}, defaultParam, _param);
		
		/**
		 * 获得标识
		 * @return {String} 获得标识
		 */
		this.getId = function(){
			return param.id;
		};
		/**
		 * 获得外卖平台名称
		 * @return {String} 名称
		 */
		this.getName = function(){
			return param.name;
		};
	}
	
	/**
	 * 收件人类
	 */
	function Receiver(_param){
		var defaultParam = {
				//标识
				id				: '',
				//姓名
				name			: '',
				//配送地址
				address			: '',
				//联系号码
				contactNumber	: ''
		};
		
		var param = $.extend({}, defaultParam, _param);
		
		/**
		 * 获得标识
		 * @return {String} 标识
		 */
		this.getId = function(){
			return param.id;
		};
		
		/**
		 * 获得收货人地址
		 * @return {String} 收货人地址
		 */
		this.getName = function(){
			return param.name;
		};
		
		/**
		 * 获得配送地址
		 * @return {String} 配送地址
		 */
		this.getAddress = function(){
			return param.address;
		};
		/**
		 * 获得联系号码
		 * @return {String} 联系号码
		 */
		this.getContactNumber = function(){
			return param.contactNumber;
		}
		
	}
	/**
	 * 打印模板类
	 */
	function PrintTemp(){
		
	}
	
	
	
	
	
	
	module.exports = WaiMai;
});