/*
CyclomaticComplexity
max = 0
switchBlockAsSingleDecisionPoint = true
tokens = (default)LITERAL_WHILE, LITERAL_DO, LITERAL_FOR, LITERAL_IF, LITERAL_ELSE, LITERAL_SWITCH, \
         LITERAL_CASE, LITERAL_DEFAULT, LITERAL_CATCH, QUESTION, LAND, LOR, METHOD_CALL


*/

package com.puppycrawl.tools.checkstyle.checks.metrics.cyclomaticcomplexity;

public class InputCyclomaticComplexity {

    public void basic() { // violation
        // 0
    }

    public void oneIf() { // violation
        if (System.currentTimeMillis() == 0) { // +1
        }
    }

    public void oneIfElse() { // violation
        if (System.currentTimeMillis() == 0) { // +1
        } else { // +1
        }
    }

    public void oneIfElseIf() { // violation
        if (System.currentTimeMillis() == 0) { // +1
        } else if (System.currentTimeMillis() == 1) { // +1
        }
    }

    public void ternary() { // violation
        int a = 0;
        int b = 1;
        int c = (a == b) ? 1 : 2; // +1
    }

    public void tryCatch() { // violation
        try {
            // do something
        }
        catch (Exception e) { // +1
            // handle exception
        }
    }

    public void switchWithCases() { // violation
        int n = 0;
        switch (n) { // +1
            case 1:
                break;
            case 2:
                break;
        }
    }

    public void switchWithCasesAndDefault() { // violation
        int n = 0;
        switch (n) { // +1
            case 1:
                break;
            case 2:
                break;
            default: // +1
                break;
        }
    }

    public void sameLogicalAndSequence() { // violation
        if (System.currentTimeMillis() != 0 && System.currentTimeMillis() != 1 && System.currentTimeMillis() != 2) { // +1 for if; +1 for all &&
            // do something
        }
    }

    public void sameLogicalOrSequence() { // violation
        if (System.currentTimeMillis() == 0 || System.currentTimeMillis() == 1 || System.currentTimeMillis() == 2) { // +1 for if; +1 for all ||
            // do something
        }
    }

    public void twoRepeatingLogicalSequences() { // violation
        if (System.currentTimeMillis() != 0 && System.currentTimeMillis() != 1 && System.currentTimeMillis() != 2 || System.currentTimeMillis() == 10 || System.currentTimeMillis() == 20) { // +1 for if; +1 for all &&; +1 for all ||
            // do something
        }
    }

    public void twoMixedLogicalSequences() { // violation
        if (System.currentTimeMillis() != 0 && System.currentTimeMillis() != 1 || System.currentTimeMillis() != 2 && System.currentTimeMillis() != 3) { // +1 for if; +1 for &&; +1 for ||; +1 for && (new)
            // do something
        }
    }

    public void recursionSample() { // violation
        if (System.currentTimeMillis() == 0) { // +1
            recursionSample(); // +1
        }
    }

    public void multipleRecursions() { // violation
        if (System.currentTimeMillis() == 0) { // +1
            multipleRecursions(); // +1
        }

        multipleRecursions(); // +1
    }

    public void noRecursion() { // violation
        System.currentTimeMillis();
    }

    // instance initializer
    { // violation
        // 0
    }
}
