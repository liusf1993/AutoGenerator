package cn.fishy.plugin.idea.auto.generator;

import cn.fishy.plugin.idea.auto.constant.GenerateType;
import cn.fishy.plugin.idea.auto.constant.ImportMapHolder;
import cn.fishy.plugin.idea.auto.domain.Code;
import cn.fishy.plugin.idea.auto.domain.Column;
import cn.fishy.plugin.idea.auto.domain.Setting;
import cn.fishy.plugin.idea.auto.domain.TemplateConfig;
import cn.fishy.plugin.idea.auto.storage.SettingManager;
import cn.fishy.plugin.idea.auto.util.NameUtil;
import cn.fishy.plugin.idea.auto.util.PathHolder;
import cn.fishy.plugin.idea.auto.util.TemplateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.util.DateUtil;

/**
 * User: duxing Date: 2015-08-15 16:19
 */
public class BaseGenerator {

  protected static List<String> deleteKeyList = new ArrayList<String>();

  static {
    deleteKeyList.add("IS_DELETE");
    deleteKeyList.add("IS_DELETED");
    deleteKeyList.add("DELETED");
  }

  protected GenerateType generateType = GenerateType.DO;

  public BaseGenerator(GenerateType generateType) {
    this.generateType = generateType;
  }

  public BaseGenerator() {
  }

  public String generate() {
    Map<String, Object> map = initMap();
    return generate(getTemplate(), map);
  }

  public String generate(String className, String queryClassName, String daoClassName, Column primaryKeyColumn) {
    Map<String, Object> map = initMap();
    map.put("objClassName", className);
    map.put("objPropertyName", NameUtil.lowFirst(className));
    map.put("className", className);
    map.put("queryClassName", queryClassName);
    map.put("queryPropertyName", NameUtil.lowFirst(queryClassName));
    try {
      map.put("primaryKeyName", primaryKeyColumn.getProperty());
      String type = primaryKeyColumn.getType();
      map.put("primaryKeyType", type);
    } catch (Exception e) {
      map.put("primaryKeyName", "id");
      map.put("primaryKeyType", "Long");
    }
    List<String> importList = getImportList(primaryKeyColumn, false, true);
    importList.add(PathHolder.impt(GenerateType.DO, className));

//        importList.add(PathHolder.impt(GenerateType.BaseDAO, GenerateType.BaseDAO.getName()));
    map.put("importList", importList);
    Setting setting = SettingManager.get();
    map.put("daoLogicDelete", setting.isDaoLogicDelete());
    return generate(map);
  }

  public String generate(String className, String entityName, String dependencyName) {
    Map<String, Object> map = initMap();
    map.put("objClassName", entityName);
    map.put("objPropertyName", NameUtil.lowFirst(className));
    map.put("className", className);
    Setting setting = SettingManager.get();
    map.put("daoLogicDelete", setting.isDaoLogicDelete());
    return generate(map);
  }

  private String getTemplate() {
    return TemplateConfig.getTemplate(getCode(), generateType());
  }

  public String generate(Map<String, Object> map) {
    return generate(getTemplate(), map);
  }

  public String generate(String tpl, Map<String, Object> map) {
    return TemplateUtil.parseByVm(tpl, map);
  }

  public Code getCode() {
    return Code.JAVA;
  }

  public GenerateType generateType() {
    return this.generateType;
  }

  public List<String> getImportList(List<Column> columnList, boolean objectClass) {
    return getImportList(columnList, objectClass, false);
  }

  public List<String> getImportList(List<Column> columnList, boolean objectClass, boolean addList) {
    List<String> importList = new ArrayList<String>();
    if (objectClass) {
      importList.add("java.io.Serializable");
    }
    if (columnList != null && columnList.size() > 0) {
      for (Column c : columnList) {
        if (c != null) {
          String i = ImportMapHolder.getImport(c.getType(), getCode());
          if (i != null && !importList.contains(i)) {
            importList.add(i);
          }
        }
      }
    }
    if (addList) {
      if (Code.JAVA.equals(getCode())) {
        importList.add("java.util.List");
        if (GenerateType.Transfer.equals(generateType())) {
          importList.add("java.util.ArrayList");
        }
      } else {
        importList.add("java.util");
      }
    }
    return importList;
  }

  public List<String> getImportList(Column column, boolean objectClass) {
    return getImportList(column, objectClass, false);
  }

  public List<String> getImportList(Column column, boolean objectClass, boolean addList) {
    List<Column> l = new ArrayList<Column>();
    l.add(column);
    return getImportList(l, objectClass, addList);
  }

  public Map<String, Object> initMap() {
    Map<String, Object> map = new HashMap<String, Object>();
    Setting setting = SettingManager.get();
    map.put("user", setting.getAuthor());
    map.put("autoName", "auto" + generateType().getName());
    map.put("today", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
    map.put("package", PathHolder.pkg(generateType()));
    return map;
  }

}
