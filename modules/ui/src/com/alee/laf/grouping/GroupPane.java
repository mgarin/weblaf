package com.alee.laf.grouping;

import com.alee.extended.painter.PainterSupport;
import com.alee.extended.painter.PartialDecoration;
import com.alee.laf.Styles;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleManager;
import com.alee.utils.LafUtils;
import com.alee.utils.laf.MarginSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Special container that can be used to visually group up various components.
 * It will force component to paint decoration according to its position in this container.
 * Note that this container will control some of the styling settings provided by skin to make components look better.
 *
 * @author Mikle Garin
 */

public class GroupPane extends WebPanel implements PropertyChangeListener, SwingConstants
{
    /**
     * Whether or not should display pane side components attached.
     */
    protected boolean attachTop = false;
    protected boolean attachLeft = false;
    protected boolean attachBottom = false;
    protected boolean attachRight = false;

    /**
     * Decorations cache.
     */
    protected transient Map<Component, PartialDecoration> decorations;

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param components components to group
     */
    public GroupPane ( final Component... components )
    {
        this ( SwingConstants.HORIZONTAL, Integer.MAX_VALUE, 1, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param orientation components flow orientation
     * @param components  components to group
     */
    public GroupPane ( final int orientation, final Component... components )
    {
        this ( orientation, Integer.MAX_VALUE, 1, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param columns    amount of columns used to place components
     * @param rows       amount of rows used to place components
     * @param components components to group
     */
    public GroupPane ( final int columns, final int rows, final Component... components )
    {
        this ( SwingConstants.HORIZONTAL, columns, rows, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param orientation components flow orientation
     * @param columns     amount of columns used to place components
     * @param rows        amount of rows used to place components
     * @param components  components to group
     */
    public GroupPane ( final int orientation, final int columns, final int rows, final Component... components )
    {
        super ( Styles.groupPane, createDefaultLayout ( orientation, columns, rows ), components );
        addPropertyChangeListener ( this );
    }

    /**
     * Overridden to inform when incorrect layout manager is provided.
     * {@link com.alee.laf.grouping.GroupPane} can use only {@link com.alee.laf.grouping.GroupPaneLayout}.
     *
     * @param mgr layout manager
     */
    @Override
    public void setLayout ( final LayoutManager mgr )
    {
        if ( !( mgr instanceof GroupPaneLayout ) )
        {
            throw new IllegalArgumentException ( "GroupPane supports only GroupPaneLayout" );
        }
        super.setLayout ( mgr );
    }

    /**
     * Returns {@link com.alee.laf.grouping.GroupPaneLayout}.
     *
     * @return {@link com.alee.laf.grouping.GroupPaneLayout}
     */
    @Override
    public GroupPaneLayout getLayout ()
    {
        return ( GroupPaneLayout ) super.getLayout ();
    }

    /**
     * Returns whether or not should display top pane side components attached.
     *
     * @return true if should display top pane side components attached, false otherwise
     */
    public boolean isAttachTop ()
    {
        return attachTop;
    }

    /**
     * Sets whether or not should display top pane side components attached.
     *
     * @param attachTop whether or not should display top pane side components attached
     */
    public void setAttachTop ( final boolean attachTop )
    {
        if ( this.attachTop != attachTop )
        {
            this.attachTop = attachTop;
            updateStyling ();
            revalidate ();
        }
    }

    /**
     * Returns whether or not should display left pane side components attached.
     *
     * @return true if should display left pane side components attached, false otherwise
     */
    public boolean isAttachLeft ()
    {
        return attachLeft;
    }

    /**
     * Sets whether or not should display left pane side components attached.
     *
     * @param attachTop whether or not should display left pane side components attached
     */
    public void setAttachLeft ( final boolean attachLeft )
    {
        if ( this.attachLeft != attachLeft )
        {
            this.attachLeft = attachLeft;
            updateStyling ();
            revalidate ();
        }
    }

    /**
     * Returns whether or not should display bottom pane side components attached.
     *
     * @return true if should display bottom pane side components attached, false otherwise
     */
    public boolean isAttachBottom ()
    {
        return attachBottom;
    }

    /**
     * Sets whether or not should display bottom pane side components attached.
     *
     * @param attachTop whether or not should display bottom pane side components attached
     */
    public void setAttachBottom ( final boolean attachBottom )
    {
        if ( this.attachBottom != attachBottom )
        {
            this.attachBottom = attachBottom;
            updateStyling ();
            revalidate ();
        }
    }

    /**
     * Returns whether or not should display right pane side components attached.
     *
     * @return true if should display right pane side components attached, false otherwise
     */
    public boolean isAttachRight ()
    {
        return attachRight;
    }

    /**
     * Sets whether or not should display right pane side components attached.
     *
     * @param attachTop whether or not should display right pane side components attached
     */
    public void setAttachRight ( final boolean attachRight )
    {
        if ( this.attachRight != attachRight )
        {
            this.attachRight = attachRight;
            updateStyling ();
            revalidate ();
        }
    }

    /**
     * Returns components placement order orientation.
     *
     * @return components placement order orientation
     */
    public int getOrientation ()
    {
        return getLayout ().getOrientation ();
    }

    /**
     * Sets components placement order orientation.
     *
     * @param orientation components placement order orientation
     */
    public void setOrientation ( final int orientation )
    {
        getLayout ().setOrientation ( orientation );
        updateStyling ();
        revalidate ();
    }

    /**
     * Returns amount of columns used to place components.
     *
     * @return amount of columns used to place components
     */
    public int getColumns ()
    {
        return getLayout ().getColumns ();
    }

    /**
     * Sets amount of columns used to place components.
     *
     * @param columns amount of columns to place components
     */
    public void setColumns ( final int columns )
    {
        getLayout ().setColumns ( columns );
        updateStyling ();
        revalidate ();
    }

    /**
     * Returns amount of rows used to place components.
     *
     * @return amount of rows used to place components
     */
    public int getRows ()
    {
        return getLayout ().getRows ();
    }

    /**
     * Sets amount of rows used to place components.
     *
     * @param rows amount of rows to place components
     */
    public void setRows ( final int rows )
    {
        getLayout ().setRows ( rows );
        updateStyling ();
        revalidate ();
    }

    /**
     * Adds specified component as new child.
     * Also ensures that added component is properly listened for changes.
     *
     * @param comp        added component
     * @param constraints component constraints
     * @param index       component index
     */
    @Override
    protected void addImpl ( final Component comp, final Object constraints, final int index )
    {
        super.addImpl ( comp, constraints, index );
        comp.addPropertyChangeListener ( this );
        updateStyling ();
    }

    /**
     * Removes child component at the specified index.
     * Ensures all data related to removed component is cleaned up.
     *
     * @param index component index
     */
    @Override
    public void remove ( final int index )
    {
        final Component component = getComponent ( index );
        component.removePropertyChangeListener ( this );
        super.remove ( index );
        StyleManager.applySkin ( ( JComponent ) component );
        updateStyling ();
    }

    /**
     * Listening to property changes that interest us.
     * These are basically properties that affect grouping display.
     *
     * @param evt property change event
     */
    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String propertyName = evt.getPropertyName ();
        if ( propertyName.equals ( WebLookAndFeel.PAINTER_PROPERTY ) )
        {
            decorations.remove ( evt.getSource () );
            updateStyling ();
        }
        else if ( propertyName.equals ( WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            repaint ();
        }
        else if ( propertyName.equals ( WebLookAndFeel.ORIENTATION_PROPERTY ) && evt.getSource () == this )
        {
            updateStyling ();
        }
    }

    /**
     * Forces group pane to update all underlying components styling.
     */
    protected void updateStyling ()
    {
        final GroupPaneLayout layout = getLayout ();

        // Retrieving actual grid size
        final GridSize gridSize = layout.getActualGridSize ( this );

        // Retrieving max shade width across all added components
        final int count = getComponentCount ();
        int shadeWidth = 0;
        for ( int i = 0; i < count; i++ )
        {
            final Component component = getComponent ( i );
            final PartialDecoration decoration = getPartialDecoration ( component );
            if ( decoration != null )
            {
                shadeWidth = Math.max ( shadeWidth, decoration.getShadeWidth () );
            }
        }

        // Updating styling
        for ( int row = 0; row < gridSize.rows; row++ )
        {
            for ( int col = 0; col < gridSize.columns; col++ )
            {
                final Component component = layout.getComponentAt ( this, col, row );
                final PartialDecoration decoration = getPartialDecoration ( component );
                if ( decoration != null )
                {
                    // Enabling decoration when grouped
                    decoration.setUndecorated ( false );

                    // Removing margins, we don't need them in group
                    final ComponentUI ui = LafUtils.getUI ( component );
                    if ( ui instanceof MarginSupport )
                    {
                        ( ( MarginSupport ) ui ).setMargin ( MarginSupport.EMPTY );
                    }

                    // Make shade widths even between all components
                    decoration.setShadeWidth ( shadeWidth );

                    // Configuring sides display
                    final boolean ltr = getComponentOrientation ().isLeftToRight ();
                    final boolean setupTop = isNeighbourSupportsPartialDecoration ( gridSize, col, row, TOP );
                    final boolean setupLeft = isNeighbourSupportsPartialDecoration ( gridSize, col, row, ltr ? LEFT : RIGHT );
                    final boolean setupBottom = isNeighbourSupportsPartialDecoration ( gridSize, col, row, BOTTOM );
                    final boolean setupRight = isNeighbourSupportsPartialDecoration ( gridSize, col, row, ltr ? RIGHT : LEFT );
                    if ( setupTop )
                    {
                        decoration.setPaintTop ( false );
                        decoration.setPaintTopLine ( false );
                    }
                    else if ( attachTop && row == 0 )
                    {
                        decoration.setPaintTop ( false );
                        decoration.setPaintTopLine ( false );
                    }
                    else
                    {
                        decoration.setPaintTop ( true );
                        decoration.setPaintTopLine ( false );
                    }
                    if ( setupLeft )
                    {
                        decoration.setPaintLeft ( false );
                        decoration.setPaintLeftLine ( false );
                    }
                    else if ( attachLeft && col == ( ltr ? 0 : gridSize.columns - 1 ) )
                    {
                        decoration.setPaintLeft ( false );
                        decoration.setPaintLeftLine ( false );
                    }
                    else
                    {
                        decoration.setPaintLeft ( true );
                        decoration.setPaintLeftLine ( false );
                    }
                    if ( setupBottom )
                    {
                        decoration.setPaintBottom ( false );
                        decoration.setPaintBottomLine ( true );
                    }
                    else if ( attachBottom && row == gridSize.rows - 1 )
                    {
                        decoration.setPaintBottom ( false );
                        decoration.setPaintBottomLine ( false );
                    }
                    else
                    {
                        decoration.setPaintBottom ( true );
                        decoration.setPaintBottomLine ( false );
                    }
                    if ( setupRight )
                    {
                        decoration.setPaintRight ( false );
                        decoration.setPaintRightLine ( true );
                    }
                    else if ( attachRight && col == ( ltr ? gridSize.columns - 1 : 0 ) )
                    {
                        decoration.setPaintRight ( false );
                        decoration.setPaintRightLine ( false );
                    }
                    else
                    {
                        decoration.setPaintRight ( true );
                        decoration.setPaintRightLine ( false );
                    }
                }
            }
        }
    }

    /**
     * Returns whether or not neighbour component supports partial decoration.
     *
     * @param gridSize  actual grid size
     * @param col       current component column
     * @param row       current component row
     * @param direction neighbour direction
     * @return true if neighbour component supports partial decoration, false otherwise
     */
    protected boolean isNeighbourSupportsPartialDecoration ( final GridSize gridSize, final int col, final int row, final int direction )
    {
        final Component neighbour = getNeighbour ( gridSize, col, row, direction );
        return neighbour != null && getPartialDecoration ( neighbour ) != null;
    }

    /**
     * Returns neighbour component.
     *
     * @param gridSize  actual grid size
     * @param col       current component column
     * @param row       current component row
     * @param direction neighbour direction
     * @return neighbour component
     */
    protected Component getNeighbour ( final GridSize gridSize, final int col, final int row, final int direction )
    {
        final GroupPaneLayout layout = getLayout ();
        if ( direction == TOP )
        {
            return row > 0 ? layout.getComponentAt ( this, col, row - 1 ) : null;
        }
        else if ( direction == LEFT )
        {
            return col > 0 ? layout.getComponentAt ( this, col - 1, row ) : null;
        }
        else if ( direction == BOTTOM )
        {
            return row < gridSize.rows - 1 ? layout.getComponentAt ( this, col, row + 1 ) : null;
        }
        else if ( direction == RIGHT )
        {
            return col < gridSize.columns - 1 ? layout.getComponentAt ( this, col + 1, row ) : null;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns component partial decoration if it is supported, {@code null} otherwise.
     * Returned result is cached to improve performance. Cache is cleared whenever related settings are changed.
     *
     * @param component component to retrieve partial decoration for
     * @return component partial decoration if it is supported, {@code null} otherwise
     */
    private PartialDecoration getPartialDecoration ( final Component component )
    {
        if ( decorations == null )
        {
            decorations = new HashMap<Component, PartialDecoration> ( getComponentCount () );
        }
        if ( decorations.containsKey ( component ) )
        {
            return decorations.get ( component );
        }
        else
        {
            final PartialDecoration decoration = PainterSupport.getPartialDecoration ( component );
            decorations.put ( component, decoration );
            return decoration;
        }
    }

    /**
     * Returns newly created default {@link com.alee.laf.grouping.GroupPane} layout.
     *
     * @param orientation components flow orientation
     * @param columns     amount of columns used to place components
     * @param rows        amount of rows used to place components
     * @return newly created default {@link com.alee.laf.grouping.GroupPane} layout
     */
    protected static GroupPaneLayout createDefaultLayout ( final int orientation, final int columns, final int rows )
    {
        return new GroupPaneLayout ( orientation, columns, rows );
    }

    /**
     * Returns newly created default {@link com.alee.laf.grouping.GroupPane} layout.
     *
     * @return newly created default {@link com.alee.laf.grouping.GroupPane} layout
     */
    protected static GroupPaneLayout createDefaultLayout ()
    {
        return new GroupPaneLayout ();
    }
}