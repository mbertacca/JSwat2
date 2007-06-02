/*********************************************************************
 *
 *      Copyright (C) 2002-2005 Nathan Fiedler
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
import com.bluemarsh.jswat.ui.UIAdapter;
import java.awt.event.ActionEvent;

/**
 * Displays the getting help screen.
 *
 * @author  Nathan Fiedler
 */
public class GettingHelpAction extends JSwatAction {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new GettingHelpAction object with the default action
     * command string of "gettingHelp".
     */
    public GettingHelpAction() {
        super("gettingHelp");
    } // GettingHelpAction

    /**
     * Performs the getting help action.
     *
     * @param  event  action event
     */
    public void actionPerformed(ActionEvent event) {
        Session session = getSession(event);
        UIAdapter adapter = session.getUIAdapter();
        adapter.showHelp(com.bluemarsh.jswat.Bundle.getResource(
            "gettingHelp"));
    } // actionPerformed
} // GettingHelpAction
