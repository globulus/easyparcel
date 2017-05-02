package net.globulus.easyparcel.processor.codegen.collection;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegen.FieldCodeGen;

import java.io.IOException;

import javawriter.EzpJavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_FLAGS;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class ParcelableArrayCodeGen implements FieldCodeGen {

  @Override public void generateWriteToParcel(ParcelableField field, EzpJavaWriter jw) throws IOException  {

    jw.emitStatement("%s.writeTypedArray(%s.%s, %s)", PARAM_PARCEL, PARAM_SOURCE,
        field.getFieldName(), PARAM_FLAGS);
  }

  @Override public void generateReadFromParcel(ParcelableField field, EzpJavaWriter jw) throws IOException {

    String type = field.getGenericsTypeArgument().toString();

    jw.emitStatement(
        "%s[] %sNullCheck = %s.createTypedArray(Parcelables.getCreator(%s.class))",
          type, field.getFieldName(), PARAM_PARCEL, type);
    jw.beginControlFlow("if (%sNullCheck != null)", field.getFieldName());
    jw.emitStatement(
        "%s.%s = java.util.Arrays.copyOf(%sNullCheck, %sNullCheck.length, %s[].class)",
        PARAM_TARGET, field.getFieldName(), field.getFieldName(), field.getFieldName(), type);
    jw.nextControlFlow("else");
    jw.emitStatement("%s.%s = null", PARAM_TARGET, field.getFieldName());
    jw.endControlFlow();
  }
}
