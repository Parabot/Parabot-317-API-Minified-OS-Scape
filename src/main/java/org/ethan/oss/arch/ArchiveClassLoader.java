package org.ethan.oss.arch;

import org.ethan.oss.component.RSCanvas;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Hashtable;
import java.util.ListIterator;

public class ArchiveClassLoader extends ClassLoader {

    private Archive<?>                  archive;
    private Hashtable<String, Class<?>> classes;
    private Injector[] injectors = { new ModelInjector(), new MessageInjector() };
    private ProtectionDomain domain;

    public ArchiveClassLoader(Archive<?> archive) throws IOException {
        this.archive = archive;
        classes = new Hashtable<>();
        Permissions permissions = getAppletPermissions();
        this.domain = new ProtectionDomain(new CodeSource(new URL("http://127.0.0.1/"), (Certificate[]) null), permissions);

    }

    public static void setSuper(ClassNode node, String superClass) {
        String replacedSuper = "";
        if (node.superName != "") {
            replacedSuper = node.superName;
        }
        if (replacedSuper != "") {
            ListIterator<?> mli = node.methods.listIterator();
            while (mli.hasNext()) {
                MethodNode      mn  = (MethodNode) mli.next();
                ListIterator<?> ili = mn.instructions.iterator();
                while (ili.hasNext()) {
                    AbstractInsnNode ain = (AbstractInsnNode) ili.next();
                    if (ain.getOpcode() == Opcodes.INVOKESPECIAL) {
                        MethodInsnNode min = (MethodInsnNode) ain;
                        if (min.owner.equals(replacedSuper)) {
                            min.owner = superClass;
                        }
                    }
                }
            }
        }
        node.superName = superClass;
    }

    private void modify(ClassNode node) {

        if (node.superName.toLowerCase().contains("canvas")) {
            setSuper(node, RSCanvas.class.getCanonicalName().replace('.', '/'));
            System.out.println("Canvas is now: " + node.superName);

        }
        for (Injector injector : injectors) {
            if (injector.canRun(node)) {
                injector.run(node);
            }
        }

    }

    @Override
    protected URL findResource(String name) {
        if (getSystemResource(name) == null) {
            if (JarArchive.resources.containsKey(name)) {
                try {
                    return JarArchive.resources.get(name).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
        return getSystemResource(name);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        String clsName = name.replaceAll("\\.", "/");

        ClassNode node = (ClassNode) archive.get(clsName);

        if (node != null) {
            modify(node);
            byte[] clsData = archive.getEntry(clsName);
            if (clsData != null) {
                Class<?> cls = defineClass(name, clsData, 0, clsData.length,
                        domain);
                if (!classes.containsKey(cls)) {
                    classes.put(name, cls);
                }
                if (resolve) {
                    resolveClass(cls);
                }
                return cls;
            }
        }
        return super.findSystemClass(name);
    }

    public Hashtable<String, Class<?>> classes() {
        return classes;
    }

    public Permissions getAppletPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }

    public ProtectionDomain getDomain() {
        return domain;
    }
}
