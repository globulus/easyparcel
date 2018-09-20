package net.globulus.easyparcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gordanglavas on 29/09/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface EasyParcel {
	boolean autoInclude() default true;
	int[] ignoreModifiers() default { };
//	boolean ignoreSuperclass() default false;
	boolean bottom() default false;
}
