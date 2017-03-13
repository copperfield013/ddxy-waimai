package cn.sowell.copframe.exception;

public class PropertyUndefinedException extends RuntimeException {

	public PropertyUndefinedException(String name) {
		super("properties文件中没有定义键[" + name + "]");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2566134016651259673L;

}
