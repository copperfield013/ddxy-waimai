<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<nav style="padding: 1em 0">
	<form class="form-inline" action="admin/order-manage/ordermonth-list" >
	</form>
</nav>
<table class="table">
	<thead>
		<tr>
			<th>序号</th>
			<th>月份</th>
			<th>总收入</th>
			<th>总订单数</th>
			<th>总杯数</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${monthlist }" var="item" varStatus="i">
			<tr>
				<td>${i.index + 1 }</td>
				<td>${item.theYear }.${item.theMonth } </td>
				<td><fmt:formatNumber value="${item.income/100 }" minFractionDigits="2" />元</td>
				<td>${item.orderCount }</td>
				<td>${item.cupCount }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>

