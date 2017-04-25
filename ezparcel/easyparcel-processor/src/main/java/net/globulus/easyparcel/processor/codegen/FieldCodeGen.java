package net.globulus.easyparcel.processor.codegen;

import net.globulus.easyparcel.processor.ParcelableField;

import java.io.IOException;

import javawriter.EzpJavaWriter;

public interface FieldCodeGen {

  void generateWriteToParcel(ParcelableField field, EzpJavaWriter jw) throws IOException;
  void generateReadFromParcel(ParcelableField field, EzpJavaWriter jw) throws IOException;
}
