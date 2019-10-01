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

import com.alee.extended.breadcrumb.element.BreadcrumbElementUtils;
import com.alee.laf.tabbedpane.Tab;

import javax.swing.*;
import java.util.List;

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

    /**
     * Used to provide component enabled state.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String enabled = "enabled";

    /**
     * Used to provide component disabled state.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String disabled = "disabled";

    /**
     * Used to provide LTR component orientation type state.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String leftToRight = "ltr";

    /**
     * Used to provide RTL component orientation type state.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String rightToLeft = "rtl";

    /**
     * Used to provide component focused state.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     * @see com.alee.laf.list.WebListCellRenderer#getStates()
     * @see com.alee.laf.table.renderers.WebTableCellRenderer#getStates()
     * @see com.alee.laf.table.renderers.WebTableBooleanCellRenderer#getStates()
     * @see com.alee.laf.tree.WebTreeCellRenderer#getStates()
     * @see com.alee.extended.tree.WebCheckBoxTreeCellRenderer#getStates()
     */
    public static final String focused = "focused";

    /**
     * Used to provide component parent focused state.
     * Note that this state might heavily affect UI performance if used incorrectly.
     * Check {@link AbstractDecorationPainter#updateInFocusedParent()} for usage tips.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String inFocusedParent = "in-focused-parent";

    /**
     * Used to provide component focused state.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     * @see com.alee.laf.list.WebListCellRenderer#getStates()
     * @see com.alee.laf.list.ListItemPainter#getDecorationStates()
     * @see com.alee.laf.tree.WebTreeCellRenderer#getStates()
     * @see com.alee.laf.tree.TreeRowPainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeNodePainter#getDecorationStates()
     */
    public static final String hover = "hover";

    /**
     * Used to provide component parent focused state.
     * Note that this state might heavily affect UI performance if used incorrectly.
     * Check {@link AbstractDecorationPainter#updateInHoveredParent()} for usage tips.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String inHoveredParent = "in-hovered-parent";

    /**
     * Used to provide component children existence state.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String hasChildren = "has-children";

    /**
     * Used to provide component children existence state.
     *
     * @see AbstractDecorationPainter#getDecorationStates()
     */
    public static final String hasNoChildren = "has-no-children";

    /**
     * Used to provide component pressed state.
     *
     * @see com.alee.laf.button.AbstractButtonPainter#getDecorationStates()
     * @see com.alee.laf.combobox.WebComboBoxRenderer#getStates()
     */
    public static final String pressed = "pressed";

    /**
     * Used to provide component selected state.
     *
     * @see com.alee.laf.button.AbstractButtonPainter#getDecorationStates()
     * @see com.alee.laf.list.WebListCellRenderer#getStates()
     * @see com.alee.laf.list.ListItemPainter#getDecorationStates()
     * @see com.alee.laf.table.TableRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableColumnPainter#getDecorationStates()
     * @see com.alee.laf.table.TableCellPainter#getDecorationStates()
     * @see com.alee.laf.table.renderers.WebTableCellRenderer#getStates()
     * @see com.alee.laf.table.renderers.WebTableBooleanCellRenderer#getStates()
     * @see com.alee.laf.tree.TreeRowPainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeNodePainter#getDecorationStates()
     * @see com.alee.laf.tree.WebTreeCellRenderer#getStates()
     * @see com.alee.extended.tree.WebCheckBoxTreeCellRenderer#getStates()
     * @see Tab#getStates()
     */
    public static final String selected = "selected";

    /**
     * Used to provide component unselected state.
     *
     * @see com.alee.laf.button.AbstractButtonPainter#getDecorationStates()
     * @see com.alee.laf.list.WebListCellRenderer#getStates()
     * @see com.alee.laf.list.ListItemPainter#getDecorationStates()
     * @see com.alee.laf.table.TableRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableColumnPainter#getDecorationStates()
     * @see com.alee.laf.table.TableCellPainter#getDecorationStates()
     * @see com.alee.laf.table.renderers.WebTableCellRenderer#getStates()
     * @see com.alee.laf.table.renderers.WebTableBooleanCellRenderer#getStates()
     * @see com.alee.laf.tree.TreeRowPainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeNodePainter#getDecorationStates()
     * @see com.alee.laf.tree.WebTreeCellRenderer#getStates()
     * @see com.alee.extended.tree.WebCheckBoxTreeCellRenderer#getStates()
     */
    public static final String unselected = "unselected";

    /**
     * Used to provide component checked state.
     *
     * @see com.alee.laf.checkbox.CheckBoxPainter#getDecorationStates()
     * @see com.alee.extended.checkbox.TristateCheckBoxPainter#getDecorationStates()
     * @see com.alee.laf.radiobutton.RadioButtonPainter#getDecorationStates()
     * @see com.alee.laf.menu.AbstractStateMenuItemPainter#getDecorationStates()
     */
    public static final String checked = "checked";

    /**
     * Used to provide component mixed state.
     *
     * @see com.alee.extended.checkbox.TristateCheckBoxPainter#getDecorationStates()
     */
    public static final String mixed = "mixed";

    /**
     * Used to provide component empty state.
     *
     * @see com.alee.laf.text.AbstractTextEditorPainter#getDecorationStates()
     */
    public static final String empty = "empty";

    /**
     * Used to provide state for components that might have an icon.
     *
     * @see com.alee.extended.collapsible.AbstractTitleLabel#getStates()
     */
    public static final String hasIcon = "has-icon";

    /**
     * Used to provide component collapsing state.
     *
     * @see com.alee.extended.collapsible.CollapsiblePanePainter#getDecorationStates()
     * @see com.alee.extended.collapsible.AbstractTitleLabel#getStates()
     * @see com.alee.extended.collapsible.AbstractHeaderPanel#getStates()
     */
    public static final String collapsing = "collapsing";

    /**
     * Used to provide component collapsed state.
     *
     * @see com.alee.laf.combobox.WebComboBoxRenderer#getStates()
     * @see com.alee.laf.combobox.ComboBoxPainter#getDecorationStates()
     * @see com.alee.laf.combobox.WebComboBoxUI.ComboBoxButton#getStates()
     * @see com.alee.laf.combobox.WebComboBoxUI.ComboBoxSeparator#getStates()
     * @see com.alee.laf.tree.WebTreeCellRenderer#getStates()
     * @see com.alee.laf.tree.TreeRowPainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeNodePainter#getDecorationStates()
     * @see com.alee.extended.tree.WebCheckBoxTreeCellRenderer#getStates()
     * @see com.alee.extended.collapsible.CollapsiblePanePainter#getDecorationStates()
     * @see com.alee.extended.collapsible.AbstractTitleLabel#getStates()
     * @see com.alee.extended.collapsible.AbstractHeaderPanel#getStates()
     */
    public static final String collapsed = "collapsed";

    /**
     * Used to provide component expanding state.
     *
     * @see com.alee.extended.collapsible.CollapsiblePanePainter#getDecorationStates()
     * @see com.alee.extended.collapsible.AbstractTitleLabel#getStates()
     * @see com.alee.extended.collapsible.AbstractHeaderPanel#getStates()
     */
    public static final String expanding = "expanding";

    /**
     * Used to provide component expanded state.
     *
     * @see com.alee.laf.combobox.WebComboBoxRenderer#getStates()
     * @see com.alee.laf.combobox.ComboBoxPainter#getDecorationStates()
     * @see com.alee.laf.combobox.WebComboBoxUI.ComboBoxButton#getStates()
     * @see com.alee.laf.combobox.WebComboBoxUI.ComboBoxSeparator#getStates()
     * @see com.alee.laf.tree.WebTreeCellRenderer#getStates()
     * @see com.alee.laf.tree.TreeRowPainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeNodePainter#getDecorationStates()
     * @see com.alee.extended.tree.WebCheckBoxTreeCellRenderer#getStates()
     * @see com.alee.extended.collapsible.CollapsiblePanePainter#getDecorationStates()
     * @see com.alee.extended.collapsible.AbstractTitleLabel#getStates()
     * @see com.alee.extended.collapsible.AbstractHeaderPanel#getStates()
     */
    public static final String expanded = "expanded";

    /**
     * Used to provide drop on state.
     *
     * @see com.alee.laf.tree.TreeDropLocationPainter#getDecorationStates()
     */
    public static final String dropOn = "drop-on";

    /**
     * Used to provide drop between state.
     *
     * @see com.alee.laf.tree.TreeDropLocationPainter#getDecorationStates()
     */
    public static final String dropBetween = "drop-between";

    /**
     * Used to provide component editable state.
     *
     * @see com.alee.laf.combobox.WebComboBoxUI.ComboBoxSeparator#getStates()
     * @see com.alee.laf.combobox.WebComboBoxUI.ComboBoxButton#getStates()
     * @see com.alee.laf.combobox.ComboBoxPainter#getDecorationStates()
     * @see com.alee.laf.text.AbstractTextEditorPainter#getDecorationStates()
     */
    public static final String editable = "editable";

    /**
     * Used to provide toolbar attached state for toolbar painter.
     *
     * @see com.alee.laf.toolbar.ToolBarPainter#getDecorationStates()
     */
    public static final String attached = "attached";

    /**
     * Used to provide toolbar floating state for toolbar painter.
     *
     * @see com.alee.laf.toolbar.ToolBarPainter#getDecorationStates()
     */
    public static final String floating = "floating";

    /**
     * Used to provide component horizontal orientation state.
     *
     * @see com.alee.laf.separator.AbstractSeparatorPainter#getDecorationStates()
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     * @see com.alee.laf.scroll.WebScrollBarUI.ScrollBarButton#getStates()
     * @see com.alee.laf.splitpane.SplitPaneDividerPainter#getDecorationStates()
     * @see com.alee.laf.splitpane.SplitPanePainter#getDecorationStates()
     * @see com.alee.laf.splitpane.WebSplitPaneDivider.OneTouchButton#getStates()
     * @see com.alee.laf.splitpane.WSplitPaneUI.NonContinuousDivider#getStates()
     * @see com.alee.extended.split.MultiSplitPaneDividerPainter#getDecorationStates()
     */
    public static final String horizontal = "horizontal";

    /**
     * Used to provide component vertical orientation state.
     *
     * @see com.alee.laf.separator.AbstractSeparatorPainter#getDecorationStates()
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     * @see com.alee.laf.scroll.WebScrollBarUI.ScrollBarButton#getStates()
     * @see com.alee.laf.splitpane.WSplitPaneUI.NonContinuousDivider#getStates()
     * @see com.alee.laf.splitpane.SplitPanePainter#getDecorationStates()
     * @see com.alee.laf.splitpane.WebSplitPaneDivider.OneTouchButton#getStates()
     * @see com.alee.laf.splitpane.SplitPaneDividerPainter#getDecorationStates()
     * @see com.alee.extended.split.MultiSplitPaneDividerPainter#getDecorationStates()
     */
    public static final String vertical = "vertical";

    /**
     * Used to provide split pane one-touch buttons visibility state.
     *
     * @see com.alee.laf.splitpane.WSplitPaneUI.NonContinuousDivider#getStates()
     * @see com.alee.laf.splitpane.SplitPanePainter#getDecorationStates()
     * @see com.alee.laf.splitpane.WebSplitPaneDivider.OneTouchButton#getStates()
     * @see com.alee.laf.splitpane.SplitPaneDividerPainter#getDecorationStates()
     * @see com.alee.extended.split.MultiSplitPaneDividerPainter#getDecorationStates()
     */
    public static final String oneTouch = "one-touch";

    /**
     * Used to provide split pane layout type state.
     *
     * @see com.alee.extended.split.MultiSplitPaneDividerPainter#getDecorationStates()
     */
    public static final String continuous = "continuous";

    /**
     * Used to provide split pane layout type state.
     *
     * @see com.alee.extended.split.MultiSplitPaneDividerPainter#getDecorationStates()
     */
    public static final String nonContinuous = "non-continuous";

    /**
     * Used to provide dragged state.
     *
     * @see com.alee.extended.split.MultiSplitPaneDividerPainter#getDecorationStates()
     */
    public static final String dragged = "dragged";

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
     * @see BreadcrumbElementUtils#addBreadcrumbElementStates(JComponent, List)
     */
    public static final String progress = "progress";

    /**
     * Used to provide progress bar type for progress bar painter.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     * @see BreadcrumbElementUtils#addBreadcrumbElementStates(JComponent, List)
     */
    public static final String indeterminate = "indeterminate";

    /**
     * Used to provide indication of component having minimum current value.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     * @see BreadcrumbElementUtils#addBreadcrumbElementStates(JComponent, List)
     */
    public static final String minimum = "minimum";

    /**
     * Used to provide indication of component having non-minimum and non-maximum current value.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     * @see BreadcrumbElementUtils#addBreadcrumbElementStates(JComponent, List)
     */
    public static final String intermediate = "intermediate";

    /**
     * Used to provide indication of component having maximum current value.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     * @see BreadcrumbElementUtils#addBreadcrumbElementStates(JComponent, List)
     */
    public static final String maximum = "maximum";

    /**
     * Used to provide unvisited state for link-type components.
     *
     * @see com.alee.extended.link.LinkPainter#getDecorationStates()
     */
    public static final String unvisited = "unvisited";

    /**
     * Used to provide visited state for link-type components.
     *
     * @see com.alee.extended.link.LinkPainter#getDecorationStates()
     */
    public static final String visited = "visited";

    /**
     * Used to provide tree node leaf state.
     *
     * @see com.alee.laf.tree.WebTreeCellRenderer#getStates()
     * @see com.alee.extended.tree.WebCheckBoxTreeCellRenderer#getStates()
     */
    public static final String leaf = "leaf";

    /**
     * Start position state.
     *
     * @see com.alee.extended.split.MultiSplitPaneDividerPainter#getDecorationStates()
     */
    public static final String start = "start";

    /**
     * End position state.
     *
     * @see com.alee.extended.split.MultiSplitPaneDividerPainter#getDecorationStates()
     */
    public static final String end = "end";

    /**
     * Even position state.
     *
     * @see com.alee.laf.list.ListItemPainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeRowPainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeNodePainter#getDecorationStates()
     * @see com.alee.laf.tree.GroupedTreeRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableColumnPainter#getDecorationStates()
     */
    public static final String even = "even";

    /**
     * Odd position state.
     *
     * @see com.alee.laf.list.ListItemPainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeRowPainter#getDecorationStates()
     * @see com.alee.laf.tree.TreeNodePainter#getDecorationStates()
     * @see com.alee.laf.tree.GroupedTreeRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableRowPainter#getDecorationStates()
     * @see com.alee.laf.table.TableColumnPainter#getDecorationStates()
     */
    public static final String odd = "odd";

    /**
     * Even position state.
     *
     * @see com.alee.laf.tree.GroupedTreeRowPainter#getDecorationStates()
     */
    public static final String innerEven = "inner-even";

    /**
     * Odd position state.
     *
     * @see com.alee.laf.tree.GroupedTreeRowPainter#getDecorationStates()
     */
    public static final String innerOdd = "inner-odd";

    /**
     * First element in the group.
     * Only applicable if there are at least two or more elements available in the group.
     *
     * @see BreadcrumbElementUtils#addBreadcrumbElementStates(JComponent, List)
     */
    public static final String first = "first";

    /**
     * Middle element in the group.
     * Only exists if there are at least three or more elements available in the group.
     *
     * @see BreadcrumbElementUtils#addBreadcrumbElementStates(JComponent, List)
     */
    public static final String middle = "middle";

    /**
     * Last element in the group.
     * Only exists if there are at least two or more elements available in the group.
     *
     * @see BreadcrumbElementUtils#addBreadcrumbElementStates(JComponent, List)
     */
    public static final String last = "last";

    /**
     * Single element in the group.
     * Special type used for case when element is alone in the breadcrumb in the group.
     *
     * @see BreadcrumbElementUtils#addBreadcrumbElementStates(JComponent, List)
     */
    public static final String single = "single";

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

    /**
     * Visible renderer checkbox.
     * Set for {@link com.alee.extended.tree.WebCheckBoxTreeCellRenderer} that renders visible checkbox.
     *
     * @see com.alee.extended.tree.WebCheckBoxTreeCellRenderer#getStates()
     */
    public static final String checkVisible = "check-visible";

    /**
     * Hidden renderer checkbox.
     * Set for {@link com.alee.extended.tree.WebCheckBoxTreeCellRenderer} that doesn't render checkbox.
     *
     * @see com.alee.extended.tree.WebCheckBoxTreeCellRenderer#getStates()
     */
    public static final String checkHidden = "check-hidden";

    /**
     * Visible popup component.
     *
     * @see com.alee.extended.button.SplitButtonPainter#getDecorationStates()
     */
    public static final String popupVisible = "popup-visible";

    /**
     * Hidden popup component.
     *
     * @see com.alee.extended.button.SplitButtonPainter#getDecorationStates()
     */
    public static final String popupHidden = "popup-hidden";
}