package cn.sowell.copframe.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface XMLTag {
	String tagName() default "";
	boolean required() default false;
	boolean ignored() default false;
	int lengthLimit() default 0;
}
