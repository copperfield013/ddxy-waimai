package cn.sowell.copframe.dao.deferedQuery.sqlFunc;


import org.springframework.util.StringUtils;

import cn.sowell.copframe.dao.deferedQuery.DeferedParamQuery;
import cn.sowell.copframe.dao.deferedQuery.Function;
/**
 * 用于在构造结果条数的查询时的Function
 * @author Copperfield
 * @date 2016年11月11日 下午3:09:55
 * @see {@link DeferedParamQuery}
 */
public class WrapForCountFunction implements Function<String, String>{
	@Override
	public String apply(String statement) {
		String trimedStatement = StringUtils.trimWhitespace(statement);
		if(trimedStatement.toUpperCase().startsWith("FROM ")){
			return "select count(*) " + statement;
		}else{
			return "select count(*) from (" + statement + ") as for__count";
		}
	}
}
