<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<base href="${basePath }" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">

<!-- jQuery -->
<script src="${basePath }media/jquery-1.11.3.js"></script>
<!-- 局部打印插件 -->
<script src="${basePath }media/admin/plugins/printArea/jquery.PrintArea.js"></script>
<!-- SeaJS -->
<script src="${basePath }media/sea-debug.js"></script>
<script src="${basePath}media/admin/plugins/layer-3.0.3/layer.js"></script>
<!-- 页面加载时把各个JS模块加载到容器当中 -->
<script type="text/javascript">
	$(function(){
		seajs.config({
			base	: '${basePath}media/admin/',
			paths	: {
				COMMON	: '${basePath}media/common/',
				MAIN	: '${basePath}media/admin/main/js/'
			},
		  	alias	: {
		    	'$CPF'		: 'COMMON/cpf/cpf-core.js',
				'utils'		: 'COMMON/cpf/cpf-utils.js',
				'console'	: 'COMMON/cpf/cpf-console.js',
				'print'		: 'COMMON/cpf/cpf-print.js'
				//..其他模块
		  	}
		});
		seajs.use('MAIN/admin-main.js');
	});
	
</script>