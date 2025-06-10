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
            "15:5: " + getCheckMessage(MSG_KEY, "basic", 0),
            "19:5: " + getCheckMessage(MSG_KEY, "oneIf", 1),
            "24:5: " + getCheckMessage(MSG_KEY, "oneIfElse", 2),
            "30:5: " + getCheckMessage(MSG_KEY, "oneIfElseIf", 2),
            "36:5: " + getCheckMessage(MSG_KEY, "ternary", 1),
            "42:5: " + getCheckMessage(MSG_KEY, "tryCatch", 1),
            "51:5: " + getCheckMessage(MSG_KEY, "switchWithCases", 1),
            "61:5: " + getCheckMessage(MSG_KEY, "switchWithCasesAndDefault", 1),
            "73:5: " + getCheckMessage(MSG_KEY, "sameLogicalAndSequence", 2),
            "79:5: " + getCheckMessage(MSG_KEY, "sameLogicalOrSequence", 2),
            "85:5: " + getCheckMessage(MSG_KEY, "twoRepeatingLogicalSequences", 3),
            "91:5: " + getCheckMessage(MSG_KEY, "twoMixedLogicalSequences", 4),
            "97:5: " + getCheckMessage(MSG_KEY, "recursionSample", 2),
            "103:5: " + getCheckMessage(MSG_KEY, "multipleRecursions", 3),
            "111:5: " + getCheckMessage(MSG_KEY, "noRecursion", 0),
            "116:5: " + getCheckMessage(MSG_KEY, "InputCyclomaticComplexity.INSTANCE_INIT", 0),
        };

        verifyWithInlineConfigParser(
                getPath("InputCyclomaticComplexity.java"), expected);
    }
}
