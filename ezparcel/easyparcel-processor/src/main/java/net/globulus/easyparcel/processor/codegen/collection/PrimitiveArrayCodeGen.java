package net.globulus.easyparcel.processor.codegen.collection;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegen.GenericCodeGen;

import java.io.IOException;

import javawriter.JavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class PrimitiveArrayCodeGen extends GenericCodeGen {

  private String mType;

  public PrimitiveArrayCodeGen(String methodSuffix, String type) {
    super(methodSuffix);
    mType = type;
  }

  @Override
  public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException  {
    jw.emitStatement("%s.writeInt( (%s.%s != null ? %s.%s.length : -1) )",
        PARAM_PARCEL, PARAM_SOURCE, field.getmFieldName(),
        PARAM_SOURCE, field.getmFieldName());
    jw.beginControlFlow("if (%s.%s != null)", PARAM_SOURCE, field.getmFieldName());
    jw.emitStatement("%s.write%s(%s.%s)", PARAM_PARCEL, mMethodSuffix, PARAM_SOURCE,
        field.getmFieldName());
    jw.endControlFlow();
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException {
    jw.emitStatement("int %sLengthHelper = -1", field.getmFieldName());
    jw.emitStatement("%sLengthHelper = %s.readInt()", field.getmFieldName(),
        PARAM_PARCEL);
    jw.beginControlFlow("if (%sLengthHelper >= 0)", field.getmFieldName());
    jw.emitStatement("%s[] %sArrayHelper = new %s[%sLengthHelper]", mType, field.getmFieldName(),
			mType, field.getmFieldName());
    jw.emitStatement("%s.read%s(%sArrayHelper)", PARAM_PARCEL, mMethodSuffix, field.getmFieldName());
    jw.emitStatement("%s.%s = %sArrayHelper", PARAM_TARGET, field.getmFieldName(),
        field.getmFieldName());
    jw.nextControlFlow("else");
    jw.emitStatement("%s.%s = null", PARAM_TARGET, field.getmFieldName());
    jw.endControlFlow();
  }
}
