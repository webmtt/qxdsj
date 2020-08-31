package cma.cimiss2.dpc.decoder.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 同一个属性允许映射多个数据表字段
 * @author shevawen
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface Columns {
	Column[] value();
}
