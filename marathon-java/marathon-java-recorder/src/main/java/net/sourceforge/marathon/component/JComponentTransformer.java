package net.sourceforge.marathon.component;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

public class JComponentTransformer implements ClassFileTransformer {

    public static final Logger LOGGER = Logger.getLogger(JComponentTransformer.class.getName());

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {
        return transformClass(classBeingRedefined, classfileBuffer, loader);
    }

    private byte[] transformClass(Class<?> classBeingRedefined, byte[] b, ClassLoader loader) {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = null;
        try {
            ctClass = classPool.makeClass(new java.io.ByteArrayInputStream(b));

            if (ctClass.getName().equals("javax.swing.JComponent")) {
            	LOGGER.finest("Transforming javax.swing.JComponent constructor...");
                CtConstructor ctConstructor = ctClass.getDeclaredConstructor(null);
                ctConstructor.insertAfter("{" + "addMouseListener(new marathon.stb.inst.CustomMouseAdapter());" + "}");
                LOGGER.finest("Constructor inserted...");
                LOGGER.finest("Getting the byte code...");
                b = ctClass.toBytecode();
                LOGGER.finest("Bytecode generated...");
            }
        } catch (Throwable e) {
        	LOGGER.log(Level.SEVERE, "Error while transforming JComponent bytecode", e);
        } finally {
            if (ctClass != null) {
                ctClass.detach();
            }
        }
        return b;
    }
}
