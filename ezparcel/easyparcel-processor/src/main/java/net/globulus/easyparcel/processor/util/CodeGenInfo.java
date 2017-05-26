package net.globulus.easyparcel.processor.util;

import net.globulus.easyparcel.processor.codegen.FieldCodeGen;

import javax.lang.model.type.TypeMirror;

public class CodeGenInfo {

  private TypeMirror mGenericsType;
  private FieldCodeGen mCodeGen;

  public CodeGenInfo(FieldCodeGen generator) {
    mCodeGen = generator;
  }

  public CodeGenInfo(FieldCodeGen generator, TypeMirror genericsType) {
    mCodeGen = generator;
    mGenericsType = genericsType;
  }

  public FieldCodeGen getCodeGenerator() {
    return mCodeGen;
  }

  public TypeMirror getGenericsType() {
    return mGenericsType;
  }
}
