package net.globulus.easyparcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.lang.model.element.Modifier;

/**
 * Created by gordanglavas on 29/09/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface EasyParcel {
	boolean autoInclude() default true;
	Modifier[] ignoreModifiers() default { };
//	boolean ignoreSuperclass() default false;
}
