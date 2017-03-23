define(function(require, exports){
	$.extend(exports, {
		log		: function(){
			doInConsole('log', arguments);
		}
	});
	
	function doInConsole(method, args){
		if(console && typeof console[method] === 'function'){
			console[method].apply(console, args);
		}
	}
	
});