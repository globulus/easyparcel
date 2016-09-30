package net.globulus.easyparcel.processor.codegenerator;

import com.squareup.javawriter.JavaWriter;

import net.globulus.easyparcel.processor.ParcelableField;

import java.io.IOException;

/**
 *
 */
public interface FieldCodeGen {

  void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException;
  void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException;
}
