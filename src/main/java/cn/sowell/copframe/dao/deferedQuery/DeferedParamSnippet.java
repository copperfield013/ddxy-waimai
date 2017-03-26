package cn.sowell.copframe.dao.deferedQuery;


public class DeferedParamSnippet {
	private String snippetName;
	private StringBuffer buffer = new StringBuffer();
	private String prependWhenNotEmpty;
	
	DeferedParamSnippet(String snippetName) {
		this.snippetName = snippetName;
	}

	DeferedParamSnippet(String snippetName, String snippet) {
		this.snippetName = snippetName;
		this.buffer.append(snippet);
	}
	
	public String getName(){
		return snippetName;
	}
	
	public String getSnippet(){
		return buffer.toString();
	}

	public boolean isEmpty(){
		return buffer.length() == 0;
	}
	
	/**
	 * 添加片段中的某一部分，并且该部分前后自动加上空格
	 * @param part
	 */
	public void append(String part) {
		appendWithoutPadding(" " + part + " ");
	}
	
	/**
	 * 添加片段中的某一部分
	 * @param part
	 * @return
	 */
	public DeferedParamSnippet appendWithoutPadding(String part){
		if(part != null){
			buffer.append(part);
		}
		return this;
	}

	public String getPrependWhenNotEmpty() {
		return prependWhenNotEmpty;
	}

	public void setPrependWhenNotEmpty(String prependWhenNotEmpty) {
		this.prependWhenNotEmpty = prependWhenNotEmpty;
	}
	
}
