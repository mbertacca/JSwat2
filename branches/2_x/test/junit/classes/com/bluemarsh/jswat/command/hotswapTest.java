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
 * MODULE:      Unit Tests
 * FILE:        hotswapTest.java
 *
 * AUTHOR:      Nathan Fiedler
 *
 * REVISION HISTORY:
 *      Name    Date            Description
 *      ----    ----            -----------
 *      nf      07/29/02        Initial version
 *
 * $Id$
 *
 ********************************************************************/

package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.SessionManager;
import com.bluemarsh.jswat.SessionSetup;
import com.sun.jdi.VirtualMachine;
import junit.extensions.*;
import junit.framework.*;

/**
 * Tests the hotswap command.
 */
public class hotswapTest extends CommandTestCase {

    public hotswapTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new SessionSetup(new TestSuite(hotswapTest.class));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    // manually controls active state
    public void test_hotswap() {
        Session session = SessionManager.beginSession();
        SimpleSessionListener ssl = new SimpleSessionListener();
        session.addListener(ssl);
        SessionManager.launchSimple("locals");

        // Skip the tests on VMs that do not support hotswap.
        VirtualMachine vm = session.getVM();
        if (vm.canRedefineClasses()) {
            // no-arg case tested elsewhere
            try {
                runCommand(session, "hotswap not_defined");
                fail("expected CommandException");
            } catch (CommandException ce) {
                // expected
            }
            try {
                runCommand(session, "hotswap locals no_such_file");
                fail("expected CommandException");
            } catch (CommandException ce) {
                // expected
            }

            // class not yet loaded
            try {
                runCommand(session, "hotswap locals");
                fail("expected CommandException");
            } catch (CommandException ce) {
                // expected
            }

            runCommand(session, "classbrk locals");
            resumeAndWait(session, ssl);
            runCommand(session, "hotswap locals");
        }

        SessionManager.deactivate(true);
        session.removeListener(ssl);
        SessionManager.endSession();
    }
}
