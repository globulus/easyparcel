package net.globulus.easyparcel.annotation;

/**
 * Created by gordanglavas on 29/09/16.
 */

public @interface Add {

	Class<?> value() default Object.class;
}
