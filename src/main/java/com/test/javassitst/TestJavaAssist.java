package com.test.javassitst;

import javassist.*;

/**
 * javaAssist练习使用
 * 使用 Javassist 动态生成 Java 字节码
 * @author sunquanhu
 */
public class TestJavaAssist {

    public static void main(String[] args) throws Exception {
        //获取类池
        ClassPool pool = ClassPool.getDefault();
        //添加系统路径
        pool.appendSystemPath();
        //导入相关依赖包
        pool.importPackage(String.class.getName());
        //获取类名
       // CtClass ctClass = pool.getCtClass("AB");
        CtClass ctClass = pool.makeClass("Test");
        //生成无参的构造函数
        CtConstructor ctConstructor = new CtConstructor(new CtClass[0],ctClass);
       // ctConstructor.setBody("{}");
        ctClass.addConstructor(ctConstructor);
        CtField field = new CtField(pool.get("java.lang.String"),"name",ctClass);
        field.setModifiers(Modifier.PROTECTED);
        ctClass.addField(field, CtField.Initializer.constant("xiaoming"));

        ctClass.writeFile("/Users/sunquanhu/Projects/study/herostory/src/main/java/com/test/javassitst");

    }

}
