/*********************************************************************
 *
 *	Copyright (C) 2001-2005 Nathan Fiedler
 *	Copyright (C)      2015 Marco Bertacca
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

package com.bluemarsh.jswat.action;

import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.ui.NoOpenViewException;
import com.bluemarsh.jswat.ui.UIAdapter;
import com.bluemarsh.jswat.view.View;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;

/**
 * Implements the find backward action used to search for text within
 * a source view.
 *
 * @author  mbertacca
 */
public class FindAgainBackAction extends FindAgainAction {
    /**
     * Creates a new FindAgainBackAction object with the default action
     * command string of "findBackAgain".
     */
    public FindAgainBackAction() {
        super("findAgainBack");
    } // FindAgainBackAction

    boolean findString (UIAdapter adapter, String query, boolean ignoreCase)
                                                throws NoOpenViewException {
         return adapter.findString(query, ignoreCase, true);
    }
} // FindAgainBackAction
