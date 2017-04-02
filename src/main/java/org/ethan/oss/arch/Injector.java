package org.ethan.oss.arch;

import org.objectweb.asm.tree.ClassNode;

public interface Injector {

    public abstract boolean canRun(ClassNode classNode);

    public abstract void run(ClassNode classNode);
}