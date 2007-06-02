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

/**
 * Class BooleanNegOperatorNode implements the boolean negation operator (!).
 *
 * @author  Nathan Fiedler
 */
class BooleanNegOperatorNode extends UnaryOperatorNode {

    /**
     * Constructs a BooleanNegOperatorNode.
     *
     * @param  node  lexical token.
     */
    public BooleanNegOperatorNode(Token node) {
        super(node);
    } // BooleanNegOperatorNode

    /**
     * Returns the value of this node.
     *
     * @param  context  evaluation context.
     * @return  a Boolean.
     * @throws  EvaluationException
     *          if an error occurred during evaluation.
     */
    protected Object eval(EvaluationContext context)
        throws EvaluationException {

        Object o1 = getChild(0).evaluate(context);

        if (isBoolean(o1)) {
            return getBooleanValue(o1) ? Boolean.FALSE : Boolean.TRUE;
        } else {
            throw new EvaluationException(
                Bundle.getString( "error.oper.bool"), getToken());
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
        return 5;
    } // precedence
} // BooleanNegOperatorNode
