package net.globulus.easyparcel.processor.codegenerator.collection;

import com.squareup.javawriter.JavaWriter;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegenerator.FieldCodeGen;

import java.io.IOException;

import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_FLAGS;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_TARGET;

/**
 * Parcelable array
 *
 *
 */
public class ParcelableArrayCodeGen implements FieldCodeGen {

  @Override public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException  {

    jw.emitStatement("%s.writeParcelableArray(%s.%s, %s)", PARAM_PARCEL, PARAM_SOURCE,
        field.getFieldName(), PARAM_FLAGS);
  }

  @Override public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException {

    String type = field.getGenericsTypeArgument().toString();

    jw.emitStatement(
        "android.os.Parcelable[] %sNullHelper = %s.readParcelableArray(%s.class.getClassLoader())",
        field.getFieldName(), PARAM_PARCEL, type);
    jw.beginControlFlow("if (%sNullHelper != null)", field.getFieldName());
    jw.emitStatement(
        "%s.%s = java.util.Arrays.copyOf(%sNullHelper, %sNullHelper.length, %s[].class)",
        PARAM_TARGET, field.getFieldName(), field.getFieldName(), field.getFieldName(), type);
    jw.nextControlFlow("else");
    jw.emitStatement("%s.%s = null", PARAM_TARGET, field.getFieldName());
    jw.endControlFlow();
  }
}
