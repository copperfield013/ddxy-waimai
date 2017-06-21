<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<base href="${basePath }" />
		<title>外卖点单系统</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
		<link rel="shortcut icon" href="${basePath }media/admin/main/image/icon.jpg" >
		<link rel="stylesheet" type="text/css" href="${basePath }media/admin/waimai/css/waimai-main-fixed.css"  >
		<jsp:include page="/WEB-INF/jsp/admin/common/admin-include.jsp"></jsp:include>
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
						<c:forEach items="${groups }" var="group" varStatus="i">
							<c:set var="activeClass" value="${i.index == 0? 'active': '' }" />
							<span class="ydd-wm-main-title tab-title ${activeClass }" tab-id="tab-${group.id }">
								<a>${group.view }</a>
							</span>
						</c:forEach>
					</div>
				</div>
				<!-- 选择商品区域 -->
				<div class="ydd-wm-main-center">
					<c:forEach items="${groups }" var="group" varStatus="i">
						<c:set var="style" value="${i.index == 0? '': 'style=\"display:none;\"' }" />
						<div class="tab-content" id="tab-${group.id }" ${style }>
							<div class="ydd-wm-main-goods-wrapper">
								<c:forEach items="${itemMap[group.id] }" var="item">
									<c:choose>
										<c:when test="${item.disabled != 1 }">
											<span data-size="${item.size }" data-price="${item.price }" data-price-a="${item.additionPrice }" data-id="${item.id }" data-name="${sizeMap[item.size] }-${item.name}" >${item.view }</span>
										</c:when>
										<c:otherwise>
											<span class="disabled" data-id="${item.id }" >${item.view }</span>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</div>
						</div>
					</c:forEach>
				</div>
				<div class="ydd-wm-main-addition">
					<div class="ydd-wm-main-addition-wrapper">
						<div class="ydd-wm-additions-container">
							<c:forEach items="${additions }" var="addition">
								<span class="addition" data-id="${addition.id }" data-price="${addition.basePrice }" data-name="${addition.name }" data-tag="${addition.tags }">${addition.view }</span>
							</c:forEach>
						</div>
						<div class="ydd-wm-heats-container">
							<c:forEach items="${heatMap }" var="heat">
								<span class="heat" data-id="${heat.key }" data-price="0" data-name="${heat.value }" >${heat.value }￥0</span>
							</c:forEach>
							<span class="heat disabled" data-id="" data-price="" data-name="" >保留T06￥0</span>
						</div>
						<div class="ydd-wm-sweetness-container">
							<c:forEach items="${sweetnessMap }" var="sweetness">
								<span class="sweetness" data-id="${sweetness.key }" data-price="0" data-name="${sweetness.value }" >${sweetness.value }￥0</span>
							</c:forEach>
						</div>
					</div>
					<div class="ydd-wm-main-addition-set">
						<span class="ydd-wm-main-addition-sure">确定</span>
						<span class="meituan-set" data-id="meituan" data-name="美团外卖"><img src="media/admin/waimai/image/meituan.png" /></span>
						<span class="eleme-set" data-id="eleme" data-name="饿了么"><img src="media/admin/waimai/image/eleme.png" /></span>
						<span class="baidu-set" data-id="baidu" data-name="百度外卖"><img style="height: 90%;width: initial;" src="media/admin/waimai/image/baidu.png" /></span>
					</div>
				</div>
				<div class="ydd-wm-main-count">
					<div class="ydd-wm-main-count-receiver">
						<input type="hidden" id="hidden-receiver-id" />
						<div class="ydd-wm-main-count-receiver-row">
							<div class="receiver-name-col">
								<div class="ydd-wm-receiver-view-title">联系人姓名:</div>
								<div id="view-receiver-name" class="ydd-wm-receiver-view-content"></div>
							</div>
							<div class="receiver-contact-col">
								<div class="ydd-wm-receiver-view-title">手机号码:</div>
								<div id="view-receiver-contact" class="ydd-wm-receiver-view-content"></div>
							</div>
						</div>
						<div class="ydd-wm-main-count-receiver-row">
							<div id="view-receiver-comment" class="ydd-wm-receiver-view-content"  style="display:none;"></div>
							<div class="ydd-wm-receiver-view-title ydd-wm-receiver-address-title">配送地址:</div>
							<div id="view-receiver-address" class="ydd-wm-receiver-view-content"></div>									
						</div>
					</div>
					<div id="total-price" class="ydd-wm-main-count-right">
						<label>0.00</label>
					</div>
				</div>
				<div class="ydd-wm-main-center-cover"></div>
			</div>
			<div class="ydd-wm-right">
				<div class="ydd-wm-right-top">
					江干天虹店
				</div>
				<div class="ydd-wm-right-desc">
					<label id="current-time">03/13 13:41:13</label>
				</div>
				<div id="order-list-container">
					<div id="order-list-title">
						<span class="order-item-number">序号</span>
						<span class="order-item-selected">产品名称</span>
						<span class="order-item-count">数量</span>
					</div>
					<div id="order-list-wrapper">
					</div>
				</div>
				<div id="checkout-container">
					<div id="checkout-view-number">
						<label id="checkout-number">0</label>
					</div>
					<div id="checkout-keyboard">
						<div id="number-key-area">
							<span data-num="1">1</span>
							<span data-num="2">2</span>
							<span data-num="3">3</span>
							<span data-num="4">4</span>
							<span data-num="5">5</span>
							<span data-num="6">6</span>
							<span data-num="7">7</span>
							<span data-num="8">8</span>
							<span data-num="9">9</span>
							<span data-num="0">0</span>
							<span data-num="00">00</span>
							<span id="checkout-clear">C</span>
						</div>
						<span id="checkout-confirm">确定</span>
					</div>
					<div class="action-area">
						<span id="open-receiver-edit">收货人信息</span>
						<span id="save-order">保存订单</span>
						<span id="print-order" class="disabled">打印订单</span>
						<span class="" id="create-order">创建新订单</span>		
					</div>
				</div>
			</div>
			<div class="ydd-wm-receiver-container" style="display: none;">
				<div class="ydd-wm-receiver-wrapper">
					<div class="ydd-wm-receiver-row">
						<div class="ydd-wm-msg-title">备注</div>
						<div class="ydd-wm-msg-content">
							<input type="text" id="receiver-comment" />
						</div>
					</div>
					<div class="ydd-wm-receiver-row">
						<div class="ydd-wm-msg-title">手机号码</div>
						<div class="ydd-wm-msg-content">
							<input type="text" id="receiver-number" />
						</div>
					</div>
					<div class="ydd-wm-receiver-row">
						<div class="ydd-wm-msg-title">收货人姓名</div>
						<div class="ydd-wm-msg-content">
							<input type="text" id="receiver-name" />
							<input type="hidden" id="receiver-id" />
						</div>
					</div>
					<div class="ydd-wm-receiver-row">
						<div class="ydd-wm-msg-title">收货地址</div>
						<div class="ydd-wm-msg-content">
							<input type="text" id="receiver-address" >
						</div>
					</div>
				</div>
				<div class="ydd-wm-dialog-button-area">
					<input type="button" id="return-with-save" value="保存并返回" />
					<input type="button" id="return-with-cancel" value="取消" />
				</div>
			</div>
		</div>
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
		
		<script type="text/javascript">
			$(function(){
				seajs.use(['waimai/js/waimai-main'], function(WaiMai){
					var main = new WaiMai();
					main.init({
						$drinks		: $('.ydd-wm-main-goods-wrapper span:not(.disabled)'),
						$additions	: $('.ydd-wm-additions-container>span:not(.disabled)'),
						$heats		: $('.ydd-wm-heats-container>span:not(.disabled)'),
						$sweetnesses: $('.ydd-wm-sweetness-container>span:not(.disabled)'),
						$takeaway	: $('.ydd-wm-main-addition-set>span[data-id]'),
						menuItemMap	: $.parseJSON('${itemMapJson}'),
						additionMap	: $.parseJSON('${additionMapJson}'),
						additionSize: $.parseJSON('${additionSizeJson}')
					});
					main.bindAll();
				});
			});
		</script>
	</body>
</html>