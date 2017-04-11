<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="detail">
	<dl>
		<dt>接收人姓名</dt>
		<dd>${order.receiverName }</dd>
	</dl>
	<dl>
		<dt>配送地址</dt>
		<dd>${order.receiverAddress }</dd>
	</dl>
	<dl>
		<dt>总收入</dt>
		<dd>${order.totalIncome/100 }元</dd>
	</dl>
	<c:if test="${orderItems != null }">
		<c:forEach items="${orderItems }" var="item">
		<input type="hidden" name="itemId" value="${item.id }"/>
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
		</c:forEach>
	</c:if>
</div>