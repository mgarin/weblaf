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

package com.alee.painter.decoration;

/**
 * Base decoration states used by WebLaF decoration painters.
 *
 * @author Mikle Garin
 * @see com.alee.painter.decoration.AbstractDecorationPainter
 * @see com.alee.painter.decoration.AbstractDecorationPainter#getDecorationStates()
 * @see com.alee.painter.decoration.IDecoration
 */

public interface DecorationState
{
    /**
     * todo 1. With negation syntax addition [ #387 ] remove some unnecessary states?
     */

    public static final String enabled = "enabled";
    public static final String disabled = "disabled";

    /**
     * Used to provide LTR component orientation type.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String leftToRight = "ltr";

    /**
     * Used to provide RTL component orientation type.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String rightToLeft = "rtl";


    public static final String focused = "focused";
    public static final String hover = "hover";
    public static final String pressed = "pressed";
    public static final String selected = "selected";
    public static final String empty = "empty";
    public static final String collapsed = "collapsed";
    public static final String expanded = "expanded";
    public static final String dragged = "dragged";
    public static final String checked = "checked";
    public static final String mixed = "mixed";
    public static final String dropOn = "drop-on";
    public static final String dropBetween = "drop-between";

    /**
     * Used to provide toolbar floating state for toolbar painter.
     *
     * @see com.alee.laf.toolbar.ToolBarPainter#getDecorationStates()
     */
    public static final String floating = "floating";

    /**
     * Used to provide component horizontal orientation state.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     */
    public static final String horizontal = "horizontal";

    /**
     * Used to provide component vertical orientation state.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     */
    public static final String vertical = "vertical";

    /**
     * Used to provide iconified state for root pane painter.
     * This state identifies that window is in iconified mode right now.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String iconified = "iconified";

    /**
     * Used to provide maximized state for root pane and internal frame painter.
     * This state identifies that window is in maximized mode right now.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     * @see com.alee.laf.desktoppane.InternalFramePainter#getDecorationStates()
     */
    public static final String maximized = "maximized";

    /**
     * Used to provide fullscreen state for root pane painter.
     * This state identifies that window is in fullscreen mode right now.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String fullscreen = "fullscreen";

    /**
     * Used to provide progress bar type for progress bar painter.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     */
    public static final String indeterminate = "indeterminate";

    /**
     * Used to provide tree node leaf state.
     *
     * @see com.alee.laf.tree.WebTreeCellRenderer#getStates()
     */
    public static final String leaf = "leaf";

    /**
     * Even position state.
     *
     * @see com.alee.laf.tree.TreeNodePainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableColumnPainter#getDecorationStates()
     */
    public static final String even = "even";

    /**
     * Odd position state.
     *
     * @see com.alee.laf.tree.TreeNodePainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableColumnPainter#getDecorationStates()
     */
    public static final String odd = "odd";

    /**
     * Natively decorated window.
     * Set for all windows using native decoration.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String nativeWindow = "native-window";

    /**
     * Custom-decorated frame.
     * Set for {@link javax.swing.JFrame} used with custom decoration.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String frame = "frame";

    /**
     * Custom-decorated dialog.
     * Set for {@link javax.swing.JDialog} used with custom decoration.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String dialog = "dialog";

    /**
     * Custom-decorated color chooser dialog.
     * Set for dialogs used for {@link javax.swing.JColorChooser} component.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String colorchooserDialog = "colorchooser-dialog";

    /**
     * Custom-decorated file chooser dialog.
     * Set for dialogs used for {@link javax.swing.JFileChooser} component.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String filechooserDialog = "filechooser-dialog";

    /**
     * Custom-decorated information dialog.
     * Set for dialogs used for information {@link javax.swing.JOptionPane} component.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String informationDialog = "information-dialog";

    /**
     * Custom-decorated error dialog.
     * Set for dialogs used for error {@link javax.swing.JOptionPane} component.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String errorDialog = "error-dialog";

    /**
     * Custom-decorated question dialog.
     * Set for dialogs used for question {@link javax.swing.JOptionPane} component.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String questionDialog = "question-dialog";

    /**
     * Custom-decorated warning dialog.
     * Set for dialogs used for warning {@link javax.swing.JOptionPane} component.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String warningDialog = "warning-dialog";
}