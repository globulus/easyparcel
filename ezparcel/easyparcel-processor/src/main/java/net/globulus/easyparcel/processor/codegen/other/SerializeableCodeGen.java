package net.globulus.easyparcel.processor.codegen.other;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegen.GenericCodeGen;

import java.io.IOException;

import javawriter.EzpJavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class SerializeableCodeGen extends GenericCodeGen {

  public SerializeableCodeGen() {
    super("Serializable");
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, EzpJavaWriter jw) throws IOException {

    jw.emitStatement("%s.%s = (%s) %s.readSerializable()", PARAM_TARGET,
        field.getFieldName(), field.getType(), PARAM_PARCEL);
  }
}
