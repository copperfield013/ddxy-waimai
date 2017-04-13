<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<script src="${basePath }media/admin/plugins/printArea/jquery.PrintArea.js"></script>
<style>
	a.cancel-order {
	    font-weight: bold;
	    color: #CD00CD;
	}
	a.reset-order {
	    font-weight: bold;
	    color: #4F94CD;
	}
</style>
<nav style="padding: 1em 0">
	<form class="form-inline" action="admin/order-manage/order-list" >
		<div class="form-group">
			<label class="form-control-title">下单时间</label>
	        <div class="controls" css-display="inline-block">
	            <div class="input-group">
	                <input type="text" class="form-control" id="timeRange" name="timeRange" readonly="readonly" 
	                	value="${criteria.timeRange }" css-width="25em"  css-cursor="text" />
	                <span class="input-group-addon">
	                    <i class="fa fa-calendar"></i>
	                </span>
	            </div>
	        </div>
			<label class="form-control-title" for="orderCode">订单号</label>
			<input type="text" css-width="8em" class="form-control" id="orderCode" name="orderCode" placeholder="订单号" value="${criteria.orderCode }">
			<label class="form-control-title" for="contactName">联系人</label>
			<input type="text" css-width="8em" class="form-control" id="contactName" name="contactName" placeholder="联系人" value="${criteria.contactName }">
			<label class="form-control-title" for="contactNumber">联系号码</label>
			<input type="text" css-width="8em" class="form-control" id="contactNumber" name="contactNumber" placeholder="联系号码" value="${criteria.contactNumber }">
<!-- 
			<input type="text" class="form-control date-picker" id="startTime" name="startTime" placeholder="起始时间">
			~
			<input type="text" class="form-control" id="endTime" name="endTime" placeholder="结束时间"> -->
		</div>
		<button type="submit" class="btn btn-default">查询</button>
	</form>
</nav>
<table class="table">
	<thead>
		<tr>
			<th>序号</th>
			<th>订单号</th>
			<th>联系人</th>
			<th>联系号码</th>
			<th>杯数</th>
			<th>下单时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${items }" var="item" varStatus="i">
			<tr data-id="${item.orderId }" data-code="${item.code }">
				<td>${i.index + 1}</td>
				<td><a href="admin/order-manage/order-detail?id=${item.orderId }" page-type="tab" target="@order-detail-${item.orderId }" title="订单${item.code }">${item.code }</a></td>
				<td>${item.receiverName }</td>
				<td>${item.receiverContact }</td>
				<td>${item.cupCount }</td>
				<td>
					<fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<a href="#" class="print-order">打印</a>
					<c:choose>
						<c:when test="${item.status == 'canceled' }">
							<a href="#" class="reset-order">恢复</a>
						</c:when>
						<c:otherwise>
							<a href="#" class="cancel-order">取消</a>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
<div style="display: none;">
			<div class="PrintArea print-wrapper">
				<div class="print-content tttt">
					<div class="print-title-area">
						<div class="print-title">
							一點點
						</div>
					</div>
					<div class="print-desc-area">
						<div class="print-desc">
							<span class="print-desc-title">编号:</span>
							<span class="print-desc-content print-order-code">M150003T</span>
							<span class="print-desc-content print-milk-divide">1/1</span>
						</div>
						<div class="print-desc float-right">
							<span class="print-desc-title">下单时间:</span>
							<span class="print-desc-content order-time">10:41:20</span>
						</div>
					</div>
					<div class="print-part-area">
						<div class="print-part-row">
							<span class="print-content-title">名称：</span>
							<span id="view-name">大杯·古早味鲜奶+波霸</span>
							<span id="heat">去冰</span>
							<span id="sweetness">七分甜</span>
						</div>
						<div class="print-addition-row">
							<span class="print-content-title">加料：</span>
							<span id="addition-area">
								<label>椰果</label>
								<label>红豆</label>
								<label>珍珠</label>
								<label>布丁</label>
								<label>椰果</label>
								<label>红豆</label>
								<label>珍珠</label>
								<label>椰果</label>
								<label>红豆</label>
								<label>珍珠</label>
								<label>布丁</label>
							</span>
						</div>
						
					</div>
					<div class="print-foot-area">
						<div class="print-amount-area">
							<span class="print-content-title">价格：</span>
							<span class="print-order-price">20元</span>
						</div>
						<div class="print-addr-area">
							<span class="print-content-title">配送地址：</span>
							<span class="print-addr">天虹商场A幢3001天虹商场A幢3001天虹商场A幢3001</span>
						</div>
						<div class="print-receiver-row">
							<div>
								<span class="print-content-title">联系号码：</span>
								<span class="print-contact">123456789012</span>
							</div>
							<div>
								<span class="print-content-title">联系人：</span>
								<span class="print-name">张三</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
<script>
	$(function(){
		seajs.use(['ajax', 'dialog', 'utils'], function(Ajax, Dialog, utils){
			function setOrderStatus(status, confirmMsg){
				var $row = $(this).closest('tr[data-id]');
				var orderId = $row.attr('data-id');
				var page = $(this).getLocatePage();
				if(orderId){
					var orderCode = $row.attr('data-code');
					Dialog.confirm(confirmMsg + orderCode + '？', function(isYes){
						if(isYes === true){
							var password = $("#password").val();
							Ajax.ajax('admin/password/validate',{
								password : password
							},function(json){
								if(json.status == 'YES'){
									Ajax.ajax('admin/order-manage/setOrderStatus', {
										'id'	: orderId,
										'status': status
									}, {
										page 	: page
									});
								}else{
									Dialog.notice("密码错误！","error");
								}
							});
								/* Ajax.ajax('admin/order-manage/setOrderStatus', {
									'id'	: orderId,
									'status': status
								}, {
									page 	: page
								}); */
							
						}
					}, {
						domHandler : function(dom){
							dom.append("<div><span>请输入密码：</span><input id=\"password\" type=\"password\" name=\"password\"/></div>");
						},
						width		: '300px',
						height		: '400px',
						top			: '100px'
					});
				}
			}
			
			$('.cancel-order').click(function(){
				setOrderStatus.apply(this, ['canceled', '确认取消订单']);
				return false;
			});
			$('.reset-order').click(function(){
				setOrderStatus.apply(this, ['', '确认恢复订单']);
				return false;
			});
			$('#timeRange').daterangepicker({
				format 				: 'YYYY-MM-DD HH:mm:ss',
				timePicker			: true,
				timePicker12Hour	: false,
				timePickerIncrement : 5,
				separator			: '~',
				locale				: {
					applyLabel	: '确定',
	                cancelLabel: '取消',
	                fromLabel: '从',
	                toLabel: '到'
	               /*  weekLabel: 'W',
	                customRangeLabel: 'Custom Range',
	                daysOfWeek: moment()._lang._weekdaysMin.slice(),
	                monthNames: moment()._lang._monthsShort.slice(),
	                firstDay: 0 */
				}
			});
			
			$(".print-order").click(function(){
				var $row = $(this).closest('tr[data-id]');
				var orderId = $row.attr('data-id');
				Ajax.ajax('admin/order-manage/getPrintOrder', {
					'id' : orderId
					},function(json){
						console.log(json);
						var itemCount = json.drinks.length;
						var number = 1;
						if(itemCount > 0){
							var $pageContainer = $('<div>');
							for(var i = 0; i< itemCount; i++){
								var item = json.drinks[i];
								var $printArea = $('.PrintArea');
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
			});
		});
	});
</script>

