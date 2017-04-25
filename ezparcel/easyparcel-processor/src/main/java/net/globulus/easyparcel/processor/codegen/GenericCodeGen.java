package net.globulus.easyparcel.processor.codegen;

import net.globulus.easyparcel.processor.ParcelableField;

import java.io.IOException;

import javawriter.EzpJavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class GenericCodeGen implements FieldCodeGen {

  protected String mMethodSuffix;

  public GenericCodeGen(String methodSuffix) {
    mMethodSuffix = methodSuffix;
  }

  @Override
  public void generateWriteToParcel(ParcelableField field, EzpJavaWriter jw) throws IOException {
    jw.emitStatement("%s.write%s(%s.%s)", PARAM_PARCEL, mMethodSuffix, PARAM_SOURCE,
        field.getmFieldName());
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, EzpJavaWriter jw) throws IOException  {
    jw.emitStatement("%s.%s = %s.read%s()", PARAM_TARGET, field.getmFieldName(), PARAM_PARCEL, mMethodSuffix);
  }
}
