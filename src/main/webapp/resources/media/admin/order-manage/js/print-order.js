/**
 * 外卖订单打印
 */
define(function(require, exports, module){
	var Ajax = require('ajax')
	function printOrder(id){
		Ajax.ajax('admin/order-manage/getPrintOrder', {
			'id' : id
			},function(json){
				var itemCount = json.drinks.length;
				var number = 1;
				if(itemCount > 0){
					var $pageContainer = $('<div>');
					for(var i = 0; i< itemCount; i++){
						var item = json.drinks[i];
						var $printArea = $('.PrintArea:first');
						var $temp = $printArea.clone();
						$('.print-order-code', $temp).text(json.orderCode);
						$('.order-time', $temp).text(json.orderTime);
						$('#view-name', $temp).text(item.name);
						$('#heat', $temp).text(item.heat == "null"? '': item.heat);
						$('#sweetness', $temp).text(item.sweetness == "null"? '': item.sweetness);
						//填写加料数据
						var $additionArea = $('#addition-area', $temp).empty();
						$additionArea.append('<label>' + item.additions + '</label>');
						//该杯价格
						$('.print-order-price', $temp).text(item.price / 100 + '元');
						//联系人信息
						$('.print-addr', $temp).text(json.receiverAddress);
						$('.print-contact', $temp).text(json.receiverConcat);
						$('.print-name', $temp).text(json.receiverName);
						
						for(var k = 0; k < item.cupCount; k++){
							var $page = $temp.clone();
							$('.print-milk-divide', $page).text(number++ + '/' + json.totalCount);
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
			    //appendStatPage($pageContainer, currentOrder);
			    var $temp1 = $pageContainer.children('div:first').clone();
				var $totalPriceRow = $('<div class="print-part-row-half">'),
					$originalPriceRow = $('<div class="print-part-row-half">'),
					$promotionRow = $('<div class="print-part-row-half">'),
					$totalCountRow = $('<div class="print-part-row-half">')
					;
				
				$originalPriceRow
					.append($('<span class="print-content-title">').text('原价：'))
					.append($('<span>').text((json.originIncome / 100 ) + '元'))
					;
				$promotionRow
					.append($('<span class="print-content-title">').text('优惠价格：'))
					.append($('<span>').text(((json.originIncome - json.totalIncome) / 100 ) + '元'))
					;
				$totalPriceRow
					.append($('<span class="print-content-title">').text('总价：'))
					.append($('<span>').text((json.totalIncome / 100 )+ '元'))
					;
				$totalCountRow
					.append($('<span class="print-content-title">').text('总杯数：'))
					.append($('<span>').text(json.totalCount))
				;
				
				$('.print-title', $temp1).text('统计单');
				$('.print-part-area', $temp1)
					.empty()
					.append($originalPriceRow)
					.append($promotionRow)
					.append($totalPriceRow)
					.append($totalCountRow)
					;
				$('.print-milk-divide', $temp1).remove();
				$('.print-amount-area', $temp1).remove();
				$pageContainer.append($temp1);
			    $pageContainer.clone().printArea(options);
			});
	}
	
	module.exports = printOrder;
});