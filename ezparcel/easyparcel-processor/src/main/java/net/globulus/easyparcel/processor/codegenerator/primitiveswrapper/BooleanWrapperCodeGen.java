package net.globulus.easyparcel.processor.codegenerator.primitiveswrapper;

import javawriter.JavaWriter;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegenerator.CodeGenerator;
import net.globulus.easyparcel.processor.codegenerator.FieldCodeGen;

import java.io.IOException;

import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_TARGET;

/**
 * It also handles null values
 *
 *
 */
public class BooleanWrapperCodeGen implements FieldCodeGen {

  @Override public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException  {

    jw.emitStatement("%s.writeByte( (byte) (%s.%s != null ? 1 : 0) )", CodeGenerator.PARAM_PARCEL,
        CodeGenerator.PARAM_SOURCE, field.getFieldName());

    jw.beginControlFlow("if (%s.%s != null)", CodeGenerator.PARAM_SOURCE, field.getFieldName());
    jw.emitStatement("%s.writeByte( (byte) (%s.%s? 1 : 0))", CodeGenerator.PARAM_PARCEL,
        CodeGenerator.PARAM_SOURCE, field.getFieldName());
    jw.endControlFlow();
  }

  @Override public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException {

    jw.emitStatement("boolean %sNullHelper", field.getFieldName());
    jw.emitStatement("%sNullHelper = ( %s.readByte() == 1 )", field.getFieldName(),
        CodeGenerator.PARAM_PARCEL);

    jw.beginControlFlow("if (%sNullHelper)", field.getFieldName());
    jw.emitStatement("%s.%s = ( %s.readByte() == 1 )", CodeGenerator.PARAM_TARGET,
        field.getFieldName(), CodeGenerator.PARAM_PARCEL);
    jw.nextControlFlow("else");
    jw.emitStatement("%s.%s = null", PARAM_TARGET, field.getFieldName());
    jw.endControlFlow();
  }
}
