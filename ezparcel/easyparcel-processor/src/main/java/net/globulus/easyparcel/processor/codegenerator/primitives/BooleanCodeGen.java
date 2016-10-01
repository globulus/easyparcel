package net.globulus.easyparcel.processor.codegenerator.primitives;

import javawriter.JavaWriter;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegenerator.CodeGenerator;
import net.globulus.easyparcel.processor.codegenerator.FieldCodeGen;

import java.io.IOException;

/**
 * For boolean primitives
 *
 *
 */
public class BooleanCodeGen implements FieldCodeGen {

  @Override public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException {

    jw.emitStatement("%s.writeByte( (byte) (%s.%s? 1 : 0))", CodeGenerator.PARAM_PARCEL,
        CodeGenerator.PARAM_SOURCE, field.getFieldName());
  }

  @Override public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException  {

    jw.emitStatement("%s.%s = ( %s.readByte() == 1 )", CodeGenerator.PARAM_TARGET,
        field.getFieldName(), CodeGenerator.PARAM_PARCEL);
  }
}
