package net.globulus.easyparcel.processor.codegenerator;

import javawriter.JavaWriter;

import net.globulus.easyparcel.processor.ParcelableField;

import java.io.IOException;

import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_TARGET;

/**
 *
 */
public class AbsCodeGen implements FieldCodeGen {

  protected String methodSuffix;

  public AbsCodeGen(String methodSuffix) {
    this.methodSuffix = methodSuffix;
  }

  @Override public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException {

    jw.emitStatement("%s.write%s(%s.%s)", PARAM_PARCEL, methodSuffix, PARAM_SOURCE,
        field.getFieldName());
  }

  @Override public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException  {

    jw.emitStatement("%s.%s = %s.read%s()", PARAM_TARGET, field.getFieldName(), PARAM_PARCEL, methodSuffix);
  }
}
