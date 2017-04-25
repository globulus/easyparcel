package net.globulus.easyparcel.processor.codegen.primitiveswrapper;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegen.FieldCodeGen;

import java.io.IOException;

import javawriter.EzpJavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class BooleanWrapperCodeGen implements FieldCodeGen {

  @Override
  public void generateWriteToParcel(ParcelableField field, EzpJavaWriter jw) throws IOException  {
    jw.emitStatement("%s.writeByte((byte) (%s.%s != null ? 1 : 0))", PARAM_PARCEL,
        PARAM_SOURCE, field.getmFieldName());
    jw.beginControlFlow("if (%s.%s != null)", PARAM_SOURCE, field.getmFieldName());
    jw.emitStatement("%s.writeByte((byte) (%s.%s? 1 : 0))", PARAM_PARCEL,
        PARAM_SOURCE, field.getmFieldName());
    jw.endControlFlow();
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, EzpJavaWriter jw) throws IOException {
    jw.emitStatement("boolean %sNullCheck", field.getmFieldName());
    jw.emitStatement("%sNullCheck = (%s.readByte() == 1)", field.getmFieldName(),
        PARAM_PARCEL);
    jw.beginControlFlow("if (%sNullCheck)", field.getmFieldName());
    jw.emitStatement("%s.%s = (%s.readByte() == 1)", PARAM_TARGET,
        field.getmFieldName(), PARAM_PARCEL);
    jw.nextControlFlow("else");
    jw.emitStatement("%s.%s = null", PARAM_TARGET, field.getmFieldName());
    jw.endControlFlow();
  }
}
