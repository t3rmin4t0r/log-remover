package org.notmysock.madness;

import org.objectweb.asm.*;

import java.lang.instrument.*;
import java.lang.reflect.*;
import java.security.ProtectionDomain;

public class LogRemover implements ClassFileTransformer {
  private final String method;
  private LogRemover(String method) {
    this.method = method;
  }
  public static void premain(String options, Instrumentation instrumentation) {
    instrumentation.addTransformer(new LogRemover("debug"));
  }
  
  private class ClassWalker extends ClassAdapter {
    public ClassWalker(ClassVisitor parent) {
      super(parent);
    }
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);      
      return new BytecodeWalker(mv);
    }
  }
  
  private class BytecodeWalker extends MethodAdapter implements Opcodes {
    private static final String LOGGER = "org/apache/commons/logging/Log";
    private static final String ObjectVoid = "(Ljava/lang/Object;)V";
    public BytecodeWalker(MethodVisitor parent) {
      super(parent);
    }
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
      if(opcode == INVOKEINTERFACE && LOGGER.equals(owner) 
          && method.equals(name) && ObjectVoid.equals(desc)) {        
        super.visitInsn(POP2);
        return;
      }
      super.visitMethodInsn(opcode, owner, name, desc);
    }
  }
  
  @Override
  public byte[] transform(ClassLoader loader, String name, Class<?> klass,
      ProtectionDomain domain, byte[] data) throws IllegalClassFormatException {    
    if(name.startsWith("org/apache/hadoop") || name.startsWith("org/notmysock")) {
      ClassReader r = new ClassReader(data);
      ClassWriter w = new ClassWriter(r, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
      ClassWalker f = new ClassWalker(w);
      r.accept(f, 0);
      return w.toByteArray();
    }
    // skip
    return null;
  }
}
