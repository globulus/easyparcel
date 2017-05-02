package net.globulus.easyparcel.processor.codegen.collection;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegen.FieldCodeGen;

import java.io.IOException;

import javawriter.EzpJavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class StringListCodeGen implements FieldCodeGen {

  @Override
  public void generateWriteToParcel(ParcelableField field, EzpJavaWriter jw) throws IOException  {
    jw.emitStatement("%s.writeStringList(%s.%s)", PARAM_PARCEL, PARAM_SOURCE,
        field.getFieldName());
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, EzpJavaWriter jw) throws IOException {
    jw.emitStatement("%s.%s = %s.createStringArrayList()", PARAM_TARGET, field.getFieldName(), PARAM_PARCEL);
  }
}
