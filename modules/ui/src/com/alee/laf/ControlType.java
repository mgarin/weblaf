/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.laf;

/**
 * This enumeration represents existing UI control types.
 *
 * @author Mikle Garin
 */
public enum ControlType
{
    /**
     * todo 1. Replace with specific control types usage?
     * todo    This is generally a simplification for available types for some use cases, but it is not fully correct.
     */

    /**
     * @see com.alee.extended.canvas.WebCanvas
     * @see com.alee.extended.image.WebImage
     * @see javax.swing.JButton
     * @see com.alee.extended.button.WebSplitButton
     * @see javax.swing.JToggleButton
     * @see javax.swing.JCheckBox
     * @see com.alee.extended.checkbox.WebTristateCheckBox
     * @see javax.swing.JRadioButton
     * @see javax.swing.JComboBox
     * @see javax.swing.JSpinner
     * @see javax.swing.JTextField
     * @see javax.swing.JFormattedTextField
     * @see javax.swing.JPasswordField
     * @see javax.swing.JColorChooser
     * @see javax.swing.JFileChooser
     * @see javax.swing.JLabel
     * @see com.alee.extended.label.WebStyledLabel
     * @see com.alee.extended.link.WebLink
     * @see javax.swing.JList
     * @see javax.swing.JPanel
     * @see com.alee.extended.window.WebPopup
     * @see javax.swing.JProgressBar
     * @see javax.swing.JScrollPane
     * @see javax.swing.JViewport
     * @see javax.swing.JSlider
     * @see javax.swing.JTabbedPane
     * @see javax.swing.JTable
     * @see javax.swing.table.JTableHeader
     * @see javax.swing.border.TitledBorder
     * @see javax.swing.JTree
     * todo @see com.alee.extended.button.WebSwitch
     * todo @see com.alee.extended.date.WebDateField
     * todo @see com.alee.extended.filechooser.WebFileChooserField
     * todo @see com.alee.extended.colorchooser.WebColorChooserField
     * todo @see com.alee.extended.dock.WebDockablePane
     * todo @see com.alee.extended.dock.WebDockableFrame
     */
    CONTROL,

    /**
     * @see javax.swing.JTextArea
     * @see javax.swing.JEditorPane
     * @see javax.swing.JTextPane
     * todo @see com.alee.extended.syntax.WebSyntaxArea
     */
    TEXT,

    /**
     * @see javax.swing.JToolTip
     * todo @see com.alee.managers.tooltip.WebCustomTooltip
     */
    TOOLTIP,

    /**
     * @see javax.swing.JPopupMenu
     * @see javax.swing.JMenuBar
     * @see javax.swing.JMenu
     * @see javax.swing.JMenuItem
     * @see javax.swing.JRadioButtonMenuItem
     * @see javax.swing.JCheckBoxMenuItem
     * @see javax.swing.JToolBar
     * todo @see javax.swing.JPopupMenu.Separator
     * todo @see com.alee.extended.menu.WebDynamicMenu
     */
    MENU,

    /**
     * @see javax.swing.JMenu
     * @see javax.swing.JMenuItem
     * @see javax.swing.JRadioButtonMenuItem
     * @see javax.swing.JCheckBoxMenuItem
     */
    MENU_SMALL,

    /**
     * @see javax.swing.JInternalFrame
     * todo @see javax.swing.JInternalFrame.JDesktopIcon -> title
     * todo @see javax.swing.JRootPane -> title
     */
    WINDOW,

    /**
     * @see javax.swing.JOptionPane
     * todo @see com.alee.managers.notification.WebNotification
     * todo @see com.alee.managers.notification.WebInnerNotification
     * todo @see com.alee.extended.optionpane.WebExtendedOptionPane
     */
    MESSAGE
}