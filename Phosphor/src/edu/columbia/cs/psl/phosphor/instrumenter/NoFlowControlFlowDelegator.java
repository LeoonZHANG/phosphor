package edu.columbia.cs.psl.phosphor.instrumenter;

import org.objectweb.asm.MethodVisitor;

import static edu.columbia.cs.psl.phosphor.instrumenter.TaintMethodRecord.NEW_EMPTY_TAINT;
import static org.objectweb.asm.Opcodes.*;

/**
 * Specifies that control flow should not propagate.
 */
public class NoFlowControlFlowDelegator extends AbstractControlFlowDelegator {

    /**
     * Visitor to which instruction visiting is delegated.
     */
    private final MethodVisitor delegate;

    public NoFlowControlFlowDelegator(MethodVisitor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void generateEmptyTaint() {
        NEW_EMPTY_TAINT.delegateVisit(delegate);
    }

    @Override
    public void visitingJump(int opcode) {
        switch(opcode) {
            case IFNULL:
            case IFNONNULL:
            case IFEQ:
            case IFNE:
            case IFGE:
            case IFGT:
            case IFLE:
            case IFLT:
                delegate.visitInsn(POP);
                break;
            case IF_ICMPEQ:
            case IF_ICMPLE:
            case IF_ICMPNE:
            case IF_ICMPLT:
            case IF_ICMPGT:
            case IF_ICMPGE:
            case IF_ACMPEQ:
            case IF_ACMPNE:
                delegate.visitInsn(POP);
                delegate.visitInsn(SWAP);
                delegate.visitInsn(POP);
                break;
        }
    }

    @Override
    public void visitingSwitch() {
        // Remove the taint tag
        delegate.visitInsn(POP);
    }
}
