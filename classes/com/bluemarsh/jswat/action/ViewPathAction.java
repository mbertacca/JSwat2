/*********************************************************************
 *
 *	Copyright (C) 2001-2005 Nathan Fiedler
 *	Copyright (C)      2018 Marco Bertacca
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
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Show the file path
 *
 * @author  mbertacca
 */
public class ViewPathAction extends JSwatAction {
    /**
     * Creates a new ViewPathAction object with the default action
     * command string of "viewPath".
     */
    public ViewPathAction() {
        super("viewPath");
    } // ViewPathAction
    public void actionPerformed(ActionEvent event) {
        // Get frame that contains our invoker.
        Frame frame = getFrame(event);
        Session session = getSession(event);
        UIAdapter adapter = session.getUIAdapter();

        // Find the currently active source view, if any.
        View view = adapter.getSelectedView();
        // If none, display a message indicating the problem.
        if (view == null) {
            displayError(frame, Bundle.getString("Find.noViewSelected"));
            return;
        }
        Object[] message = {
            new JTextField(view.getLongTitle())
        };
        ((JTextField) message[0]).setEditable (false);
        JOptionPane.showMessageDialog (frame, message);
    }
} // ViewPathAction
