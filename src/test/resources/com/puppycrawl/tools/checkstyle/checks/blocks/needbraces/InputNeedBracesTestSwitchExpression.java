/*
NeedBraces
allowSingleLineStatement = true
allowEmptyLoopBody = (default)false
tokens = LITERAL_CASE, LITERAL_DEFAULT, LAMBDA


*/

// Java17
package com.puppycrawl.tools.checkstyle.checks.blocks.needbraces;

public class InputNeedBracesTestSwitchExpression {
    void howMany1(NumsTwo k) {
        switch (k) {
            case ONE: // violation ''case' construct must use '{}'s'
                System.out.println("case two");
                MathOperationTwo case5 = (a, b) -> // violation ''->' construct must use '{}'s'
                        (a + b);
            case TWO, THREE: // violation ''case' construct must use '{}'s'
                System.out.println("case two");

            case FOUR: // violation ''case' construct must use '{}'s'
                System.out.println("case three");

            default: // violation ''default' construct must use '{}'s'
                throw new IllegalStateException("Not a nums");
        }
    }

    void howMany2(NumsTwo k) {
        switch (k) { // cannot have more than one statement without block
            case ONE -> // violation ''case' construct must use '{}'s'
                    System.out.println("case one");

            case TWO, THREE -> // violation ''case' construct must use '{}'s'
                    System.out.println("case two");

            case FOUR -> // violation ''case' construct must use '{}'s'
                    System.out.println("case three");

            default -> // violation ''default' construct must use '{}'s'
                    throw new IllegalStateException("Not a nums");
        }
    }

    int howMany3(NumsTwo k) {
        return switch (k) {
            case ONE: // violation ''case' construct must use '{}'s'
                MathOperationTwo case5 = (a, b) -> // violation ''->' construct must use '{}'s'
                        (a + b);
                yield 3;
            case TWO, THREE: // violation ''case' construct must use '{}'s'
                yield 5;

            case FOUR: // violation ''case' construct must use '{}'s'
                yield 9;

            default: // violation ''default' construct must use '{}'s'
                throw new IllegalStateException("Not a Nums");
        };
    }

    /**
     * Braces required in switch expression with switch labled block
     */
    int howMany4(NumsTwo k) {
        return switch (k) {
            case ONE -> {
                yield 4;
            }
            case TWO, THREE -> {
                MathOperationTwo case5 = (a, b) -> // violation ''->' construct must use '{}'s'
                        (a + b);
                yield 42;
            }
            case FOUR -> {
                yield 99;
            }
            default -> throw new IllegalStateException("Not a Nums");

        };
    }

    /**
     * Braces not allowed in switch expression with switch labeled expression
     */
    int howMany5(NumsTwo k) {
        return switch (k) {
            case ONE -> 1; // braces not allowed, ok
            case TWO, THREE -> 3; // braces not allowed, ok
            case FOUR -> 4; // braces not allowed, ok
            default -> {throw new IllegalStateException("Not a Nums");}
        };
    }

    void howMany6(NumsTwo k) {
        switch (k) {
            case ONE: System.out.println("case two");
            case TWO, THREE: System.out.println("case two");
            case FOUR: System.out.println("case three");
            default: throw new IllegalStateException("Not a nums");
        }
    }

    void howMany7(NumsTwo k) {
        switch (k) {
            case ONE -> System.out.println("case one");
            case TWO, THREE -> System.out.println("case two");
            case FOUR -> System.out.println("case three");
            default -> throw new IllegalStateException("Not a nums");
        }
    }
}

enum NumsTwo {ONE, TWO, THREE, FOUR}

interface MathOperationTwo {
    int operation(int a, int b);
}
