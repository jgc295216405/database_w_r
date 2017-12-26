/**
 * Wangyin.com Inc.
 * Copyright (c) 2003-2014 All Rights Reserved.
 */
package com.netease.cloud.util;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扩展mybaties生成代码,添加page方法,完成mysql的数据库层分页.
 *
 * @author 蒋鲁宾
 * @version v 0.1 2014/11/3 17:06 Exp $$
 */
public class MySQLPaginationPlugin extends PluginAdapter {

    public static void generate() {
        String config = MySQLPaginationPlugin.class.getClassLoader().getResource(
                "generatorConfig.xml").getFile();
        String[] arg = {"-configfile", config, "-overwrite"};
        ShellRunner.main(arg);
    }

    public static void main(String[] args) {
        generate();

    }


    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
        if (CollectionUtils.isNotEmpty(introspectedColumns)) {
            IntrospectedColumn introspectedColumn = introspectedColumns.get(0);
            Attribute attribute = new Attribute("useGeneratedKeys", "true");
            element.addAttribute(attribute);
            Attribute attribute1 = new Attribute("keyProperty", introspectedColumn.getJavaProperty());
            element.addAttribute(attribute1);
        }
        return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {

        List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
        if (CollectionUtils.isNotEmpty(introspectedColumns)) {
            IntrospectedColumn introspectedColumn = introspectedColumns.get(0);
            Attribute attribute = new Attribute("useGeneratedKeys", "true");
            element.addAttribute(attribute);
            Attribute attribute1 = new Attribute("keyProperty", introspectedColumn.getJavaProperty());
            element.addAttribute(attribute1);
        }
        return super.sqlMapInsertElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
                                              IntrospectedTable introspectedTable) {
        //mybaties自动生成mapper的时候是追加操作,但是我们平常都是删除重新生成,所以需要扩展删除原来的mapper让mybaties再生成一个
        List<GeneratedXmlFile> generatedXmlFiles = introspectedTable.getGeneratedXmlFiles();
        for (GeneratedFile generatedFile : generatedXmlFiles) {
            generatedFile.getFormattedContent();

            File project = new File(generatedFile.getTargetProject() + generatedFile.getTargetPackage());
            File file = new File(project, generatedFile.getFileName());
            if (file.exists()) {
                file.delete();
            }
        }
        // add field, getter, setter for limit clause
        addPage(topLevelClass, introspectedTable, "page");

        return super.modelExampleClassGenerated(topLevelClass,
                introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType(
                "org.springframework.stereotype.Repository"));
//        interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional"));
        interfaze.addAnnotation("@Repository");
//        interfaze.addAnnotation("@Transactional");
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        // 产生分页语句前半部分
//        XmlElement paginationPrefixElement = new XmlElement("sql");
//        paginationPrefixElement.addAttribute(new Attribute("id",
//                "MySQLDialectPrefix"));
//        XmlElement pageStart = new XmlElement("if");
//        pageStart.addAttribute(new Attribute("test", "page != null"));
//        pageStart.addElement(new TextElement(
//                "select * from ( "));
//        paginationPrefixElement.addElement(pageStart);
//        parentElement.addElement(paginationPrefixElement);

        // 产生分页语句后半部分
        XmlElement paginationSuffixElement = new XmlElement("sql");
        paginationSuffixElement.addAttribute(new Attribute("id",
                "MySQLDialectSuffix"));
        XmlElement pageEnd = new XmlElement("if");
        pageEnd.addAttribute(new Attribute("test", "page != null"));
        pageEnd.addElement(new TextElement(
                "LIMIT #{page.dbBeginIndex}, #{page.itemsPerPage}"));
        paginationSuffixElement.addElement(pageEnd);
        parentElement.addElement(paginationSuffixElement);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement isNotNullElement = new XmlElement("include"); //$NON-NLS-1$
        isNotNullElement.addAttribute(new Attribute("refid",
                "MySQLDialectSuffix"));
        element.getElements().add(isNotNullElement);

        return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element,
                introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {

//        XmlElement pageStart = new XmlElement("include"); //$NON-NLS-1$
//        pageStart.addAttribute(new Attribute("refid", "MySQLDialectPrefix"));
//        element.getElements().add(0, pageStart);

        XmlElement isNotNullElement = new XmlElement("include"); //$NON-NLS-1$
        isNotNullElement.addAttribute(new Attribute("refid",
                "MySQLDialectSuffix"));
        element.getElements().add(isNotNullElement);

        return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element,
                introspectedTable);
    }

    /**
     * @param topLevelClass
     * @param introspectedTable
     * @param name
     */
    private void addPage(TopLevelClass topLevelClass,
                         IntrospectedTable introspectedTable, String name) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType(
                "com.jd.idn.pay.promotion.common.model.Page"));
        CommentGenerator commentGenerator = context.getCommentGenerator();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(new FullyQualifiedJavaType("com.jd.idn.pay.promotion.common.model.Page"));
        field.setName(name);
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("set" + camel);
        method.addParameter(new Parameter(new FullyQualifiedJavaType(
                "com.jd.idn.pay.promotion.common.model.Page"), name));
        method.addBodyLine("this." + name + "=" + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType(
                "com.jd.idn.pay.promotion.common.model.Page"));
        method.setName("get" + camel);
        method.addBodyLine("return " + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    private void generateToString(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
        List<Field> fields = topLevelClass.getFields();
        Map<String, Field> map = new HashMap<String, Field>();
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columns) {
            Field f = map.get(column.getJavaProperty());
            if (f != null) {
                f.getJavaDocLines().clear();
                if (column.getRemarks() != null) {
                    f.addJavaDocLine("/** " + column.getRemarks() + " */");
                }
            }
        }
    }


    /**
     * This plugin is always valid - no properties are required
     */
    public boolean validate(List<String> warnings) {
        return true;
    }
}
