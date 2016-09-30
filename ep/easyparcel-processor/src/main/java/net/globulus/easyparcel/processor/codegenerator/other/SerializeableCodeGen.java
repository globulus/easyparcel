package net.globulus.easyparcel.processor.codegenerator.other;

import com.squareup.javawriter.JavaWriter;

import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.codegenerator.AbsCodeGen;

import java.io.IOException;

import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.codegenerator.CodeGenerator.PARAM_TARGET;

/**
 * Code generartor for Serializable
 *
 *
 */
public class SerializeableCodeGen extends AbsCodeGen {

  public SerializeableCodeGen() {
    super("Serializable");
  }

  @Override public void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException {

    jw.emitStatement("%s.%s = (%s) %s.readSerializable()", PARAM_TARGET,
        field.getFieldName(), field.getType(), PARAM_PARCEL);
  }
}
