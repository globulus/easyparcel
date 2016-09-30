package net.globulus.easyparcel.processor.codegenerator.android;

import com.squareup.javawriter.JavaWriter;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegenerator.FieldCodeGen;

import java.io.IOException;

import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_FLAGS;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_TARGET;

/**
 *
 */
public class ParcelableCodeGen implements FieldCodeGen {

  @Override
  public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException  {
    jw.emitStatement("%s.writeParcelable(%s.%s, %s)", PARAM_PARCEL, PARAM_SOURCE,
        field.getFieldName(), PARAM_FLAGS);
  }

  @Override
  public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException {
    jw.emitStatement("%s.%s = %s.readParcelable(%s.class.getClassLoader())",
            PARAM_TARGET, field.getFieldName(), PARAM_PARCEL, field.getType());
  }
}
