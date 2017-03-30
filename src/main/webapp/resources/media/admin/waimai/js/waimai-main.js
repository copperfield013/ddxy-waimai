/**
 * 外卖点单系统
 */
define(function(require, exports, module){
	var utils = require('utils'),
		Print = require('print'),
		Ajax = require('ajax')
	function WaiMai(){
		var 
			DEFAULT_ORDER_CODE = 'wait-for-generate',
			defaultParam = {
				//订单生成算法
				generateOrderCode	: function(){
					return DEFAULT_ORDER_CODE;
				},
				//所有饮料的对象
				$drinks				: null,
				$additions			: null,
				$heats				: null,
				$sweetnesses		: null,
				$takeaway			: null,
				menuItemMap			: {},
				additionMap			: {}
			};
		
		var param = null;
		var currentOrder = null,
			currentOrderItem = null,
			$currentOrderItem = null,
			$printArea = $('.PrintArea')
			;
		var ORDER_ITEM_KEY = 'order-item-key';
		var $orderItemContainer = $('#order-list-wrapper'),
			editable = true,
			_this = this,
			$orderWrapper = $('#order-list-wrapper');;
		/**
		 * 初始化
		 */
		this.init = function(_param){
			param = $.extend({}, defaultParam, _param);
			this.buildNewOrder();
		};
		
		/**
		 * 创建新的订单，之前的订单将会被替换
		 */
		this.buildNewOrder = function(){
			var order = new Order(param.generateOrderCode());
			currentOrder = order;
			currentOrderItem = null;
			$currentOrderItem = null;
			//清空联系人信息
			$('#view-receiver-name').text('');
			$('#view-receiver-contact').text('');
			$('#view-receiver-address').text('');
			
			//取消所有选项的选择
			setOrderItemAsCurrent(null);
			//清空订单节点
			$orderWrapper.empty();
			//重新计算价格
			viewTotalPrice();
			//禁用打印
			enablePrint(false);
			editable = true;
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
			if(param.$drinks instanceof $){
				var $drinks = param.$drinks;
				$drinks.click(function(){
					//如果当前没有正在编辑的订单条目的话，那么可以创建新的订单条目
					if(editable && currentOrderItem == null){
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
							$currentOrderItem = appendOrderItem(orderItem);
							$currentOrderItem.addClass('selected');
						}
					}
					viewTotalPrice();
				});
			}
			//加料的事件绑定
			if(param.$additions instanceof $){
				var $additions = param.$additions;
				$additions.click(function(){
					var $this = $(this);
					//只有当页面上已经创建了某个订单条目时才可以使用加料
					if(editable && currentOrderItem != null){
						if(!$this.is('.selected')){
							//根据元素创建加料对象
							var addition = createInfo('addition', $this);
							if(addition instanceof AdditionInfo){
								//订单条目中添加加料对象
								currentOrderItem.addAddition(addition);
								//当前加料对象勾选
								$this.addClass('selected');
								//条目中显示加料
								appendAddition(addition);
								
								//处理单选的加料
								var tags = addition.getTags(/^single-.+$/);
								for(var i in tags){
									var singleTag = tags[i],
										additions = currentOrderItem.getAdditions(),
										toRemove = [];
									for(var j in additions){
										if(additions[j] !== addition && additions[j].hasTag(singleTag)){
											toRemove.push(additions[j]);
										}
									}
									for(var j in toRemove){
										currentOrderItem.removeAddition(toRemove[j]);
										$('span[data-id="' + toRemove[j].getId() + '"]', $currentOrderItem).remove();
										$('.ydd-wm-additions-container span[data-id="' + toRemove[j].getId() + '"]').removeClass('selected');
									}
								}
								
							}
						}else{
							//移除该加料
							var id = $this.attr('data-id');
							if(id){
								currentOrderItem.removeAddition(id);
								$('span[data-id="' + id + '"]', $currentOrderItem).remove();
								$this.removeClass('selected');
							}
						}
					}
					viewTotalPrice();
				});
			}
			//设置热度的事件绑定
			bindSingleChoose('heat', param.$heats);
			//甜度的事件绑定
			bindSingleChoose('sweetness', param.$sweetnesses);
			//外卖平台的事件绑定
			bindSingleChoose('takeaway', param.$takeaway);
			//绑定杯数编辑
			utils.NumberEdit($('#checkout-number'), function(num){
				setOrderItemCount(num);
			});
			//绑定订单条目的选择
			$orderItemContainer.on('click', '.order-item', function(){
				if(!editable){
					return false;
				}
				var $this = $(this);
				var orderItem = $this.data(ORDER_ITEM_KEY);
				if(orderItem !== currentOrderItem){
					if(orderItem instanceof OrderItem){
						setOrderItemAsCurrent(orderItem);
					}
				}
				var drink = orderItem.getDrink();
				if(drink){
					var $drink = param.$drinks.filter('[data-id="' + drink.getId() + '"]');
					if($drink.length == 1){
						var tabId = $drink.closest('.tab-content').attr('id');
						if(tabId){
							$('.ydd-wm-main-title-wrapper .tab-title[tab-id="' + tabId + '"]').trigger('click');
						}
					}
				}
				return false;
			});
			//点击加料确定
			$('.ydd-wm-main-addition-sure').click(function(){
				if(!editable){
					return false;
				}
				setOrderItemAsCurrent(null);
			});
			var receiverLayer = null;
			//点击填写收货人信息
			$('#open-receiver-edit').click(function(){
				if(!editable){
					return false;
				}
				var $receiverContainer = $('.ydd-wm-receiver-container');
				//弹出输入框时，初始化所有表单的信息
				var receiverInfo = currentOrder.getReceiver();
				if(receiverInfo != null){
					$('#receiver-number', $receiverContainer).val(receiverInfo.getContactNumber());
					$('#receiver-name', $receiverContainer).val(receiverInfo.getName());
					$('#receiver-address', $receiverContainer).val(receiverInfo.getAddress());
					$('#receiver-id', $receiverContainer).val(receiverInfo.getId());
				}
				receiverLayer = layer.open({
					  type		: 1,
					  area		: ['800px', '400px'],
					  title		: '收货人信息填写',
					  content	: $receiverContainer,
					  success	: function(layero, index){
						  $('#receiver-number').focus();
					  },
					  closeBtn	: 0,
					  end		: function(){
						  $('.ydd-wm-receiver-container').find(':text,#receiver-id').val('');
					  }
				  });
			});
			//点击弹出框中保存并返回按钮
			$('#return-with-save').click(function(){
				var receiver = createInfo('receiver', $('#ydd-wm-receiver-container'));
				if(receiver instanceof Receiver){
					if(!receiver.getContactNumber()){
						alertMsg('请填写手机号码');
						return;
					}else{
						//TODO:验证手机号的合法性
						var testNumber = utils.testContactNumber(receiver.getContactNumber());
						if(!testNumber){
							alertMsg('手机号码格式不正确，请重新填写');
							return ;
						}
					}
					if(!receiver.getName()){
						alertMsg('请填写收货人姓名');
						return;
					}
					if(!receiver.getAddress()){
						alertMsg('请填写收货地址');
						return;
					}
					//将信息显示到主页面当中并关闭当前按钮
					//收货人姓名
					$('#view-receiver-name').text(receiver.getName());
					//手机号码
					$('#view-receiver-contact').text(receiver.getContactNumber());
					//收货地址
					$('#view-receiver-address').text(receiver.getAddress());
					//将该收货人信息保存到订单对象当中
					currentOrder.setReceiver(receiver);
					//关闭弹出框
					layer.close(receiverLayer);
					receiverLayer = null;
				}
				
			});
			//弹出框中取消按钮
			$('#return-with-cancel').click(function(){
				layer.close(receiverLayer);
				receiverLayer = null;
			});
			//标签页切换
			$('.tab-title').click(function(){
				var $this = $(this),
					tabId = $this.attr('tab-id')
					;
				if(tabId){
					var $siblings = $this.siblings('.tab-title');
					var tabIds = [];
					$siblings.each(function(){
						tabIds.push($(this).attr('tab-id'));
					});
					var $allContents = $('.tab-content').filter(function(){
						var thisContentId = $(this).attr('id');
						if($.inArray(thisContentId, tabIds) > -1){
							return true;
						}
						return false;
					});
					$allContents.hide();
					$siblings.removeClass('active');
					$('.tab-content[id="' + tabId + '"]').show();
					$this.addClass('active');
				}
				
			});
			//双击删除订单条目
			$orderItemContainer.on('dblclick', '.order-item', function(){
				if(!editable){
					return false;
				}
				var $this = $(this),
					thisOrderItem = $this.data(ORDER_ITEM_KEY);
				currentOrder.removeItem(thisOrderItem);
				$this.remove();
				if($this.is($currentOrderItem)){
					setOrderItemAsCurrent(null);
				}
				$('.order-item', $orderItemContainer).each(function(i){
					$('.order-item-number', this).text(i + 1);
				});
				viewTotalPrice();
				return false;
			});
			//数字输入区的按键回调
			$('#number-key-area span[data-num]').click(function(){
				if(!editable){
					return false;
				}
				var num = $(this).attr('data-num'),
					$label = $('#checkout-number'),
					currentView = $label.text();
				if(currentView === '0'){
					if(num !== '0' && num !== '00'){
						$label.text(num);
					}
				}else{
					$label.text(currentView + num);
				}
				return false;
			});
			//数字输入区的清理回调
			$('#checkout-clear').click(function(){
				if(!editable){
					return false;
				}
				$('#checkout-number').text(0);
				return false;
			});
			//数字输入区的确定按钮回调
			$('#checkout-confirm').click(function(){
				var $number = $('#checkout-number');
				var num = parseInt($number.text());
				$number.text(0);
				setOrderItemCount(num);
			});
			//保存订单
			$('#save-order').click(function(){
				if($(this).is('.disabled')){
					return false;
				}
				if(!currentOrder){
					alertMsg('请先创建订单');
					return false;
				}
				if(currentOrder.getItemCount() === 0){
					alertMsg('当前订单没有添加条目');
					return false;
				}
				if(currentOrder.getCode() !== DEFAULT_ORDER_CODE){
					alertMsg('当前订单已经保存，不需要重新保存');
					return false;
				}
				if(currentOrderItem){
					alertMsg('请先保存当前正在操作的奶茶条目');
					return false;
				}
				var submitData = currentOrder.getSubmitData();
				if(!submitData.receiver){
					alertMsg('请先填写收货人信息');
					return false;
				}
				//发送订单信息
				Ajax.postJson('admin/waimai/submitOrder', submitData, function(json, status){
					console.log(json);
					if(json != null){
						if(json.orderCode){
							//返回订单号
							currentOrder.setCode(json.orderCode);
							enablePrint(true);
							return;
						}
					}
					alertMsg('保存订单时出现问题');
				});
			});
			
			/**
			 * 打印订单按钮
			 */
			$('#print-order').click(function(){
				if(!$(this).is('.disabled')){
					printCurrentOrder();
				}
			});
			//输入联系人的手机号码之后，自动加载联系人的其他信息
			$('#receiver-number').change(function(){
				var contact = $(this).val();
				if(utils.testContactNumber(contact)){
					loadReceiverInfo(contact, function(receiver){
						$('#receiver-id').val(receiver.id || '');
						$('#receiver-name').val(receiver.name || '');
						$('#receiver-address').val(receiver.address || '');
					});
				}
			});
			//创建新订单
			$('#create-order').click(function(){
				if(editable){
					//当前订单还没有保存
					if(currentOrder.getItemCount() === 0){
						_this.buildNewOrder();
						return false;
					}
					
					layer.confirm('当前订单未保存，是否保存？', {
						btn	: ['保存', '不保存，直接创建新的订单', '取消'],
						area: '30em'
					}, function(index, layero){
						$('#save-order').trigger('click');
						layer.close(index);
					}, function(index, layero){
						_this.buildNewOrder();
						layer.close(index);
					}, function(index, layero){
						layer.close(index);
					});
				}else{
					if(!currentOrder.hasPrinted()){
						layer.confirm('当前订单尚未打印，是否继续创建？', {
							btn	: ['稍后再创建', '不打印了，直接创建新的订单']
						}, function(index){
							layer.close(index);
						}, function(index){
							_this.buildNewOrder();
							layer.close(index);
						});
					}else{
						_this.buildNewOrder();
					}
				}
			});
			
			//设置定时器，显示当前时间
			var viewTimeTimer = setInterval(function(){
				var timeStr = utils.formatDate(new Date(), 'MM/dd hh:mm:ss');
				$('#current-time').text(timeStr);
			},100);
			
			//设置每三分钟访问一次后台，防止session过期
			var pollingTimer = setInterval(function(){
				Ajax.postJson('admin/waimai/polling',{
					
				}, function(json){
					
				});
			}, 180000);
			
			
		}
		
		/**
		 * 启用当前订单的打印
		 */
		function enablePrint(flag){
			var $printBtn = $('#print-order'),
				$saveBtn = $('#save-order'),
				$receiverEdit = $('#open-receiver-edit');
			flag = flag === undefined? true: flag;
			if(flag){
				//禁用编辑
				$printBtn.removeClass('disabled');
				$saveBtn.addClass('disabled');
				$receiverEdit.addClass('disabled');
				$('.ydd-wm-main-center-cover').show();
				editable = false;
			}else{
				//启用编辑
				$printBtn.addClass('disabled');
				$saveBtn.removeClass('disabled');
				$receiverEdit.removeClass('disabled');
				$('.ydd-wm-main-center-cover').hide();
				editable = true;
			}
		}
		
		
		function setOrderItemCount(num){
			if(editable && currentOrderItem != null){
				currentOrderItem.setCount(num);
				$('.order-item-count', $currentOrderItem).text(num);
				viewTotalPrice();
			}
		}
		
		/**
		 * 设置订单条目为当前处理的订单条目
		 */
		function setOrderItemAsCurrent(orderItem){
			//取消所有饮料、加料、温度等选择
			$('.ydd-wm-main-center .ydd-wm-main-goods-wrapper span.selected').removeClass('selected');
			$('.ydd-wm-main-addition-wrapper span.selected').removeClass('selected')
			$('.ydd-wm-main-addition-set>span').removeClass('selected');
			
			currentOrderItem = orderItem;
			$('.order-item.selected', $orderItemContainer).removeClass('selected');
			if(orderItem instanceof OrderItem){
				$currentOrderItem = orderItem.getDom();
				$currentOrderItem.addClass('selected');
				//选择订单条目所选的饮料
				var drink = orderItem.getDrink();
				$('.ydd-wm-main-goods-wrapper span[data-id="' + drink.getId() + '"]').addClass('selected');
				var $additionWrapper = $('.ydd-wm-main-addition-wrapper'),
					additions = orderItem.getAdditions(),
					heat = orderItem.getHeat(),
					sweetness = orderItem.getSweetness();
				for(var i in additions){
					if(additions[i] instanceof AdditionInfo){
						$('span[data-id="' + additions[i].getId() + '"]', $additionWrapper).addClass('selected');
					}
				}
				if(heat != null){
					$('span.heat[data-id="' + heat.getId() + '"]', $additionWrapper).addClass('selected');
				}
				if(sweetness != null){
					$('span.sweetness[data-id="' + sweetness.getId() + '"]', $additionWrapper).addClass('selected');
				}
				
				//选择订单条目所选的添加选择
				
			}else if(orderItem === null){
				$currentOrderItem = null;
			}
		}
		
		/**
		 * 工厂方法，用于创建POJO
		 */
		function createInfo(infoType, param){
			if(infoType === 'drink'){
				if(param instanceof $){
					var obj = {
						id 		: param.attr('data-id'),
						name 	: param.attr('data-name'),
						uPrice 	: param.attr('data-price'),
						aPrice	: param.attr('data-price-a'),
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
					if(obj.aPrice){
						obj.aPrice = parseInt(obj.aPrice);
					}
					return new Drink(obj);
				}
			}else if(infoType === 'addition'){
				if(param instanceof $){
					var obj = abstractAdditionValue(param);
					return new AdditionInfo(obj);
				}
			}else if(infoType === 'sweetness'){
				if(param instanceof $){
					var obj = abstractAdditionValue(param);
					return new Sweetness(obj);
				}
			}else if(infoType === 'heat'){
				if(param instanceof $){
					var obj = abstractAdditionValue(param);
					return new HeatInfo(obj);
				}			
			}else if(infoType === 'takeaway'){
				if(param instanceof $){
					var obj = abstractAdditionValue(param);
					return new TakeawayPlatform(obj);
				}
			}else if(infoType === 'receiver'){
				if(param instanceof $){
					var obj = {};
					if(param.is('.ydd-wm-main-count-receiver')){
						obj = {
							contactNumber		: $('#view-receiver-contact', param).text(),
							name				: $('#view-receiver-name', param).text(),
							id					: $('#hidden-receiver-id', param).val(),
							address				: $('#view-receiver-address', param).val()
						};
					}else{
						obj = {
								contactNumber 	: $('#receiver-number').val(),
								name 			: $('#receiver-name').val(),
								id 				: $('#receiver-id').val(),
								address 		: $('#receiver-address').val()
							};
					}
					return new Receiver(obj);
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
		
		function abstractAdditionValue(param){
			var obj = {
				id			: param.attr('data-id'),
				name		: param.attr('data-name'),
				basePrice	: param.attr('data-price'),
				tagsStr		: param.attr('data-tag'),
				strategy	: normalAdditionPriceStrategy
			};
			if(!obj.id){
				return null;
			}
			if(obj.tagsStr){
				obj.tags = obj.tagsStr.split(',');
			}
			if(obj.basePrice){
				obj.basePrice = parseInt(obj.basePrice);
			}
			return obj;
		}
		
		/**
		 * 往当前的订单中添加订单条目
		 */
		function appendOrderItem(orderItem){
			if(orderItem instanceof OrderItem){
				var $item = $('<div class="order-item">'),
					$row = $('<div class="ydd-wm-order-row">'),
					$selected = $('<span class="order-item-selected">'),
					$number = $('<span class="order-item-number">'),
					$count = $('<span class="order-item-count">').text(1)
				;
				$item.append($row.append($number).append($selected).append($count));
				var drink = orderItem.getDrink();
				$selected.text(drink.getName());
				$number.text($('.order-item', $orderWrapper).length + 1);
				$orderWrapper.append($item);
				//把对象和dom关联起来
				orderItem.setDom($item);
				$item.data(ORDER_ITEM_KEY, orderItem);
				scrollToCurrentItem();
				return $item;
			}
		}
		
		/**
		 * 往当前的订单条目中添加加料信息
		 */
		function appendAddition(addition){
			if($currentOrderItem instanceof $){
				var $additionRow = $('.addition-row', $currentOrderItem);
				var $addition = $('<span>');
				$addition
					.attr('data-id', addition.getId())
					.text(addition.getName());
				if($additionRow.length == 0){
					$additionRow = $('<div class="ydd-wm-order-row addition-row">');
					var $desc = $('<span class="order-item-desc">');
					$desc.append($addition).appendTo($additionRow);
					$currentOrderItem.children('.ydd-wm-order-row:first').after($additionRow);
				}else{
					var $desc = $('.order-item-desc', $additionRow);
					$desc.append($addition);
				}
				scrollToCurrentItem();
				return $additionRow;
			}
		}
		/**
		 * 窗口滚动到当前处理的订单条目
		 */
		function scrollToCurrentItem(){
			utils.scrollTo($orderItemContainer, $currentOrderItem);
		}
		/**
		 * 一般加料的加价策略
		 */
		function normalAdditionPriceStrategy(orderItem){
			var price = this.getBasePrice(),
				drink = orderItem.getDrink();
			var additionSizeMap = param.additionSize['id-' + this.getId()];
			if(additionSizeMap){
				var sizeData = additionSizeMap['size-' + drink.getCupSize()];
				if(sizeData){
					price = sizeData['price'];
				}
			}
			if(utils.isInteger(price)){
				return price;
			}else{
				return 0;
			}
		}
		/**
		 * 设置描述的显示
		 */
		function setDescInfoView(info){
			if($currentOrderItem instanceof $){
				var $descRow = $('.desc-row', $currentOrderItem);
				var $info = $('<span>');
				if(info instanceof HeatInfo){
					$info.addClass('heat');
				}else if(info instanceof Sweetness){
					$info.addClass('sweetness');
				}else if(info instanceof TakeawayPlatform){
					return $descRow;
				}else{
					$.error('未知类型的描述对象');
				}
				$info
					.attr('data-id', info.getId())
					.text(info.getName());
				if($descRow.length == 0){
					$descRow = $('<div class="ydd-wm-order-row desc-row">');
					var $desc = $('<span class="order-item-desc">');
					$desc.append($info).appendTo($descRow);
					$currentOrderItem.append($descRow);
				}else{
					var $desc = $('.order-item-desc', $descRow);
					$desc.append($info);
				}
				scrollToCurrentItem();
				return $descRow;
			}
		}
		/**
		 * 绑定单选按钮（heat和sweetness）的事件
		 */
		function bindSingleChoose(infoType, $targets){
			if((infoType === 'heat' || infoType === 'sweetness' || infoType === 'takeaway') 
					&& $targets instanceof $){
				$targets.click(function(){
					if(!editable){
						return false;
					}
					var $this = $(this);
					//只有当页面上已经创建了某个订单条目时才可以点选热度
					if(((infoType === 'heat' || infoType === 'sweetness') && currentOrderItem != null) 
							|| (infoType === 'takeaway' && currentOrder != null)){
						var setMethod = $.noop;
						if(infoType === 'heat'){
							setMethod = currentOrderItem.setHeat;
						}else if(infoType === 'sweetness'){
							setMethod = currentOrderItem.setSweetness;
						}else if(infoType === 'takeaway'){
							setMethod = currentOrder.setTakeawayPlatform;
						}
						
						//判断是否已经已经点选了其他热度
						//如果存在其他热度，那么就覆盖原热度对象
						//如果不存在其他热度，那么直接使用当前创建的热度
						//如果当前热度已经被点选，那么移除当前热度
						var id = $this.attr('data-id');
						if($this.is('.selected')){
							setMethod(null);
							$('span[data-id="' + id + '"]', $currentOrderItem).remove();
							$this.removeClass('selected');
						}else{
							var info = createInfo(infoType, $this);
							$targets.filter('.selected').removeClass('selected');
							$this.addClass('selected');
							$('span.' + infoType, $currentOrderItem).remove();
							setMethod(info);
							setDescInfoView(info);
						}
					}
				});
			}
		}
		/**
		 * 调用当前处理的订单的价格计算方法，将计算结果显示到页面当中
		 */
		function viewTotalPrice(){
			$('#total-price label').text((currentOrder.accountPrice() / 100).toFixed(2));
		}
		
		/**
		 * 打印当前订单
		 */
		function printCurrentOrder(){
			if(currentOrder != null){
				//当前订单存在
				if(currentOrderItem != null){
					alertMsg('请先确认当前正在处理的奶茶后执行打印。');
				}
				
				var 
					//统计对象
					statistics = currentOrder.makeStatistics(),
					//节点数
					itemCount = currentOrder.getItemCount(),
					number = 1
					;
				if(itemCount > 0){
					var $pageContainer = $('<div>');
					var receiver = currentOrder.getReceiver();
					for(var i = 0; i < itemCount; i++){
						var item = currentOrder.getItem(i);
						if(item instanceof OrderItem){
							//获得杯数
							var cupCount = item.getCount(),
								drink = item.getDrink(),
								heat = item.getHeat(),
								sweetness = item.getSweetness(),
								cupSize = drink.getCupSize(),
								viewName = drink.getName(),
								additions = item.getAdditions();
							
							var $temp = $printArea.clone();
							$('.print-order-code', $temp).text(currentOrder.getCode());
							$('.order-time', $temp).text(utils.formatDate('hh:mm:ss'));
							
							$('#view-name', $temp).text(viewName);
							$('#heat', $temp).text(heat == null? '': heat.getName());
							$('#sweetness', $temp).text(sweetness == null? '': sweetness.getName());
							//填写加料数据
							var $additionArea = $('#addition-area', $temp).empty();
							
							for(var j in additions){
								var addition = additions[j];
								if(addition instanceof AdditionInfo){
									$additionArea.append('<label>' + addition.getName() + '</label>');
								}
							}
							//该杯价格
							$('.print-order-price', $temp).text(item.eachPrice() / 100 + '元');
							//联系人信息
							$('.print-addr', $temp).text(receiver.getAddress());
							$('.print-contact', $temp).text(receiver.getContactNumber());
							$('.print-name', $temp).text(receiver.getName());
							
							
							for(var k = 0; k < cupCount; k++){
								var $page = $temp.clone();
								$('.print-milk-divide', $page).text(number++ + '/' + statistics.cupCount);
								$pageContainer.prepend($page);
							}
						}
					}
					
					
					var headElements ='<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="chrome=1"/>';
					var basePath = $('base').attr('href');
				    var options = { 
				    		mode 		: "iframe", 
				    		extraHead 	: headElements,
				    		extraCss	: basePath + 'media/admin/waimai/css/print.css'
				    	};
				    
				    $pageContainer.clone().printArea(options);
				    currentOrder.setPrinted();
				}
				//判断当前不存在正在处理的订单条目
				/*if(currentOrderItem == null){
					$('.print-order-code', $printArea).text(currentOrder.getCode());
					$('.print-milk-divide', $printArea).text('1/1');
					$('.order-time', $printArea).text('10:41:20');
					$('.print-order-price', $printArea).text('10元');
					$('.print-addr', $printArea).text('天虹商场A幢502');
					$('.print-contact', $printArea).text('888270507');
					var headElements ='<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="chrome=1"/>';
					var basePath = $('base').attr('href');
				    var options = { 
				    		mode 		: "iframe", 
				    		extraHead 	: headElements,
				    		extraCss	: basePath + 'media/admin/waimai/css/print.css'
				    	};
				    
					$printArea.clone().printArea(options);
					//currentOrder.print(printTemp);
				    
				}else{
					alertMsg('请先确认当前正在处理的奶茶后执行打印。');
				}*/
			}
		}
		/**
		 * 调用layer插件提示信息
		 */
		function alertMsg(msg){
			layer.alert(msg,{icon: 1});
		}
		
		/**
		 * 根据手机号从后台获得已经记录的收件人信息
		 */
		function loadReceiverInfo(contact, loadFunc){
			$.post('admin/waimai/loadReceiver', {
				contact		: contact
			}, function(data){
				var json = data;
				if(typeof data === 'string'){
					json = $.parseJSON(data);
				}
				if(json.status === 'suc'){
					(loadFunc || $.noop).apply(this, [json.receiver]);
				}
			});
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
			cash = 0,
			//订单是否被打印过
			printed = false
		;
		
		/**
		 * 创建
		 */
		this.init = function(){
			
		};
		/**
		 * 获得订单号
		 * @return {String} 订单号
		 */
		this.getCode = function(){
			return orderCode;
		};
		/**
		 * 设置订单号
		 * @param {String} 订单号
		 * @return this
		 */
		this.setCode = function(_orderCode){
			orderCode = _orderCode;
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
		};
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
			//超过10杯的优惠
			var cupCount = this.makeStatistics().cupCount;
			//获得要优惠的杯数
			var promotionCount = Math.floor(cupCount / 11);
			//获得优惠价格
			var promotionPrice = Math.floor(result * (promotionCount / cupCount) / 100) * 100;
			//减去优惠价格
			result -= promotionPrice;
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
				result.cupCount += itemList[i].getCount();
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
			if(platform instanceof TakeawayPlatform || platform === null)
			takeawayPlatform = platform;
		};
		/**
		 * 获得订单的外卖平台信息
		 */
		this.getTakeawayPlatform = function(){
			return takeawayPlatform;
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
		};
		/**
		 * 获得当前的收货人对象
		 * @return {Receiver} 收货人对象。如果还没填写，则返回null
		 */
		this.getReceiver = function(){
			return receiver;
		};
		/**
		 * 设置收货人对象
		 * @param receiver {Receiver} 要设置的收货人对象
		 * @return this
		 */
		this.setReceiver = function(_receiver){
			receiver = _receiver;
		};
		/**
		 * 将订单对象转换成要提交的数据类型
		 */
		this.getSubmitData = function(){
			var data = {};
			//订单号
			data.code = this.getCode();
			//订单总价格
			data.totalIncome = this.accountPrice();
			//收货信息
			if(receiver instanceof Receiver){
				data.receiver = {
					id		: receiver.getId(),
					name	: receiver.getName(),
					address	: receiver.getAddress(),
					contact	: receiver.getContactNumber()
				}
			}
			//外卖平台信息
			var takeaway = this.getTakeawayPlatform();
			if(takeaway instanceof TakeawayPlatform){
				data.takeaway = {
					key		: takeaway.getId()
				};
			}
			//订单条目
			data.items = [];
			for(var i in itemList){
				var item = itemList[i];
				if(item instanceof OrderItem){
					var itemData = {};
					itemData.drinkId = item.getDrink().getId();
					itemData.count = item.getCount();
					itemData.additions = [];
					itemData.income = item.accountPrice();
					var additions = item.getAdditions();
					for(var j in additions){
						var addition = additions[j];
						if(addition instanceof AdditionInfo){
							itemData.additions.push({
								id		: addition.getId()
							});
						}
					}
					var heat = item.getHeat();
					if(heat instanceof HeatInfo){
						itemData.heatKey = heat.getId();
					}
					var sweetness = item.getSweetness();
					if(sweetness instanceof Sweetness){
						itemData.sweetnessKey = sweetness.getId();
					}
					
					data.items.push(itemData);
				}
			}
			return data;
		};
		/**
		 * 判断当前订单是否已经被打印
		 * @return {Boolean} 已经打印过，返回true
		 */
		this.hasPrinted = function(){
			return printed; 
		};
		/**
		 * 调用该方法，标识当前订单已经被打印过
		 */
		this.setPrinted = function(){
			printed = true;
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
			count = 1,
			//item
			$dom	= null
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
		 * 获得所有加料对象
		 * @return {Array[AdditionInfo]} 加料对象集合 
		 */
		this.getAdditions = function(){
			var additions = [];
			for(var key in additionMap){
				additions.push(additionMap[key]);
			}
			return additions;
		}
		/**
		 * 设置热度
		 * @param heat {HeatInfo} 热度对象
		 * @return this
		 */
		this.setHeat = function(_heat){
			if(_heat instanceof HeatInfo || _heat == null){
				heat = _heat;
			}else{
				$.error('传入的heat参数不是heatInfo对象[' + _heat + ']');
			}
		};
		/**
		 * 返回热度对象
		 * @retrun {HeatInfo} 
		 */
		this.getHeat = function(){
			return heat;
		};
		/**
		 * 设置甜度
		 * @param sweetness {Sweetness} 甜度对象
		 * @return this
		 */
		this.setSweetness = function(_sweetness){
			if(_sweetness instanceof Sweetness || _sweetness == null){
				sweetness = _sweetness;
			}else{
				$.error('传入的sweetness参数不是Sweetness对象[' + _sweetness + ']');
			}
		};
		/**
		 * 返回甜度
		 * @return {Sweetness}甜度
		 */
		this.getSweetness = function(){
			return sweetness;
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
			return this.eachPrice() * this.getCount();
		};
		this.eachPrice = function(){
			var drink = this.getDrink();
			var result = drink.getPrice();
			var hasAddition = false;
			//遍历所有加料，添加加价
			for(var key in additionMap){
				if(additionMap[key] instanceof AdditionInfo){
					result += additionMap[key].getAdditionPrice(this);
					hasAddition = true;
				}
			}
			//加料时加价
			if(hasAddition){
				result += (drink.getAdditionPrice() || 0);
			}
			//温度是否加价
			if(heat != null){
				result += heat.getAdditionPrice(this);
			}
			//甜度是否加价
			if(sweetness != null){
				result += sweetness.getAdditionPrice(this);
			}
			if(utils.isInteger(result)){
				return result;
			}else{
				return 0;
			}
		}
		
		
		/**
		 * 获得当前订单条目所选的饮料
		 * @return {Drink} 所选的饮料信息对象
		 */
		this.getDrink = function(){
			return param.drink;
		};
		/**
		 * 设置订单条目的jQueryDom对象
		 * @param $dom jQuery对象
		 * @return this
		 */
		this.setDom = function(_$dom){
			$dom = _$dom;
		};
		/**
		 * 获得订单条目的jQueryDom对象
		 * @return jQuery
		 */
		this.getDom = function(){
			return $dom;
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
				//加料时加价
				aPrice	: 0,
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
		 * 
		 */
		this.getAdditionPrice = function(){
			return parseInt(param.aPrice);
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
				//标签数组
				tags		: [],
				//加价策略(函数this为当前AdditionInfo对象，参数为搭配的OrderItem对象)
				strategy	: $.noop
		}
		
		var param = $.extend({}, defaultParam, _param);
		
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
			if(orderItem instanceof OrderItem && param.strategy !== $.noop && typeof param.strategy === 'function'){
				return parseInt(param.strategy.apply(this, [orderItem]));
			}
		};
		/**
		 * 判断是否有标签
		 * @param tag {String} 标签字符串 
		 * @return {Boolean} 标签是否存在
		 */
		this.hasTag = function(tag){
			if(typeof tag === 'string'){
				return $.inArray(tag, param.tags) >= 0;
			}else if(tag instanceof RegExp){
				return this.getTags(tag).length > 0;
			}
		};
		/**
		 * 根据正则对象获得符合正则表达式的标签
		 * @param regex {RegExp} 没有传入的话，返回所有的tag
		 * @return {Array[String]} 标签数组
		 */
		this.getTags = function(regex){
			var tags = [];
			for(var i in param.tags){
				if(regex === undefined || (regex instanceof RegExp && regex.test(param.tags[i]))){
					tags.push(param.tags[i]);
				}
			}
			return tags;
		}
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
					return param.strategy.apply(this, [orderItem]);
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