package net.globulus.easyparcel.processor.codegenerator.collection;

import com.squareup.javawriter.JavaWriter;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegenerator.CodeGenerator;
import net.globulus.easyparcel.processor.codegenerator.FieldCodeGen;

import java.io.IOException;

import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_TARGET;

/**
 * It also handles null values
 *
 *
 */
public class AbsListCodeGen implements FieldCodeGen {

  private String listType;

  public AbsListCodeGen(String listType) {
    this.listType = listType;
  }

  @Override public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException {

    jw.emitStatement("%s.writeByte( (byte) (%s.%s != null ? 1 : 0) )", CodeGenerator.PARAM_PARCEL,
        CodeGenerator.PARAM_SOURCE, field.getFieldName());

    jw.beginControlFlow("if (%s.%s != null)", CodeGenerator.PARAM_SOURCE, field.getFieldName());
    jw.emitStatement("%s.writeList(%s.%s)", PARAM_PARCEL, PARAM_SOURCE, field.getFieldName());
    jw.endControlFlow();
  }

  @Override public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException  {

    String typeArguments = field.getGenericsTypeArgument().toString();

    jw.emitStatement("boolean %sNullHelper", field.getFieldName());
    jw.emitStatement("%sNullHelper = ( %s.readByte() == 1 )", field.getFieldName(),
        CodeGenerator.PARAM_PARCEL);

    jw.beginControlFlow("if (%sNullHelper)", field.getFieldName());
    jw.emitStatement("%s<%s> %sListHelper = new %s<%s>()", listType, typeArguments,
        field.getFieldName(), listType, typeArguments);
    jw.emitStatement("%s.readList(%sListHelper, %s.class.getClassLoader())", PARAM_PARCEL,
        field.getFieldName(), typeArguments);
    jw.emitStatement("%s.%s = %sListHelper", PARAM_TARGET, field.getFieldName(),
        field.getFieldName());
    jw.nextControlFlow("else");
    jw.emitStatement("%s.%s = null", PARAM_TARGET, field.getFieldName());
    jw.endControlFlow();
  }
}
