/*********************************************************************
 *
 *      Copyright (C) 2002 Nathan Fiedler
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
 * FILE:        dummyCommand.java
 *
 * AUTHOR:      Nathan Fiedler
 *
 * REVISION HISTORY:
 *      Name    Date            Description
 *      ----    ----            -----------
 *      nf      07/28/02        Initial version
 *
 * $Id$
 *
 ********************************************************************/

package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.Session;

/**
 * Defines the class that handles the 'dummy' command.
 *
 * @author  Nathan Fiedler
 */
public class dummyCommand extends JSwatCommand {

    /**
     * Perform the 'dummy' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        // do absolutely nothing
    } // perform
} // dummyCommand
