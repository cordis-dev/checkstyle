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

import static com.puppycrawl.tools.checkstyle.checks.metrics.CyclomaticComplexityCheck.MSG_KEY;
import org.junit.jupiter.api.Test;
import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;

public class CyclomaticComplexityCheckTest
    extends AbstractModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/puppycrawl/tools/checkstyle/checks/metrics/cyclomaticcomplexity";
    }

    @Test
    public void test() throws Exception {

        final String[] expected = {
            "15:5: " + getCheckMessage(MSG_KEY, 0, 0),
            "19:5: " + getCheckMessage(MSG_KEY, 1, 0), // oneIf
            "24:5: " + getCheckMessage(MSG_KEY, 2, 0), // oneIfElse
            "30:5: " + getCheckMessage(MSG_KEY, 2, 0), // oneIfElseIf
            "36:5: " + getCheckMessage(MSG_KEY, 1, 0), // ternary
            "42:5: " + getCheckMessage(MSG_KEY, 1, 0), // tryCatch
            "51:5: " + getCheckMessage(MSG_KEY, 1, 0), // switchWithCases
            "61:5: " + getCheckMessage(MSG_KEY, 1, 0), // switchWithCasesAndDefault
            "73:5: " + getCheckMessage(MSG_KEY, 2, 0), // sameLogicalAndSequence
            "79:5: " + getCheckMessage(MSG_KEY, 2, 0), // sameLogicalOrSequence
            "85:5: " + getCheckMessage(MSG_KEY, 3, 0), // twoRepeatingLogicalSequences
            "91:5: " + getCheckMessage(MSG_KEY, 4, 0), // twoMixedLogicalSequences
            "97:5: " + getCheckMessage(MSG_KEY, 2, 0), // recursionSample
            "103:5: " + getCheckMessage(MSG_KEY, 3, 0), // multipleRecursions
            "111:5: " + getCheckMessage(MSG_KEY, 0, 0), // noRecursion
        };

        verifyWithInlineConfigParser(
                getPath("InputCyclomaticComplexity.java"), expected);
    }
}
