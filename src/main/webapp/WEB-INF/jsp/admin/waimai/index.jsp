<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<base href="${basePath }" />
		<jsp:include page="/WEB-INF/jsp/admin/common/admin-include.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="media/admin/waimai/css/waimai-main.css"   />
	</head>
	<body>
		<div class="ydd-wm-container">
			<div class="ydd-wm-left">
				<ul class="ydd-wm-left-wrapper">
					<li class="ydd-wm-left-item">
						<a class="ydd-wm-left-label">下单</a>
					</li>
					<li class="ydd-wm-left-item">
						<a class="ydd-wm-left-label">订单管理</a>
					</li>
				</ul>
			</div>
			<div class="ydd-wm-main">
				<div class="ydd-wm-main-top">
					<div class="ydd-wm-main-title-wrapper">
						<span class="ydd-wm-main-title active">
							<a>找好茶</a>
						</span>
						<span class="ydd-wm-main-title">
							<a>找奶茶</a>
						</span>
						<span class="ydd-wm-main-title">
							<a>找口感</a>
						</span>
						<span class="ydd-wm-main-title">
							<a>找新鲜</a>
						</span>
						<span class="ydd-wm-main-title">
							<a>找拿铁</a>
						</span>
						<span class="ydd-wm-main-title">
							<a>特殊饮品</a>
						</span>
						<span class="ydd-wm-main-title">
							<a>店长推荐</a>
						</span>
					</div>
				</div>
				<!-- 选择商品区域 -->
				<div class="ydd-wm-main-center">
					<jsp:include page="menu1.jsp"></jsp:include>
				</div>
				<div class="ydd-wm-main-addition">
					<div class="ydd-wm-main-addition-wrapper">
						<jsp:include page="addition.jsp" />
					</div>
					<div class="ydd-wm-main-addition-set">
						<span class="ydd-wm-main-addition-sure">加料确定</span>
						<span class="meituan-set"><img src="media/admin/waimai/image/meituan.png" /></span>
						<span class="ele-set"><img src="media/admin/waimai/image/eleme.png" /></span>
						<span class="baidu-set"><img style="height: 90%;width: initial;" src="media/admin/waimai/image/baidu.png" /></span>
					</div>
				</div>
				<div class="ydd-wm-main-count">
					<div class="ydd-wm-main-count-left">
						<div class="ydd-wm-main-count-row">
							<div id="original-price-div" class="ydd-wm-main-count-item">
								<span class="ydd-wm-main-count-title">原价：</span>
								<span id="original-price" class="ydd-wm-main-count-value">52.0</span>
							</div>
							<div id="milk-count-div" class="ydd-wm-main-count-item">
								<span class="ydd-wm-main-count-title">数量：</span>
								<span id="milk-count" class="ydd-wm-main-count-value">5</span>
							</div>
						</div>
						<div class="ydd-wm-main-count-row">
							<div id="promotion-div" class="ydd-wm-main-count-item">
								<span class="ydd-wm-main-count-title">优惠：</span>
								<span id="promotion" class="ydd-wm-main-count-value">0.0</span>
							</div>
							<div id="change-price-div" class="ydd-wm-main-count-item">
								<span class="ydd-wm-main-count-title">找零：</span>
								<span id="change-price" class="ydd-wm-main-count-value" >0.0</span>
							</div>
						</div>
					</div>
					<div id="total-price" class="ydd-wm-main-count-right">
						<label>52.0</label>
					</div>
				</div>
			</div>
			<div class="ydd-wm-right">
				<div class="ydd-wm-right-top">
					江干天虹店
				</div>
				<div class="ydd-wm-right-desc">
					<label id="current-time">03/13 13:41:13</label>
					<label id="">早班</label>
					<label id="order-code">2017031302003</label>
				</div>
				<div id="order-list-container">
					<div id="order-list-title">
						<span class="order-item-selected">产品名称</span>
						<span class="order-item-number">序号</span>
						<span class="order-item-count">数量</span>
					</div>
					<div id="order-list-wrapper">
						<div class="order-item">
							<div class="ydd-wm-order-row">
								<span class="order-item-selected">大·波霸绿</span>
								<span class="order-item-number">1</span>
								<span class="order-item-count">1</span>
							</div>
							<div class="ydd-wm-order-row desc-row">
								<span class="order-item-desc">
									奶少，仙草，燕麦
								</span>
							</div>
						</div>
						<div class="order-item">
							<div class="ydd-wm-order-row">
								<span class="order-item-selected">大·波霸绿</span>
								<span class="order-item-number">1</span>
								<span class="order-item-count">1</span>
							</div>
							<div class="ydd-wm-order-row desc-row">
								<span class="order-item-desc">
									奶少，仙草，燕麦
								</span>
							</div>
						</div>
						<div class="order-item">
							<div class="ydd-wm-order-row">
								<span class="order-item-selected">大·波霸绿</span>
								<span class="order-item-number">1</span>
								<span class="order-item-count">1</span>
							</div>
							<div class="ydd-wm-order-row desc-row">
								<span class="order-item-desc">
									奶少，仙草，燕麦
								</span>
							</div>
						</div>
						<div class="order-item">
							<div class="ydd-wm-order-row">
								<span class="order-item-selected">大·波霸绿</span>
								<span class="order-item-number">1</span>
								<span class="order-item-count">1</span>
							</div>
							<div class="ydd-wm-order-row desc-row">
								<span class="order-item-desc">
									奶少，仙草，燕麦
								</span>
							</div>
						</div>
						<div class="order-item">
							<div class="ydd-wm-order-row">
								<span class="order-item-selected">大·波霸绿</span>
								<span class="order-item-number">1</span>
								<span class="order-item-count">1</span>
							</div>
							<div class="ydd-wm-order-row desc-row">
								<span class="order-item-desc">
									奶少，仙草，燕麦
								</span>
							</div>
						</div>
						<div class="order-item">
							<div class="ydd-wm-order-row">
								<span class="order-item-selected">大·波霸绿</span>
								<span class="order-item-number">1</span>
								<span class="order-item-count">1</span>
							</div>
							<div class="ydd-wm-order-row desc-row">
								<span class="order-item-desc">
									奶少，仙草，燕麦
								</span>
							</div>
						</div>
					</div>
				</div>
				<div id="checkout-container">
					<div id="checkout-view-number">
						<label id="checkout-number">0</label>
					</div>
					<div id="checkout-keyboard">
						<div id="number-key-area">
							<span>1</span>
							<span>2</span>
							<span>3</span>
							<span>4</span>
							<span>5</span>
							<span>6</span>
							<span>7</span>
							<span>8</span>
							<span>9</span>
							<span>0</span>
							<span>00</span>
							<span id="checkout-clear">C</span>
						</div>
						<span id="payment-mode"></span>
					</div>
					<div class="action-area">
						<span>会员</span>
						<span class="disabled">外送管理</span>
						<span class="disabled">分机</span>
						<span class="disabled">现金结账</span>		
					</div>
				</div>
			</div>
		</div>
	</body>
</html>