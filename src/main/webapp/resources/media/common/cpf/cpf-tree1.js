define(function(require, exports, module){
	var $CPF = require('$CPF');
	var utils = require('utils');
	$CPF.addDefaultParam({
		//构建树的class
		treeClass	: 'cpf-tree',
		treeDataKey	: 'CPF-TREE-DATA',
		treeNodeDataKey	: 'CPF-TREE-NODE-DATA',
		treeDefaultNodeText	: '节点',
		treeNodeFolderOpendIconClass	: 'fa-folder-open',
		treeNodeFolderClosedIconClass	: 'fa-folder',
		treeNodeItemIconClass	: 'fa-inbox',
		//树节点id的前缀
		treeNodeIdPrefix	: 'cpf-tree-node-',
		treeEventNamePrefix	: 'treeEvent-',
		//树节点都有的class
		treeNodeClass		: 'cpf-tree-node',
		//节点的id生成函数
		treeNodeIdGenerator	: function(node){
			return utils.uuid(5, 62);
		}
	});
	
	var
		CLASS_FOLDER = 'tree-folder',
		CLASS_ITEM = 'tree-item',
		CLASS_FOLDER_HEADER = 'tree-folder-header',
		CLASS_FOLDER_ICON = 'tree-folder-icon',
		CLASS_FOLDER_CONTENT = 'tree-folder-content',
		CLASS_FOLDER_LOADER = 'tree-loader',
		CLASS_FOLDER_NAME = 'tree-folder-name',
		CLASS_NODE_TEXT = 'tree-node-name',
		CLASS_PREFIX_NODE_ACTION = 'tree-node-action-',
		CLASS_TREE_ACTIONS = 'tree-actions'
		;
	$.fn.extend({
		/**
		 * 获得节点所属的树的Tree对象
		 */
		getCpfTree	: function(){
			var $tree = $(this).closest('.' + $CPF.getParam('treeClass'));
			if($tree.length == 1){
				var tree = $tree.data($CPF.getParam('treeDataKey'));
				if(tree instanceof Tree){
					return tree;
				}
			}
		},
		getCpfTreeNode	: function(){
			var $treeNode = $(this).closest('.' + $CPF.getParam('treeNodeClass'));
			if($treeNode.length == 1){
				var node = $treeNode.data($CPF.getParam('treeNodeDataKey'));
				if(node instanceof Tree.TreeNode){
					return node;
				}
			}
		}
	});
	
	/**
	 * 树形结构对象
	 */
	function Tree(_param){
		var _this = this;
		var param = $.extend({
			/**
			 * 根据某个节点的数据判断其是否是一个folder节点
			 * 如果返回true或者false，代表判断结果；否则按照node的asFolder来判断
			 */
			asFolderChecker	: function(node){
				var level = 0;
				var cNode = node;
				while(cNode.parentNode != null){
					cNode = cNode.parentNode;
					level++;
				}
				if(level < param.treeMaxLevel){
					return true;
				}
				return false;
			},
			dealNode		: $.noop,
			treeMaxLevel	: 3
		}, _param);
		var $dom = $('<div class="tree">');
		insertChildren(param.nodes, $dom, param);
		$dom.data($CPF.getParam('treeDataKey'), this);
		this.$dom = $dom;
		
		
		/**
		 * 先序遍历所有节点
		 * @param func 函数对象。函数有一个参数，表示当前遍历到的节点，this是Tree对象
		 */
		this.iterateNode = function(func){
			if(typeof func === 'function'){
				$dom.children('.' + $CPF.getParam('treeNodeClass')).each(function(){
					var treeNode = $(this).data($CPF.getParam('treeNodeDataKey'));
					if(treeNode instanceof TreeNode){
						utils.iterateTree(treeNode, function(node){
							return node.getChildren();
						}, function(node){
							return func.apply(_this, [node]);
						});
					}
				});
			}
			return this;
		};
		/**
		 * 触发树形结构的事件
		 * @param $node jQuery对象，触发事件的节点
		 * @param action 触发的事件类型
		 * @param event jQuery封装的事件对象
		 */
		this.trigger = function(treeNode, action, event){
			treeNode.trigger(action, event);
			return this;
		};
		/**
		 * 直接绑定树对象下所有节点的action的回调
		 * @param action 绑定的节点的action的名称
		 * @param callback action的回调函数。函数有两个参数，
		 * 			第一个参数是event，是jQuery的事件对象；
		 * 			第二个参数是$action，是触发事件的jqueryDom对象
		 */
		this.bindAction = function(action, callback){
			if(typeof action === 'string' && typeof callback === 'function'){
				this.iterateNode(function(treeNode){
					treeNode.bind('action-' + action, callback);
				});
			}
			return this;
		};
		
		
		/**
		 * 递归插入树的节点数据对象数组到dom下面作为子节点
		 */
		function insertChildren(nodes, dom, parentNode){
			for(var i in nodes){
				var node = nodes[i];
				node.parentNode = parentNode;
				var treeNode = new TreeNode(node);
				appendChildNode(dom, treeNode.getDom());
				var children = getNodeChildren(node);
				if($.isArray(children) && children.length > 0){
					insertChildren(children, treeNode.getDom(), node);
				}
			}
		}
		
		/**
		 * 将子节点的dom对象放到父节点dom下面
		 */
		function appendChildNode(parentDom, childDom){
			if(parentDom.is('.tree-folder')){
				parentDom
					.children('.tree-folder-content')
					.append(childDom);
			}else{
				parentDom.append(childDom);
			}
		}
		
		/**
		 * 根据父节点获得子节点数组，如果不存在子节点
		 */
		function getNodeChildren(parentNode){
			return parentNode && parentNode.nodes;
		}
		
		/**
		 * 树形节点对象
		 */
		function TreeNode(node){
			var id = node.id || $CPF.getParam('treeNodeIdGenerator')(node);
			var _this = this;
			//将node中包含的data数据保存下来
			var data = $.extend({}, node.data);
			//构建节点
			var $node = buildTreeNodeDom(node, id);
			//绑定当前对象到生成的dom对象中
			$node.data($CPF.getParam('treeNodeDataKey'), this);
			this.getDom = function(){
				return $node;
			};
			/**
			 * 所在的树对象
			 * @return Tree对象
			 */
			this.getTree = function(){
				return $node.getCpfTree();
			};
			/**
			 * 获得父节点
			 * @return TreeNode对象
			 */
			this.getParent = function(){
				if($node.parent('.' + CLASS_FOLDER_CONTENT).length != 0){
					var $parent = $node.closest('.' + CLASS_FOLDER),
						treeNode = $parent.data($CPF.getParam('treeNodeDataKey'));
					if(treeNode instanceof TreeNode){
						return treeNode;
					}
				}
			};
			/**
			 * 孩子节点
			 * @return TreeNode数组对象
			 */
			this.getChildren = function(){
				var children = [];
				var $content = $node.find('.' + CLASS_FOLDER_CONTENT);
				if($content.length > 0){
					var $children = $content.eq(0).children();
					$children.each(function(){
						var treeNode = $(this).data($CPF.getParam('treeNodeDataKey'));
						if(treeNode instanceof TreeNode){
							children.push(treeNode);
						}
					});
				}
			};
			/**当前节点是否是叶子节点
			 * @return boolean 当前节点是否是叶子节点
			 */
			this.isLeaf = function(){
				return $node.is('.' + CLASS_ITEM);
			};
			this.getText = function(){
				var $header = $node.children('.' + CLASS_FOLDER_HEADER);
				if($header.length > 0){
					return $header.find('.' + CLASS_NODE_TEXT).text();
				}else{
					return $node.find('.' + CLASS_NODE_TEXT).text();
				}
			};
			/**
			 * 获得节点的唯一id
			 */
			this.getId = function(){
				return id;
			};
			/**
			 * 折叠子节点，仅在当前节点不是叶子节点时有效
			 */
			this.fold = function(){
				return this.toggleFold(false);
			};
			/**
			 * 展开子节点，仅在当前节点不是叶子节点时有效
			 * @param showParent 是否展开父节点。只有在传入false的时候不展开
			 */
			this.unfold = function(showParent){
				if(showParent !== false){
					var pNode = this.getParent();
					if(pNode != null){
						pNode.unfold();
					}
				}
				return this.toggleFold(true);
			};
			/**
			 * 展开/折叠子节点，仅在当前节点不是叶子节点时有效
			 * @param toShow 是否显示子节点
			 */
			this.toggleFold = function(toShow){
				if(this.isLeaf()){
					var $content = $node.children('.' + CLASS_FOLDER_CONTENT),
					$header = $node.children('.' + CLASS_FOLDER_HEADER),
					$folderIcon = $header.children(CLASS_FOLDER_ICON);
					if($content.length == 1 && $header.length == 1){
						if(typeof toShow !== 'boolean'){
							toShow = !$content.is(':visible');
						}
						$content.toggle(toShow);
						$folderIcon.toggleClass($CPF.getParam('treeNodeFolderClosedIconClass'), !toShow);
						$folderIcon.toggleClass($CPF.getParam('treeNodeFolderOpendIconClass'), toShow);
					}
				}
				return this;
			};
			/**
			 * 触发节点事件
			 * @param action 触发事件的名称
			 */
			this.trigger = function(action, $action){
				var treeNode = $action && $action.getCpfTreeNode();
				$node.trigger($CPF.getParam('treeEventNamePrefix') + action, [$action, treeNode]);
				return this;
			};
			/**
			 * 绑定节点事件
			 * @param action 事件名称
			 * @param callback 事件回调。由两个参数。
			 * 			第一个参数时jQuery的event事件对象；第二个参数时触发事件的dom对象。
			 * 			注意event.target肯定是返回节点的dom对象（因为调用的是$node.trigger）
			 */
			this.bind = function(action, callback){
				$node.on($CPF.getParam('treeEventNamePrefix') + action, function(event, $action, treeNode){
					callback.apply(_this, [event, $action, treeNode]);
				});
				return this;
			};
			/**
			 * 设置节点的数据，可以覆盖初始化节点时的data字段
			 * @param key 
			 * @param value 为空时返回key对应的对象，不为空时设置key对象的value
			 * @return 只有key参数，并且不存在数据时，返回undefined。设置的key对应的value成功，返回自身
			 */
			this.data = function(key, value){
				if(typeof key === 'string'){
					if(value != undefined){
						data[key] = value;
						return this;
					}else{
						return data[key];
					}
				}else{
					$.error('key must be string');
				}
			}
			
			/************************私有方法区*************************/
			
			/**
			 * 将action数组放到node数据对象中，在构造dom之前，对node数据对象进行统一处理
			 */
			function appendActions(node, actions, selector){
				selector = typeof selector === 'function'? selector: $.noop;
				utils.iterateTree(node, function(pnode){return pnode.nodes}, function(cnode){
					if(selector(cnode) != false){
						if(!cnode.actions){
							cnode.actions = [];
						}
						for(var i in actions){
							cnode.actions.push(actions[i]);
						}
					}
				});
			}

			/**
			 * 根据node的数据构建对应的dom对象
			 */
			function buildTreeNodeDom(node, id){
				var children = getNodeChildren(node);
				var hasChildren = children && children.length > 0;
				var $node = $('<div>').addClass($CPF.getParam('treeNodeClass'));
				if(id){
					$node.attr('id', $CPF.getParam('treeNodeIdPrefix') + id);
				}
				var nodeText = node.text || $CPF.getParam('treeDefaultNodeText');
				var asFolderChecker = param.asFolderChecker;
				if(typeof asFolderChecker === 'function'){
					var checkResult = asFolderChecker(node, id);
					if(typeof checkResult === 'boolean'){
						node.asFolder = checkResult;
					}
				}
				if(typeof param.dealNode === 'function'){
					param.dealNode(node);
				}
				if(hasChildren || node.asFolder){
					//有子节点
					$node.addClass('tree-folder');
					var $header = $('<div>').addClass(CLASS_FOLDER_HEADER),
						$content = $('<div>').addClass(CLASS_FOLDER_CONTENT).hide(),
						$loader = $('<div>').addClass(CLASS_FOLDER_LOADER)
						;
					
					var $folderName = $('<div>').addClass(CLASS_FOLDER_NAME);
					$folderName.append('<span class="' + CLASS_NODE_TEXT + '">' + nodeText + '</span>');
					var $folderIcon = $('<i class="fa ' + CLASS_FOLDER_ICON + ' ' + $CPF.getParam('treeNodeFolderClosedIconClass') + '">');
					$header.append($folderIcon)
							.append($folderName)
							;
					var $actions = buildActionsDom(node.actions);
					if($actions){
						$folderName.append($actions);
					}
					
					$loader
						.hide()
						.append('<div class="tree-loading"><i class="fa fa-rotate-right fa-spin"></i></div>')
					;
					$node
						.append($header)
						.append($content)
						.append($loader);
					
					/**
					 * 父节点点击事件
					 */
					$header.click(function(e){
						if($(e.target).closest('.' + CLASS_TREE_ACTIONS).length > 0){
							return;
						}
						var toShow = !$content.is(':visible');
						$content.toggle(toShow);
						$folderIcon.toggleClass($CPF.getParam('treeNodeFolderClosedIconClass'), !toShow);
						$folderIcon.toggleClass($CPF.getParam('treeNodeFolderOpendIconClass'), toShow);
						return false;
					});
				}else{
					//叶子节点
					$node.addClass('tree-item');
					var $itemName = $('<div class="tree-item-name">');
					$itemName
						.append('<i class="fa ' + $CPF.getParam('treeNodeItemIconClass') + '"></i>')
						.append('<span class="' + CLASS_NODE_TEXT + '">' + nodeText + '</span>');
					var $actions = buildActionsDom(node.actions);
					if($actions){
						$itemName.append($actions);
					}
					
					$node.append($itemName);
				}
				return $node;
			}
			
			
			/**
			 * 构造actions的jQuery对象
			 * @param actions 对象数组，每个对象至少包括两个字段，分别是name和styleClass
			 * 					其中name可以用来标识action，styleClass指定一个或者多个action的样式
			 */
			function buildActionsDom(actions){
				if(actions && actions.length > 0){
					var $actions = $('<div>').addClass(CLASS_TREE_ACTIONS);
					for(var i in actions){
						var action = actions[i];
						if(action){
							var actionName = action.name,
								actionClass = action.styleClass || '',
								$action = $('<i class="fa">').addClass(actionClass);
							if(actionName){
								$action.addClass(CLASS_PREFIX_NODE_ACTION + actionName);
								$action.attr('title', action.title);
								bindActionEvent($action, actionName);
								$actions.append($action);
							}
						}
					}
					return $actions;
				}
			}
			
			
			/**
			 * 节点action的事件绑定
			 */
			function bindActionEvent($action, actionName){
				$action.click(function(e){
					var treeNode = $action.getCpfTreeNode();
					if(treeNode){
						treeNode.trigger('action-' + actionName, $action);
					}
				});
			}
			
			
		}
		
		Tree.TreeNode = TreeNode;
	}
	
	
	
	
	module.exports = {
		Tree	: Tree	
	};
	
});