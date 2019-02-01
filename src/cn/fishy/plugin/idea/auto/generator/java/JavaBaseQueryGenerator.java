package cn.fishy.plugin.idea.auto.generator.java;

import cn.fishy.plugin.idea.auto.constant.GenerateType;
import cn.fishy.plugin.idea.auto.domain.Code;
import cn.fishy.plugin.idea.auto.generator.BaseGenerator;

/**
 * User: duxing Date: 2015.08.14 23:24
 */
public class JavaBaseQueryGenerator extends BaseGenerator {

  @Override
  public Code getCode() {
    return Code.JAVA;
  }

  @Override
  public GenerateType generateType() {
    return null;
  }
}
