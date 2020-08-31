package cma.cimiss2.dpc.decoder.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
@Repeatable(value = Columns.class)
public @interface Column {

	String value();
	
	String pattern() default "";

}
