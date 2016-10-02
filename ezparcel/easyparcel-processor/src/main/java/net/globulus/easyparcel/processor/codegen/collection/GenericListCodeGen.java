package net.globulus.easyparcel.processor.codegen.collection;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegen.FieldCodeGen;

import java.io.IOException;

import javawriter.JavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class GenericListCodeGen implements FieldCodeGen {

  private String mListType;

  public GenericListCodeGen(String listType) {
    mListType = listType;
  }

  @Override
  public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException {
    jw.emitStatement("%s.writeByte( (byte) (%s.%s != null ? 1 : 0) )", PARAM_PARCEL,
        PARAM_SOURCE, field.getmFieldName());
    jw.beginControlFlow("if (%s.%s != null)", PARAM_SOURCE, field.getmFieldName());
    jw.emitStatement("%s.writeList(%s.%s)", PARAM_PARCEL, PARAM_SOURCE, field.getmFieldName());
    jw.endControlFlow();
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException {
    String typeArguments = field.getGenericsTypeArgument().toString();
    jw.emitStatement("boolean %sNullCheck", field.getmFieldName());
    jw.emitStatement("%sNullCheck = (%s.readByte() == 1)", field.getmFieldName(), PARAM_PARCEL);
    jw.beginControlFlow("if (%sNullCheck)", field.getmFieldName());
    jw.emitStatement("%s<%s> %sListHelper = new %s<%s>()", mListType, typeArguments,
        field.getmFieldName(), mListType, typeArguments);
    jw.emitStatement("%s.readList(%sListHelper, %s.class.getClassLoader())", PARAM_PARCEL,
        field.getmFieldName(), typeArguments);
    jw.emitStatement("%s.%s = %sListHelper", PARAM_TARGET, field.getmFieldName(),
        field.getmFieldName());
    jw.nextControlFlow("else");
    jw.emitStatement("%s.%s = null", PARAM_TARGET, field.getmFieldName());
    jw.endControlFlow();
  }
}
