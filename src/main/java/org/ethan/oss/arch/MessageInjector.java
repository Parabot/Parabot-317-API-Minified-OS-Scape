package org.ethan.oss.arch;

import org.ethan.oss.hook.Hook;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.regex.Pattern;

public class MessageInjector implements Injector {

    @SuppressWarnings("deprecation")
    private static void callBack(MethodNode mn) {
        InsnList nl = new InsnList();
        boolean  b  = false;
        for (int i = 0; i < mn.instructions.size(); i++) {
            AbstractInsnNode abs = mn.instructions.get(i);
            if (abs.getOpcode() == 184 && !b) {
                nl.add(abs);
                b = true;
                System.out.println("Injecting MessageCallback");
                nl.add(new VarInsnNode(Opcodes.ILOAD, 0));
                nl.add(new VarInsnNode(Opcodes.ALOAD, 1));
                nl.add(new VarInsnNode(Opcodes.ALOAD, 2));
                nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/ethan/oss/api/callbacks/MessageCallBack", "messageListenerHook",
                        "(ILjava/lang/String;Ljava/lang/String;)V"));

            } else {
                nl.add(abs);
            }
        }
    }

    @Override
    public boolean canRun(ClassNode classNode) {

        return Hook.getInstance().getClass("getMessageCallback", true).replaceAll(Pattern.quote("."), "/").equals(classNode.name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(ClassNode classNode) {
        for (MethodNode methodNode : (List<MethodNode>) classNode.methods) {
            if (methodNode.name.equals(Hook.getInstance().getField("getMessageCallback", true).replaceAll(Pattern.quote("."), "/"))) {
                if (methodNode.desc.equals("(ILjava/lang/String;Ljava/lang/String;)V")) {
                    System.out.println("We found MessageCallBack method.");
                    callBack(methodNode);
                    break;
                }

            }
        }

    }

}