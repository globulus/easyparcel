package net.globulus.easyparcel.processor.codegen;

import net.globulus.easyparcel.processor.ParcelableField;

import java.io.IOException;

import javawriter.JavaWriter;

public interface FieldCodeGen {

  void generateWriteToParcel(ParcelableField field, JavaWriter jw) throws IOException;
  void generateReadFromParcel(ParcelableField field, JavaWriter jw) throws IOException;
}
