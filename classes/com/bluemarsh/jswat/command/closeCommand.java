/*********************************************************************
 *
 *      Copyright (C) 2000-2002 Nathan Fiedler
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
 * PROJECT:     JSwat
 * MODULE:      JSwat Commands
 * FILE:        closeCommand.java
 *
 * AUTHOR:      Nathan Fiedler
 *
 * REVISION HISTORY:
 *      Name    Date            Description
 *      ----    ----            -----------
 *      nf      08/07/00        Initial version
 *      nf      09/03/01        Added error message
 *
 * $Id$
 *
 ********************************************************************/

package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.Session;

/**
 * Defines the class that handles the 'close' command.
 *
 * @author  Nathan Fiedler
 */
public class closeCommand extends JSwatCommand {

    /**
     * Perform the 'close' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        // Tell the Session to deactivate, which usually means
        // closing the debuggee VM (or disconnecting if remote).
        if (session.isActive()) {
            session.deactivate(false, this);
        } else {
            throw new CommandException(Bundle.getString("noActiveSession"));
        }
    } // perform
} // closeCommand
