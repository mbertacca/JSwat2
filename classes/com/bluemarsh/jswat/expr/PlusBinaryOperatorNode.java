/*********************************************************************
 *
 *      Copyright (C) 2002-2004 Nathan Fiedler
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * $Id$
 *
 ********************************************************************/

package com.bluemarsh.jswat.expr;

import com.bluemarsh.jswat.parser.java.node.Token;
import com.bluemarsh.jswat.util.Strings;

/**
 * Class PlusBinaryOperatorNode implements the addition binary operator (+).
 *
 * @author  Nathan Fiedler
 */
class PlusBinaryOperatorNode extends BinaryOperatorNode {

    /**
     * Constructs a PlusBinaryOperatorNode.
     *
     * @param  node  lexical token.
     */
    public PlusBinaryOperatorNode(Token node) {
        super(node);
    } // PlusBinaryOperatorNode

    /**
     * Returns the value of this node.
     *
     * @param  context  evaluation context.
     * @return  value, either a Number or a String.
     * @throws  EvaluationException
     *          if an error occurred during evaluation.
     */
    protected Object eval(EvaluationContext context)
        throws EvaluationException {

        Object o1 = getChild(0).evaluate(context);
        Object o2 = getChild(1).evaluate(context);

        if (isNumber(o1) && isNumber(o2)) {
            if (isFloating(o1) || isFloating(o2)) {
                if (isDouble(o1) || isDouble(o2)) {
                    double d1 = getDoubleValue(o1);
                    double d2 = getDoubleValue(o2);
                    return new Double(d1 + d2);
                } else {
                    float f1 = getFloatValue(o1);
                    float f2 = getFloatValue(o2);
                    return new Float(f1 + f2);
                }
            } else {
                if (isLong(o1) || isLong(o2)) {
                    long l1 = getLongValue(o1);
                    long l2 = getLongValue(o2);
                    return new Long(l1 + l2);
                } else {
                    int i1 = getIntValue(o1);
                    int i2 = getIntValue(o2);
                    return new Integer(i1 + i2);
                }
            }

        } else {
            StringBuffer buf = new StringBuffer();
            if (o1 != null) {
                buf.append(Strings.trimQuotes(o1.toString()));
            } else {
                // Append the null.
                buf.append(o1);
            }
            if (o2 != null) {
                buf.append(Strings.trimQuotes(o2.toString()));
            } else {
                // Append the null.
                buf.append(o2);
            }
            return buf.toString();
        }
    } // eval

    /**
     * Returns this operator's precedence value. The lower the value the
     * higher the precedence. The values are equivalent to those
     * described in the Java Language Reference book (2nd ed.), p 106.
     *
     * @return  precedence value.
     */
    public int precedence() {
        return 7;
    } // precedence
} // PlusBinaryOperatorNode
