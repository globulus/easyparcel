package net.globulus.easyparcel.processor.util;

import net.globulus.easyparcel.processor.codegen.FieldCodeGen;

import javax.lang.model.type.TypeMirror;

public class CodeGenInfo {

  private TypeMirror mGenericsType;
  private FieldCodeGen mCodeGene;

  public CodeGenInfo(FieldCodeGen generator) {
    mCodeGene = generator;
  }

  public CodeGenInfo(FieldCodeGen generator, TypeMirror genericsType) {
    mCodeGene = generator;
    mGenericsType = genericsType;
  }

  public FieldCodeGen getCodeGenerator() {
    return mCodeGene;
  }

  public TypeMirror getGenericsType() {
    return mGenericsType;
  }
}
