<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<script src="${basePath }media/admin/plugins/printArea/jquery.PrintArea.js"></script>
<style>
	.order-table-cell{
		display: block;
		width: 100%;
	}
	.order-table-cell>.item-detail-wrapper:nth-child(odd) {
	    clear: left;
	}
	.item-detail-wrapper {
	    display: block;
	    float: left;
	    width: 41%;
	    margin-right: 3%;
	}
	.item-detail{
		display: table;
	    width: 100%;
	    border: 1px solid #FF0000;
	    padding: 10px 10px;
	    margin: 20px 10px 10px 0;
	}
	.item-detail>dl{
		display: table-row;
   		width: 100%;
	}
	.item-detail>dl>dt, .item-detail>dl>dd{
		border-bottom: dashed 1px #ddd;
	    height: 2em;
	    line-height: 2em;
	    vertical-align: middle;
	}
	.item-detail>dl>dt{
		display: table-cell;
	    width: 8em;
	    text-align: center;
	}
	.item-detail>dl>dd{
		display: table-cell;
		padding: 0 0.5em;
	}
</style>
<div class="detail" id="order-${order.id }">
	<input id="orderIdHidden" type="hidden" name="id" value="${order.id }">
	<dl>
		<dt>订单号</dt>
		<dd>
			${order.code }
			<a href="#" class="fa fa-print print-order" title="打印"></a>
		</dd>
	</dl>
	<dl>
		<dt>收货人姓名</dt>
		<dd>${order.receiverName }</dd>
	</dl>
	<dl>
		<dt>收货人联系方式</dt>
		<dd>${order.receiverContact }</dd>
	</dl>
	<dl>
		<dt>收货地址</dt>
		<dd>${order.receiverAddress }</dd>
	</dl>
	<dl>
		<dt>订单原价</dt>
		<dd>${order.originIncome/100 }元</dd>
	</dl>
	<dl>
		<dt>支付金额</dt>
		<dd>${order.totalIncome/100 }元</dd>
	</dl>
	<dl>
		<dt>总杯数</dt>
		<dd>${order.cupCount }</dd>
	</dl>
	<dl>
		<dt>下单时间</dt>
		<dd>
			<fmt:formatDate value="${order.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
		</dd>
	</dl>
	<dl>
		<dt></dt>
		<dd>
			<div class="order-table-cell">
				<c:if test="${orderItems != null }">
					<c:forEach items="${orderItems }" var="item" varStatus="status">
						<div class="item-detail-wrapper">
							<div class="item-detail">
								<dl>
									<dt>饮料名称</dt>
									<dd>${item.drinkName }</dd>
								</dl>
								<dl>
									<dt>杯数</dt>
									<dd>${item.count }</dd>
								</dl>
								<dl>
									<dt>甜度</dt>
									<dd>${sweetnessMap[item.sweetnessKey] }</dd>
								</dl>
								<dl>
									<dt>冰度</dt>
									<dd>${heatMap[item.heatKey] }</dd>
								</dl>
								<dl>
									<dt>加料</dt>
									<dd>${additionsNameMap[item.id] }</dd>
								</dl>
							</div>
						</div>
					</c:forEach>
				</c:if>
			</div>
		</dd>
	</dl>
</div>
<jsp:include page="printArea.jsp"></jsp:include>
<script>
	$(function(){
		seajs.use(['order-manage/js/print-order'],function(printOrder){
			
			var orderId = '${order.id}';
			var $page = $('#order-' + orderId);
			
			$(".print-order", $page).click(function(){
				printOrder(orderId);
			});
		});
	});
</script>
