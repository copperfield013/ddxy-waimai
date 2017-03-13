package cn.sowell.copframe.dao.deferedQuery;

public interface Function<T, R> {
	R apply(T parameter);
}
