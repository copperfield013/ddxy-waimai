<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<nav style="padding: 1em 0">
	<form class="form-inline" action="admin/order-manage/order-statistics" >
		<div class="form-group">
			<label class="form-control-title">下单时间</label>
	        <div class="controls" css-display="inline-block">
	            <div class="input-group">
	                <input type="text" class="form-control" id="dateRange" name="dateRange" readonly="readonly" 
	                	value="${criteria.dateRange }" css-width="25em"  css-cursor="text" />
	                <span class="input-group-addon">
	                    <i class="fa fa-calendar"></i>
	                </span>
	            </div>
	        </div>
		</div>
		<button type="submit" class="btn btn-default">查询</button>
	</form>
</nav>
<table class="table">
	<thead>
		<tr>
			<th>序号</th>
			<th>日期</th>
			<th>总收入</th>
			<th>总订单数</th>
			<th>总杯数</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${statisticsList }" var="item" varStatus="i">
			<tr>
				<td>${i.index + 1 }</td>
				<td><fmt:formatDate value="${item.theDay }" pattern="yyyy-MM-dd" />  </td>
				<td><fmt:formatNumber value="${item.income/100 }" minFractionDigits="2" />元</td>
				<td>${item.orderCount }</td>
				<td>${item.cupCount }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
<script>
	$(function(){
		seajs.use([], function(){
			$('#dateRange').daterangepicker({
				format 				: 'YYYY-MM-DD',
				separator			: '~',
				locale				: {
					applyLabel	: '确定',
	                cancelLabel: '取消',
	                fromLabel: '从',
	                toLabel: '到'
				}
			});
		});
	});

</script>
