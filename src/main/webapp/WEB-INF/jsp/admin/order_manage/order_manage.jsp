<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<base href="${basePath }" />
		<title>订单管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
		
		
		<!--Basic Styles-->
	    <link href="media/admin/plugins/beyond/css/bootstrap.min.css" rel="stylesheet" />
	    <link href="media/admin/plugins/beyond/css/font-awesome.min.css" rel="stylesheet" />
	
	    <!--Fonts-->
	    <!-- <link href="http://fonts.useso.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,400,600,700,300" rel="stylesheet" type="text/css"> -->
	
	    <!--Beyond styles-->
	    <link id="beyond-link" href="media/admin/plugins/beyond/css/beyond.min.css" rel="stylesheet" type="text/css" />
	    <!--  -->
	    <link href="media/admin/cpf/css/cpf-main.css" rel="stylesheet" type="text/css" />
	    <style type="text/css">
	    	.navbar .navbar-inner{
	    		background-color: #044d22;
	    	}
	    </style>
	    <!--Basic Scripts-->
	    <script src="media/jquery-1.11.3.js"></script>
	    <script src="media/admin/plugins/beyond/js/jquery-ui-1.10.4.custom.js"></script>
	    <!--Skin Script: Place this script in head to load scripts for skins and rtl support-->
	    <script src="media/admin/plugins/beyond/js/skins.min.js"></script>
	    <script src="media/admin/plugins/beyond/js/datetime/bootstrap-datepicker.js"></script>
	    <script src="media/admin/plugins/beyond/js/datetime/bootstrap-timepicker.js"></script>
	    <script src="media/admin/plugins/beyond/js/datetime/moment.js"></script>
	    <script src="media/admin/plugins/beyond/js/datetime/daterangepicker.js"></script>
	    <script type="text/javascript" src="media/admin/plugins/bootstrapt-treeview/dist/bootstrap-treeview.min.js"></script>
	</head>
	<body>
		<div class="navbar">
	        <div class="navbar-inner">
	            <div class="navbar-container">
	                <div class="navbar-header pull-left">
	                    <a href="#" class="navbar-brand">
	                        <small>
	                            <img src="media/admin/order-manage/ydd-logo.png" alt="" />
	                        </small>
	                    </a>
	                </div>
	                <div class="sidebar-collapse" id="sidebar-collapse">
	                    <i class="collapse-icon fa fa-bars"></i>
	                </div>
				</div>
			</div>
		</div>
	    <div class="main-container container-fluid">
	        <div class="page-container">
	            <div class="page-sidebar" id="sidebar">
	                <ul class="nav sidebar-menu">
	                	<li class="open">
	                		<a href="#">
	                			<i class="menu-icon glyphicon glyphicon-home"></i>
	                			<span class="menu-text">主页面</span>
	                		</a>
	                	</li>
						 <li>
	                        <a href="#" class="menu-dropdown">
	                            <i class="menu-icon fa fa-desktop"></i>
	                            <span class="menu-text">订单管理</span>
	                            <i class="menu-expand"></i>
	                        </a>
							<ul class="submenu">
                               	<li>
                               		<a class="tab" href="admin/order-manage/order-list" target="order_list" title="订单查看">
                               			<span class="menu-text">订单查看</span>
                               		</a>
                               	</li>
                               	<li>
                               		<a class="tab" href="admin/order-manage/order-statistics" target="orer-statistics" title="订单统计">
                               			<span class="menu-text">订单统计</span>
                               		</a>
                               	</li>
							</ul>
	                    </li>
					</ul>
				</div>
				<div class="page-content">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="main-tab-title-container">
							<li class="active main-tab-title">
							    <a data-toggle="tab" href="#cpf-home-tab">
									主页
							    </a>
							</li>
						</ul>
						<div class="tab-content" id="main-tab-content-container">
							<div id="cpf-home-tab" class="tab-pane active main-tab-content">
								<jsp:include page="home.jsp"></jsp:include>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	    <script src="media/admin/plugins/beyond/js/bootstrap.js"></script>
	    <script src="media/admin/plugins/beyond/js/toastr/toastr.js"></script>
	    <script src="media/admin/plugins/beyond/js/beyond.min.js"></script>
	    <!-- SeaJS -->
	    <script src="${basePath }media/sea-debug.js"></script>
	    
	    <script type="text/javascript">
	    	$(function(){
	    		seajs.config({
				base	: '${basePath}media/admin/',
				paths	: {
					  COMMON	: '${basePath}media/common/',
					  MAIN		: '${basePath}media/admin/main/js/'
				},
				alias	: {
					'$CPF'	: 'COMMON/cpf/cpf-core.js',
					'utils'	: 'COMMON/cpf/cpf-utils.js',
					'page'	: 'COMMON/cpf/cpf-page.js',
					'dialog': 'COMMON/cpf/cpf-dialog.js',
					'paging': 'COMMON/cpf/cpf-paging.js',
					'tree'	: 'COMMON/cpf/cpf-tree.js',
					'form'	: 'COMMON/cpf/cpf-form.js',
					'tab' 	: 'COMMON/cpf/cpf-tab.js',
					'ajax'	: 'COMMON/cpf/cpf-ajax.js',
					'css'	: 'COMMON/cpf/cpf-css.js'
				}
	    		});
	    		seajs.use('COMMON/cpf/cpf-main.js');
	    	});
	    </script>
	</body>
</html>