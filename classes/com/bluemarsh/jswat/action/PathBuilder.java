/*********************************************************************
 *
 *      Copyright (C) 2001-2005 David Taylor
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

import com.bluemarsh.jswat.ui.SessionFrameMapper;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * The PathBuilder is a component that allows a user to build a
 * classpath or sourcepath by selecting directories files using a
 * filesystem browser.
 *
 * @author  David Taylor
 * @author  Nathan Fiedler
 */
public class PathBuilder extends JPanel
    implements ActionListener, ListSelectionListener {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;
    /** The table that displays the path elements. */
    private PathTable pathElementTable;
    /** The table model that holds the path elements. */
    private PathElementTableModel pathElementModel;
    /** The button to add an element. */
    private JButton addElement;
    /** The button to remove an element. */
    private JButton removeElement;
    /** The button to modify an element. */
    private JButton modifyElement;
    /** The button to move an element towards the front of the path. */
    private JButton moveUp;
    /** The button to move an element towards the end of the path. */
    private JButton moveDown;
    /** Whether the move buttons are enabled or not. */
    private boolean moveButtonsEnabled = true;
    /** Whether to enabled multi-selection in the file chooser or not. */
    private boolean multiSelectionEnabled;
    /** The elements of the path. */
    private Vector elements;
    /** The initial directory to show in the file dialog. */
    private String startDirectory;
    /** A file filter to set on the file chooser. */
    private FileFilter filter;
    /** The last directory seen */
    private String lastSeenDirectory;
    /** True if session is active, false otherwise. */
    private boolean active;

    /**
     * Creates a default PathBuilder. If the session is active, none of
     * the buttons will be enabled, nor become enabled at any point
     * during interaction with the user.
     *
     * @param  active  true if session is active.
     */
    public PathBuilder(boolean active) {
        super(new BorderLayout());
        this.active = active;

        elements = new Vector();
        pathElementModel = new PathElementTableModel();

        addElement = new JButton(Bundle.getString("PathBuilder.add"));
        addElement.addActionListener(this);

        removeElement = new JButton(Bundle.getString("PathBuilder.remove"));
        removeElement.addActionListener(this);

        modifyElement = new JButton(Bundle.getString("PathBuilder.modify"));
        modifyElement.addActionListener(this);

        moveUp = new JButton(Bundle.getString("PathBuilder.moveUp"));
        moveUp.addActionListener(this);

        moveDown = new JButton(Bundle.getString("PathBuilder.moveDown"));
        moveDown.addActionListener(this);

        if (active) {
            // When active, all of the actions are disallowed.
            addElement.setEnabled(false);
            modifyElement.setEnabled(false);
            removeElement.setEnabled(false);
            moveUp.setEnabled(false);
            moveDown.setEnabled(false);
        }

        JPanel btnPanel = new JPanel();
        btnPanel.add(addElement);
        btnPanel.add(modifyElement);
        btnPanel.add(removeElement);
        btnPanel.add(moveUp);
        btnPanel.add(moveDown);
        add(btnPanel, BorderLayout.SOUTH);

        pathElementTable = new PathTable(pathElementModel);
        JScrollPane tableScroller = new JScrollPane(pathElementTable);
        tableScroller.setMinimumSize(new Dimension(100, 200));
        add(tableScroller, BorderLayout.CENTER);

        pathElementTable.getSelectionModel().addListSelectionListener(this);
        if (elements.size() > 0) {
            pathElementTable.setRowSelectionInterval(0, 0);
        }
        ToolTipManager.sharedInstance().registerComponent(pathElementTable);
    } // PathBuilder

    /**
     * Invoked when one of the buttons has been pressed.
     *
     * @param  e  the action event.
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(addElement)) {
            JFileChooser chooser;
            if (lastSeenDirectory != null) {
                chooser = new JFileChooser(lastSeenDirectory);
            } else if (startDirectory != null) {
                chooser = new JFileChooser(startDirectory);
            } else {
                chooser = new JFileChooser(System.getProperty("user.dir"));
            }

            chooser.setFileHidingEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if (multiSelectionEnabled) {
                chooser.setMultiSelectionEnabled(true);
            }
            if (filter != null) {
                chooser.addChoosableFileFilter(filter);
            }
            chooser.setDialogTitle(Bundle.getString("PathBuilder.addTitle"));
            int returnVal = chooser.showDialog(
                null, Bundle.getString("PathBuilder.select"));

            String lastdir = chooser.getCurrentDirectory().getPath();
            if (lastdir != null) {
                lastSeenDirectory = lastdir;
            }

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    if (multiSelectionEnabled) {
                        File[] files = chooser.getSelectedFiles();
                        for (int i = 0; i < files.length; i++) {
                            pathElementModel.add(files[i].getCanonicalPath());
                        }
                    } else {
                        pathElementModel.add(
                            chooser.getSelectedFile().getCanonicalPath());
                    }

                    if (elements.size() == 1) {
                        pathElementTable.setRowSelectionInterval(0, 0);
                    }
                } catch (IOException ioe) {
                    // Send the message via the table.
                    pathElementModel.add(ioe.toString());
                }
            }
        } else if (source.equals(removeElement)) {
            int row = pathElementTable.getSelectedRow();
            if (row >= 0) {
                pathElementModel.remove(row);
            }
        } else if (source.equals(modifyElement)) {
            int row = pathElementTable.getSelectedRow();
            String value = (String) pathElementTable.getValueAt(row, 0);
            value = editPathElement(e, value);
            if (row >= 0) {
                pathElementModel.setValueAt(value, row, 0);
            }
        } else if (source.equals(moveUp)) {
            int row = pathElementTable.getSelectedRow();
            if (row >= 1) {
                pathElementModel.moveUp(row);
            }
        } else if (source.equals(moveDown)) {
            int row = pathElementTable.getSelectedRow();
            if (row < (elements.size() - 1)) {
                pathElementModel.moveDown(row);
            }
        }

        int tableSize = elements.size();
        if (!active) {
            if (tableSize < 1) {
                modifyElement.setEnabled(false);
                removeElement.setEnabled(false);
            } else if (tableSize > 0) {
                modifyElement.setEnabled(true);
                removeElement.setEnabled(true);
            }
        }

        // update the move up/down buttons
        valueChanged(null);
    } // actionPerformed

    /**
     * Present a dialog to the user and allow the given value to be
     * modified.
     *
     * @param  event  event object with which to get main window.
     * @param  value  path element to edit.
     * @return  edited path element.
     */
    protected String editPathElement(Object event, String value) {
        Frame window = SessionFrameMapper.getOwningFrame(event);

        // get class and line number from user
        Object[] messages = {
            Bundle.getString("PathBuilder.elementField"),
            new JTextField(value, 30)
        };

        int response = JOptionPane.showOptionDialog(
            window, messages, Bundle.getString("PathBuilder.modifyTitle"),
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, null, null);
        if (response == JOptionPane.OK_OPTION) {
            value = ((JTextField) messages[1]).getText();
        }

        return value;
    } // editPathElement

    /**
     * Get the last directory that the user visited in the file browser.
     *
     * @return  the last directory; may be null.
     */
    public String getLastDirectorySeen() {
        return lastSeenDirectory;
    } // getLastDirectorySeen

    /**
     * Returns the path built using this PathBuilder as a single String,
     * with the elements of the path separated by File.pathSeparator.
     *
     * @return  the path built using this PathBuilder.
     */
    public String getPath() {
        StringBuffer sb = new StringBuffer();
        if (elements.size() > 0) {
            sb.append((String) elements.elementAt(0));
        }
        for (int i = 1; i < elements.size(); i++) {
            sb.append(File.pathSeparator);
            sb.append((String) elements.elementAt(i));
        }
        return sb.toString();
    } // getPath

    /**
     * Returns the path built using this PathBuilder as an array of
     * Strings.
     *
     * @return the path built using this PathBuilder.
     */
    public String[] getPathArray() {
        String[] pathArray = new String[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            pathArray[i] = (String) elements.elementAt(i);
        }
        return pathArray;
    } // getPathArray

    /**
     * Set the text of the add element button.
     *
     * @param  text  the String to display on the add element button.
     */
    public void setAddButtonText(String text) {
        addElement.setText(text);
    } // setAddButtonText

    /**
     * Set a filter to customise what files are displayed.
     *
     * @param  filter  the filter to use.
     */
    public void setFileFilter(FileFilter filter) {
        this.filter = filter;
    } // setFileFilter

    /**
     * Enable or disable the move buttons.
     *
     * @param  enabled  true to enable the move up and move down buttons,
     *                  false to disable them.
     */
    public void setMoveButtonsEnabled(boolean enabled) {
        moveButtonsEnabled = enabled;
        moveUp.setEnabled(enabled);
        moveDown.setEnabled(enabled);
    } // setMoveButtonsEnabled

    /**
     * Set the text of the move up button.
     *
     * @param  text  the String to display on the move up button.
     */
    public void setMoveUpButtonText(String text) {
        moveUp.setText(text);
    } // setMoveUpButtonText

    /**
     * Set the text of the move down button.
     *
     * @param  text  the String to display on the move down button.
     */
    public void setMoveDownButtonText(String text) {
        moveDown.setText(text);
    } // setMoveDownButtonText

    /**
     * Enable or disable multiple file selection in the file chooser.
     *
     * @param  multiSelectionEnabled  true to enable multiple file selection,
     *                                false to disable it.
     */
    public void setMultiSelectionEnabled(boolean multiSelectionEnabled) {
        this.multiSelectionEnabled = multiSelectionEnabled;
    } // setMultiSelectionEnabled

    /**
     * Set the path to be displayed in the list box.
     *
     * @param  path  path elements delineated by path separators.
     */
    public void setPath(String path) {
        // This method is provided for the jEdit plugin.
        StringTokenizer st = new StringTokenizer(path, File.pathSeparator);

        // First clear out the model.
        int size = elements.size();
        elements.clear();
        if (size > 0) {
            pathElementModel.fireTableRowsDeleted(0, size - 1);
        }

        // Populate the model with the new data.
        size = st.countTokens();
        for (int ii = 0; ii < size; ii++) {
            String entry = st.nextToken();
            elements.addElement(entry);
        }

        // Fire off the model change events.
        if (elements.size() > 0) {
            pathElementModel.fireTableRowsInserted(0, elements.size() - 1);
            pathElementTable.setRowSelectionInterval(0, 0);
        }
    } // setPath

    /**
     * Set the path to be displayed in the list box.
     *
     * @param  path  an array of the current path elements.
     */
    public void setPath(String[] path) {
        int size = elements.size();
        elements.clear();
        if (size > 0) {
            pathElementModel.fireTableRowsDeleted(0, size - 1);
        }

        for (int i = 0; i < path.length; i++) {
            elements.addElement(path[i]);
        }
        if (elements.size() > 0) {
            pathElementModel.fireTableRowsInserted(0, elements.size() - 1);
            pathElementTable.setRowSelectionInterval(0, 0);
        }
    } // setPath

    /**
     * Set the text of the remove element button.
     *
     * @param  text  the String to display on the remove element button.
     */
    public void setRemoveButtonText(String text) {
        removeElement.setText(text);
    } // setRemoveButtonText

    /**
     * Sets the initial directory to be displayed by the file dialog.
     *
     * @param  startDirectory  the initial directory to be displayed by the
     *                         file dialog.
     */
    public void setStartDirectory(String startDirectory) {
        this.startDirectory = startDirectory;
    } // setStartDirectory

    /**
     * Handle list selection events.
     *
     * @param  e  the list selection event.
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e == null || !e.getValueIsAdjusting() && !active) {
            int row = pathElementTable.getSelectedRow();
            int tableSize = elements.size();

            if (tableSize < 2) {
                moveUp.setEnabled(false);
                moveDown.setEnabled(false);
                return;
            }

            if (row < 1) {
                moveUp.setEnabled(false);
                if (tableSize > 1 && !moveDown.isEnabled()) {
                    moveDown.setEnabled(true);
                }
            } else if (row == (tableSize - 1)) {
                moveDown.setEnabled(false);
                if (!moveUp.isEnabled()) {
                    moveUp.setEnabled(true);
                }
            } else {
                moveUp.setEnabled(true);
                moveDown.setEnabled(true);
            }
        }
    } // valueChanged

    /**
     * A simple table model of the pathElementTable.
     */
    class PathElementTableModel extends AbstractTableModel {
        /** silence the compiler warnings */
        private static final long serialVersionUID = 1L;

        /**
         * Add an element to the path model.
         *
         * @param  value  the path element to be added.
         */
        protected void add(String value) {
            int rows = elements.size();
            elements.addElement(value);
            fireTableRowsInserted(rows, rows);
        } // add

        /**
         * Returns the column count.
         *
         * @return  column count.
         */
        public int getColumnCount() {
            return 1;
        } // getColumnCount

        /**
         * Returns the name of the nth column.
         *
         * @param  column  index of desired column.
         * @return  name of the column.
         */
        public String getColumnName(int column) {
            return Bundle.getString("PathBuilder.elements");
        } // getColumnName

        /**
         * Returns the row count.
         *
         * @return  row count.
         */
        public int getRowCount() {
            return elements.size();
        } // getRowCount

        /**
         * Retrieves the value at the specified cell.
         *
         * @param  row     row of cell.
         * @param  column  column of cell.
         * @return  value in cell.
         */
        public Object getValueAt(int row, int column) {
            return elements.elementAt(row);
        } // getValueAt

        /**
         * Returns true if the cell at <code>rowIndex</code> and
         * <code>columnIndex</code> is editable.
         *
         * @param  rowIndex     the row whose value to be queried
         * @param  columnIndex  the column whose value to be queried
         * @return  true if the cell is editable
         */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        } // isCellEditable

        /**
         * Move an element up (towards the front of) the path.
         *
         * @param  row  the element to be moved.
         */
        protected void moveUp(int row) {
            Object a = elements.elementAt(row);
            Object b = elements.elementAt(row - 1);
            elements.setElementAt(a, row - 1);
            elements.setElementAt(b, row);
            fireTableRowsUpdated(row - 1, row);
            pathElementTable.setRowSelectionInterval(row - 1, row - 1);
        } // moveUp

        /**
         * Move an element down (towards the end of) the path.
         *
         * @param  row  the element to be moved.
         */
        protected void moveDown(int row) {
            Object a = elements.elementAt(row);
            Object b = elements.elementAt(row + 1);
            elements.setElementAt(a, row + 1);
            elements.setElementAt(b, row);
            fireTableRowsUpdated(row, row + 1);
            pathElementTable.setRowSelectionInterval(row + 1, row + 1);
        } // moveDown

        /**
         * Remove an element from the path model.
         *
         * @param row the index of the element to remove.
         */
        protected void remove(int row) {
            elements.removeElementAt(row);
            fireTableRowsDeleted(row, row);
            if (elements.size() > 0) {
                if (elements.size() > row) {
                    pathElementTable.setRowSelectionInterval(row, row);
                } else {
                    row = elements.size() - 1;
                    pathElementTable.setRowSelectionInterval(row, row);
                }
            }
        } // remove

        /**
         * Sets a value for the record in the cell at 'col' and 'row'.
         * The 'val' is the new value. This notifies model listeners
         * that the cell data has changed.
         *
         * @param  val  the new value
         * @param  row  the row whose value is to be changed
         * @param  col  the column whose value is to be changed
         */
        public void setValueAt(Object val, int row, int col) {
            if (row < 0 || row >= elements.size()) {
                throw new ArrayIndexOutOfBoundsException(row);
            }
            elements.setElementAt(val, row);
            fireTableCellUpdated(row, col);
        } // setValueAt
    } // PathElementTableModel

    /**
     * Class ClasspathFilter implmenets a FileFilter that only accepts
     * directories and files ending in .jar or .zip.
     */
    public static class ClasspathFilter extends FileFilter {

        /**
         * Test if the given file or directory is acceptable.
         *
         * @param  f  file to consider.
         * @return  true if file is acceptable.
         */
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String fname = f.getName();
            int fnameLength = fname.length();
            if (fnameLength < 5) {
                return false;
            }

            String ext = fname.substring(fnameLength - 4, fnameLength);
            if (ext.equalsIgnoreCase(".jar")
                || ext.equalsIgnoreCase(".zip")) {
                return true;
            }
            return false;
        } // accept

        /**
         * Returns the description of this file filter.
         *
         * @return  String description of this filter.
         */
        public String getDescription() {
            return "Classpath elements";
        } // getDescription
    } // ClasspathFilter

    /**
     * Class SourcepathFilter implmenets a FileFilter that only accepts
     * directories.
     */
    public static class SourcepathFilter extends FileFilter {

        /**
         * Test if the given file or directory is acceptable.
         *
         * @param  f  file to consider.
         * @return  true if file is acceptable.
         */
        public boolean accept(File f) {
            String fname = f.getName().toLowerCase();
            if (f.isDirectory() || fname.endsWith(".zip")
                || fname.endsWith(".jar")) {
                return true;
            } else {
                return false;
            }
        } // accept

        /**
         * Returns the description of this file filter.
         *
         * @return  String description of this filter.
         */
        public String getDescription() {
            return "Sourcepath elements";
        } // getDescription
    } // SourcepathFilter

    /**
     * Class PathTable extends JTable in order to provide tooltips.
     */
    class PathTable extends JTable {
        /** silence the compiler warnings */
        private static final long serialVersionUID = 1L;

        /**
         * Constructs a table for the given model.
         *
         * @param  model  table model.
         */
        public PathTable(TableModel model) {
            super(model);
        } // PathTable

        /**
         * Returns custom tooltip text for this tree.
         *
         * @param  me  Mouse event.
         * @return  Custom tooltip text.
         */
        public String getToolTipText(MouseEvent me) {
            int row = rowAtPoint(me.getPoint());
            int col = columnAtPoint(me.getPoint());
            PathElementTableModel model = (PathElementTableModel) getModel();
            String value = (String) model.getValueAt(row, col);
            if (value == null || value.length() == 0) {
                return null;
            } else {
                return value;
            }
        } // getToolTipText
    } // PathTable
} // PathBuilder
