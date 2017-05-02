package net.globulus.easyparcel.processor.codegen.primitives;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegen.FieldCodeGen;

import java.io.IOException;

import javawriter.EzpJavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class BooleanCodeGen implements FieldCodeGen {

  @Override
  public void generateWriteToParcel(ParcelableField field, EzpJavaWriter jw) throws IOException {
    jw.emitStatement("%s.writeByte((byte) (%s.%s ? 1 : 0))", PARAM_PARCEL,
        PARAM_SOURCE, field.getFieldName());
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, EzpJavaWriter jw) throws IOException  {
    jw.emitStatement("%s.%s = (%s.readByte() == 1)", PARAM_TARGET,
        field.getFieldName(), PARAM_PARCEL);
  }
}
