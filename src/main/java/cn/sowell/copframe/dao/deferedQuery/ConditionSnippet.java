package cn.sowell.copframe.dao.deferedQuery;

public class ConditionSnippet extends DeferedParamSnippet{

	ConditionSnippet(String snippetName) {
		super(snippetName);
	}
	
	@Override
	public String getSnippet() {
		String origin = super.getSnippet();
		String trimed = origin.trim();
		String result = origin;
		if(trimed.startsWith("and")){
			result = origin.replaceFirst("and", "");
		}
		if(trimed.startsWith("or")){
			result = origin.replace("or", "");
		}
		return result;
	}

}
