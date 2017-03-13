package cn.sowell.copframe.dao.deferedQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;
import org.springframework.util.Assert;

/**
 * 封装Hibernate的查询接口</br>
 * Hibernate的Query的一个缺陷是，在设置条件的时候，拼接语句和设置值必须是分开的</br>
 * 大部分情况下，拼接和设置值的条件是一致的。大抵步骤是
 * <ul>
 * 	<li>1.编写主要的sql部分，例如select、from和join部分，以及部分where</li>
 * 	<li>2.构造对象。然后根据criteria给语句添加其他筛选语句，并设置变量值</li>
 * 	<li>3.设置语句中非criteria条件的变量的值</li>
 * 	<li>4.装饰sql</li>
 * 	<li>5.根据语句创建Query对象</li>
 * 	<li>6.按前后顺序设置Query的变量值</li>
 * 	<li>7.设置Query的其他属性，例如分页、结果映射器等</li>
 * 	<li>8.执行查询，返回结果</li>
 * 	</ul>
 * DeferedParamQuery可以按照这些步骤并提供方法来实现方便灵活的查询</br/>
 * <h4>核心思想在于“<b>语句、参数、查询的解耦</b>”</h4>
 * <ul>
 * <li>语句的生成完全自由，不受约束，可以在对象外随意拼接、装饰、处理字符串，直到要生成查询对象时才构造好这个语句都可以。
 * 为了便于构造查询语句，当前类也提供了一系列方法，例如{@link #setSnippet(String, String)}方法可以设置语句中的片段</li>
 * <li>查询语句中的参数随时指定，不受时机约束。也就是说，拼接sql和设置拼接语句的参数值是可以同时进行的，而没必要在根据完整语句构造SQLQuery对象之后再设置参数值
 * 这样有利于条件语句的简化</li>
 * <li>查询结果的简便映射。jdbc本身就有提供按照别名来获得查询结果的数据。
 * 但是Hibernate在{@link ResultTransformer}接口中只提供了两个接口，分别代表查询结果的字段别名和查询数据值。</br>
 * 通过{@link ResultSetter}对象可以映射查询结果转换器
 * </li>
 * </ul> 
 * @author Copperfield Zhang
 * @date 2016年10月11日 下午4:58:37
 */
public class DeferedParamQuery{
	//构造时的查询语句
	private String queryString;
	
	//条件语句数组
	private List<String> conditions = new ArrayList<String>();
	//查询中的参数map
	private LinkedHashMap<String, QueryParameter> paramMap = new LinkedHashMap<String, QueryParameter>();
	//查询结果映射器
	@SuppressWarnings("rawtypes")
	private ResultSetter resultSetter;
	//语句片段map
	private LinkedHashMap<String, String> snippettMap = new LinkedHashMap<String, String>();
	
	/**
	 * 根据一个语句构造延迟查询对象，该语句可以是任何字符串，甚至是null。因为查询语句在生成查询对象之前都可以被替换
	 * @param queryString
	 */
	public DeferedParamQuery(String queryString) {
		this.queryString = queryString;
	}
	/**
	 * 添加条件语句
	 * @param conditionString
	 * @return
	 */
	public DeferedParamQuery appendCondition(String conditionString){
		this.conditions.add(conditionString);
		return this;
	}
	
	/**
	 * 设置查询的参数值,参数在语句中可以不存在，不存在时不会set这个参数
	 * @param key
	 * @param value
	 * @param type 参数的类型。当value为null时，type一定要指定
	 * @param isRestrict 是否强制设置参数，
	 * 如果为true，那么一定要参数在语句中一定要有声明，否则在{@link #createSQLQuery(Session, boolean, Function)}的时候会报错
	 * @return
	 */
	public DeferedParamQuery setParam(String key, Object value, Type type, boolean isRestrict){
		QueryParameter parameter = null;
		if(value instanceof QueryParameter){
			parameter = (QueryParameter) value;
		}else{
			parameter = new QueryParameter();
			parameter.setValue(value);
			parameter.setType(type);
			parameter.setRestrict(isRestrict);
		}
		parameter.setParameterName(key);
		this.paramMap.put(key, parameter);
		return this;
	}
	
	/**
	 * 设置查询的参数值
	 * 此时的参数的类型将按照value的类型来设置，用于一般的类型对象
	 * 参数在语句中一定要声明。如果不确定参数是否声明，可以调用{@link #setParam(String, Object, boolean)}方法
	 * @param key 参数名
	 * @param value 参数值
	 * @return
	 */
	public DeferedParamQuery setParam(String key, Object value){
		return this.setParam(key, value, true);
	}
	
	/**
	 * 设置查询的参数值，并且需要提供该参数在语句中是否强制声明。
	 * 此时的参数的类型将按照value的类型来设置，用于一般的类型对象。
	 * @param key 参数名
	 * @param value 参数值
	 * @param isRestrict 参数在语句中是否一定会声明。为true时，如果没有声明该参数，那么{@link #createSQLQuery(Session, boolean, Function)}时会报错 
	 * @return
	 */
	public DeferedParamQuery setParam(String key, Object value, boolean isRestrict){
		return this.setParam(key, value, null, isRestrict);
	}
	/**
	 * 设置查询的参数值，参数在语句中要求一定要存在
	 * @param key
	 * @param value
	 * @param type
	 * @return
	 */
	public DeferedParamQuery setParam(String key, Object value, Type type){
		return this.setParam(key, value, type, true);
	}
	
	/**
	 * 清空所有参数
	 * @return
	 */
	public DeferedParamQuery clearParam(){
		this.paramMap.clear();
		return this;
	}
	
	/**
	 * 重置查询语句，对其他参数无影响，例如condition和param都不变
	 * @param queryString
	 * @return
	 */
	public DeferedParamQuery resetQueryString(String queryString){
		this.queryString = queryString;
		return this;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends Query> T createQuery(Class<T> queryClass, Session session, boolean addWhere, Function<String, String> resetSQLFunc) {
		String sql = this.queryString;
		//添加条件语句
		if(this.conditions != null && this.conditions.size() > 0){
			if(addWhere){
				sql += " where 1=1 ";
			}
			for (String condition : conditions) {
				sql += " " + condition + " ";
			}
		}
		//替换语句中的参数
		for (Entry<String, String> snippetEntry : this.snippettMap.entrySet()) {
			String regex = "@" + snippetEntry.getKey();
			String replacement = snippetEntry.getValue();
			if(replacement == null){
				replacement = "";
			}
			if(sql.contains(regex)){
				sql = sql.replaceAll(regex, replacement);
			}
		}
		//生成的最终语句，可以调用方法进行装饰、重置语句
		if(resetSQLFunc != null){
			String funcResult = resetSQLFunc.apply(sql);
			if(funcResult != null){
				sql = funcResult;
			}else{
				return null;
			}
		}
		Query query = null;
		//创建查询对象
		if(SQLQuery.class.equals(queryClass)){
			query = session.createSQLQuery(sql);
		}else if(Query.class.equals(queryClass)){
			query = session.createQuery(sql);
		}else{
			throw new RuntimeException("不支持的类");
		}
		//设置查询语句的参数
		String[] parameterNames = query.getNamedParameters();
		//获得所有参数名
		Set<String> parameterNameSet = new HashSet<String>();
		for (String parameterName : parameterNames) {
			parameterNameSet.add(parameterName);
		}
		//遍历所有参数
		for (Entry<String, QueryParameter> entry : this.paramMap.entrySet()) {
			QueryParameter param = entry.getValue();
			if(param.isRestrict() || parameterNameSet.contains(param.getParameterName())){
				//如果有提供类型，那么按照类型来设置参数值
				//如果没有提供类型，那么按照hibernate的默认规则来按照参数类型来设置参数值
				Object value = param.getValue();
				if(param.getType() == null){
					if(value instanceof Collection){
						query.setParameterList(param.getParameterName(), (Collection) value);
					}else if(value != null && value.getClass().isArray()){
						query.setParameterList(param.getParameterName(), (Object[])value);
					}else{
						query.setParameter(param.getParameterName(), param.getValue());
					}
				}else{
					if(value instanceof Collection){
						query.setParameterList(param.getParameterName(), (Collection) value, param.getType());
					}else if(value != null && value.getClass().isArray()){
						query.setParameterList(param.getParameterName(), (Object[])value, param.getType());
					}else{
						query.setParameter(param.getParameterName(), value, param.getType());
					}
				}
			}
		}
		return (T) query;
	}
	
	/**
	 * 调用{@link Session#createSQLQuery(String)}创建SQL查询对象
	 * @param session 创建的Session
	 * @param addWhere 添加condition时是否需要添加where
	 * @param resetSQLFunc 设置在提交前重新生成SQL的方法对象，
	 * 这里的参数是一个有一个参数的函数接口，函数的参数是当前的查询语句， 函数返回的字符串是用于构造Query的查询语句。
	 * 如果函数的返回值是null，那么不执行查询，创建一个null对象
	 * @return
	 */
	public SQLQuery createSQLQuery(Session session, boolean addWhere, Function<String, String> resetSQLFunc){
		return this.createQuery(SQLQuery.class, session, addWhere, resetSQLFunc);
	}
	
	/**
	 * 调用{@link Session#createQuery(String)}创建hql查询对象
	 * @param session 创建的Session
	 * @param addWhere 添加condition时是否需要添加where
	 * @param resetSQLFunc 设置在提交前重新生成HQL的方法对象，
	 * 这里的参数是一个有一个参数的函数接口，函数的参数是当前的查询语句， 函数返回的字符串是用于构造Query的查询语句。
	 * 如果函数的返回值是null，那么不执行查询，创建一个null对象
	 * @return
	 */
	public Query createQuery(Session session, boolean addWhere, Function<String, String> resetSQLFunc){
		return this.createQuery(Query.class, session, addWhere, resetSQLFunc);
	}
	
	/**
	 * 设置查询结果映射</br>
	 * <b>*注意这个方法只是用于存放这个映射器，生成的SQLQuery对象如果要使用这个映射器，那么需要手动调用
	 * {@link SQLQuery#setResultTransformer(ResultTransformer)}方法指定这个映射器</b>
	 * @param resultSetter
	 */
	public <T> void setResultSetter(ResultSetter<T> resultSetter) {
		this.resultSetter = resultSetter;
	}
	
	/**
	 * 将set进来的{@link ResultSetter}对象转换成{@link ResultTransformer}对象
	 * @return
	 */
	public ResultTransformer getResultTransformer(){
		if(this.resultSetter != null){
			return new ResultTransformer() {
				
				private static final long serialVersionUID = -1880685803872109376L;

				@Override
				public Object transformTuple(Object[] tuple, String[] aliases) {
					return resultSetter.transform(tuple, aliases);
				}
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
				public List transformList(List collection) {
					return new ArrayList(collection);
				}
			};
		}
		return null;
	}
	
	/**
	 * 获得构造时的查询语句
	 * @return
	 */
	public String getQueryString() {
		return queryString;
	}
	
	/**
	 * 设置语句中的片段
	 * @param snippetName
	 * @param snippet
	 * @return
	 */
	public DeferedParamQuery setSnippet(String snippetName, String snippet){
		Assert.hasText(snippetName);
		this.snippettMap.put(snippetName, snippet);
		return this;
	}
	
}
