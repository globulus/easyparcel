package net.globulus.easyparcel.processor.codegen.android;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegen.FieldCodeGen;

import java.io.IOException;

import javawriter.JavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_FLAGS;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class ParcelableCodeGen implements FieldCodeGen {

  @Override
  public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException {
    jw.emitStatement("%s.writeParcelable(%s.%s, %s)", PARAM_PARCEL, PARAM_SOURCE,
        field.getmFieldName(), PARAM_FLAGS);
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException {
    jw.emitStatement("%s.%s = %s.readParcelable(%s.class.getClassLoader())",
            PARAM_TARGET, field.getmFieldName(), PARAM_PARCEL, field.getType());
  }
}
