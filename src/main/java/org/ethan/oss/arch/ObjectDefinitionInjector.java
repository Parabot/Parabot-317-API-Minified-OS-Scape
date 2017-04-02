package org.ethan.oss.arch;

import org.ethan.oss.api.callbacks.ObjectDefinitionCallBack;
import org.ethan.oss.hook.Hook;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class ObjectDefinitionInjector implements Injector {

    @SuppressWarnings("deprecation")
    private static void callBack(MethodNode mn) {
        InsnList           nl      = new InsnList();
        AbstractInsnNode[] mnNodes = mn.instructions.toArray();
        for (AbstractInsnNode abstractInsnNode : mnNodes) {
            if (abstractInsnNode.getOpcode() == Opcodes.ARETURN) {
                System.out.println("Injecting ObjectDefinition Callback...");
                nl.add(new VarInsnNode(Opcodes.ILOAD, 0));
                nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ObjectDefinitionCallBack.class.getCanonicalName().replace('.', '/'), "add", "(" + "Ljava/lang/Object;I" + ")V"));
                nl.add(new VarInsnNode(Opcodes.ALOAD, 2));

            }
            nl.add(abstractInsnNode);
        }
        mn.instructions = nl;
        mn.visitMaxs(0, 0);
        mn.visitEnd();
    }

    @Override
    public boolean canRun(ClassNode classNode) {
        return Hook.getInstance().getClass("getObjectDefinition", false).equals(classNode.name);
    }

    @Override
    public void run(ClassNode classNode) {
        @SuppressWarnings("unchecked")
        ListIterator<MethodNode> mnIt = classNode.methods.listIterator();
        while (mnIt.hasNext()) {
            MethodNode mn = mnIt.next();
            if (mn.desc.equals(Hook.getInstance().getDesc("getObjectDefinition"))) {
                callBack(mn);
            }
        }
    }

}