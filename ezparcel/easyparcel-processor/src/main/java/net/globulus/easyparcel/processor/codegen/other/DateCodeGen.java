package net.globulus.easyparcel.processor.codegen.other;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegen.FieldCodeGen;

import java.io.IOException;

import javawriter.JavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class DateCodeGen implements FieldCodeGen {

  @Override
  public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException {
    jw.emitStatement("%s.writeByte( (byte) (%s.%s != null ? 1 : 0) )", PARAM_PARCEL,
        PARAM_SOURCE, field.getmFieldName());
    jw.beginControlFlow("if (%s.%s != null)", PARAM_SOURCE, field.getmFieldName());
    jw.emitStatement("%s.writeLong(%s.%s.getTime())", PARAM_PARCEL, PARAM_SOURCE,
        field.getmFieldName());
    jw.endControlFlow();
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException  {

    jw.emitStatement("boolean %sNullCheck", field.getmFieldName());
    jw.emitStatement("%sNullCheck = (%s.readByte() == 1)", field.getmFieldName(),
        PARAM_PARCEL);
    jw.beginControlFlow("if (%sNullCheck)", field.getmFieldName());
    jw.emitStatement("long %sLongHelper = %s.readLong()", field.getmFieldName(), PARAM_PARCEL);
    jw.emitStatement("%s.%s = new java.util.Date(%sLongHelper)", PARAM_TARGET, field.getmFieldName(),
        field.getmFieldName());
    jw.nextControlFlow("else");
    jw.emitStatement("%s.%s = null", PARAM_TARGET, field.getmFieldName());
    jw.endControlFlow();
  }
}
