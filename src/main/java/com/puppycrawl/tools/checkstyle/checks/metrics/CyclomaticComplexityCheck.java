///////////////////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code and other text files for adherence to a set of rules.
// Copyright (C) 2001-2025 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
///////////////////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks.metrics;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;

import com.puppycrawl.tools.checkstyle.FileStatefulCheck;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtil;

@FileStatefulCheck
public class CyclomaticComplexityCheck
    extends AbstractCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "cyclomaticComplexity";

    /** The initial current value. */
    private static final BigInteger INITIAL_VALUE = BigInteger.ZERO;

    /** The initial method name value. */
    private static final String INITIAL_METHOD_NAME = "";

    /** Default allowed complexity. */
    private static final int DEFAULT_COMPLEXITY_VALUE = 10;

    /** Stack of values - all but the current value. */
    private final Deque<BigInteger> valueStack = new ArrayDeque<>();

    /** Stack of method names for tracking recursion. */
    private final Deque<String> methodNameStack = new ArrayDeque<>();

    /** Control whether to treat the whole switch block as a single decision point. */
    private boolean switchBlockAsSingleDecisionPoint;

    /** The current value. */
    private BigInteger currentValue = INITIAL_VALUE;

    /** The current method name. */
    private String currentMethodName = INITIAL_METHOD_NAME;

    /** Specify the maximum threshold allowed. */
    private int max = DEFAULT_COMPLEXITY_VALUE;

    /**
     * Setter to control whether to treat the whole switch block as a single decision point.
     *
     * @param switchBlockAsSingleDecisionPoint whether to treat the whole switch
     *                                          block as a single decision point.
     * @since 6.11
     */
    public void setSwitchBlockAsSingleDecisionPoint(boolean switchBlockAsSingleDecisionPoint) {
        this.switchBlockAsSingleDecisionPoint = switchBlockAsSingleDecisionPoint;
    }

    /**
     * Setter to specify the maximum threshold allowed.
     *
     * @param max the maximum threshold
     * @since 3.2
     */
    public final void setMax(int max) {
        this.max = max;
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[] {
            TokenTypes.CTOR_DEF,
            TokenTypes.METHOD_DEF,
            TokenTypes.INSTANCE_INIT,
            TokenTypes.STATIC_INIT,
            TokenTypes.LITERAL_WHILE,
            TokenTypes.LITERAL_DO,
            TokenTypes.LITERAL_FOR,
            TokenTypes.LITERAL_IF,
            TokenTypes.LITERAL_ELSE,
            TokenTypes.LITERAL_SWITCH,
            TokenTypes.LITERAL_CASE,
            TokenTypes.LITERAL_DEFAULT,
            TokenTypes.LITERAL_CATCH,
            TokenTypes.QUESTION,
            TokenTypes.LAND,
            TokenTypes.LOR,
            TokenTypes.COMPACT_CTOR_DEF,
            TokenTypes.LITERAL_WHEN,
            TokenTypes.METHOD_CALL,
        };
    }

    @Override
    public int[] getAcceptableTokens() {
        return new int[] {
            TokenTypes.CTOR_DEF,
            TokenTypes.METHOD_DEF,
            TokenTypes.INSTANCE_INIT,
            TokenTypes.STATIC_INIT,
            TokenTypes.LITERAL_WHILE,
            TokenTypes.LITERAL_DO,
            TokenTypes.LITERAL_FOR,
            TokenTypes.LITERAL_IF,
            TokenTypes.LITERAL_ELSE,
            TokenTypes.LITERAL_SWITCH,
            TokenTypes.LITERAL_CASE,
            TokenTypes.LITERAL_DEFAULT,
            TokenTypes.LITERAL_CATCH,
            TokenTypes.QUESTION,
            TokenTypes.LAND,
            TokenTypes.LOR,
            TokenTypes.COMPACT_CTOR_DEF,
            TokenTypes.LITERAL_WHEN,
            TokenTypes.METHOD_CALL,
        };
    }

    @Override
    public final int[] getRequiredTokens() {
        return new int[] {
            TokenTypes.CTOR_DEF,
            TokenTypes.METHOD_DEF,
            TokenTypes.INSTANCE_INIT,
            TokenTypes.STATIC_INIT,
            TokenTypes.COMPACT_CTOR_DEF,
        };
    }

    @Override
    public void visitToken(DetailAST ast) {
        switch (ast.getType()) {
            case TokenTypes.CTOR_DEF:
            case TokenTypes.METHOD_DEF:
            case TokenTypes.INSTANCE_INIT:
            case TokenTypes.STATIC_INIT:
            case TokenTypes.COMPACT_CTOR_DEF:
                visitMethodDef(ast);
                break;
            default:
                visitTokenHook(ast);
        }
    }

    @Override
    public void leaveToken(DetailAST ast) {
        switch (ast.getType()) {
            case TokenTypes.CTOR_DEF:
            case TokenTypes.METHOD_DEF:
            case TokenTypes.INSTANCE_INIT:
            case TokenTypes.STATIC_INIT:
            case TokenTypes.COMPACT_CTOR_DEF:
                leaveMethodDef(ast);
                break;
            default:
                break;
        }
    }

    /**
     * Hook called when visiting a token. Will not be called the method
     * definition tokens.
     *
     * @param ast the token being visited
     */
    private void visitTokenHook(DetailAST ast) {
        // Skip LITERAL_IF when it's part of an "else if" construct
        if (ast.getType() == TokenTypes.LITERAL_IF && isElseIf(ast)) {
            return;
        }

        // Check for recursive method calls
        if (ast.getType() == TokenTypes.METHOD_CALL) {
            if (isRecursiveCall(ast)) {
                incrementCurrentValue(BigInteger.ONE);
            }
            return;
        }

        // Special handling for logical operators - only count start of sequences
        if (ast.getType() == TokenTypes.LAND || ast.getType() == TokenTypes.LOR) {
            if (isStartOfLogicalSequence(ast)) {
                incrementCurrentValue(BigInteger.ONE);
            }
            return;
        }

        if (!ScopeUtil.isInBlockOf(ast, TokenTypes.LITERAL_SWITCH)) {
                incrementCurrentValue(BigInteger.ONE);
            }
    }

    /**
     * Checks if the given METHOD_CALL token is a recursive call to the current method.
     *
     * @param ast the METHOD_CALL token to check
     * @return true if this is a recursive call, false otherwise
     */
    private boolean isRecursiveCall(DetailAST ast) {
        if (INITIAL_METHOD_NAME.equals(currentMethodName)) {
            return false;
        }
        
        // Get the method name from the METHOD_CALL
        final DetailAST methodNameNode = ast.getFirstChild();
        if (methodNameNode != null && methodNameNode.getType() == TokenTypes.IDENT) {
            final String calledMethodName = methodNameNode.getText();
            return currentMethodName.equals(calledMethodName);
        }
        
        return false;
    }

    /**
     * Checks if the given LITERAL_IF token is part of an "else if" construct.
     *
     * @param ast the LITERAL_IF token to check
     * @return true if this IF is part of an "else if", false otherwise
     */
    private boolean isElseIf(DetailAST ast) {
        final DetailAST parent = ast.getParent();
        return parent != null && parent.getType() == TokenTypes.LITERAL_ELSE;
    }

    /**
     * Checks if the given LAND or LOR token is the start of a logical sequence.
     * A logical sequence is a consecutive series of the same logical operator.
     * Only the first operator in each sequence should contribute to complexity.
     *
     * @param ast the LAND or LOR token to check
     * @return true if this token starts a new logical sequence, false otherwise
     */
    private boolean isStartOfLogicalSequence(DetailAST ast) {
        int tokenType = ast.getType();
        if (tokenType != TokenTypes.LAND && tokenType != TokenTypes.LOR) {
            return false;
        }
        
        DetailAST parent = ast.getParent();
        // If parent is not the same logical operator, this starts a new sequence
        return parent == null || parent.getType() != tokenType;
    }

    /**
     * Process the end of a method definition.
     *
     * @param ast the token representing the method definition
     */
    private void leaveMethodDef(DetailAST ast) {
        log(ast, MSG_KEY, currentMethodName, currentValue);
        popValue();
        popMethodName();
    }

    /**
     * Increments the current value by a specified amount.
     *
     * @param amount the amount to increment by
     */
    private void incrementCurrentValue(BigInteger amount) {
        currentValue = currentValue.add(amount);
    }

    /** Push the current value on the stack. */
    private void pushValue() {
        valueStack.push(currentValue);
        currentValue = INITIAL_VALUE;
    }

    /**
     * Pops a value off the stack and makes it the current value.
     */
    private void popValue() {
        currentValue = valueStack.pop();
    }

    /** Push the current method name on the stack. */
    private void pushMethodName(String methodName) {
        methodNameStack.push(currentMethodName);
        currentMethodName = methodName;
    }

    /**
     * Pops a method name off the stack and makes it the current method name.
     */
    private void popMethodName() {
        currentMethodName = methodNameStack.pop();
    }
    
    /** 
     * Process the start of the method definition and extract method name.
     * 
     * @param ast the token representing the method definition
     */
    private void visitMethodDef(DetailAST ast) {
        pushValue();
        
        // Extract method name for recursion detection
        String methodName = INITIAL_METHOD_NAME;
        switch (ast.getType()) {
            case TokenTypes.METHOD_DEF:
            case TokenTypes.CTOR_DEF:
            case TokenTypes.COMPACT_CTOR_DEF:
                final DetailAST methodNameNode = ast.findFirstToken(TokenTypes.IDENT);
                if (methodNameNode != null) {
                    methodName = methodNameNode.getText();
                }
                break;
            case TokenTypes.INSTANCE_INIT:
                methodName = "INSTANCE_INIT";
                break;
            case TokenTypes.STATIC_INIT:
                methodName = "STATIC_INIT";
                break;
            default:
                // Keep INITIAL_METHOD_NAME for other types
                break;
        }
        
        pushMethodName(methodName);
    }

}