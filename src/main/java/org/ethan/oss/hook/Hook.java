package org.ethan.oss.hook;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.utils.NetUtil;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Hook {
    private static final Hook INSTANCE = new Hook();

    public static Hook getInstance() {
        return INSTANCE;
    }

    public String getClass(String str, boolean field) {
        if (field) {
            return ServerEngine.getInstance().getFieldMap().get(str).clazz.replaceAll("/", ".");
        } else {
            return ServerEngine.getInstance().getMethodMap().get(str).clazz.replaceAll("/", ".");
        }
    }

    public String getField(String str, boolean field) {
        if (field) {
            return ServerEngine.getInstance().getFieldMap().get(str).field.replaceAll("/", ".");
        } else {
            return ServerEngine.getInstance().getMethodMap().get(str).field.replaceAll("/", ".");
        }
    }

    public int getMult(String str) {

        return ServerEngine.getInstance().getFieldMap().get(str).mult;
    }

    public String getDesc(String str) {

        return ServerEngine.getInstance().getMethodMap().get(str).desc;
    }

    public void init() {
        System.out.println("OS-Scape Hooks: 0.2 \n");
        ServerEngine.getInstance().setFieldMap(new HashMap<String, FieldHook>());
        ServerEngine.getInstance().setMethodMap(new HashMap<String, MethodHook>());
        StringBuilder builder = new StringBuilder();
        String[]      hooks   = NetUtil.readPage("https://pastebin.com/raw/RbKze8FA");
        for (String str : hooks) {
            if (str.contains("#")) {
                String[] split     = str.split(" ");
                String   className = split[2].replaceAll(":", "");
                String   obfName   = split[3].replaceAll(",", "");
                builder.append("Class:" + className + ":" + obfName);
                builder.append("\n");
            }
            if (str.contains("~>")) {
                String[] split      = str.split(" ");
                String   fieldName  = split[6].replaceAll(":", "");
                String   obfName    = split[8];
                String[] fieldSplit = obfName.split(Pattern.quote("."));
                String   fieldClass = fieldSplit[0].replaceAll("/", ".");
                String   fieldField = fieldSplit[1];
                int      multNum    = -1;
                if (split.length >= 10) {
                    multNum = Integer.parseInt(split[10]);
                } else {
                    multNum = -1;
                }
                ServerEngine.getInstance().getFieldMap().put(fieldName, new FieldHook(fieldClass, fieldField, multNum));

                builder.append("Field:" + fieldName + ":" + obfName + ":" + multNum);
                builder.append("\n");
            }
        }
    }

}
