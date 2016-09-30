package net.globulus.easyparcel.processor.codegenerator.collection;

import com.squareup.javawriter.JavaWriter;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegenerator.FieldCodeGen;

import java.io.IOException;

import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_SOURCE;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_TARGET;

/**
 * String list
 *
 * @author Yaroslav Heriatovych
 */
public class StringListCodeGen implements FieldCodeGen {

  @Override public void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException  {

    jw.emitStatement("%s.writeStringList(%s.%s)", PARAM_PARCEL, PARAM_SOURCE,
        field.getFieldName());
  }

  @Override public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException {

    jw.emitStatement("%s.%s = %s.createStringArrayList()", PARAM_TARGET, field.getFieldName(), PARAM_PARCEL);
  }
}
