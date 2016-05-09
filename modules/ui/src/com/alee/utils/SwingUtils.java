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

import com.alee.extended.date.WebCalendar;
import com.alee.extended.date.WebDateField;
import com.alee.extended.filechooser.WebFileChooserField;
import com.alee.extended.filechooser.WebPathField;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.log.Log;
import com.alee.utils.swing.EventPump;
import com.alee.utils.swing.SizeMethods;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.FocusManager;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineBreakMeasurer;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.text.AttributedString;
import java.util.*;
import java.util.List;

/**
 * This class provides a set of utilities to work with Swing components, their settings and events.
 *
 * @author Mikle Garin
 */

public final class SwingUtils extends CoreSwingUtils
{
    /**
     * Client property key that identifies that component can handle enabled state changes.
     */
    public static final String HANDLES_ENABLE_STATE = "HANDLES_ENABLE_STATE";

    /**
     * Constant transparent color used for text rendering fix.
     */
    public static final Color RENDERING_FIX_COLOR = new Color ( 231, 157, 94, 0 );

    /**
     * System shortcut modifier.
     */
    private static Integer systemShortcutModifier = null;

    /**
     * Label for default system font retrieval.
     */
    private static JLabel label = null;

    /**
     * System font names array.
     */
    private static String[] fontNames;

    /**
     * System fonts array.
     */
    private static Font[] fonts;

    /**
     * Threads for smooth component scrolling.
     */
    private static Thread scrollThread1;
    private static Thread scrollThread2;

    /**
     * Access to  charsBuffer is to be synchronized on charsBufferLock.
     */
    private static final int CHAR_BUFFER_SIZE = 100;
    private static final Object charsBufferLock = new Object ();
    private static char[] charsBuffer = new char[ CHAR_BUFFER_SIZE ];
    public static final FontRenderContext DEFAULT_FRC = new FontRenderContext ( null, false, false );

    /**
     * Most of applications use 10 or less fonts simultaneously.
     */
    private static final int STRONG_BEARING_CACHE_SIZE = 10;

    /**
     * Strong cache for the left and right side bearings for STRONG_BEARING_CACHE_SIZE most recently used fonts.
     */
    private static final BearingCacheEntry[] strongBearingCache = new BearingCacheEntry[ STRONG_BEARING_CACHE_SIZE ];

    /**
     * Next index to insert an entry into the strong bearing cache.
     */
    private static int strongBearingCacheNextIndex = 0;

    /**
     * Soft cache for the left and right side bearings.
     */
    private static final Set<SoftReference<BearingCacheEntry>> softBearingCache = new HashSet<SoftReference<BearingCacheEntry>> ();

    /**
     * Enables logging of all uncaught exceptions occured within EDT.
     */
    public static void enableEventQueueLogging ()
    {
        Toolkit.getDefaultToolkit ().getSystemEventQueue ().push ( new EventQueue ()
        {
            @Override
            protected void dispatchEvent ( final AWTEvent event )
            {
                try
                {
                    super.dispatchEvent ( event );
                }
                catch ( final Throwable e )
                {
                    Log.get ().error ( "Uncaught EventQueue exception: " + e, e );
                }
            }
        } );
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
     * Returns whether or not provided insets are empty.
     * {@code null} insets are considered as empty as well.
     *
     * @param insets insets to process
     * @return true if provided insets are empty, false otherwise
     */
    public static boolean isEmpty ( final Insets insets )
    {
        return insets == null || insets.top == 0 && insets.left == 0 && insets.bottom == 0 && insets.right == 0;
    }

    /**
     * Returns whether UI delegate should preserve current border on this component or not.
     *
     * @param component component to process
     * @return true if UI delegate should preserve current border on this component, false otherwise
     */
    public static boolean isPreserveBorders ( final JComponent component )
    {
        return getHonorUserBorders ( component ) && !isUIResource ( component.getBorder () );
    }

    /**
     * Returns whether or not specified border is a UI resource.
     *
     * @param border border to process
     * @return true if specified border is a UI resource, false otherwise
     */
    public static boolean isUIResource ( final Border border )
    {
        return border == null || border instanceof UIResource;
    }

    /**
     * Returns whether UI delegate should honor a user-specified border on this component or not.
     *
     * @param component component to process
     * @return true if UI delegate should honor a user-specified border on this component, false otherwise
     */
    public static boolean getHonorUserBorders ( final JComponent component )
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
    public static void setHonorUserBorders ( final JComponent component, final boolean honor )
    {
        component.putClientProperty ( WebLookAndFeel.PROPERTY_HONOR_USER_BORDER, honor );
    }

    /**
     * Returns whether event involves left mouse button or not.
     *
     * @param e mouse event
     * @return true if event involves left mouse button, false otherwise
     */
    public static boolean isLeftMouseButton ( final MouseEvent e )
    {
        return ( e.getModifiers () & InputEvent.BUTTON1_MASK ) != 0;
    }

    /**
     * Returns whether event involves middle mouse button or not.
     *
     * @param e mouse event
     * @return true if event involves middle mouse button, false otherwise
     */
    public static boolean isMiddleMouseButton ( final MouseEvent e )
    {
        return ( e.getModifiers () & InputEvent.BUTTON2_MASK ) == InputEvent.BUTTON2_MASK;
    }

    /**
     * Returns whether event involves right mouse button or not.
     *
     * @param e mouse event
     * @return true if event involves right mouse button, false otherwise
     */
    public static boolean isRightMouseButton ( final MouseEvent e )
    {
        return ( e.getModifiers () & InputEvent.BUTTON3_MASK ) == InputEvent.BUTTON3_MASK;
    }

    /**
     * Packs all table rows to their preferred height.
     *
     * @param table table to process
     */
    public static void packRowHeights ( final JTable table )
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
    public static void packColumnWidths ( final JTable table )
    {
        packColumnWidths ( table, 2 );
    }

    /**
     * Packs all table columns to their preferred width.
     *
     * @param table  table to process
     * @param margin column side margin
     */
    public static void packColumnWidths ( final JTable table, final int margin )
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
    public static void packColumnWidth ( final JTable table, final int col )
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
    public static void packColumnWidth ( final JTable table, final int col, final int margin )
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
        Component comp = renderer.getTableCellRendererComponent ( table, column.getHeaderValue (), false, false, 0, 0 );
        width = comp.getPreferredSize ().width;

        // Cells width
        for ( int r = 0; r < table.getRowCount (); r++ )
        {
            renderer = table.getCellRenderer ( r, col );
            comp = renderer.getTableCellRendererComponent ( table, table.getValueAt ( r, col ), false, false, r, col );
            width = Math.max ( width, comp.getPreferredSize ().width );
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
    public static boolean isPopupTrigger ( final MouseEvent e )
    {
        return e.isPopupTrigger () || SwingUtilities.isRightMouseButton ( e );
    }

    /**
     * Destroys container by destroying its children structure and removing all listeners.
     *
     * @param container container to destroy
     */
    public static void destroyContainer ( final Container container )
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
    public static List<Container> collectAllContainers ( final Container container )
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
    public static List<Container> collectAllContainers ( final Container container, final List<Container> containers )
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
     * Returns top component inside the specified container component at the specified point.
     *
     * @param component container component to process
     * @param x         X coordinate
     * @param y         Y coordinate
     * @return top component inside the specified container component at the specified point
     */
    public static Component getTopComponentAt ( final Component component, final int x, final int y )
    {
        final Component child = component.getComponentAt ( x, y );
        if ( child == component || !( child instanceof Container ) )
        {
            return component;
        }
        else
        {
            final Rectangle b = child.getBounds ();
            return getTopComponentAt ( child, x - b.x, y - b.y );
        }
    }

    /**
     * Displays the specified frame as modal to the owner frame.
     * Note that this method returns only after the modal frame is closed.
     * <p>
     * This method is a Swing hack and not recommended for real use.
     * Still it might be useful for some specific cases.
     *
     * @param frame frame to display as modal
     * @param owner owner frame
     */
    public static void showAsModal ( final Frame frame, final Frame owner )
    {
        frame.addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowOpened ( final WindowEvent e )
            {
                owner.setEnabled ( false );
            }

            @Override
            public void windowClosed ( final WindowEvent e )
            {
                owner.setEnabled ( true );
                frame.removeWindowListener ( this );
            }
        } );

        owner.addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowActivated ( final WindowEvent e )
            {
                if ( frame.isShowing () )
                {
                    frame.setExtendedState ( JFrame.NORMAL );
                    frame.toFront ();
                }
                else
                {
                    owner.removeWindowListener ( this );
                }
            }
        } );

        frame.setVisible ( true );
        try
        {
            new EventPump ( frame ).start ();
        }
        catch ( final Throwable throwable )
        {
            throw new RuntimeException ( throwable );
        }
    }

    /**
     * Groups all buttons inside this container and returns created button group.
     *
     * @param container container to process
     * @return created button group
     */
    public static ButtonGroup groupButtons ( final Container container )
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
    public static ButtonGroup groupButtons ( final Container container, final boolean recursive )
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
    public static void groupButtons ( final Container container, final boolean recursive, final ButtonGroup buttonGroup )
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
    public static ButtonGroup groupButtons ( final AbstractButton... buttons )
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
    public static void groupButtons ( final ButtonGroup buttonGroup, final AbstractButton... buttons )
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
    public static void copyOrientation ( final Component from, final Component to )
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
    public static void changeOrientation ( final Component component )
    {
        final boolean ltr = component.getComponentOrientation ().isLeftToRight ();
        component.applyComponentOrientation ( ltr ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT );
    }

    /**
     * Updates component orientation for all existing components.
     */
    public static void updateGlobalOrientations ()
    {
        updateGlobalOrientations ( WebLookAndFeel.getOrientation () );
    }

    /**
     * Sets specified component orientation for all existing components.
     *
     * @param orientation component orientation to set
     */
    public static void updateGlobalOrientations ( final ComponentOrientation orientation )
    {
        for ( final Window window : Window.getWindows () )
        {
            // Applying orientation
            window.applyComponentOrientation ( orientation );

            // Updating root pane
            final JRootPane rootPane = getRootPane ( window );
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
    public static void setOrientation ( final Component component )
    {
        setOrientation ( component, false );
    }

    /**
     * Sets component orientation to specified component if needed or if forced.
     *
     * @param component component to modify
     * @param forced    force orientation change
     */
    public static void setOrientation ( final Component component, final boolean forced )
    {
        final ComponentOrientation orientation = WebLookAndFeel.getOrientation ();
        if ( forced || orientation.isLeftToRight () != component.getComponentOrientation ().isLeftToRight () )
        {
            component.setComponentOrientation ( orientation );
        }
    }

    /**
     * Applies component orientation to specified component.
     *
     * @param component component to modify
     */
    public static void applyOrientation ( final Component component )
    {
        applyOrientation ( component, false );
    }

    /**
     * Applies component orientation to specified component if needed or if forced.
     *
     * @param component component to modify
     * @param forced    force orientation change
     */
    public static void applyOrientation ( final Component component, final boolean forced )
    {
        final ComponentOrientation orientation = WebLookAndFeel.getOrientation ();
        if ( forced || orientation.isLeftToRight () != component.getComponentOrientation ().isLeftToRight () )
        {
            component.applyComponentOrientation ( orientation );
        }
    }

    /**
     * Returns maximum component width.
     *
     * @param components components to process
     * @return maximum component width
     */
    public static int maxWidth ( final Component... components )
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
    public static int maxHeight ( final Component... components )
    {
        int max = 0;
        for ( final Component component : components )
        {
            max = Math.max ( max, component.getPreferredSize ().height );
        }
        return max;
    }

    /**
     * Returns whether specified window is a HeavyWeightWindow or not.
     *
     * @param window window to process
     * @return true if specified window is a HeavyWeightWindow, false otherwise
     */
    public static boolean isHeavyWeightWindow ( final Window window )
    {
        if ( window == null )
        {
            return false;
        }
        final String can = window.getClass ().getCanonicalName ();
        return can != null && can.endsWith ( "HeavyWeightWindow" );
    }

    /**
     * Returns first parent which is instance of specified class type or null if none found.
     *
     * @param component   component to look parent for
     * @param parentClass parent component class
     * @param <T>         parent component class type
     * @return first parent which is instance of specified class type or null if none found
     */
    public static <T extends Container> T getFirstParent ( final Component component, final Class<T> parentClass )
    {
        Component parent = component.getParent ();
        while ( !parentClass.isInstance ( parent ) && parent != null )
        {
            parent = parent.getParent ();
        }
        return ( T ) parent;
    }

    /**
     * Returns first component placed in the specified container which is instance of specified class type or null if none found.
     *
     * @param container      container to look for component in
     * @param componentClass component class
     * @param <T>            component class type
     * @return first component placed in the specified container which is instance of specified class type or null if none found
     */
    public static <T extends Component> T getFirst ( final Container container, final Class<T> componentClass )
    {
        return getFirst ( container, componentClass, false );
    }

    /**
     * Returns first component placed in the specified container which is instance of specified class type or null if none found.
     *
     * @param container      container to look for component in
     * @param componentClass component class
     * @param recursive      whether to check all sub-containers or not
     * @param <T>            component class type
     * @return first component placed in the specified container which is instance of specified class type or null if none found
     */
    public static <T extends Component> T getFirst ( final Container container, final Class<T> componentClass, final boolean recursive )
    {
        for ( int i = 0; i < container.getComponentCount (); i++ )
        {
            final Component component = container.getComponent ( i );
            if ( componentClass.isInstance ( component ) )
            {
                return ( T ) component;
            }
            if ( recursive )
            {
                if ( component instanceof Container )
                {
                    final T first = getFirst ( ( Container ) component, componentClass, recursive );
                    if ( first != null )
                    {
                        return first;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns first parent component which supports drag and drop actions.
     *
     * @param component component to look parent supporting drop for
     * @param <T>       parent supporting drop component class type
     * @return first parent component which supports drag and drop actions
     */
    public static <T extends JComponent> T getFirstParentSupportingDrop ( final Component component )
    {
        final Container parent = component.getParent ();
        if ( parent instanceof JComponent )
        {
            final JComponent c = ( JComponent ) parent;
            if ( c.getTransferHandler () != null )
            {
                return ( T ) c;
            }
        }
        return getFirstParentSupportingDrop ( parent );
    }

    /**
     * Returns root pane for the specified component or null if it doesn't exist.
     *
     * @param component component to look under
     * @return root pane for the specified component or null if it doesn't exist
     */
    public static JRootPane getRootPane ( final Component component )
    {
        if ( component == null )
        {
            return null;
        }
        else if ( component instanceof JFrame )
        {
            return ( ( JFrame ) component ).getRootPane ();
        }
        else if ( component instanceof JDialog )
        {
            return ( ( JDialog ) component ).getRootPane ();
        }
        else if ( component instanceof JWindow )
        {
            return ( ( JWindow ) component ).getRootPane ();
        }
        else if ( component instanceof JApplet )
        {
            return ( ( JApplet ) component ).getRootPane ();
        }
        else if ( component instanceof JRootPane )
        {
            return ( JRootPane ) component;
        }
        else
        {
            return getRootPane ( component.getParent () );
        }
    }

    /**
     * Returns first available visible application window.
     *
     * @return first available visible application window
     */
    public static Window getAvailableWindow ()
    {
        final Window activeWindow = SwingUtils.getActiveWindow ();
        if ( activeWindow != null )
        {
            if ( activeWindow instanceof JFrame || activeWindow instanceof JDialog || activeWindow instanceof JWindow )
            {
                // todo Ignore notification popup windows
                return activeWindow;
            }
        }
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
                        return window;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns content pane for the specified component or null if it doesn't exist.
     *
     * @param component component to look under
     * @return content pane for the specified component or null if it doesn't exist
     */
    public static Container getContentPane ( final Component component )
    {
        final JRootPane rootPane = getRootPane ( component );
        return rootPane != null ? rootPane.getContentPane () : null;
    }

    /**
     * Returns layered pane for the specified component or null if it doesn't exist.
     *
     * @param component component to look under
     * @return layered pane for the specified component or null if it doesn't exist
     */
    public static JLayeredPane getLayeredPane ( final Component component )
    {
        final JRootPane rootPane = getRootPane ( component );
        return rootPane != null ? rootPane.getLayeredPane () : null;
    }

    /**
     * Returns glass pane for the specified component or null if it doesn't exist.
     *
     * @param component component to look under
     * @return glass pane for the specified component or null if it doesn't exist
     */
    public static Component getGlassPane ( final Component component )
    {
        final JRootPane rootPane = getRootPane ( component );
        return rootPane != null ? rootPane.getGlassPane () : null;
    }

    /**
     * Returns whether component font is plain or not.
     *
     * @param component component to process
     * @return true if component font is plain, false otherwise
     */
    public static boolean isPlainFont ( final Component component )
    {
        return !( component != null && component.getFont () != null ) || component.getFont ().isPlain ();
    }

    /**
     * Changes font to plain for the specified component.
     *
     * @param component component to modify
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setPlainFont ( final C component )
    {
        return setPlainFont ( component, true );
    }

    /**
     * Changes font to plain for the specified component.
     *
     * @param component component to modify
     * @param apply     whether to apply font changes or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setPlainFont ( final C component, final boolean apply )
    {
        if ( apply && component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( Font.PLAIN ) );
        }
        return component;
    }

    /**
     * Returns whether component font is bold or not.
     *
     * @param component component to process
     * @return true if component font is bold, false otherwise
     */
    public static boolean isBoldFont ( final Component component )
    {
        return component != null && component.getFont () != null && component.getFont ().isBold ();
    }

    /**
     * Changes font to bold for the specified component.
     *
     * @param component component to modify
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setBoldFont ( final C component )
    {
        return setBoldFont ( component, true );
    }

    /**
     * Changes font to bold for the specified component.
     *
     * @param component component to modify
     * @param apply     whether to apply font changes or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setBoldFont ( final C component, final boolean apply )
    {
        if ( apply && component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( Font.BOLD ) );
        }
        return component;
    }

    /**
     * Returns whether component font is italic or not.
     *
     * @param component component to process
     * @return true if component font is italic, false otherwise
     */
    public static boolean isItalicFont ( final Component component )
    {
        return component != null && component.getFont () != null && component.getFont ().isItalic ();
    }

    /**
     * Changes font to italic for the specified component.
     *
     * @param component component to modify
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setItalicFont ( final C component )
    {
        return setItalicFont ( component, true );
    }

    /**
     * Changes font to italic for the specified component.
     *
     * @param component component to modify
     * @param apply     whether to apply font changes or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setItalicFont ( final C component, final boolean apply )
    {
        if ( apply && component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( Font.ITALIC ) );
        }
        return component;
    }

    /**
     * Sets font size of the specified component.
     *
     * @param component component to modify
     * @param fontSize  new font size
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontSize ( final C component, final int fontSize )
    {
        if ( component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( ( float ) fontSize ) );
        }
        return component;
    }

    /**
     * Changes font size of the specified component.
     *
     * @param component component to modify
     * @param change    font size change amount
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C changeFontSize ( final C component, final int change )
    {
        if ( component != null && component.getFont () != null )
        {
            final Font font = component.getFont ();
            component.setFont ( font.deriveFont ( ( float ) font.getSize () + change ) );
        }
        return component;
    }

    /**
     * Returns font size of the specified component.
     *
     * @param component component to process
     * @return font size of the specified component
     */
    public static int getFontSize ( final Component component )
    {
        if ( component != null && component.getFont () != null )
        {
            return component.getFont ().getSize ();
        }
        return -1;
    }

    /**
     * Sets font  style for the specified component.
     *
     * @param component component to modify
     * @param bold      whether should set bold font or not
     * @param italic    whether should set italic font or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontStyle ( final C component, final boolean bold, final boolean italic )
    {
        final int style = bold && italic ? Font.BOLD | Font.ITALIC : bold ? Font.BOLD : italic ? Font.ITALIC : Font.PLAIN;
        return setFontStyle ( component, style );
    }

    /**
     * Sets font  style for the specified component.
     *
     * @param component component to modify
     * @param style     new style
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontStyle ( final C component, final int style )
    {
        if ( component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( style ) );
        }
        return component;
    }

    /**
     * Sets font size and style for the specified component.
     *
     * @param component component to modify
     * @param fontSize  new font size
     * @param bold      whether should set bold font or not
     * @param italic    whether should set italic font or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontSizeAndStyle ( final C component, final int fontSize, final boolean bold,
                                                                final boolean italic )
    {
        final int style = bold && italic ? Font.BOLD | Font.ITALIC : bold ? Font.BOLD : italic ? Font.ITALIC : Font.PLAIN;
        return setFontSizeAndStyle ( component, fontSize, style );
    }

    /**
     * Sets font size and style for the specified component.
     *
     * @param component component to modify
     * @param fontSize  new font size
     * @param style     new style
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontSizeAndStyle ( final C component, final int fontSize, final int style )
    {
        if ( component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( style, ( float ) fontSize ) );
        }
        return component;
    }

    /**
     * Sets component font name.
     *
     * @param component component font name
     * @param fontName  new font name
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontName ( final C component, final String fontName )
    {
        if ( component != null && component.getFont () != null )
        {
            final Font oldFont = component.getFont ();
            component.setFont ( new Font ( fontName, oldFont.getStyle (), oldFont.getSize () ) );
        }
        return component;
    }

    /**
     * Returns component font name.
     *
     * @param component component to process
     * @return component font name
     */
    public static String getFontName ( final Component component )
    {
        if ( component != null && component.getFont () != null )
        {
            return component.getFont ().getFontName ();
        }
        return null;
    }

    /**
     * Returns maximum insets combined from the specified ones.
     *
     * @param insets1 first insets
     * @param insets2 second insets
     * @return maximum insets
     */
    public static Insets max ( final Insets insets1, final Insets insets2 )
    {
        if ( insets1 != null && insets2 != null )
        {
            return new Insets ( Math.max ( insets1.top, insets2.top ), Math.max ( insets1.left, insets2.left ),
                    Math.max ( insets1.bottom, insets2.bottom ), Math.max ( insets1.right, insets2.right ) );
        }
        else if ( insets1 != null )
        {
            return insets1;
        }
        else if ( insets2 != null )
        {
            return insets2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns minimum insets combined from the specified ones.
     *
     * @param insets1 first insets
     * @param insets2 second insets
     * @return minimum insets
     */
    public static Insets min ( final Insets insets1, final Insets insets2 )
    {
        if ( insets1 != null && insets2 != null )
        {
            return new Insets ( Math.min ( insets1.top, insets2.top ), Math.min ( insets1.left, insets2.left ),
                    Math.min ( insets1.bottom, insets2.bottom ), Math.min ( insets1.right, insets2.right ) );
        }
        else if ( insets1 != null )
        {
            return insets1;
        }
        else if ( insets2 != null )
        {
            return insets2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns maximum dimension combined from specified components dimensions.
     *
     * @param component1 first component
     * @param component2 second component
     * @return maximum dimension
     */
    public static Dimension max ( final Component component1, final Component component2 )
    {
        return max ( component1.getPreferredSize (), component2.getPreferredSize () );
    }

    /**
     * Returns maximum dimension combined from specified components dimensions.
     *
     * @param components components
     * @return maximum dimension
     */
    public static Dimension max ( final Component... components )
    {
        Dimension max = components.length > 0 ? components[ 0 ].getPreferredSize () : new Dimension ( 0, 0 );
        for ( int i = 1; i < components.length; i++ )
        {
            max = max ( max, components[ i ].getPreferredSize () );
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
    public static Dimension max ( final Dimension dimension1, final Dimension dimension2 )
    {
        if ( dimension1 == null && dimension2 == null )
        {
            return null;
        }
        else if ( dimension1 == null )
        {
            return dimension2;
        }
        else if ( dimension2 == null )
        {
            return dimension1;
        }
        else
        {
            return new Dimension ( Math.max ( dimension1.width, dimension2.width ), Math.max ( dimension1.height, dimension2.height ) );
        }
    }

    /**
     * Returns minimum dimension combined from specified components dimensions.
     *
     * @param component1 first component
     * @param component2 second component
     * @return minimum dimension
     */
    public static Dimension min ( final Component component1, final Component component2 )
    {
        return min ( component1.getPreferredSize (), component2.getPreferredSize () );
    }

    /**
     * Returns minimum dimension combined from specified ones.
     *
     * @param dimension1 first dimension
     * @param dimension2 second dimension
     * @return minimum dimension
     */
    public static Dimension min ( final Dimension dimension1, final Dimension dimension2 )
    {
        if ( dimension1 == null || dimension2 == null )
        {
            return null;
        }
        else
        {
            return new Dimension ( Math.min ( dimension1.width, dimension2.width ), Math.min ( dimension1.height, dimension2.height ) );
        }
    }

    /**
     * Returns rectange shrunk by provided insets.
     *
     * @param r      rectange to shrink
     * @param insets insets used to shrink bounds
     * @return rectange shrunk by provided insets
     */
    public static Rectangle shrink ( final Rectangle r, final Insets insets )
    {
        return insets != null ? new Rectangle ( r.x + insets.left, r.y + insets.top, r.width - insets.left - insets.right,
                r.height - insets.top - insets.bottom ) : new Rectangle ( r );
    }

    /**
     * Sets opaque state of component and all of its children.
     *
     * @param component component to modify
     * @param opaque    whether opaque state or not
     */
    public static void setOpaqueRecursively ( final Component component, final boolean opaque )
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
    public static void setOpaqueRecursively ( final Component component, final boolean opaque, final boolean childrenOnly )
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
    public static void setDoubleBufferedRecursively ( final Component component, final boolean doubleBuffered )
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
    public static void setDoubleBufferedRecursively ( final Component component, final boolean doubleBuffered, final boolean childrenOnly )
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
     * Adds HANDLES_ENABLE_STATE mark into component client properties.
     *
     * @param component component to process
     */
    public static void setHandlesEnableStateMark ( final JComponent component )
    {
        component.putClientProperty ( HANDLES_ENABLE_STATE, Boolean.TRUE );
    }

    /**
     * Removes HANDLES_ENABLE_STATE mark from component client properties.
     *
     * @param component component to process
     */
    public static void removeHandlesEnableStateMark ( final JComponent component )
    {
        component.putClientProperty ( HANDLES_ENABLE_STATE, Boolean.FALSE );
    }

    /**
     * Returns whether HANDLES_ENABLE_STATE mark is set in this component to true or not.
     *
     * @param component component to process
     * @return true if HANDLES_ENABLE_STATE mark is set in this component to true, false otherwise
     */
    public static boolean isHandlesEnableState ( final Component component )
    {
        if ( component instanceof JComponent )
        {
            final Object handlesEnabledState = ( ( JComponent ) component ).getClientProperty ( HANDLES_ENABLE_STATE );
            if ( handlesEnabledState != null && handlesEnabledState instanceof Boolean && ( Boolean ) handlesEnabledState )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets enabled state of component and all of its children.
     *
     * @param component component to modify
     * @param enabled   whether component is enabled or not
     */
    public static void setEnabledRecursively ( final Component component, final boolean enabled )
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
    public static void setEnabledRecursively ( final Component component, final boolean enabled, final boolean startFromChildren )
    {
        if ( component == null )
        {
            return;
        }
        if ( !startFromChildren )
        {
            component.setEnabled ( enabled );
        }
        if ( component instanceof Container )
        {
            if ( !startFromChildren && isHandlesEnableState ( component ) )
            {
                return;
            }
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
    public static List<Component> disableRecursively ( final Component component, final boolean startFromChildren,
                                                       final boolean excludePanels, final Component... excluded )
    {
        return disableRecursively ( component, startFromChildren, excludePanels, Arrays.asList ( excluded ) );
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
    public static List<Component> disableRecursively ( final Component component, final boolean startFromChildren,
                                                       final boolean excludePanels, final List<Component> excluded )
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
    private static void disableRecursively ( final Component component, final boolean startFromChildren, final boolean excludePanels,
                                             final List<Component> excluded, final List<Component> disabled )
    {
        final boolean b = !startFromChildren && !excluded.contains ( component ) && !( component instanceof JPanel && excludePanels );
        if ( b && component.isEnabled () )
        {
            component.setEnabled ( false );
            disabled.add ( component );
        }
        if ( component instanceof Container )
        {
            if ( b && isHandlesEnableState ( component ) )
            {
                return;
            }
            final Container container = ( Container ) component;
            for ( final Component child : container.getComponents () )
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
    public static void enable ( final List<Component> disabled )
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
    public static void setFocusableRecursively ( final JComponent component, final boolean focusable )
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
    public static void setFocusableRecursively ( final JComponent component, final boolean focusable, final boolean childrenOnly )
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
    public static void setBackgroundRecursively ( final Component component, final Color bg )
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
    public static void setBackgroundRecursively ( final Component component, final Color bg, final boolean childrenOnly )
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
    public static void setForegroundRecursively ( final JComponent component, final Color foreground )
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
    public static void setForegroundRecursively ( final JComponent component, final Color foreground, final boolean childrenOnly )
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
    public static void setFontRecursively ( final JComponent component, final Font font )
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
    public static void setFontRecursively ( final JComponent component, final Font font, final boolean childrenOnly )
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
    public static BufferedImage createComponentSnapshot ( final Component content )
    {
        return createComponentSnapshot ( content, content.getWidth (), content.getHeight () );
    }

    /**
     * Returns component snapshot image.
     * Component must be showing to render properly using this method.
     *
     * @param content component for snapshot
     * @param opacity snapshot opacity
     * @return component snapshot image
     */
    public static BufferedImage createComponentSnapshot ( final Component content, final float opacity )
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
    public static BufferedImage createComponentSnapshot ( final Component content, final int width, final int height )
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
    public static BufferedImage createComponentSnapshot ( final Component content, final int width, final int height, final float opacity )
    {
        // Creating snapshot
        final BufferedImage bi = ImageUtils.createCompatibleImage ( width, height, Transparency.TRANSLUCENT );
        if ( content != null )
        {
            final Graphics2D g2d = bi.createGraphics ();
            content.setSize ( width, height );
            content.paintAll ( g2d );
            g2d.dispose ();
        }

        // Making it transparent if needed
        if ( opacity < 1f )
        {
            final BufferedImage transparent = ImageUtils.createCompatibleImage ( width, height, Transparency.TRANSLUCENT );
            final Graphics2D g2d = transparent.createGraphics ();
            GraphicsUtils.setupAlphaComposite ( g2d, opacity );
            g2d.drawImage ( bi, 0, 0, null );
            g2d.dispose ();
            return transparent;
        }
        else
        {
            return bi;
        }
    }

    /**
     * Sets menu item accelerator using the specified hotkey data.
     *
     * @param menuItem menu item
     * @param hotkey   hotkey data
     */
    public static void setAccelerator ( final JMenuItem menuItem, final HotkeyData hotkey )
    {
        if ( hotkey != null && hotkey.isHotkeySet () )
        {
            final int ctrl = hotkey.isCtrl () ? getSystemShortcutModifier () : 0;
            final int alt = hotkey.isAlt () ? KeyEvent.ALT_MASK : 0;
            final int shift = hotkey.isShift () ? KeyEvent.SHIFT_MASK : 0;
            menuItem.setAccelerator ( KeyStroke.getKeyStroke ( hotkey.getKeyCode (), ctrl | alt | shift ) );
        }
    }

    /**
     * Returns active application window.
     *
     * @return active application window
     */
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
    public static boolean isShortcut ( final InputEvent event )
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
    public static String hotkeyToString ( final KeyEvent keyEvent )
    {
        return hotkeyToString ( isCtrl ( keyEvent ), isAlt ( keyEvent ), isShift ( keyEvent ), keyEvent.getKeyCode () );
    }

    /**
     * Returns readable form of specified hotkey data.
     *
     * @param hotkeyData hotkey data to process
     * @return readable form of specified hotkey data
     */
    public static String hotkeyToString ( final HotkeyData hotkeyData )
    {
        return hotkeyToString ( hotkeyData.isCtrl (), hotkeyData.isAlt (), hotkeyData.isShift (), hotkeyData.getKeyCode () );
    }

    /**
     * Returns readable form of specified key stroke.
     *
     * @param keyStroke key stroke to process
     * @return readable form of specified key stroke
     */
    public static String hotkeyToString ( final KeyStroke keyStroke )
    {
        return hotkeyToString ( isCtrl ( keyStroke.getModifiers () ), isAlt ( keyStroke.getModifiers () ),
                isShift ( keyStroke.getModifiers () ), keyStroke.getKeyCode () );
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
    public static String hotkeyToString ( final boolean isCtrl, final boolean isAlt, final boolean isShift, final Integer keyCode )
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
    public static boolean isCtrl ( final InputEvent event )
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
    public static boolean isAlt ( final InputEvent event )
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
    public static boolean isShift ( final InputEvent event )
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
    public static HotkeyData getHotkeyData ( final KeyStroke keyStroke )
    {
        final int m = keyStroke.getModifiers ();
        return new HotkeyData ( isCtrl ( m ), isAlt ( m ), isShift ( m ), keyStroke.getKeyCode () );
    }

    /**
     * Returns default label font.
     * This method might be used as a hack with other L&amp;Fs to retrieve system default font for simple text.
     *
     * @return default label font
     */
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
     * This method might be used as a hack with other L&amp;Fs to retrieve system default font metrics for simple text.
     *
     * @return default label font metrics
     */
    public static FontMetrics getDefaultLabelFontMetrics ()
    {
        if ( label == null )
        {
            label = new JLabel ();
        }
        return label.getFontMetrics ( label.getFont () );
    }

    /**
     * Returns scroll pane for specified component if exists, null otherwise.
     *
     * @param component component to process
     * @return scroll pane for specified component if exists, null otherwise
     */
    public static JScrollPane getScrollPane ( final Component component )
    {
        if ( component != null && component.getParent () != null && component.getParent () instanceof JViewport &&
                component.getParent ().getParent () != null && component.getParent ().getParent () instanceof JScrollPane )
        {
            return ( JScrollPane ) component.getParent ().getParent ();
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns first focusable component found in the container.
     *
     * @param container container to process
     * @return first focusable component found in the container
     */
    public static Component findFocusableComponent ( final Container container )
    {
        final FocusTraversalPolicy focusTraversalPolicy = container.getFocusTraversalPolicy ();
        if ( focusTraversalPolicy != null )
        {
            return focusTraversalPolicy.getFirstComponent ( container );
        }
        else
        {
            for ( final Component component : container.getComponents () )
            {
                if ( component.isFocusable () )
                {
                    return component;
                }
                if ( component instanceof Container )
                {
                    final Component focusable = findFocusableComponent ( ( Container ) component );
                    if ( focusable != null )
                    {
                        return focusable;
                    }
                }
            }
            return null;
        }
    }

    /**
     * Returns list of all components that visually contains the specified text.
     *
     * @param text      text to find
     * @param component component or container to look for specified text
     * @return list of all components that visually contains the specified text
     */
    public static List<Component> findComponentsWithText ( final String text, final Component component )
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
    public static List<Component> findComponentsWithText ( final String text, final Component component, final List<Component> components )
    {
        try
        {
            if ( text == null || text.equals ( "" ) )
            {
                return components;
            }
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
                if ( findComponentsWithText ( text, ( ( WebCollapsiblePane ) component ).getHeaderPanel () ).size () > 0 )
                {
                    components.add ( component );
                }
            }
            else if ( component instanceof JComboBox )
            {
                // todo Check each renderer for text
                final JComboBox comboBox = ( JComboBox ) component;
                if ( comboBox.getSelectedItem ().toString ().toLowerCase ( Locale.ROOT ).contains ( text.toLowerCase ( Locale.ROOT ) ) )
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
                                comboBox.getRenderer ().getListCellRendererComponent ( null, comboBox.getSelectedItem (), -1, true, true ) )
                                .size () > 0 )
                        {
                            components.add ( component );
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
                if ( ( ( AbstractButton ) component ).getText ().toLowerCase ( Locale.ROOT ).contains ( text.toLowerCase ( Locale.ROOT ) ) )
                {
                    components.add ( component );
                }
            }
            else if ( component instanceof JTextComponent )
            {
                // Check value for text
                if ( ( ( JTextComponent ) component ).getText ().toLowerCase ( Locale.ROOT ).contains ( text.toLowerCase ( Locale.ROOT ) ) )
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
        catch ( final Throwable e )
        {
            //
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
    private static void checkContent ( final String text, final Container container, final List<Component> components )
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
     * @param parent container
     * @param child  child component
     * @return component index within the specified parent container
     */
    public static int indexOf ( final Container parent, final Component child )
    {
        for ( int i = 0; i < parent.getComponentCount (); i++ )
        {
            if ( parent.getComponent ( i ) == child )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns screen bounds within which most part of the specified component is placed.
     *
     * @param component component to find screen bounds for
     * @return screen bounds within which most part of the specified component is placed
     */
    public static Rectangle getScreenBounds ( final Component component )
    {
        final Rectangle componentBounds = new Rectangle ( component.getLocationOnScreen (), component.getSize () );
        int maxIntersectionSize = 0;
        Rectangle screenBounds = null;
        for ( final GraphicsDevice device : SystemUtils.getGraphicsDevices () )
        {
            final Rectangle sb = device.getDefaultConfiguration ().getBounds ();
            final Rectangle intersection = sb.intersection ( componentBounds );
            if ( intersection.width > 0 && intersection.height > 0 )
            {
                final int size = intersection.width * intersection.height;
                if ( maxIntersectionSize < size )
                {
                    maxIntersectionSize = size;
                    screenBounds = sb;
                }
            }
        }
        return screenBounds;
    }

    /**
     * Returns component bounds on screen.
     *
     * @param component component to process
     * @return component bounds on screen
     */
    public static Rectangle getBoundsOnScreen ( final Component component )
    {
        return new Rectangle ( component.getLocationOnScreen (), component.getSize () );
    }

    /**
     * Returns component bounds inside its window.
     * This will return component bounds relative to window root pane location, not the window location.
     *
     * @param component component to process
     * @return component bounds inside its window
     */
    public static Rectangle getBoundsInWindow ( final Component component )
    {
        return component instanceof Window || component instanceof JApplet ? getRootPane ( component ).getBounds () :
                getRelativeBounds ( component, getRootPane ( component ) );
    }

    /**
     * Returns component bounds relative to another component.
     *
     * @param component  component to process
     * @param relativeTo component relative to which bounds will be returned
     * @return component bounds relative to another component
     */
    public static Rectangle getRelativeBounds ( final Component component, final Component relativeTo )
    {
        return new Rectangle ( getRelativeLocation ( component, relativeTo ), component.getSize () );
    }

    /**
     * Returns component location relative to another component.
     *
     * @param component  component to process
     * @param relativeTo component relative to which location will be returned
     * @return component location relative to another component
     */
    public static Point getRelativeLocation ( final Component component, final Component relativeTo )
    {
        final Point los = component.getLocationOnScreen ();
        final Point rt = relativeTo.getLocationOnScreen ();
        return new Point ( los.x - rt.x, los.y - rt.y );
    }

    /**
     * Returns whether specified components have the same ancestor or not.
     *
     * @param component1 first component
     * @param component2 second component
     * @return true if specified components have the same ancestor, false otherwise
     */
    public static boolean isSameAncestor ( final Component component1, final Component component2 )
    {
        return getWindowAncestor ( component1 ) == getWindowAncestor ( component2 );
    }

    /**
     * Returns map of container child components preferred sizes.
     *
     * @param container container to process
     * @return map of container child components preferred sizes
     */
    public static Map<Component, Dimension> getChildPreferredSizes ( final Container container )
    {
        final int cc = container.getComponentCount ();
        final Map<Component, Dimension> cps = new HashMap<Component, Dimension> ( cc );
        for ( int i = 0; i < cc; i++ )
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
    public static void equalizeComponentsSize ( final Component... components )
    {
        equalizeComponentsSize ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component sizes equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsSize ( final List<String> properties, final Component... components )
    {
        equalizeComponentsSizeImpl ( true, true, properties, components );
    }

    /**
     * Makes all specified component sizes equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsSize ( final List<? extends Component> components )
    {
        equalizeComponentsSize ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component sizes equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsSize ( final List<String> properties, final List<? extends Component> components )
    {
        equalizeComponentsSizeImpl ( true, true, properties, components.toArray ( new Component[ components.size () ] ) );
    }

    /**
     * Makes all specified component widths equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsWidth ( final Component... components )
    {
        equalizeComponentsWidth ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component widths equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsWidth ( final List<String> properties, final Component... components )
    {
        equalizeComponentsSizeImpl ( true, false, properties, components );
    }

    /**
     * Makes all specified component widths equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsWidth ( final List<? extends Component> components )
    {
        equalizeComponentsWidth ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component widths equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsWidth ( final List<String> properties, final List<? extends Component> components )
    {
        equalizeComponentsSizeImpl ( true, false, properties, components.toArray ( new Component[ components.size () ] ) );
    }

    /**
     * Makes all specified component heights equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsHeight ( final Component... components )
    {
        equalizeComponentsHeight ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component heights equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsHeight ( final List<String> properties, final Component... components )
    {
        equalizeComponentsSizeImpl ( false, true, properties, components );
    }

    /**
     * Makes all specified component heights equal.
     *
     * @param components components to modify
     */
    public static void equalizeComponentsHeight ( final List<? extends Component> components )
    {
        equalizeComponentsHeight ( Collections.<String>emptyList (), components );
    }

    /**
     * Makes all specified component heights equal.
     *
     * @param properties properties to listen for later size updates
     * @param components components to modify
     */
    public static void equalizeComponentsHeight ( final List<String> properties, final List<? extends Component> components )
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
    private static void equalizeComponentsSizeImpl ( final boolean width, final boolean height, final List<String> properties,
                                                     final Component... components )
    {
        equalizeComponentsSizeImpl ( width, height, components );
        if ( !CollectionUtils.isEmpty ( properties ) )
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
    private static void equalizeComponentsSizeImpl ( final boolean width, final boolean height, final Component... components )
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
    private static void resetComponentsSizeImpl ( final boolean width, final boolean height, final Component... components )
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
     * @param component1 first component to compare
     * @param component2 second component to compare
     * @return true if the first component or any of its children are equal to second component, false otherwise
     */
    public static boolean isEqualOrChild ( final Component component1, final Component component2 )
    {
        if ( component1 == component2 )
        {
            return true;
        }
        else if ( component1 == null && component2 != null || component1 != null && component2 == null )
        {
            return false;
        }
        else
        {
            if ( component1 instanceof Container )
            {
                for ( final Component c : ( ( Container ) component1 ).getComponents () )
                {
                    if ( isEqualOrChild ( c, component2 ) )
                    {
                        return true;
                    }
                }
                return false;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Returns whether component or any of its children has focus or not.
     *
     * @param component component to process
     * @return true if component or any of its children has focus, false otherwise
     */
    public static boolean hasFocusOwner ( final Component component )
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
    public static boolean hasFocusableComponent ( final Container container )
    {
        for ( final Component component : container.getComponents () )
        {
            if ( component.isFocusable () )
            {
                return true;
            }
            else if ( component instanceof Container )
            {
                if ( hasFocusableComponent ( ( Container ) component ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns system font names array.
     *
     * @return system font names array
     */
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
    public static Font[] createFonts ( final String[] fontNames )
    {
        final Font[] fonts = new Font[ fontNames.length ];
        for ( int i = 0; i < fontNames.length; i++ )
        {
            fonts[ i ] = new Font ( fontNames[ i ], Font.PLAIN, 13 );
        }
        return fonts;
    }

    /**
     * Will perform an "invokeLater" call when the specified delay time passes.
     *
     * @param delay    delay time in milliseconds
     * @param runnable runnable
     */
    public static void delayInvokeLater ( final long delay, final Runnable runnable )
    {
        WebTimer.delay ( "delayInvokeLater", delay, false, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                invokeLater ( runnable );
            }
        } );
    }

    /**
     * Will invoke the specified action later in EDT in case it is called from non-EDT thread.
     * Otherwise action will be performed immediately.
     *
     * @param runnable hotkey runnable
     * @param e        key event
     */
    public static void invokeLater ( final HotkeyRunnable runnable, final KeyEvent e )
    {
        if ( SwingUtilities.isEventDispatchThread () )
        {
            runnable.run ( e );
        }
        else
        {
            SwingUtilities.invokeLater ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    runnable.run ( e );
                }
            } );
        }
    }

    /**
     * Returns insets converted into RTL orientation.
     *
     * @param insets insets to convert
     * @return insets converted into RTL orientation
     */
    public static Insets toRTL ( final Insets insets )
    {
        return new Insets ( insets.top, insets.right, insets.bottom, insets.left );
    }

    /**
     * Scrolls scroll pane visible area smoothly to destination values.
     *
     * @param scrollPane scroll pane to scroll through
     * @param xValue     horizontal scroll bar value
     * @param yValue     vertical scroll bar value
     */
    public static void scrollSmoothly ( final JScrollPane scrollPane, int xValue, int yValue )
    {
        // todo 1. Use single thread to scroll through
        // todo 2. Make this method multiply usage possible
        // todo 3. Use timer instead of thread

        final JScrollBar hor = scrollPane.getHorizontalScrollBar ();
        final JScrollBar ver = scrollPane.getVerticalScrollBar ();

        final Dimension viewportSize = scrollPane.getViewport ().getSize ();
        xValue = xValue > hor.getMaximum () - viewportSize.width ? hor.getMaximum () - viewportSize.width : xValue;
        yValue = yValue > ver.getMaximum () - viewportSize.height ? ver.getMaximum () - viewportSize.height : yValue;
        final int x = xValue < 0 ? 0 : xValue;
        final int y = yValue < 0 ? 0 : yValue;


        final int xSign = hor.getValue () > x ? -1 : 1;
        final int ySign = ver.getValue () > y ? -1 : 1;

        final Thread scroller1 = new Thread ( new Runnable ()
        {
            @Override
            public void run ()
            {
                scrollThread1 = Thread.currentThread ();
                int lastValue = hor.getValue ();
                while ( lastValue != x )
                {
                    if ( scrollThread1 != Thread.currentThread () )
                    {
                        Thread.currentThread ().interrupt ();
                    }
                    if ( lastValue != x )
                    {
                        final int value = lastValue + xSign * Math.max ( Math.abs ( lastValue - x ) / 4, 1 );
                        lastValue = value;
                        SwingUtilities.invokeLater ( new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                hor.setValue ( value );
                            }
                        } );
                        if ( xSign < 0 && value == hor.getMinimum () || xSign > 0 && value == hor.getMaximum () )
                        {
                            break;
                        }
                    }
                    ThreadUtils.sleepSafely ( 25 );
                }
            }
        } );
        scroller1.setDaemon ( true );

        final Thread scroller2 = new Thread ( new Runnable ()
        {
            @Override
            public void run ()
            {
                scrollThread2 = Thread.currentThread ();
                int lastValue = ver.getValue ();
                while ( lastValue != y )
                {
                    if ( scrollThread2 != Thread.currentThread () )
                    {
                        Thread.currentThread ().interrupt ();
                    }
                    if ( lastValue != y )
                    {
                        final int value = lastValue + ySign * Math.max ( Math.abs ( lastValue - y ) / 4, 1 );
                        lastValue = value;
                        SwingUtilities.invokeLater ( new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                ver.setValue ( value );
                            }
                        } );
                        if ( ySign < 0 && value == ver.getMinimum () || ySign > 0 && value == ver.getMaximum () )
                        {
                            break;
                        }
                    }
                    ThreadUtils.sleepSafely ( 25 );
                }
            }
        } );
        scroller2.setDaemon ( true );

        scroller1.start ();
        scroller2.start ();
    }

    /**
     * Paints string with underlined character at the specified index.
     *
     * @param g               graphics context
     * @param text            painted text
     * @param underlinedIndex underlined character index
     * @param x               text X coordinate
     * @param y               text Y coordinate
     */
    public static void drawStringUnderlineCharAt ( final Graphics g, final String text, final int underlinedIndex, final int x,
                                                   final int y )
    {
        // Painting string
        drawString ( g, text, x, y );

        // Painting character underline
        if ( underlinedIndex >= 0 && underlinedIndex < text.length () )
        {
            final FontMetrics fm = g.getFontMetrics ();
            g.fillRect ( x + fm.stringWidth ( text.substring ( 0, underlinedIndex ) ), y + fm.getDescent () - 1,
                    fm.charWidth ( text.charAt ( underlinedIndex ) ), 1 );
        }
    }

    /**
     * Paints string.
     *
     * @param g    graphics context
     * @param text painted text
     * @param x    text X coordinate
     * @param y    text Y coordinate
     */
    public static void drawString ( final Graphics g, final String text, final int x, final int y )
    {
        g.drawString ( text, x, y );
    }

    /**
     * Installs text antialiasing hints into specified graphics context.
     *
     * @param g graphics context
     * @return old text antialiasing hints
     */
    public static Map setupTextAntialias ( final Graphics g )
    {
        return setupTextAntialias ( ( Graphics2D ) g );
    }

    /**
     * Installs text antialiasing hints into specified graphics context.
     *
     * @param g2d graphics context
     * @return old text antialiasing hints
     */
    public static Map setupTextAntialias ( final Graphics2D g2d )
    {
        return setupTextAntialias ( g2d, StyleConstants.textRenderingHints );
    }

    /**
     * Installs text antialiasing hints into specified graphics context.
     *
     * @param g     graphics context
     * @param hints text antialiasing hints
     * @return old text antialiasing hints
     */
    public static Map setupTextAntialias ( final Graphics g, final Map hints )
    {
        return setupTextAntialias ( ( Graphics2D ) g, hints );
    }

    /**
     * Installs text antialiasing hints into specified graphics context.
     *
     * @param g2d   graphics context
     * @param hints text antialiasing hints
     * @return old text antialiasing hints
     */
    public static Map setupTextAntialias ( final Graphics2D g2d, final Map hints )
    {
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
            final Map oldHints = getRenderingHints ( g2d, hints, null );
            g2d.addRenderingHints ( hints );
            return oldHints;
        }
        else
        {
            return null;
        }
    }

    /**
     * Restores text antialiasing hints into specified graphics context
     *
     * @param g     graphics context
     * @param hints old text antialiasing hints
     */
    public static void restoreTextAntialias ( final Graphics g, final Map hints )
    {
        restoreTextAntialias ( ( Graphics2D ) g, hints );
    }

    /**
     * Restores text antialiasing hints into specified graphics context
     *
     * @param g2d   graphics context
     * @param hints old text antialiasing hints
     */
    public static void restoreTextAntialias ( final Graphics2D g2d, final Map hints )
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
    private static Map getRenderingHints ( final Graphics2D g2d, final Map hintsToSave, Map savedHints )
    {
        if ( savedHints == null )
        {
            savedHints = new RenderingHints ( null );
        }
        else
        {
            savedHints.clear ();
        }
        if ( hintsToSave == null || hintsToSave.size () == 0 )
        {
            return savedHints;
        }
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
        return savedHints;
    }

    /**
     * Returns the FontMetrics for the current Font of the passed in Graphics.
     * This method is used when a Graphics is available, typically when painting.
     * If a Graphics is not available the JComponent method of the same name should be used.
     * <p>
     * This does not necessarily return the FontMetrics from the Graphics.
     *
     * @param c JComponent requesting FontMetrics, may be null
     * @param g Graphics Graphics
     * @return FontMetrics for the current Font of the passed in Graphics
     */
    public static FontMetrics getFontMetrics ( final JComponent c, final Graphics g )
    {
        return getFontMetrics ( c, g, g.getFont () );
    }

    /**
     * Returns the FontMetrics for the specified Font.
     * This method is used when a Graphics is available, typically when painting.
     * If a Graphics is not available the JComponent method of the same name should be used.
     * <p>
     * This does not necessarily return the FontMetrics from the Graphics.
     *
     * @param c    JComponent requesting FontMetrics, may be null
     * @param g    Graphics Graphics
     * @param font Font to get FontMetrics for
     * @return FontMetrics for the specified Font
     */
    public static FontMetrics getFontMetrics ( final JComponent c, final Graphics g, final Font font )
    {
        if ( c != null )
        {
            return c.getFontMetrics ( font );
        }
        else
        {
            return g.getFontMetrics ( font );
        }
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
    public static void firePropertyChanged ( final Component component, final String property, final Object oldValue,
                                             final Object newValue )
    {
        try
        {
            ReflectUtils.callMethod ( component, "firePropertyChange", property, oldValue, newValue );
        }
        catch ( final NoSuchMethodException e )
        {
            Log.error ( LafUtils.class, e );
        }
        catch ( final InvocationTargetException e )
        {
            Log.error ( LafUtils.class, e );
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( LafUtils.class, e );
        }
    }

    /**
     * Returns the width of the passed in String.
     * If the passed String is null, returns zero.
     *
     * @param fm     FontMetrics used to measure the String width
     * @param string String to get the width of
     * @return width of the passed in String
     */
    public static int stringWidth ( final FontMetrics fm, final String string )
    {
        if ( string == null || string.equals ( "" ) )
        {
            return 0;
        }
        return fm.stringWidth ( string );
    }

    /**
     * Clips the passed in String to the space provided.
     *
     * @param c              JComponent that will display the string, may be null
     * @param fm             FontMetrics used to measure the String width
     * @param string         String to display
     * @param availTextWidth Amount of space that the string can be drawn in
     * @return Clipped string that can fit in the provided space.
     */
    public static String clipStringIfNecessary ( final JComponent c, final FontMetrics fm, final String string, final int availTextWidth )
    {
        if ( ( string == null ) || string.equals ( "" ) )
        {
            return "";
        }
        final int textWidth = stringWidth ( fm, string );
        if ( textWidth > availTextWidth )
        {
            return clipString ( c, fm, string, availTextWidth );
        }
        return string;
    }

    /**
     * Clips the passed in String to the space provided.  NOTE: this assumes
     * the string does not fit in the available space.
     *
     * @param c              JComponent that will display the string, may be null
     * @param fm             FontMetrics used to measure the String width
     * @param string         String to display
     * @param availTextWidth Amount of space that the string can be drawn in
     * @return Clipped string that can fit in the provided space.
     */
    public static String clipString ( final JComponent c, final FontMetrics fm, String string, int availTextWidth )
    {
        // c may be null here.
        final String clipString = "...";
        final int stringLength = string.length ();
        availTextWidth -= stringWidth ( fm, clipString );
        if ( availTextWidth <= 0 )
        {
            //can not fit any characters
            return clipString;
        }

        final boolean needsTextLayout;
        synchronized ( charsBufferLock )
        {
            if ( charsBuffer == null || charsBuffer.length < stringLength )
            {
                charsBuffer = string.toCharArray ();
            }
            else
            {
                string.getChars ( 0, stringLength, charsBuffer, 0 );
            }
            needsTextLayout = FontUtils.isComplexLayout ( charsBuffer, 0, stringLength );
            if ( !needsTextLayout )
            {
                int width = 0;
                for ( int nChars = 0; nChars < stringLength; nChars++ )
                {
                    width += fm.charWidth ( charsBuffer[ nChars ] );
                    if ( width > availTextWidth )
                    {
                        string = string.substring ( 0, nChars );
                        break;
                    }
                }
            }
        }
        if ( needsTextLayout )
        {
            final FontRenderContext frc = getFontRenderContext ( c, fm );
            final AttributedString aString = new AttributedString ( string );
            final LineBreakMeasurer measurer = new LineBreakMeasurer ( aString.getIterator (), frc );
            final int nChars = measurer.nextOffset ( availTextWidth );
            string = string.substring ( 0, nChars );

        }
        return string + clipString;
    }

    /**
     * Returns FontRenderContext associated with Component.
     * FontRenderContext from Component.getFontMetrics is associated
     * with the component.
     * <p>
     * Uses Component.getFontMetrics to get the FontRenderContext from.
     * see JComponent.getFontMetrics and TextLayoutStrategy.java
     *
     * @param c  Component
     * @param fm font metrics
     * @return FontRenderContext associated with Component
     */
    public static FontRenderContext getFontRenderContext ( final Component c, final FontMetrics fm )
    {
        return c == null ? DEFAULT_FRC : fm.getFontRenderContext ();
    }

    /**
     * Returns the left side bearing of the first character of string.
     * The left side bearing is calculated from the passed in FontMetrics.
     *
     * @param c      JComponent that will display the string
     * @param fm     FontMetrics used to measure the String width
     * @param string String to get the left side bearing for
     * @return left side bearing of the first character of string
     */
    public static int getLeftSideBearing ( final JComponent c, final FontMetrics fm, final String string )
    {
        if ( ( string == null ) || ( string.length () == 0 ) )
        {
            return 0;
        }
        return getLeftSideBearing ( c, fm, string.charAt ( 0 ) );
    }

    /**
     * Returns the left side bearing of the specified character.
     * The left side bearing is calculated from the passed in FontMetrics.
     *
     * @param c         JComponent that will display the string
     * @param fm        FontMetrics used to measure the String width
     * @param firstChar Character to get the left side bearing for
     * @return left side bearing of the specified character
     */
    public static int getLeftSideBearing ( final JComponent c, final FontMetrics fm, final char firstChar )
    {
        return getBearing ( c, fm, firstChar, true );
    }

    /**
     * Returns the right side bearing of the last character of string.
     * The right side bearing is calculated from the passed in FontMetrics.
     *
     * @param c      JComponent that will display the string
     * @param fm     FontMetrics used to measure the String width
     * @param string String to get the right side bearing for
     * @return right side bearing of the last character of string
     */
    public static int getRightSideBearing ( final JComponent c, final FontMetrics fm, final String string )
    {
        if ( ( string == null ) || ( string.length () == 0 ) )
        {
            return 0;
        }
        return getRightSideBearing ( c, fm, string.charAt ( string.length () - 1 ) );
    }

    /**
     * Returns the right side bearing of the specified character.
     * The right side bearing is calculated from the passed in FontMetrics.
     *
     * @param c        JComponent that will display the string
     * @param fm       FontMetrics used to measure the String width
     * @param lastChar Character to get the right side bearing for
     * @return right side bearing of the specified character
     */
    public static int getRightSideBearing ( final JComponent c, final FontMetrics fm, final char lastChar )
    {
        return getBearing ( c, fm, lastChar, false );
    }

    /**
     * Returns the left and right side bearing for a character.
     * Strongly caches bearings for STRONG_BEARING_CACHE_SIZE most recently used Fonts and softly caches as many as GC allows.
     *
     * @param comp          JComponent that will display the string
     * @param fm            FontMetrics used to measure the String width
     * @param c             Character to get the right side bearing for
     * @param isLeftBearing whether left bearing is calculated or not
     * @return the left and right side bearing for a character
     */
    private static int getBearing ( final JComponent comp, FontMetrics fm, final char c, final boolean isLeftBearing )
    {
        if ( fm == null )
        {
            if ( comp == null )
            {
                return 0;
            }
            else
            {
                fm = comp.getFontMetrics ( comp.getFont () );
            }
        }
        synchronized ( SwingUtils.class )
        {
            final BearingCacheEntry searchKey = new BearingCacheEntry ( fm );
            BearingCacheEntry entry = null;
            // See if we already have an entry in the strong cache
            for ( final BearingCacheEntry cacheEntry : strongBearingCache )
            {
                if ( searchKey.equals ( cacheEntry ) )
                {
                    entry = cacheEntry;
                    break;
                }
            }
            // See if we already have an entry in the soft cache
            if ( entry == null )
            {
                final Iterator<SoftReference<BearingCacheEntry>> iterator = softBearingCache.iterator ();
                while ( iterator.hasNext () )
                {
                    final BearingCacheEntry cacheEntry = iterator.next ().get ();
                    if ( cacheEntry == null )
                    {
                        // Remove discarded soft reference from the cache
                        iterator.remove ();
                        continue;
                    }
                    if ( searchKey.equals ( cacheEntry ) )
                    {
                        entry = cacheEntry;
                        putEntryInStrongCache ( entry );
                        break;
                    }
                }
            }
            if ( entry == null )
            {
                // No entry, add it
                entry = searchKey;
                cacheEntry ( entry );
            }
            return ( isLeftBearing ) ? entry.getLeftSideBearing ( c ) : entry.getRightSideBearing ( c );
        }
    }

    /**
     * Adds entry into cache.
     *
     * @param entry bearing cache entry
     */
    private synchronized static void cacheEntry ( final BearingCacheEntry entry )
    {
        // Move the oldest entry from the strong cache into the soft cache
        final BearingCacheEntry oldestEntry = strongBearingCache[ strongBearingCacheNextIndex ];
        if ( oldestEntry != null )
        {
            softBearingCache.add ( new SoftReference<BearingCacheEntry> ( oldestEntry ) );
        }
        // Put entry in the strong cache
        putEntryInStrongCache ( entry );
    }

    /**
     * Adds entry into strong cache.
     *
     * @param entry bearing cache entry
     */
    private synchronized static void putEntryInStrongCache ( final BearingCacheEntry entry )
    {
        strongBearingCache[ strongBearingCacheNextIndex ] = entry;
        strongBearingCacheNextIndex = ( strongBearingCacheNextIndex + 1 ) % STRONG_BEARING_CACHE_SIZE;
    }

    /**
     * BearingCacheEntry is used to cache left and right character bearings for a particular Font and FontRenderContext.
     */
    private static class BearingCacheEntry
    {
        private final FontMetrics fontMetrics;
        private final Font font;
        private final FontRenderContext frc;
        private final Map<Character, Short> cache;
        private static final char[] oneChar = new char[ 1 ];

        /**
         * Constructs new BearingCacheEntry.
         *
         * @param fontMetrics font metrics
         */
        public BearingCacheEntry ( final FontMetrics fontMetrics )
        {
            this.fontMetrics = fontMetrics;
            this.font = fontMetrics.getFont ();
            this.frc = fontMetrics.getFontRenderContext ();
            this.cache = new HashMap<Character, Short> ();
            assert font != null && frc != null;
        }

        /**
         * Returns left side bearing for the specified character.
         *
         * @param aChar character to calculate left side bearings for
         * @return left side bearing for the specified character
         */
        public int getLeftSideBearing ( final char aChar )
        {
            Short bearing = cache.get ( aChar );
            if ( bearing == null )
            {
                bearing = calcBearing ( aChar );
                cache.put ( aChar, bearing );
            }
            return ( ( 0xFF00 & bearing ) >>> 8 ) - 127;
        }

        /**
         * Returns right side bearing for the specified character.
         *
         * @param aChar character to calculate right side bearings for
         * @return right side bearing for the specified character
         */
        public int getRightSideBearing ( final char aChar )
        {
            Short bearing = cache.get ( aChar );
            if ( bearing == null )
            {
                bearing = calcBearing ( aChar );
                cache.put ( aChar, bearing );
            }
            return ( 0xFF & bearing ) - 127;
        }

        /**
         * Calculates and returns left and right side bearings for a character.
         * Makes an assumption that bearing is a value between -127 and +127.
         * Stores LSB and RSB as single two-byte number (short): LSB is the high byte, RSB is the low byte.
         *
         * @param aChar character to calculate left and right side bearings for
         * @return left and right side bearings for the specified character
         */
        private short calcBearing ( final char aChar )
        {
            oneChar[ 0 ] = aChar;
            final GlyphVector gv = font.createGlyphVector ( frc, oneChar );
            final Rectangle pixelBounds = gv.getGlyphPixelBounds ( 0, frc, 0f, 0f );

            // Get bearings
            int lsb = pixelBounds.x;
            int rsb = pixelBounds.width - fontMetrics.charWidth ( aChar );

            // HRGB/HBGR LCD glyph images will always have a pixel on the left and a pixel on the right used in colour fringe reduction
            // Text rendering positions this correctly but here we are using the glyph image to adjust that position so must account for it
            if ( lsb < 0 )
            {
                final Object aaHint = frc.getAntiAliasingHint ();
                if ( aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB || aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR )
                {
                    lsb++;
                }
            }
            if ( rsb > 0 )
            {
                final Object aaHint = frc.getAntiAliasingHint ();
                if ( aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB || aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR )
                {
                    rsb--;
                }
            }

            // Make sure that LSB and RSB are valid (see 6472972)
            if ( lsb < -127 || lsb > 127 )
            {
                lsb = 0;
            }
            if ( rsb < -127 || rsb > 127 )
            {
                rsb = 0;
            }

            final int bearing = ( ( lsb + 127 ) << 8 ) + rsb + 127;
            return ( short ) bearing;
        }

        @Override
        public boolean equals ( final Object entry )
        {
            if ( entry == this )
            {
                return true;
            }
            if ( !( entry instanceof BearingCacheEntry ) )
            {
                return false;
            }
            final BearingCacheEntry oEntry = ( BearingCacheEntry ) entry;
            return font.equals ( oEntry.font ) && frc.equals ( oEntry.frc );
        }

        @Override
        public int hashCode ()
        {
            int result = 17;
            if ( font != null )
            {
                result = 37 * result + font.hashCode ();
            }
            if ( frc != null )
            {
                result = 37 * result + frc.hashCode ();
            }
            return result;
        }
    }
}
