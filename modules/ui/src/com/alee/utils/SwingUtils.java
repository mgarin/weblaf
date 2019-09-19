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

package com.alee.utils;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.collapsible.WebCollapsiblePane;
import com.alee.extended.date.WebCalendar;
import com.alee.extended.date.WebDateField;
import com.alee.extended.filechooser.WebFileChooserField;
import com.alee.extended.pathfield.WebPathField;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.painter.decoration.content.TextRasterization;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.swing.extensions.SizeMethods;
import org.slf4j.LoggerFactory;

import javax.swing.FocusManager;
import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.*;

/**
 * This class provides a set of utilities to work with Swing components, their settings and events.
 *
 * @author Mikle Garin
 */
public final class SwingUtils
{
    /**
     * Client property key that identifies that component can handle enabled state changes.
     */
    @NotNull
    public static final String HANDLES_ENABLE_STATE = "HANDLES_ENABLE_STATE";

    /**
     * Constant transparent color used for text rendering fix.
     */
    @NotNull
    public static final Color RENDERING_FIX_COLOR = new Color ( 231, 157, 94, 0 );

    /**
     * System shortcut modifier.
     */
    @Nullable
    private static Integer systemShortcutModifier = null;

    /**
     * Label for default system font retrieval.
     */
    @Nullable
    private static JLabel label = null;

    /**
     * System font names array.
     */
    @Nullable
    private static String[] fontNames;

    /**
     * System fonts array.
     */
    @Nullable
    private static Font[] fonts;

    /**
     * Private constructor to avoid instantiation.
     */
    private SwingUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns whether or not specified component is opaque.
     *
     * @param component component to check opacity for
     * @return {@code true} if specified component is opaque, {@code false} otherwise
     */
    public static boolean isOpaque ( @NotNull final Component component )
    {
        final boolean opaque;
        if ( component instanceof Window )
        {
            opaque = ProprietaryUtils.isWindowOpaque ( ( Window ) component );
        }
        else
        {
            opaque = component.isOpaque ();
        }
        return opaque;
    }

    /**
     * Performs full component view update.
     *
     * @param component component to update
     */
    public static void update ( final Component component )
    {
        if ( component instanceof JComponent )
        {
            ( ( JComponent ) component ).revalidate ();
        }
        else
        {
            component.invalidate ();
        }
        component.repaint ();
    }

    /**
     * Removes {@link Component} from its current parent if it has one.
     *
     * @param component {@link Component} to remove from its parent
     * @param update    whether or not should update parent {@link Container} layout and size
     */
    public static void removeFromParent ( @Nullable final Component component, final boolean update )
    {
        if ( component != null )
        {
            final Container parent = component.getParent ();
            if ( parent != null )
            {
                parent.remove ( component );
                if ( update )
                {
                    update ( parent );
                }
            }
        }
    }

    /**
     * Returns whether or not specified {@link JTextComponent} content is empty.
     *
     * @param component {@link JTextComponent} to check
     * @return {@code true} if specified {@link JTextComponent} content is empty, {@code false} otherwise
     */
    public static boolean isEmpty ( @NotNull final JTextComponent component )
    {
        final Document document = component.getDocument ();
        return document == null || document.getLength () == 0;
    }

    /**
     * Returns whether or not provided insets are empty.
     * {@code null} insets are considered as empty as well.
     *
     * @param insets insets to process
     * @return true if provided insets are empty, false otherwise
     */
    public static boolean isEmpty ( @Nullable final Insets insets )
    {
        return insets == null || insets.top == 0 && insets.left == 0 && insets.bottom == 0 && insets.right == 0;
    }

    /**
     * Returns whether UI delegate should preserve current border on this component or not.
     *
     * @param component component to process
     * @return true if UI delegate should preserve current border on this component, false otherwise
     */
    public static boolean isPreserveBorders ( @NotNull final JComponent component )
    {
        return getHonorUserBorders ( component ) && !isUIResource ( component.getBorder () );
    }

    /**
     * Returns whether or not specified value is a UI resource.
     *
     * @param value value {@link Object} to process
     * @return {@code true} if specified value is a UI resource, {@code false} otherwise
     */
    public static boolean isUIResource ( @Nullable final Object value )
    {
        return value == null || value instanceof UIResource;
    }

    /**
     * Returns whether UI delegate should honor a user-specified border on this component or not.
     *
     * @param component component to process
     * @return true if UI delegate should honor a user-specified border on this component, false otherwise
     */
    public static boolean getHonorUserBorders ( @NotNull final JComponent component )
    {
        return Boolean.getBoolean ( WebLookAndFeel.PROPERTY_HONOR_USER_BORDERS ) ||
                Boolean.TRUE.equals ( component.getClientProperty ( WebLookAndFeel.PROPERTY_HONOR_USER_BORDER ) );
    }

    /**
     * Sets whether UI delegate should honor a user-specified border on this component or not.
     *
     * @param component component to set property for
     * @param honor     whether UI delegate should honor a user-specified border on this component or not
     */
    public static void setHonorUserBorders ( @NotNull final JComponent component, final boolean honor )
    {
        if ( honor )
        {
            component.putClientProperty ( WebLookAndFeel.PROPERTY_HONOR_USER_BORDER, Boolean.TRUE );
        }
        else
        {
            component.putClientProperty ( WebLookAndFeel.PROPERTY_HONOR_USER_BORDER, null );
        }
    }

    /**
     * Returns whether event involves left mouse button or not.
     *
     * @param e mouse event
     * @return true if event involves left mouse button, false otherwise
     */
    public static boolean isLeftMouseButton ( @NotNull final MouseEvent e )
    {
        return ( e.getModifiers () & InputEvent.BUTTON1_MASK ) != 0;
    }

    /**
     * Returns whether event involves middle mouse button or not.
     *
     * @param e mouse event
     * @return true if event involves middle mouse button, false otherwise
     */
    public static boolean isMiddleMouseButton ( @NotNull final MouseEvent e )
    {
        return ( e.getModifiers () & InputEvent.BUTTON2_MASK ) == InputEvent.BUTTON2_MASK;
    }

    /**
     * Returns whether event involves right mouse button or not.
     *
     * @param e mouse event
     * @return true if event involves right mouse button, false otherwise
     */
    public static boolean isRightMouseButton ( @NotNull final MouseEvent e )
    {
        return ( e.getModifiers () & InputEvent.BUTTON3_MASK ) == InputEvent.BUTTON3_MASK;
    }

    /**
     * Returns whether or not event represents most common double click event.
     *
     * @param e mouse event
     * @return true if event represents most common double click event, false otherwise
     */
    public static boolean isDoubleClick ( @NotNull final MouseEvent e )
    {
        return isDoubleClick ( e, true );
    }

    /**
     * Returns whether or not event represents most common double click event.
     *
     * @param e          mouse event
     * @param repeatable whether or not double click condition can be accepted more than once within a single click sequence
     * @return true if event represents most common double click event, false otherwise
     */
    public static boolean isDoubleClick ( @NotNull final MouseEvent e, final boolean repeatable )
    {
        return isLeftMouseButton ( e ) && ( repeatable ? e.getClickCount () % 2 == 0 : e.getClickCount () == 2 );
    }

    /**
     * Packs all table rows to their preferred height.
     *
     * @param table table to process
     */
    public static void packRowHeights ( @NotNull final JTable table )
    {
        for ( int row = 0; row < table.getRowCount (); row++ )
        {
            int maxHeight = 0;
            for ( int column = 0; column < table.getColumnCount (); column++ )
            {
                final TableCellRenderer cellRenderer = table.getCellRenderer ( row, column );
                final Object valueAt = table.getValueAt ( row, column );
                final Component renderer = cellRenderer.getTableCellRendererComponent ( table, valueAt, false, false, row, column );
                final int heightPreferable = renderer != null ? renderer.getPreferredSize ().height : 0;
                maxHeight = Math.max ( heightPreferable, maxHeight );
            }
            table.setRowHeight ( row, maxHeight );
        }
    }

    /**
     * Packs all table columns to their preferred width.
     *
     * @param table table to process
     */
    public static void packColumnWidths ( @NotNull final JTable table )
    {
        packColumnWidths ( table, 2 );
    }

    /**
     * Packs all table columns to their preferred width.
     *
     * @param table  table to process
     * @param margin column side margin
     */
    public static void packColumnWidths ( @NotNull final JTable table, final int margin )
    {
        for ( int i = 0; i < table.getColumnCount (); i++ )
        {
            packColumnWidth ( table, i, margin );
        }
    }

    /**
     * Packs table column at the specified index to its preferred width.
     *
     * @param table table to process
     * @param col   column index
     */
    public static void packColumnWidth ( @NotNull final JTable table, final int col )
    {
        packColumnWidth ( table, col, 2 );
    }

    /**
     * Packs table column at the specified index to its preferred width.
     *
     * @param table  table to process
     * @param col    column index
     * @param margin column side margin
     */
    public static void packColumnWidth ( @NotNull final JTable table, final int col, final int margin )
    {
        final DefaultTableColumnModel columnModel = ( DefaultTableColumnModel ) table.getColumnModel ();
        final TableColumn column = columnModel.getColumn ( col );
        int width;

        // Header renderer
        TableCellRenderer renderer = column.getHeaderRenderer ();
        if ( renderer == null )
        {
            renderer = table.getTableHeader ().getDefaultRenderer ();
        }

        // Header width
        Component rendererComponent = renderer.getTableCellRendererComponent ( table, column.getHeaderValue (), false, false, 0, 0 );
        width = rendererComponent.getPreferredSize ().width;

        // Cells width
        for ( int r = 0; r < table.getRowCount (); r++ )
        {
            renderer = table.getCellRenderer ( r, col );
            rendererComponent = renderer.getTableCellRendererComponent ( table, table.getValueAt ( r, col ), false, false, r, col );
            width = Math.max ( width, rendererComponent.getPreferredSize ().width );
        }

        // Margin
        width += 2 * margin;

        // Final values
        column.setPreferredWidth ( width );
        column.setWidth ( width );
    }

    /**
     * Returns whether the specific mouse events triggers popup menu or not.
     * This method might act differently on different operating systems.
     *
     * @param e mouse event
     * @return true if the specific mouse events triggers popup menu, false otherwise
     */
    public static boolean isPopupTrigger ( @NotNull final MouseEvent e )
    {
        return e.isPopupTrigger () || SwingUtilities.isRightMouseButton ( e );
    }

    /**
     * Destroys container by destroying its children structure and removing all listeners.
     *
     * @param container container to destroy
     */
    public static void destroyContainer ( @NotNull final Container container )
    {
        for ( final Container toDestroy : collectAllContainers ( container ) )
        {
            toDestroy.removeAll ();
            toDestroy.setLayout ( null );

            for ( final MouseListener listener : toDestroy.getMouseListeners () )
            {
                toDestroy.removeMouseListener ( listener );
            }
            for ( final MouseMotionListener listener : toDestroy.getMouseMotionListeners () )
            {
                toDestroy.removeMouseMotionListener ( listener );
            }
            for ( final MouseWheelListener listener : toDestroy.getMouseWheelListeners () )
            {
                toDestroy.removeMouseWheelListener ( listener );
            }
            for ( final KeyListener listener : toDestroy.getKeyListeners () )
            {
                toDestroy.removeKeyListener ( listener );
            }
            for ( final ComponentListener listener : toDestroy.getComponentListeners () )
            {
                toDestroy.removeComponentListener ( listener );
            }
            for ( final ContainerListener listener : toDestroy.getContainerListeners () )
            {
                toDestroy.removeContainerListener ( listener );
            }
            if ( toDestroy instanceof JComponent )
            {
                final JComponent jComponent = ( JComponent ) toDestroy;
                for ( final AncestorListener listener : jComponent.getAncestorListeners () )
                {
                    jComponent.removeAncestorListener ( listener );
                }
            }
        }
    }

    /**
     * Returns list of all sub-containers for this container.
     *
     * @param container container to process
     * @return list of all sub-containers
     */
    @NotNull
    public static List<Container> collectAllContainers ( @NotNull final Container container )
    {
        return collectAllContainers ( container, new ArrayList<Container> () );
    }

    /**
     * Returns list of all sub-containers for this container.
     *
     * @param container  container to process
     * @param containers list to collect sub-containers into
     * @return list of all sub-containers
     */
    @NotNull
    public static List<Container> collectAllContainers ( @NotNull final Container container, @NotNull final List<Container> containers )
    {
        containers.add ( container );
        for ( final Component component : container.getComponents () )
        {
            if ( component instanceof Container )
            {
                collectAllContainers ( ( Container ) component, containers );
            }
        }
        return containers;
    }

    /**
     * Groups all buttons inside this container and returns created button group.
     *
     * @param container container to process
     * @return created button group
     */
    @NotNull
    public static ButtonGroup groupButtons ( @NotNull final Container container )
    {
        return groupButtons ( container, false );
    }

    /**
     * Groups all buttons inside this container and all sub-containers if requested and returns created button group.
     *
     * @param container container to process
     * @param recursive whether to check all sub-containers or not
     * @return created button group
     */
    @NotNull
    public static ButtonGroup groupButtons ( @NotNull final Container container, final boolean recursive )
    {
        final ButtonGroup buttonGroup = new ButtonGroup ();
        groupButtons ( container, recursive, buttonGroup );
        return buttonGroup;
    }

    /**
     * Groups all buttons inside this container and all sub-containers if requested and returns created button group.
     *
     * @param container   container to process
     * @param recursive   whether to check all sub-containers or not
     * @param buttonGroup button group
     */
    public static void groupButtons ( @NotNull final Container container, final boolean recursive, @NotNull final ButtonGroup buttonGroup )
    {
        for ( final Component component : container.getComponents () )
        {
            if ( component instanceof AbstractButton )
            {
                buttonGroup.add ( ( AbstractButton ) component );
            }
            if ( recursive )
            {
                if ( component instanceof Container )
                {
                    groupButtons ( container, true );
                }
            }
        }
    }

    /**
     * Groups specified buttons and returns created button group.
     *
     * @param buttons buttons to group
     * @return created button group
     */
    @NotNull
    public static ButtonGroup groupButtons ( @NotNull final AbstractButton... buttons )
    {
        final ButtonGroup buttonGroup = new ButtonGroup ();
        groupButtons ( buttonGroup, buttons );
        return buttonGroup;
    }

    /**
     * Groups buttons in the specified button group.
     *
     * @param buttonGroup button group
     * @param buttons     buttons to group
     */
    public static void groupButtons ( @NotNull final ButtonGroup buttonGroup, @NotNull final AbstractButton... buttons )
    {
        for ( final AbstractButton button : buttons )
        {
            buttonGroup.add ( button );
        }
    }

    /**
     * Copies component orientation from one component to another.
     *
     * @param from component to copy orientation from
     * @param to   component to copy orientation into
     */
    public static void copyOrientation ( @NotNull final Component from, @NotNull final Component to )
    {
        final ComponentOrientation fo = from.getComponentOrientation ();
        if ( fo.isLeftToRight () != to.getComponentOrientation ().isLeftToRight () )
        {
            to.applyComponentOrientation ( fo );
        }
    }

    /**
     * Applies opposite component orientation to the specified component and all of its children.
     *
     * @param component component to change orientation for
     */
    public static void changeOrientation ( @NotNull final Component component )
    {
        final boolean ltr = component.getComponentOrientation ().isLeftToRight ();
        component.applyComponentOrientation ( ltr ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT );
    }

    /**
     * Updates component orientation for all existing components.
     */
    public static void updateGlobalOrientation ()
    {
        updateGlobalOrientation ( WebLookAndFeel.getOrientation () );
    }

    /**
     * Sets specified component orientation for all existing components.
     *
     * @param orientation component orientation to set
     */
    public static void updateGlobalOrientation ( @NotNull final ComponentOrientation orientation )
    {
        for ( final Window window : Window.getWindows () )
        {
            // Applying orientation
            window.applyComponentOrientation ( orientation );

            // Updating root pane
            final JRootPane rootPane = CoreSwingUtils.getRootPane ( window );
            if ( rootPane != null )
            {
                update ( rootPane );
            }
            else
            {
                update ( window );
            }
        }
    }

    /**
     * Sets component orientation to specified component.
     *
     * @param component component to modify
     */
    public static void setOrientation ( @NotNull final Component component )
    {
        setOrientation ( component, false );
    }

    /**
     * Sets component orientation to specified component if needed or if forced.
     *
     * @param component component to modify
     * @param forced    force orientation change
     */
    public static void setOrientation ( @NotNull final Component component, final boolean forced )
    {
        final ComponentOrientation orientation = WebLookAndFeel.getOrientation ();
        if ( forced || orientation.isLeftToRight () != component.getComponentOrientation ().isLeftToRight () )
        {
            component.setComponentOrientation ( orientation );
        }
    }

    /**
     * Returns whether specified window is a HeavyWeightWindow or not.
     *
     * @param window window to process
     * @return true if specified window is a HeavyWeightWindow, false otherwise
     */
    public static boolean isHeavyWeightWindow ( @Nullable final Window window )
    {
        final boolean heavyWeight;
        if ( window != null )
        {
            final String can = window.getClass ().getCanonicalName ();
            heavyWeight = can != null && can.endsWith ( "HeavyWeightWindow" );
        }
        else
        {
            heavyWeight = false;
        }
        return heavyWeight;
    }

    /**
     * Returns first parent which is instance of specified class type or null if none found.
     *
     * @param component   component to look parent for
     * @param parentClass parent component class
     * @param <C>         parent component class type
     * @return first parent which is instance of specified class type or null if none found
     */
    @Nullable
    public static <C extends Container> C getFirstParent ( @NotNull final Component component, @NotNull final Class<C> parentClass )
    {
        Component parent = component.getParent ();
        while ( !parentClass.isInstance ( parent ) && parent != null )
        {
            parent = parent.getParent ();
        }
        return ( C ) parent;
    }

    /**
     * Returns first component placed in the specified container which is instance of specified class type or null if none found.
     *
     * @param container      container to look for component in
     * @param componentClass component class
     * @param <C>            component class type
     * @return first component placed in the specified container which is instance of specified class type or null if none found
     */
    @Nullable
    public static <C extends Component> C getFirst ( @NotNull final Container container, @NotNull final Class<C> componentClass )
    {
        return getFirst ( container, componentClass, false );
    }

    /**
     * Returns first component placed in the specified container which is instance of specified class type or null if none found.
     *
     * @param container      container to look for component in
     * @param componentClass component class
     * @param recursive      whether to check all sub-containers or not
     * @param <C>            component class type
     * @return first component placed in the specified container which is instance of specified class type or null if none found
     */
    @Nullable
    public static <C extends Component> C getFirst ( @NotNull final Container container, @NotNull final Class<C> componentClass,
                                                     final boolean recursive )
    {
        C result = null;
        for ( int i = 0; i < container.getComponentCount (); i++ )
        {
            final Component component = container.getComponent ( i );
            if ( componentClass.isInstance ( component ) )
            {
                result = ( C ) component;
                break;
            }
            if ( recursive )
            {
                if ( component instanceof Container )
                {
                    final C another = getFirst ( ( Container ) component, componentClass, true );
                    if ( another != null )
                    {
                        result = another;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns first parent component which supports drag and drop actions.
     *
     * @param component component to look parent supporting drop for
     * @param <C>       parent supporting drop component class type
     * @return first parent component which supports drag and drop actions
     */
    @Nullable
    public static <C extends JComponent> C getFirstParentSupportingDrop ( @NotNull final Component component )
    {
        C result = null;
        final Container parent = component.getParent ();
        if ( parent instanceof JComponent )
        {
            final JComponent c = ( JComponent ) parent;
            if ( c.getTransferHandler () != null )
            {
                result = ( C ) c;
            }
        }
        if ( result == null )
        {
            result = getFirstParentSupportingDrop ( parent );
        }
        return result;
    }

    /**
     * Returns first available visible application window.
     *
     * @return first available visible application window
     */
    @Nullable
    public static Window getAvailableWindow ()
    {
        Window result = null;
        final Window activeWindow = SwingUtils.getActiveWindow ();
        if ( activeWindow != null )
        {
            if ( activeWindow instanceof JFrame || activeWindow instanceof JDialog || activeWindow instanceof JWindow )
            {
                // todo Ignore notification popup windows
                result = activeWindow;
            }
        }
        if ( result == null )
        {
            final Window[] allWindows = Window.getWindows ();
            if ( allWindows != null && allWindows.length > 0 )
            {
                for ( final Window window : allWindows )
                {
                    if ( window.isShowing () )
                    {
                        if ( window instanceof JFrame || window instanceof JDialog || window instanceof JWindow )
                        {
                            // todo Ignore notification popup windows
                            result = window;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns content pane for the specified component or null if it doesn't exist.
     *
     * @param component component to look under
     * @return content pane for the specified component or null if it doesn't exist
     */
    @Nullable
    public static Container getContentPane ( @NotNull final Component component )
    {
        final JRootPane rootPane = CoreSwingUtils.getRootPane ( component );
        return rootPane != null ? rootPane.getContentPane () : null;
    }

    /**
     * Returns layered pane for the specified component or null if it doesn't exist.
     *
     * @param component component to look under
     * @return layered pane for the specified component or null if it doesn't exist
     */
    @Nullable
    public static JLayeredPane getLayeredPane ( @NotNull final Component component )
    {
        final JRootPane rootPane = CoreSwingUtils.getRootPane ( component );
        return rootPane != null ? rootPane.getLayeredPane () : null;
    }

    /**
     * Returns glass pane for the specified component or null if it doesn't exist.
     *
     * @param component component to look under
     * @return glass pane for the specified component or null if it doesn't exist
     */
    @Nullable
    public static Component getGlassPane ( @NotNull final Component component )
    {
        final JRootPane rootPane = CoreSwingUtils.getRootPane ( component );
        return rootPane != null ? rootPane.getGlassPane () : null;
    }

    /**
     * Returns {@code insets} increased by amount specified in {@code amount}.
     *
     * @param insets insets to increased
     * @param amount increase amount
     * @return {@code insets} increased by amount specified in {@code amount}
     */
    @NotNull
    public static Insets increase ( @NotNull final Insets insets, @Nullable final Insets amount )
    {
        if ( amount != null )
        {
            insets.top += amount.top;
            insets.left += amount.left;
            insets.bottom += amount.bottom;
            insets.right += amount.right;
        }
        return insets;
    }

    /**
     * Returns {@code dimension} increased by amount specified in {@code amount}.
     *
     * @param dimension dimension to increased
     * @param amount    increase amount
     * @return {@code dimension} increased by amount specified in {@code amount}
     */
    @NotNull
    public static Dimension increase ( @NotNull final Dimension dimension, @Nullable final Insets amount )
    {
        if ( amount != null )
        {
            dimension.width += amount.left + amount.right;
            dimension.height += amount.top + amount.bottom;
        }
        return dimension;
    }

    /**
     * Returns {@code insets} decreased by amount specified in {@code amount}.
     *
     * @param insets insets to decreased
     * @param amount decrease amount
     * @return {@code insets} decreased by amount specified in {@code amount}
     */
    @NotNull
    public static Insets decrease ( @NotNull final Insets insets, @Nullable final Insets amount )
    {
        if ( amount != null )
        {
            insets.top -= amount.top;
            insets.left -= amount.left;
            insets.bottom -= amount.bottom;
            insets.right -= amount.right;
        }
        return insets;
    }

    /**
     * Returns maximum insets combined from the specified ones.
     *
     * @param insets1 first insets
     * @param insets2 second insets
     * @return maximum insets
     */
    @Nullable
    public static Insets max ( @Nullable final Insets insets1, @Nullable final Insets insets2 )
    {
        final Insets max;
        if ( insets1 != null && insets2 != null )
        {
            max = new Insets (
                    Math.max ( insets1.top, insets2.top ),
                    Math.max ( insets1.left, insets2.left ),
                    Math.max ( insets1.bottom, insets2.bottom ),
                    Math.max ( insets1.right, insets2.right )
            );
        }
        else if ( insets1 != null )
        {
            max = insets1;
        }
        else
        {
            max = insets2;
        }
        return max;
    }

    /**
     * Returns minimum insets combined from the specified ones.
     *
     * @param insets1 first insets
     * @param insets2 second insets
     * @return minimum insets
     */
    @Nullable
    public static Insets min ( @Nullable final Insets insets1, @Nullable final Insets insets2 )
    {
        final Insets min;
        if ( insets1 != null && insets2 != null )
        {
            min = new Insets (
                    Math.min ( insets1.top, insets2.top ),
                    Math.min ( insets1.left, insets2.left ),
                    Math.min ( insets1.bottom, insets2.bottom ),
                    Math.min ( insets1.right, insets2.right )
            );
        }
        else if ( insets1 != null )
        {
            min = insets1;
        }
        else
        {
            min = insets2;
        }
        return min;
    }

    /**
     * Returns maximum dimension combined from specified components dimensions.
     *
     * @param component1 first component
     * @param component2 second component
     * @return maximum dimension
     */
    @NotNull
    public static Dimension maxPreferredSize ( @NotNull final Component component1, @NotNull final Component component2 )
    {
        return maxNonNull ( component1.getPreferredSize (), component2.getPreferredSize () );
    }

    /**
     * Returns maximum dimension combined from specified components dimensions.
     *
     * @param components components
     * @return maximum dimension
     */
    @NotNull
    public static Dimension maxPreferredSize ( @NotNull final Component... components )
    {
        Dimension max = components.length > 0 ? components[ 0 ].getPreferredSize () : new Dimension ( 0, 0 );
        for ( int i = 1; i < components.length; i++ )
        {
            max = maxNonNull ( max, components[ i ].getPreferredSize () );
        }
        return max;
    }

    /**
     * Returns maximum component width.
     *
     * @param components components to process
     * @return maximum component width
     */
    public static int maxPreferredWidth ( @NotNull final Component... components )
    {
        int max = 0;
        for ( final Component component : components )
        {
            max = Math.max ( max, component.getPreferredSize ().width );
        }
        return max;
    }

    /**
     * Returns maximum component height.
     *
     * @param components components to process
     * @return maximum component height
     */
    public static int maxPreferredHeight ( @NotNull final Component... components )
    {
        int max = 0;
        for ( final Component component : components )
        {
            max = Math.max ( max, component.getPreferredSize ().height );
        }
        return max;
    }

    /**
     * Returns maximum dimension combined from specified ones.
     *
     * @param dimension1 first dimension
     * @param dimension2 second dimension
     * @return maximum dimension
     */
    @Nullable
    public static Dimension max ( @Nullable final Dimension dimension1, @Nullable final Dimension dimension2 )
    {
        final Dimension max;
        if ( dimension1 == null && dimension2 == null )
        {
            max = null;
        }
        else if ( dimension1 == null )
        {
            max = dimension2;
        }
        else if ( dimension2 == null )
        {
            max = dimension1;
        }
        else
        {
            max = new Dimension ( Math.max ( dimension1.width, dimension2.width ), Math.max ( dimension1.height, dimension2.height ) );
        }
        return max;
    }

    /**
     * Returns maximum dimension combined from specified ones.
     *
     * @param dimension1 first dimension
     * @param dimension2 second dimension
     * @return maximum dimension
     */
    @NotNull
    public static Dimension maxNonNull ( @NotNull final Dimension dimension1, @NotNull final Dimension dimension2 )
    {
        return new Dimension (
                Math.max ( dimension1.width, dimension2.width ),
                Math.max ( dimension1.height, dimension2.height )
        );
    }

    /**
     * Returns minimum dimension combined from specified components dimensions.
     *
     * @param component1 first component
     * @param component2 second component
     * @return minimum dimension
     */
    @NotNull
    public static Dimension minPreferredSize ( @NotNull final Component component1, @NotNull final Component component2 )
    {
        return minNonNull ( component1.getPreferredSize (), component2.getPreferredSize () );
    }

    /**
     * Returns maximum dimension combined from specified components dimensions.
     *
     * @param components components
     * @return maximum dimension
     */
    @NotNull
    public static Dimension minPreferredSize ( @NotNull final Component... components )
    {
        Dimension min = components.length > 0 ? components[ 0 ].getPreferredSize () : new Dimension ( 0, 0 );
        for ( int i = 1; i < components.length; i++ )
        {
            min = minNonNull ( min, components[ i ].getPreferredSize () );
        }
        return min;
    }

    /**
     * Returns minimum dimension combined from specified ones.
     *
     * @param dimension1 first dimension
     * @param dimension2 second dimension
     * @return minimum dimension
     */
    @Nullable
    public static Dimension min ( @Nullable final Dimension dimension1, @Nullable final Dimension dimension2 )
    {
        final Dimension min;
        if ( dimension1 != null && dimension2 != null )
        {
            min = new Dimension (
                    Math.min ( dimension1.width, dimension2.width ),
                    Math.min ( dimension1.height, dimension2.height )
            );
        }
        else
        {
            min = null;
        }
        return min;
    }

    /**
     * Returns minimum dimension combined from specified ones.
     *
     * @param dimension1 first dimension
     * @param dimension2 second dimension
     * @return minimum dimension
     */
    @NotNull
    public static Dimension minNonNull ( @NotNull final Dimension dimension1, @NotNull final Dimension dimension2 )
    {
        return new Dimension (
                Math.min ( dimension1.width, dimension2.width ),
                Math.min ( dimension1.height, dimension2.height )
        );
    }

    /**
     * Returns {@link Rectangle} shrunk by provided {@link Insets}.
     *
     * @param rectangle {@link Rectangle} to shrink
     * @param insets    {@link Insets} used to shrink {@link Rectangle}
     * @return {@link Rectangle} shrunk by provided {@link Insets}
     */
    @NotNull
    public static Rectangle shrink ( @NotNull final Rectangle rectangle, @Nullable final Insets insets )
    {
        final Rectangle result;
        if ( insets != null )
        {
            result = new Rectangle (
                    rectangle.x + insets.left,
                    rectangle.y + insets.top,
                    rectangle.width - insets.left - insets.right,
                    rectangle.height - insets.top - insets.bottom
            );
        }
        else
        {
            result = new Rectangle ( rectangle );
        }
        return result;
    }

    /**
     * Returns {@link Rectangle} which location is moved by X and Y from the specified {@link Point} .
     *
     * @param rectangle {@link Rectangle} to move
     * @param point     {@link Point} to retrieve X and Y from
     * @return {@link Rectangle} which location is moved by X and Y from the specified {@link Point}
     */
    @NotNull
    public static Rectangle moveBy ( @NotNull final Rectangle rectangle, @Nullable final Point point )
    {
        final Rectangle result;
        if ( point != null )
        {
            result = new Rectangle (
                    rectangle.x + point.x,
                    rectangle.y + point.y,
                    rectangle.width,
                    rectangle.height
            );
        }
        else
        {
            result = new Rectangle ( rectangle );
        }
        return result;
    }

    /**
     * Returns dimension shrunk by provided insets.
     *
     * @param dimension dimension to shrink
     * @param insets    insets used to shrink dimension
     * @return dimension shrunk by provided insets
     */
    @NotNull
    public static Dimension shrink ( @NotNull final Dimension dimension, @Nullable final Insets insets )
    {
        final Dimension result;
        if ( insets != null )
        {
            result = new Dimension (
                    dimension.width - insets.left - insets.right,
                    dimension.height - insets.top - insets.bottom
            );
        }
        else
        {
            result = new Dimension ( dimension );
        }
        return result;
    }

    /**
     * Returns dimension stretched by provided insets.
     *
     * @param dimension dimension to stretch
     * @param insets    insets used to stretch dimension
     * @return dimension stretched by provided insets
     */
    @NotNull
    public static Dimension stretch ( @NotNull final Dimension dimension, @Nullable final Insets insets )
    {
        final Dimension result;
        if ( insets != null )
        {
            result = new Dimension (
                    dimension.width + insets.left + insets.right,
                    dimension.height + insets.top + insets.bottom
            );
        }
        else
        {
            result = new Dimension ( dimension );
        }
        return result;
    }

    /**
     * Sets opaque state of component and all of its children.
     *
     * @param component component to modify
     * @param opaque    whether opaque state or not
     */
    public static void setOpaqueRecursively ( @NotNull final Component component, final boolean opaque )
    {
        setOpaqueRecursively ( component, opaque, false );
    }

    /**
     * Sets opaque state of component and all of its children.
     *
     * @param component    component to modify
     * @param opaque       whether opaque state or not
     * @param childrenOnly whether exclude component from changes or not
     */
    public static void setOpaqueRecursively ( @NotNull final Component component, final boolean opaque, final boolean childrenOnly )
    {
        if ( component instanceof JComponent )
        {
            final JComponent jComponent = ( JComponent ) component;
            if ( !childrenOnly )
            {
                jComponent.setOpaque ( opaque );
            }
        }
        if ( component instanceof Container )
        {
            for ( final Component child : ( ( Container ) component ).getComponents () )
            {
                setOpaqueRecursively ( child, opaque, false );
            }
        }
    }

    /**
     * Sets double buffered state of component and all of its children.
     *
     * @param component      component to modify
     * @param doubleBuffered whether use double buffering or not
     */
    public static void setDoubleBufferedRecursively ( @NotNull final Component component, final boolean doubleBuffered )
    {
        setDoubleBufferedRecursively ( component, doubleBuffered, false );
    }

    /**
     * Sets double buffered state of component and all of its children.
     *
     * @param component      component to modify
     * @param doubleBuffered whether use double buffering or not
     * @param childrenOnly   whether exclude component from changes or not
     */
    public static void setDoubleBufferedRecursively ( @NotNull final Component component, final boolean doubleBuffered,
                                                      final boolean childrenOnly )
    {
        if ( component instanceof JComponent )
        {
            final JComponent jComponent = ( JComponent ) component;
            if ( !childrenOnly )
            {
                jComponent.setDoubleBuffered ( doubleBuffered );
            }
        }
        if ( component instanceof Container )
        {
            for ( final Component child : ( ( Container ) component ).getComponents () )
            {
                setDoubleBufferedRecursively ( child, doubleBuffered, false );
            }
        }
    }

    /**
     * Adds {@link #HANDLES_ENABLE_STATE} client property into the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to set client property for
     */
    public static void setHandlesEnableStateMark ( @NotNull final JComponent component )
    {
        component.putClientProperty ( HANDLES_ENABLE_STATE, Boolean.TRUE );
    }

    /**
     * Removes {@link #HANDLES_ENABLE_STATE} client property from the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove client property from
     */
    public static void removeHandlesEnableStateMark ( @NotNull final JComponent component )
    {
        component.putClientProperty ( HANDLES_ENABLE_STATE, Boolean.FALSE );
    }

    /**
     * Returns whether or not {@link #HANDLES_ENABLE_STATE} client property is set to {@code true} in the specified {@link Component}.
     *
     * @param component {@link Component} to check client property in
     * @return {@code true} if {@link #HANDLES_ENABLE_STATE} client property is set to {@code true}, {@code false} otherwise
     */
    public static boolean isHandlesEnableState ( @NotNull final Component component )
    {
        final boolean handlesEnabledState;
        if ( component instanceof JComponent )
        {
            final Object property = ( ( JComponent ) component ).getClientProperty ( HANDLES_ENABLE_STATE );
            handlesEnabledState = property instanceof Boolean && ( Boolean ) property;
        }
        else
        {
            handlesEnabledState = false;
        }
        return handlesEnabledState;
    }

    /**
     * Sets enabled state of component and all of its children.
     *
     * @param component component to modify
     * @param enabled   whether component is enabled or not
     */
    public static void setEnabledRecursively ( @NotNull final Component component, final boolean enabled )
    {
        setEnabledRecursively ( component, enabled, false );
    }

    /**
     * Sets enabled state of component and all of its children.
     *
     * @param component         component to modify
     * @param enabled           whether component is enabled or not
     * @param startFromChildren whether exclude component from changes or not
     */
    public static void setEnabledRecursively ( @NotNull final Component component, final boolean enabled, final boolean startFromChildren )
    {
        if ( !startFromChildren )
        {
            component.setEnabled ( enabled );
        }
        if ( component instanceof Container && ( startFromChildren || !isHandlesEnableState ( component ) ) )
        {
            for ( final Component child : ( ( Container ) component ).getComponents () )
            {
                setEnabledRecursively ( child, enabled, false );
            }
        }
    }

    /**
     * Disables component and all of its children recursively.
     *
     * @param component         component to disable
     * @param startFromChildren whether should disable only component children or not
     * @param excludePanels     whether should exclude panels from disabling or not
     * @param excluded          components to exclude from disabling
     * @return list of actually disabled components
     */
    @NotNull
    public static List<Component> disableRecursively ( @NotNull final Component component, final boolean startFromChildren,
                                                       final boolean excludePanels, @NotNull final Component... excluded )
    {
        return disableRecursively ( component, startFromChildren, excludePanels, new ImmutableList<Component> ( excluded ) );
    }

    /**
     * Disables component and all of its children recursively.
     *
     * @param component         component to disable
     * @param startFromChildren whether should disable only component children or not
     * @param excludePanels     whether should exclude panels from disabling or not
     * @param excluded          components to exclude from disabling
     * @return list of actually disabled components
     */
    @NotNull
    public static List<Component> disableRecursively ( @NotNull final Component component, final boolean startFromChildren,
                                                       final boolean excludePanels, @NotNull final List<Component> excluded )
    {
        final List<Component> disabled = new ArrayList<Component> ();
        disableRecursively ( component, startFromChildren, excludePanels, excluded, disabled );
        return disabled;
    }

    /**
     * Disables component and all of its children recursively.
     *
     * @param component         component to disable
     * @param startFromChildren whether should disable only component children or not
     * @param excludePanels     whether should exclude panels from disabling or not
     * @param excluded          components to exclude from disabling
     * @param disabled          list of actually disabled components
     */
    private static void disableRecursively ( @NotNull final Component component, final boolean startFromChildren,
                                             final boolean excludePanels, @NotNull final List<Component> excluded,
                                             @NotNull final List<Component> disabled )
    {
        final boolean b = !startFromChildren && !excluded.contains ( component ) && !( component instanceof JPanel && excludePanels );
        if ( b && component.isEnabled () )
        {
            component.setEnabled ( false );
            disabled.add ( component );
        }
        if ( component instanceof Container && ( !b || !isHandlesEnableState ( component ) ) )
        {
            for ( final Component child : ( ( Container ) component ).getComponents () )
            {
                disableRecursively ( child, false, excludePanels, excluded, disabled );
            }
        }
    }

    /**
     * Enables specified components.
     *
     * @param disabled disabled components list
     */
    public static void enable ( @NotNull final List<Component> disabled )
    {
        for ( final Component component : disabled )
        {
            component.setEnabled ( true );
        }
    }

    /**
     * Sets focusable state of component and all of its children.
     *
     * @param component component to modify
     * @param focusable whether component is focusable or not
     */
    public static void setFocusableRecursively ( @NotNull final JComponent component, final boolean focusable )
    {
        setFocusableRecursively ( component, focusable, false );
    }

    /**
     * Sets focusable state of component and all of its children.
     *
     * @param component    component to modify
     * @param focusable    whether component is focusable or not
     * @param childrenOnly whether exclude component from changes or not
     */
    public static void setFocusableRecursively ( @NotNull final JComponent component, final boolean focusable, final boolean childrenOnly )
    {
        if ( !childrenOnly )
        {
            component.setFocusable ( focusable );
        }
        for ( int i = 0; i < component.getComponentCount (); i++ )
        {
            if ( component.getComponent ( i ) instanceof JComponent )
            {
                setFocusableRecursively ( ( JComponent ) component.getComponent ( i ), focusable, false );
            }
        }
    }

    /**
     * Sets background color of component and all of its children.
     *
     * @param component component to modify
     * @param bg        new background color
     */
    public static void setBackgroundRecursively ( @NotNull final Component component, final Color bg )
    {
        setBackgroundRecursively ( component, bg, false );
    }

    /**
     * Sets background color of component and all of its children.
     *
     * @param component    component to modify
     * @param bg           new background color
     * @param childrenOnly whether exclude component from changes or not
     */
    public static void setBackgroundRecursively ( @NotNull final Component component, final Color bg, final boolean childrenOnly )
    {
        if ( !childrenOnly )
        {
            component.setBackground ( bg );
        }
        if ( component instanceof Container )
        {
            for ( final Component child : ( ( Container ) component ).getComponents () )
            {
                setBackgroundRecursively ( child, bg, false );
            }
        }
    }

    /**
     * Sets foreground color of component and all of its children.
     *
     * @param component  component to modify
     * @param foreground new foreground color
     */
    public static void setForegroundRecursively ( @NotNull final JComponent component, @Nullable final Color foreground )
    {
        setForegroundRecursively ( component, foreground, false );
    }

    /**
     * Sets foreground color of component and all of its children.
     *
     * @param component    component to modify
     * @param foreground   new foreground color
     * @param childrenOnly whether exclude component from changes or not
     */
    public static void setForegroundRecursively ( @NotNull final JComponent component, @Nullable final Color foreground,
                                                  final boolean childrenOnly )
    {
        if ( !childrenOnly )
        {
            component.setForeground ( foreground );
        }
        for ( int i = 0; i < component.getComponentCount (); i++ )
        {
            if ( component.getComponent ( i ) instanceof JComponent )
            {
                setForegroundRecursively ( ( JComponent ) component.getComponent ( i ), foreground, false );
            }
        }
    }

    /**
     * Sets font of component and all of its children.
     *
     * @param component component to modify
     * @param font      new font
     */
    public static void setFontRecursively ( @NotNull final JComponent component, @Nullable final Font font )
    {
        setFontRecursively ( component, font, false );
    }

    /**
     * Sets font of component and all of its children.
     *
     * @param component    component to modify
     * @param font         new font
     * @param childrenOnly whether exclude component from changes or not
     */
    public static void setFontRecursively ( @NotNull final JComponent component, @Nullable final Font font, final boolean childrenOnly )
    {
        if ( !childrenOnly )
        {
            component.setFont ( font );
        }
        for ( int i = 0; i < component.getComponentCount (); i++ )
        {
            if ( component.getComponent ( i ) instanceof JComponent )
            {
                setFontRecursively ( ( JComponent ) component.getComponent ( i ), font, false );
            }
        }
    }

    /**
     * Returns component snapshot image.
     * Component must be showing to render properly using this method.
     *
     * @param content component for snapshot
     * @return component snapshot image
     */
    @NotNull
    public static BufferedImage createComponentSnapshot ( @NotNull final Component content )
    {
        return createComponentSnapshot ( content, content.getWidth (), content.getHeight (), 1f );
    }

    /**
     * Returns component snapshot image.
     * Component must be showing to render properly using this method.
     *
     * @param content component for snapshot
     * @param opacity snapshot opacity
     * @return component snapshot image
     */
    @NotNull
    public static BufferedImage createComponentSnapshot ( @NotNull final Component content, final float opacity )
    {
        return createComponentSnapshot ( content, content.getWidth (), content.getHeight (), opacity );
    }

    /**
     * Returns component snapshot image of specified size.
     * Component must be showing to render properly using this method.
     *
     * @param content component for snapshot
     * @param width   snapshot image width
     * @param height  snapshot image height
     * @return component snapshot image
     */
    @NotNull
    public static BufferedImage createComponentSnapshot ( @NotNull final Component content, final int width, final int height )
    {
        return createComponentSnapshot ( content, width, height, 1f );
    }

    /**
     * Returns component snapshot image of specified size.
     * Component must be showing to render properly using this method.
     *
     * @param content component for snapshot
     * @param width   snapshot image width
     * @param height  snapshot image height
     * @param opacity snapshot opacity
     * @return component snapshot image
     */
    @NotNull
    public static BufferedImage createComponentSnapshot ( @NotNull final Component content, final int width, final int height,
                                                          final float opacity )
    {
        // Creating snapshot
        final BufferedImage bi = ImageUtils.createCompatibleImage ( width, height, Transparency.TRANSLUCENT );
        final Graphics2D snapshot2d = bi.createGraphics ();
        final Dimension size = content.getSize ();
        content.setSize ( width, height );
        content.paintAll ( snapshot2d );
        content.setSize ( size );
        snapshot2d.dispose ();

        // Required to restore any damage caused by size change
        if ( content instanceof JComponent )
        {
            ( ( JComponent ) content ).revalidate ();
            content.repaint ();
        }

        // Making it transparent if needed
        // Transparency is applied separately to avoid components from being transparent between each other when painted
        final BufferedImage result;
        if ( opacity < 1f )
        {
            result = ImageUtils.createCompatibleImage ( width, height, Transparency.TRANSLUCENT );
            final Graphics2D transparent2d = result.createGraphics ();
            GraphicsUtils.setupAlphaComposite ( transparent2d, opacity );
            transparent2d.drawImage ( bi, 0, 0, null );
            transparent2d.dispose ();
        }
        else
        {
            result = bi;
        }
        return result;
    }

    /**
     * Returns menu item accelerator for the specified hotkey.
     *
     * @param hotkey hotkey to provide accelerator based on
     * @return menu item accelerator for the specified hotkey
     */
    @Nullable
    public static KeyStroke getAccelerator ( @Nullable final HotkeyData hotkey )
    {
        return hotkey != null && hotkey.isHotkeySet () ? hotkey.getKeyStroke () : null;
    }

    /**
     * Returns focus accelerator key mask.
     *
     * @return focus accelerator key mask
     */
    public static int getFocusAcceleratorKeyMask ()
    {
        int mask = ActionEvent.ALT_MASK;
        if ( SystemUtils.isJava7orAbove () )
        {
            // This toolkit method was added in JDK 7 and later ones
            // It is recommended to use instead of the hardcoded accelerator mask
            final Toolkit toolkit = Toolkit.getDefaultToolkit ();
            if ( Objects.equals ( toolkit.getClass ().getCanonicalName (), "sun.awt.SunToolkit" ) )
            {
                final Object toolkitMask = ReflectUtils.callMethodSafely ( toolkit, "getFocusAcceleratorKeyMask" );
                if ( toolkitMask != null )
                {
                    mask = ( Integer ) toolkitMask;
                }
            }
        }
        return mask;
    }

    /**
     * Returns index of the first occurrence of {@code mnemonic} within string {@code text}.
     * Matching algorithm is not case-sensitive.
     *
     * @param text     text to search through, may be {@code null}
     * @param mnemonic mnemonic to find the character for
     * @return index into the string if exists, otherwise -1
     */
    public static int getMnemonicIndex ( final String text, final int mnemonic )
    {
        final int index;
        if ( text != null && mnemonic != '\0' )
        {
            final char uc = Character.toUpperCase ( ( char ) mnemonic );
            final char lc = Character.toLowerCase ( ( char ) mnemonic );
            final int uci = text.indexOf ( uc );
            final int lci = text.indexOf ( lc );
            if ( uci == -1 )
            {
                index = lci;
            }
            else if ( lci == -1 )
            {
                index = uci;
            }
            else
            {
                index = lci < uci ? lci : uci;
            }
        }
        else
        {
            index = -1;
        }
        return index;
    }

    /**
     * Returns active application window.
     *
     * @return active application window
     */
    @Nullable
    public static Window getActiveWindow ()
    {
        final Window[] windows = Window.getWindows ();
        Window window = null;
        for ( final Window w : windows )
        {
            if ( w.isShowing () && w.isActive () && w.isFocused () )
            {
                window = w;
                break;
            }
        }
        return window;
    }

    /**
     * Returns whether specified event contains shortcut modifier or not.
     *
     * @param event event to process
     * @return true if specified event contains shortcut modifier, false otherwise
     */
    public static boolean isShortcut ( @NotNull final InputEvent event )
    {
        return ( event.getModifiers () & getSystemShortcutModifier () ) != 0;
    }

    /**
     * Returns system shortcut modifier.
     *
     * @return system shortcut modifier
     */
    public static int getSystemShortcutModifier ()
    {
        if ( systemShortcutModifier == null )
        {
            systemShortcutModifier = Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask ();
        }
        return systemShortcutModifier;
    }

    /**
     * Returns readable form of hotkey triggered by specified key event.
     *
     * @param keyEvent key event to process
     * @return readable form of hotkey triggered by specified key event
     */
    @NotNull
    public static String hotkeyToString ( @NotNull final KeyEvent keyEvent )
    {
        return hotkeyToString (
                isCtrl ( keyEvent ),
                isAlt ( keyEvent ),
                isShift ( keyEvent ),
                keyEvent.getKeyCode ()
        );
    }

    /**
     * Returns readable form of specified hotkey data.
     *
     * @param hotkeyData hotkey data to process
     * @return readable form of specified hotkey data
     */
    @NotNull
    public static String hotkeyToString ( @NotNull final HotkeyData hotkeyData )
    {
        return hotkeyToString (
                hotkeyData.isCtrl (),
                hotkeyData.isAlt (),
                hotkeyData.isShift (),
                hotkeyData.getKeyCode ()
        );
    }

    /**
     * Returns readable form of specified key stroke.
     *
     * @param keyStroke key stroke to process
     * @return readable form of specified key stroke
     */
    @NotNull
    public static String hotkeyToString ( @NotNull final KeyStroke keyStroke )
    {
        return hotkeyToString (
                isCtrl ( keyStroke.getModifiers () ),
                isAlt ( keyStroke.getModifiers () ),
                isShift ( keyStroke.getModifiers () ),
                keyStroke.getKeyCode ()
        );
    }

    /**
     * Returns readable form for specified hotkey.
     *
     * @param isCtrl  whether hotkey requires CTRL modifier or not
     * @param isAlt   whether hotkey requires ALT modifier or not
     * @param isShift whether hotkey requires SHIFT modifier or not
     * @param keyCode key code for hotkey
     * @return readable form for specified hotkey
     */
    @NotNull
    public static String hotkeyToString ( final boolean isCtrl, final boolean isAlt, final boolean isShift,
                                          @Nullable final Integer keyCode )
    {
        return "" + ( isCtrl ? KeyEvent.getKeyModifiersText ( SwingUtils.getSystemShortcutModifier () ) +
                ( isAlt || isShift || keyCode != null ? "+" : "" ) : "" ) +
                ( isAlt ? KeyEvent.getKeyModifiersText ( Event.ALT_MASK ) + ( isShift || keyCode != null ? "+" : "" ) : "" ) +
                ( isShift ? KeyEvent.getKeyModifiersText ( Event.SHIFT_MASK ) + ( keyCode != null ? "+" : "" ) : "" ) +
                ( keyCode != null ? KeyEvent.getKeyText ( keyCode ) : "" );
    }

    /**
     * Returns whether CTRL modifier is triggered by the specified event or not.
     *
     * @param event event to process
     * @return true if CTRL modifier is triggered by the specified event, false otherwise
     */
    public static boolean isCtrl ( @NotNull final InputEvent event )
    {
        return isCtrl ( event.getModifiers () );
    }

    /**
     * Returns whether CTRL modifier is triggered by the specified modifiers or not.
     *
     * @param modifiers modifiers to process
     * @return true if CTRL modifier is triggered by the specified modifiers, false otherwise
     */
    public static boolean isCtrl ( final int modifiers )
    {
        return ( modifiers & InputEvent.CTRL_MASK ) != 0;
    }

    /**
     * Returns whether ALT modifier is triggered by the specified event or not.
     *
     * @param event event to process
     * @return true if ALT modifier is triggered by the specified event, false otherwise
     */
    public static boolean isAlt ( @NotNull final InputEvent event )
    {
        return isAlt ( event.getModifiers () );
    }

    /**
     * Returns whether ALT modifier is triggered by the specified modifiers or not.
     *
     * @param modifiers modifiers to process
     * @return true if ALT modifier is triggered by the specified modifiers, false otherwise
     */
    public static boolean isAlt ( final int modifiers )
    {
        return ( modifiers & InputEvent.ALT_MASK ) != 0;
    }

    /**
     * Returns whether SHIFT modifier is triggered by the specified event or not.
     *
     * @param event event to process
     * @return true if SHIFT modifier is triggered by the specified event, false otherwise
     */
    public static boolean isShift ( @NotNull final InputEvent event )
    {
        return isShift ( event.getModifiers () );
    }

    /**
     * Returns whether SHIFT modifier is triggered by the specified modifiers or not.
     *
     * @param modifiers modifiers to process
     * @return true if SHIFT modifier is triggered by the specified modifiers, false otherwise
     */
    public static boolean isShift ( final int modifiers )
    {
        return ( modifiers & InputEvent.SHIFT_MASK ) != 0;
    }

    /**
     * Returns hotkey data extracted from the specified key stroke.
     *
     * @param keyStroke key stroke to process
     * @return hotkey data
     */
    @NotNull
    public static HotkeyData getHotkeyData ( @NotNull final KeyStroke keyStroke )
    {
        final int m = keyStroke.getModifiers ();
        return new HotkeyData ( isCtrl ( m ), isAlt ( m ), isShift ( m ), keyStroke.getKeyCode () );
    }

    /**
     * Returns default label font.
     * This method might be used as a hack with other LaFs to retrieve system default font for simple text.
     *
     * @return default label font
     */
    @NotNull
    public static Font getDefaultLabelFont ()
    {
        if ( label == null )
        {
            label = new JLabel ();
        }
        return label.getFont ();
    }

    /**
     * Returns default label font metrics.
     * This method might be used as a hack with other LaFs to retrieve system default font metrics for simple text.
     *
     * @return default label font metrics
     */
    @NotNull
    public static FontMetrics getDefaultLabelFontMetrics ()
    {
        if ( label == null )
        {
            label = new JLabel ();
        }
        return label.getFontMetrics ( label.getFont () );
    }

    /**
     * Returns {@link JSplitPane} for the specified {@link Component} if it is directly added into one, {@code null} otherwise.
     *
     * @param component {@link Component} to process
     * @return {@link JSplitPane} for the specified {@link Component} if it is directly added into one, {@code null} otherwise
     */
    @Nullable
    public static JScrollPane getScrollPane ( @Nullable final Component component )
    {
        final JScrollPane scrollPane;
        if ( component != null && component.getParent () != null && component.getParent () instanceof JViewport &&
                component.getParent ().getParent () != null && component.getParent ().getParent () instanceof JScrollPane )
        {
            scrollPane = ( JScrollPane ) component.getParent ().getParent ();
        }
        else
        {
            scrollPane = null;
        }
        return scrollPane;
    }

    /**
     * Performs composite focus request and returns {@link Component} that requested focus.
     *
     * @param component {@link Component} to perform composite focus request for
     * @return {@link Component} that requested focus
     */
    @Nullable
    public static Component compositeRequestFocus ( @NotNull final Component component )
    {
        Component focused = null;
        if ( component instanceof Container )
        {
            final Container container = ( Container ) component;
            if ( container.isFocusCycleRoot () )
            {
                final FocusTraversalPolicy policy = container.getFocusTraversalPolicy ();
                final Component defaultComponent = policy.getDefaultComponent ( container );
                if ( defaultComponent != null )
                {
                    defaultComponent.requestFocus ();
                    focused = defaultComponent;
                }
            }
            if ( focused == null )
            {
                final Container focusCycleRootAncestor = container.getFocusCycleRootAncestor ();
                if ( focusCycleRootAncestor != null )
                {
                    final FocusTraversalPolicy policy = focusCycleRootAncestor.getFocusTraversalPolicy ();
                    final Component after = policy.getComponentAfter ( focusCycleRootAncestor, container );
                    if ( after != null && SwingUtilities.isDescendingFrom ( after, container ) )
                    {
                        after.requestFocus ();
                        focused = after;
                    }
                }
            }
        }
        if ( focused == null && component.isFocusable () )
        {
            component.requestFocus ();
            focused = component;
        }
        return focused;
    }

    /**
     * Returns first focusable component found in the container.
     *
     * @param container container to process
     * @return first focusable component found in the container
     */
    @Nullable
    public static Component findFocusableComponent ( @NotNull final Container container )
    {
        Component focusable = null;
        final FocusTraversalPolicy focusTraversalPolicy = container.getFocusTraversalPolicy ();
        if ( focusTraversalPolicy != null )
        {
            focusable = focusTraversalPolicy.getFirstComponent ( container );
        }
        else
        {
            for ( final Component component : container.getComponents () )
            {
                if ( component.isFocusable () )
                {
                    focusable = component;
                }
                else if ( component instanceof Container )
                {
                    focusable = findFocusableComponent ( ( Container ) component );
                    if ( focusable != null )
                    {
                        break;
                    }
                }
            }
        }
        return focusable;
    }

    /**
     * Returns list of all components that visually contains the specified text.
     *
     * @param text      text to find
     * @param component component or container to look for specified text
     * @return list of all components that visually contains the specified text
     */
    @NotNull
    public static List<Component> findComponentsWithText ( @Nullable final String text, @Nullable final Component component )
    {
        return findComponentsWithText ( text, component, new ArrayList<Component> () );
    }

    /**
     * Returns list of all components that visually contains the specified text.
     *
     * @param text       text to find
     * @param component  component or container to look for specified text
     * @param components list of found components
     * @return list of all components that visually contains the specified text
     */
    @NotNull
    public static List<Component> findComponentsWithText ( @Nullable final String text, @Nullable final Component component,
                                                           @NotNull final List<Component> components )
    {
        if ( text != null && !text.equals ( "" ) && component != null )
        {
            try
            {
                if ( component instanceof WebPathField || component instanceof WebFileChooserField ||
                        component instanceof WebDateField || component instanceof WebCalendar )
                {
                    // Check children for text in composite components
                    for ( final Component child : ( ( Container ) component ).getComponents () )
                    {
                        if ( findComponentsWithText ( text, child ).size () > 0 )
                        {
                            components.add ( component );
                            break;
                        }
                    }
                }
                else if ( component instanceof WebCollapsiblePane )
                {
                    // Check header panel for text
                    final WebCollapsiblePane collapsiblePane = ( WebCollapsiblePane ) component;
                    if ( findComponentsWithText ( text, collapsiblePane.getTitleComponent () ).size () > 0 )
                    {
                        components.add ( component );
                    }
                }
                else if ( component instanceof JComboBox )
                {
                    // todo Check each renderer for text
                    final JComboBox comboBox = ( JComboBox ) component;
                    final Object selectedItem = comboBox.getSelectedItem ();
                    if ( selectedItem != null )
                    {
                        if ( selectedItem.toString ().toLowerCase ( Locale.ROOT ).contains ( text.toLowerCase ( Locale.ROOT ) ) )
                        {
                            components.add ( component );
                        }
                        else
                        {
                            if ( comboBox.isEditable () )
                            {
                                if ( findComponentsWithText ( text, comboBox.getEditor ().getEditorComponent () ).size () > 0 )
                                {
                                    components.add ( component );
                                }
                            }
                            else
                            {
                                if ( findComponentsWithText ( text,
                                        comboBox.getRenderer ().getListCellRendererComponent ( null, selectedItem, -1, true, true ) )
                                        .size () > 0 )
                                {
                                    components.add ( component );
                                }
                            }
                        }
                    }
                }
                else if ( component instanceof JSpinner )
                {
                    // Check value for text
                    if ( ( ( JSpinner ) component ).getValue ().toString ().toLowerCase ( Locale.ROOT )
                            .contains ( text.toLowerCase ( Locale.ROOT ) ) )
                    {
                        components.add ( component );
                    }
                }
                else if ( component instanceof JLabel )
                {
                    // Check value for text
                    if ( ( ( JLabel ) component ).getText ().toLowerCase ( Locale.ROOT ).contains ( text.toLowerCase ( Locale.ROOT ) ) )
                    {
                        components.add ( component );
                    }
                }
                else if ( component instanceof AbstractButton )
                {
                    // Check value for text
                    if ( ( ( AbstractButton ) component ).getText ().toLowerCase ( Locale.ROOT )
                            .contains ( text.toLowerCase ( Locale.ROOT ) ) )
                    {
                        components.add ( component );
                    }
                }
                else if ( component instanceof JTextComponent )
                {
                    // Check value for text
                    if ( ( ( JTextComponent ) component ).getText ().toLowerCase ( Locale.ROOT )
                            .contains ( text.toLowerCase ( Locale.ROOT ) ) )
                    {
                        // Return scroll instead of the text component
                        if ( component.getParent () != null &&
                                component.getParent ().getParent () != null &&
                                component.getParent ().getParent () instanceof JScrollPane )
                        {
                            components.add ( component.getParent ().getParent () );
                        }
                        else
                        {
                            components.add ( component );
                        }
                    }
                }
                else if ( component instanceof JTabbedPane )
                {
                    final JTabbedPane tp = ( JTabbedPane ) component;

                    // Only content check
                    for ( int i = 0; i < tp.getTabCount (); i++ )
                    {
                        if ( tp.getComponentAt ( i ) instanceof Container )
                        {
                            checkContent ( text, ( Container ) tp.getComponentAt ( i ), components );
                        }
                    }

                    // Content and titles check
                    //                boolean found = false;
                    //                for ( int i = 0; i < tp.getTabCount (); i++ )
                    //                {
                    //                    // Checking tab title
                    //                    if ( tp.getTitleAt ( i ).toLowerCase ( Locale.ROOT ).contains ( text.toLowerCase ( Locale.ROOT ) ) )
                    //                    {
                    //                        components.add ( component );
                    //                        found = true;
                    //                        break;
                    //                    }
                    //                    // Checking tab button content
                    //                    final Component tabComponent = tp.getTabComponentAt ( i );
                    //                    if ( tabComponent != null )
                    //                    {
                    //                        if ( findComponentsWithText ( text, tabComponent ).size () > 0 )
                    //                        {
                    //                            components.add ( component );
                    //                            found = true;
                    //                            break;
                    //                        }
                    //                    }
                    //                }
                    //
                    //                if (!found) {
                    //                    return
                    //                }
                }
                else if ( component instanceof Container )
                {
                    checkContent ( text, ( Container ) component, components );
                }
            }
            catch ( final Exception ignored )
            {
                /**
                 * We're not interested in handling any errors here.
                 */
            }
        }
        return components;
    }

    /**
     * Searches for the specified text inside the container.
     *
     * @param text       text to find
     * @param container  container to look for specified text
     * @param components list of found components
     */
    private static void checkContent ( @Nullable final String text, @NotNull final Container container,
                                       @NotNull final List<Component> components )
    {
        // Check children for text
        for ( final Component child : container.getComponents () )
        {
            findComponentsWithText ( text, child, components );
        }
    }

    /**
     * Returns component index within the specified parent container.
     *
     * @param container container
     * @param child     child component
     * @return component index within the specified parent container
     */
    public static int indexOf ( @NotNull final Container container, @Nullable final Component child )
    {
        int index = -1;
        for ( int i = 0; i < container.getComponentCount (); i++ )
        {
            if ( container.getComponent ( i ) == child )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns map of container child components preferred sizes.
     *
     * @param container container to process
     * @return map of container child components preferred sizes
     */
    @NotNull
    public static Map<Component, Dimension> getChildPreferredSizes ( @NotNull final Container container )
    {
        final Map<Component, Dimension> cps = new HashMap<Component, Dimension> ( container.getComponentCount () );
        for ( int i = 0; i < container.getComponentCount (); i++ )
        {
            final Component component = container.getComponent ( i );
            cps.put ( component, component.getPreferredSize () );
        }
        return cps;
    }

    /**
     * Makes all specified component sizes equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsSize ( @NotNull final Component... components )
    {
        equalizeComponentsSize ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component sizes equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsSize ( @NotNull final List<String> properties,
                                                @NotNull final Component... components )
    {
        equalizeComponentsSizeImpl ( true, true, properties, components );
    }

    /**
     * Makes all specified component sizes equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsSize ( @NotNull final List<? extends Component> components )
    {
        equalizeComponentsSize ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component sizes equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsSize ( @NotNull final List<String> properties,
                                                @NotNull final List<? extends Component> components )
    {
        equalizeComponentsSizeImpl ( true, true, properties, components.toArray ( new Component[ components.size () ] ) );
    }

    /**
     * Makes all specified component widths equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsWidth ( @NotNull final Component... components )
    {
        equalizeComponentsWidth ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component widths equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsWidth ( @NotNull final List<String> properties,
                                                 @NotNull final Component... components )
    {
        equalizeComponentsSizeImpl ( true, false, properties, components );
    }

    /**
     * Makes all specified component widths equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsWidth ( @NotNull final List<? extends Component> components )
    {
        equalizeComponentsWidth ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component widths equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsWidth ( @NotNull final List<String> properties,
                                                 @NotNull final List<? extends Component> components )
    {
        equalizeComponentsSizeImpl ( true, false, properties, components.toArray ( new Component[ components.size () ] ) );
    }

    /**
     * Makes all specified component heights equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsHeight ( @NotNull final Component... components )
    {
        equalizeComponentsHeight ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component heights equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsHeight ( @NotNull final List<String> properties,
                                                  @NotNull final Component... components )
    {
        equalizeComponentsSizeImpl ( false, true, properties, components );
    }

    /**
     * Makes all specified component heights equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsHeight ( @NotNull final List<? extends Component> components )
    {
        equalizeComponentsHeight ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component heights equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsHeight ( @NotNull final List<String> properties,
                                                  @NotNull final List<? extends Component> components )
    {
        equalizeComponentsSizeImpl ( false, true, properties, components.toArray ( new Component[ components.size () ] ) );
    }

    /**
     * Makes all specified component sizes equal.
     * Also provides appropriate property listeners to update sizes when needed.
     *
     * @param width      whether or not should equalize widths
     * @param height     whether or not should equalize heights
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    private static void equalizeComponentsSizeImpl ( final boolean width, final boolean height,
                                                     @NotNull final List<String> properties,
                                                     @NotNull final Component... components )
    {
        equalizeComponentsSizeImpl ( width, height, components );
        if ( CollectionUtils.notEmpty ( properties ) )
        {
            final PropertyChangeListener listener = new PropertyChangeListener ()
            {
                @Override
                public void propertyChange ( final PropertyChangeEvent e )
                {
                    resetComponentsSizeImpl ( width, height, components );
                    equalizeComponentsSizeImpl ( width, height, components );
                }
            };
            for ( final Component component : components )
            {
                for ( final String property : properties )
                {
                    component.addPropertyChangeListener ( property, listener );
                }
            }
        }
    }

    /**
     * Makes all specified component sizes equal.
     *
     * @param width      whether or not should equalize widths
     * @param height     whether or not should equalize heights
     * @param components components to modify
     */
    private static void equalizeComponentsSizeImpl ( final boolean width, final boolean height, @NotNull final Component... components )
    {
        final Dimension maxSize = new Dimension ( 0, 0 );
        for ( final Component component : components )
        {
            if ( component != null )
            {
                final Dimension ps = component.getPreferredSize ();
                maxSize.width = Math.max ( maxSize.width, ps.width );
                maxSize.height = Math.max ( maxSize.height, ps.height );
            }
        }
        for ( final Component component : components )
        {
            if ( component != null )
            {
                if ( component instanceof SizeMethods )
                {
                    final SizeMethods sizeMethods = ( SizeMethods ) component;
                    if ( width )
                    {
                        sizeMethods.setPreferredWidth ( maxSize.width );
                    }
                    if ( height )
                    {
                        sizeMethods.setPreferredHeight ( maxSize.height );
                    }
                }
                else
                {
                    final Dimension ps = component.getPreferredSize ();
                    if ( width && height )
                    {
                        component.setPreferredSize ( maxSize );
                    }
                    else if ( width )
                    {
                        component.setPreferredSize ( new Dimension ( maxSize.width, ps.height ) );
                    }
                    else if ( height )
                    {
                        component.setPreferredSize ( new Dimension ( ps.width, maxSize.height ) );
                    }
                }
            }
        }
    }

    /**
     * Returns default preferred sizes for all specified components.
     *
     * @param width      whether or not should restore widths
     * @param height     whether or not should restore heights
     * @param components components to reset
     */
    private static void resetComponentsSizeImpl ( final boolean width, final boolean height, @NotNull final Component... components )
    {
        for ( final Component component : components )
        {
            if ( component != null )
            {
                if ( component instanceof SizeMethods )
                {
                    final SizeMethods sizeMethods = ( SizeMethods ) component;
                    if ( width )
                    {
                        sizeMethods.setPreferredWidth ( SizeMethods.UNDEFINED );
                    }
                    if ( height )
                    {
                        sizeMethods.setPreferredHeight ( SizeMethods.UNDEFINED );
                    }
                }
                else
                {
                    component.setPreferredSize ( null );
                }
            }
        }
    }

    /**
     * Returns whether the first component or any of its children are equal to second component or not.
     *
     * @param component first component to compare
     * @param child     second component to compare
     * @return {@code true} if the first component or any of its children are equal to second component, {@code false} otherwise
     */
    public static boolean isEqualOrChild ( @Nullable final Component component, @Nullable final Component child )
    {
        boolean equalOrChild = false;
        if ( component == child )
        {
            equalOrChild = true;
        }
        else if ( component instanceof Container && child != null )
        {
            for ( final Component c : ( ( Container ) component ).getComponents () )
            {
                if ( isEqualOrChild ( c, child ) )
                {
                    equalOrChild = true;
                    break;
                }
            }
        }
        return equalOrChild;
    }

    /**
     * Returns whether component or any of its children has focus or not.
     *
     * @param component component to process
     * @return true if component or any of its children has focus, false otherwise
     */
    public static boolean hasFocusOwner ( @NotNull final Component component )
    {
        final Component focusOwner = FocusManager.getCurrentManager ().getFocusOwner ();
        return component == focusOwner || component instanceof Container && ( ( Container ) component ).isAncestorOf ( focusOwner );
    }

    /**
     * Returns whether at least one of child components within the specified container is focusable or not.
     *
     * @param container container to process
     * @return true if at least one of child components within the specified container is focusable, false otherwise
     */
    public static boolean hasFocusableComponent ( @NotNull final Container container )
    {
        boolean hasFocusableComponent = false;
        for ( final Component component : container.getComponents () )
        {
            if ( component.isFocusable () )
            {
                hasFocusableComponent = true;
                break;
            }
            else if ( component instanceof Container )
            {
                if ( hasFocusableComponent ( ( Container ) component ) )
                {
                    hasFocusableComponent = true;
                    break;
                }
            }
        }
        return hasFocusableComponent;
    }

    /**
     * Returns system font names array.
     *
     * @return system font names array
     */
    @NotNull
    public static String[] getFontNames ()
    {
        if ( fontNames == null )
        {
            fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment ().getAvailableFontFamilyNames ();
        }
        return fontNames;
    }

    /**
     * Returns system fonts array.
     *
     * @return system fonts array
     */
    @NotNull
    public static Font[] getFonts ()
    {
        if ( fonts == null )
        {
            fonts = createFonts ( getFontNames () );
        }
        return fonts;
    }

    /**
     * Creates fonts by their font-names
     */

    /**
     * Returns an array of fonts created using specified array of font names.
     *
     * @param fontNames array of font names
     * @return an array of fonts
     */
    @NotNull
    public static Font[] createFonts ( @NotNull final String[] fontNames )
    {
        final Font[] fonts = new Font[ fontNames.length ];
        for ( int i = 0; i < fontNames.length; i++ )
        {
            fonts[ i ] = new Font ( fontNames[ i ], Font.PLAIN, 13 );
        }
        return fonts;
    }

    /**
     * Returns insets converted into RTL orientation.
     *
     * @param insets insets to convert
     * @return insets converted into RTL orientation
     */
    @NotNull
    public static Insets toRTL ( @NotNull final Insets insets )
    {
        return new Insets ( insets.top, insets.right, insets.bottom, insets.left );
    }

    /**
     * Installs text antialiasing hints into specified graphics context.
     *
     * @param g graphics context
     * @return old text antialiasing hints
     */
    public static Map setupTextAntialias ( @NotNull final Graphics g )
    {
        return setupTextAntialias ( ( Graphics2D ) g, TextRasterization.subpixel.getRenderingHints () );
    }

    /**
     * Installs text antialiasing hints into specified graphics context.
     *
     * @param g2d graphics context
     * @return old text antialiasing hints
     */
    public static Map setupTextAntialias ( @NotNull final Graphics2D g2d )
    {
        return setupTextAntialias ( g2d, TextRasterization.subpixel.getRenderingHints () );
    }

    /**
     * Installs text antialiasing hints into specified graphics context.
     *
     * @param g             graphics context
     * @param rasterization text rasterization option
     * @return old text antialiasing hints
     */
    public static Map setupTextAntialias ( @NotNull final Graphics g, @NotNull final TextRasterization rasterization )
    {
        return setupTextAntialias ( ( Graphics2D ) g, rasterization.getRenderingHints () );
    }

    /**
     * Installs text antialiasing hints into specified graphics context.
     *
     * @param g2d           graphics context
     * @param rasterization text rasterization option
     * @return old text antialiasing hints
     */
    public static Map setupTextAntialias ( @NotNull final Graphics2D g2d, @NotNull final TextRasterization rasterization )
    {
        return setupTextAntialias ( g2d, rasterization.getRenderingHints () );
    }

    /**
     * Installs text antialiasing hints into specified graphics context.
     *
     * @param g2d   graphics context
     * @param hints text antialiasing hints
     * @return old text antialiasing hints
     */
    private static Map setupTextAntialias ( @NotNull final Graphics2D g2d, @Nullable final Map hints )
    {
        final Map oldHints;
        if ( hints != null )
        {
            // This is a workaround for native text rendering issue
            // Issue appears for some components with preset text rendering color like buttons
            // If the graphics doesn't recieve new instance of paint before the text is rendered it will ignore native rendering hints
            // This bug appears on all JDK versions and can only be avoided by graphics paint update
            final Paint paint = g2d.getPaint ();
            g2d.setPaint ( RENDERING_FIX_COLOR );
            g2d.setPaint ( paint );

            // Updating rendering hints
            oldHints = getRenderingHints ( g2d, hints, null );
            g2d.addRenderingHints ( hints );
        }
        else
        {
            oldHints = null;
        }
        return oldHints;
    }

    /**
     * Restores text antialiasing hints into specified graphics context
     *
     * @param g     graphics context
     * @param hints old text antialiasing hints
     */
    public static void restoreTextAntialias ( @NotNull final Graphics g, @Nullable final Map hints )
    {
        restoreTextAntialias ( ( Graphics2D ) g, hints );
    }

    /**
     * Restores text antialiasing hints into specified graphics context
     *
     * @param g2d   graphics context
     * @param hints old text antialiasing hints
     */
    public static void restoreTextAntialias ( @NotNull final Graphics2D g2d, @Nullable final Map hints )
    {
        if ( hints != null )
        {
            g2d.addRenderingHints ( hints );
        }
    }

    /**
     * Returns map with rendering hints from a Graphics instance.
     *
     * @param g2d         Graphics instance
     * @param hintsToSave map of RenderingHint key-values
     * @param savedHints  map to save hints into
     * @return map with rendering hints from a Graphics instance
     */
    @NotNull
    private static Map getRenderingHints ( @NotNull final Graphics2D g2d, @Nullable final Map hintsToSave, @Nullable Map savedHints )
    {
        if ( savedHints == null )
        {
            savedHints = new RenderingHints ( null );
        }
        else
        {
            savedHints.clear ();
        }
        if ( hintsToSave != null && hintsToSave.size () != 0 )
        {
            final Set objects = hintsToSave.keySet ();
            for ( final Object o : objects )
            {
                final RenderingHints.Key key = ( RenderingHints.Key ) o;
                final Object value = g2d.getRenderingHint ( key );
                if ( value != null )
                {
                    savedHints.put ( key, value );
                }
            }
        }
        return savedHints;
    }

    /**
     * Returns the FontMetrics for the current Font of the passed in Graphics.
     * This method is used when a Graphics is available, typically when painting.
     * If a Graphics is not available the JComponent method of the same name should be used.
     * <p>
     * This does not necessarily return the FontMetrics from the Graphics.
     *
     * @param component JComponent requesting FontMetrics, may be null
     * @param g         Graphics Graphics
     * @return FontMetrics for the current Font of the passed in Graphics
     */
    @NotNull
    public static FontMetrics getFontMetrics ( @Nullable final JComponent component, @NotNull final Graphics g )
    {
        return getFontMetrics ( component, g, g.getFont () );
    }

    /**
     * Returns the FontMetrics for the specified Font.
     * This method is used when a Graphics is available, typically when painting.
     * If a Graphics is not available the JComponent method of the same name should be used.
     * <p>
     * This does not necessarily return the FontMetrics from the Graphics.
     *
     * @param component JComponent requesting FontMetrics, may be null
     * @param g         Graphics Graphics
     * @param font      Font to get FontMetrics for
     * @return FontMetrics for the specified Font
     */
    @NotNull
    public static FontMetrics getFontMetrics ( @Nullable final JComponent component, @NotNull final Graphics g, @NotNull final Font font )
    {
        final FontMetrics fontMetrics;
        if ( component != null )
        {
            fontMetrics = component.getFontMetrics ( font );
        }
        else
        {
            fontMetrics = g.getFontMetrics ( font );
        }
        return fontMetrics;
    }

    /**
     * Fires painter property change event.
     * This is a workaround since {@code firePropertyChange()} method is protected and cannot be called w/o using reflection.
     *
     * @param component component to fire property change to
     * @param property  changed property
     * @param oldValue  old value
     * @param newValue  new value
     */
    public static void firePropertyChanged ( @NotNull final Component component, @NotNull final String property,
                                             @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        try
        {
            ReflectUtils.callMethod ( component, "firePropertyChange", property, oldValue, newValue );
        }
        catch ( final NoSuchMethodException e )
        {
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
        }
        catch ( final InvocationTargetException e )
        {
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
        }
        catch ( final IllegalAccessException e )
        {
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
        }
    }

    /**
     * Returns delay in milliseconds for the preferred frame rate.
     *
     * @param frameRate frame rate per second (FPS)
     * @return delay in milliseconds for the preferred frame rate
     */
    public static long frameRateDelay ( final int frameRate )
    {
        return Math.min ( 10L, 1000L / frameRate );
    }

    /**
     * Returns the width of the passed in String.
     * If the passed String is null, returns zero.
     *
     * @param fontMetrics FontMetrics used to measure the String width
     * @param string      String to get the width of
     * @return width of the passed in String
     */
    public static int stringWidth ( @NotNull final FontMetrics fontMetrics, @Nullable final String string )
    {
        return TextUtils.notEmpty ( string ) ? fontMetrics.stringWidth ( string ) : 0;
    }
}