<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
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
				<td>${item.code }</td>
				<td>${item.receiverName }</td>
				<td>${item.receiverContact }</td>
				<td>${item.cupCount }</td>
				<td>
					<fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
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
							Ajax.ajax('admin/order-manage/setOrderStatus', {
								'id'	: orderId,
								'status': status
							}, {
								page 	: page
							});
						}
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
		});
		
	});
</script>

