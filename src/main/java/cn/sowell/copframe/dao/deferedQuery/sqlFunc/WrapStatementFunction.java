package cn.sowell.copframe.dao.deferedQuery.sqlFunc;

import cn.sowell.copframe.dao.deferedQuery.Function;

/**
 * 传入语句前和语句后要填充的语句，在调用函数时组装后返回
 * @author Copperfield
 * @date 2016年11月16日 下午3:31:52
 */
public class WrapStatementFunction implements Function<String, String>{

	private String prepend;
	private String append;
	public WrapStatementFunction(String prepend, String append) {
		this.prepend = prepend;
		this.append = append;
	}
	
	@Override
	public String apply(String sql) {
		String prependStr = this.prepend == null? "" : this.prepend,
				appendStr = this.append == null? "" : this.append;
		return prependStr + sql + appendStr;
	}
}
