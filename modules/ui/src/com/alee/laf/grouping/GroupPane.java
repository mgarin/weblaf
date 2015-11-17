package com.alee.laf.grouping;

import com.alee.painter.PainterSupport;
import com.alee.painter.PartialDecoration;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.MarginSupport;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.skin.Skin;
import com.alee.managers.style.skin.StyleListener;
import com.alee.utils.LafUtils;

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

public class GroupPane extends WebPanel implements StyleListener, PropertyChangeListener, SwingConstants
{
    /**
     * todo 1. Instead of forcing custom settings onto decoration - move logic INTO decoration (WebDecorationPainter)
     * todo 2. Leave only some update listeners and optional features here
     */

    /**
     * Whether or not should visually group provided children components.
     * It is always enabled by default but can be disabled if required.
     * Disabling this option will automatically ungroup all components.
     */
    protected Boolean group;

    /**
     * Whether or not should group toggle state elements like togglebuttons, radiobuttons or checkboxes.
     * Only elements placed straight within this {@link com.alee.laf.grouping.GroupPane} are grouped.
     */
    protected Boolean groupButtons;

    /**
     * Button group used to group toggle state elements placed within this group pane.
     * Only elements placed straight within this {@link com.alee.laf.grouping.GroupPane} are grouped.
     *
     * @see javax.swing.ButtonGroup
     * @see com.alee.utils.swing.UnselectableButtonGroup
     */
    protected ButtonGroup buttonGroup;

    /**
     * Whether or not should display pane side components attached.
     */
    protected Boolean attachTop;
    protected Boolean attachLeft;
    protected Boolean attachBottom;
    protected Boolean attachRight;

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
        this ( StyleId.grouppane, SwingConstants.HORIZONTAL, Integer.MAX_VALUE, 1, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param orientation components flow orientation
     * @param components  components to group
     */
    public GroupPane ( final int orientation, final Component... components )
    {
        this ( StyleId.grouppane, orientation, Integer.MAX_VALUE, 1, components );
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
        this ( StyleId.grouppane, SwingConstants.HORIZONTAL, columns, rows, components );
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
        this ( StyleId.grouppane, orientation, columns, rows, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param id         style ID
     * @param components components to group
     */
    public GroupPane ( final StyleId id, final Component... components )
    {
        this ( id, SwingConstants.HORIZONTAL, Integer.MAX_VALUE, 1, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param id          style ID
     * @param orientation components flow orientation
     * @param components  components to group
     */
    public GroupPane ( final StyleId id, final int orientation, final Component... components )
    {
        this ( id, orientation, Integer.MAX_VALUE, 1, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param id         style ID
     * @param columns    amount of columns used to place components
     * @param rows       amount of rows used to place components
     * @param components components to group
     */
    public GroupPane ( final StyleId id, final int columns, final int rows, final Component... components )
    {
        this ( id, SwingConstants.HORIZONTAL, columns, rows, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param id          style ID
     * @param orientation components flow orientation
     * @param columns     amount of columns used to place components
     * @param rows        amount of rows used to place components
     * @param components  components to group
     */
    public GroupPane ( final StyleId id, final int orientation, final int columns, final int rows, final Component... components )
    {
        super ( id, createDefaultLayout ( orientation, columns, rows ), components );
        StyleManager.addStyleListener ( this, this );
        addPropertyChangeListener ( this );
        updateStyling ();
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
     * Returns whether or not should visually group provided children components.
     *
     * @return true if should visually group provided children components, false otherwise
     */
    public boolean isGroup ()
    {
        if ( group == null )
        {
            group = true;
        }
        return group;
    }

    /**
     * Sets whether or not should visually group provided children components.
     *
     * @param group whether or not should visually group provided children components
     */
    public void setGroup ( final boolean group )
    {
        if ( isGroup () != group )
        {
            this.group = group;
            updateStyling ();
            revalidate ();
        }
    }

    /**
     * Returns whether or not should group toggle state elements.
     *
     * @return true if should group toggle state elements, false otherwise
     */
    public boolean isGroupButtons ()
    {
        if ( groupButtons == null )
        {
            groupButtons = true;
        }
        return groupButtons;
    }

    /**
     * Sets whether or not should group toggle state elements.
     * Changing this flag will automatically group or ungroup toggle state elements in this {@link com.alee.laf.grouping.GroupPane}.
     *
     * @param group whether or not should group toggle state elements
     */
    public void setGroupButtons ( final boolean group )
    {
        if ( isGroupButtons () != group )
        {
            this.groupButtons = group;
            updateToggleElementsGrouping ( group );
        }
    }

    /**
     * Updates toggle state elements grouping.
     *
     * @param group whether or not should group toggle state elements
     */
    protected void updateToggleElementsGrouping ( final boolean group )
    {
        for ( int i = 0; i < getComponentCount (); i++ )
        {
            final Component component = getComponent ( i );
            if ( component instanceof AbstractButton )
            {
                final AbstractButton abstractButton = ( AbstractButton ) component;
                if ( group )
                {
                    getButtonGroup ().add ( abstractButton );
                }
                else
                {
                    getButtonGroup ().remove ( abstractButton );
                }
            }
        }
    }

    /**
     * Updates toggle state element grouping.
     *
     * @param component possible toggle state element
     * @param added     whether element was added or removed
     */
    protected void updateToggleElementGrouping ( final Component component, final boolean added )
    {
        if ( isGroupButtons () && ( component instanceof JToggleButton ||
                component instanceof JCheckBox ||
                component instanceof JCheckBoxMenuItem ||
                component instanceof JRadioButton ||
                component instanceof JRadioButtonMenuItem ) )
        {
            final AbstractButton abstractButton = ( AbstractButton ) component;
            if ( added )
            {
                getButtonGroup ().add ( abstractButton );
            }
            else
            {
                getButtonGroup ().remove ( abstractButton );
            }
        }
    }

    /**
     * Returns button group used to group toggle state elements placed within this group pane.
     *
     * @return button group used to group toggle state elements placed within this group pane
     */
    public ButtonGroup getButtonGroup ()
    {
        if ( buttonGroup == null )
        {
            buttonGroup = new ButtonGroup ();
        }
        return buttonGroup;
    }

    /**
     * Sets button group used to group toggle state elements placed within this group pane.
     * Changing group will automatically regroup toggle state elements in this {@link com.alee.laf.grouping.GroupPane} under new group.
     *
     * @param group button group used to group toggle state elements placed within this group pane
     * @see javax.swing.ButtonGroup
     * @see com.alee.utils.swing.UnselectableButtonGroup
     */
    public void setButtonGroup ( final ButtonGroup group )
    {
        updateToggleElementsGrouping ( false );
        this.buttonGroup = group;
        updateToggleElementsGrouping ( isGroupButtons () );
    }

    /**
     * Returns whether or not should display top pane side components attached.
     *
     * @return true if should display top pane side components attached, false otherwise
     */
    public boolean isAttachTop ()
    {
        if ( attachTop == null )
        {
            attachTop = false;
        }
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
        if ( attachLeft == null )
        {
            attachLeft = false;
        }
        return attachLeft;
    }

    /**
     * Sets whether or not should display left pane side components attached.
     *
     * @param attachLeft whether or not should display left pane side components attached
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
        if ( attachBottom == null )
        {
            attachBottom = false;
        }
        return attachBottom;
    }

    /**
     * Sets whether or not should display bottom pane side components attached.
     *
     * @param attachBottom whether or not should display bottom pane side components attached
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
        if ( attachRight == null )
        {
            attachRight = false;
        }
        return attachRight;
    }

    /**
     * Sets whether or not should display right pane side components attached.
     *
     * @param attachRight whether or not should display right pane side components attached
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
     * @param component   added component
     * @param constraints component constraints
     * @param index       component index
     */
    @Override
    protected void addImpl ( final Component component, final Object constraints, final int index )
    {
        // Adding child component
        super.addImpl ( component, constraints, index );

        // Adding custom child listeners
        if ( component instanceof JComponent )
        {
            StyleManager.addStyleListener ( ( JComponent ) component, this );
        }
        component.addPropertyChangeListener ( this );

        // Updating grouping style
        updateStyling ();

        // Updating toggle elements grouping
        updateToggleElementGrouping ( component, true );
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
        // Removing custom child listeners
        final Component component = getComponent ( index );
        if ( component instanceof JComponent )
        {
            StyleManager.removeStyleListener ( ( JComponent ) component, this );
        }
        component.removePropertyChangeListener ( this );

        // Removing child component
        super.remove ( index );

        // Re-applying child style
        StyleManager.installSkin ( ( JComponent ) component );

        // Updating grouping style
        updateStyling ();

        // Updating toggle elements grouping
        updateToggleElementGrouping ( component, false );
    }

    @Override
    public void skinChanged ( final JComponent component, final Skin oldSkin, final Skin newSkin )
    {
        //
    }

    @Override
    public void styleChanged ( final JComponent component, final StyleId oldStyleId, final StyleId newStyleId )
    {
        //
    }

    @Override
    public void skinUpdated ( final JComponent component, final StyleId styleId )
    {
        // Updating styling on skin updates
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
            // Removing painter from cache
            if ( evt.getOldValue () != null )
            {
                decorations.remove ( evt.getOldValue () );
            }
        }
        else if ( propertyName.equals ( WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            // Repainting on enabled state change
            repaint ();
        }
        else if ( propertyName.equals ( WebLookAndFeel.ORIENTATION_PROPERTY ) && evt.getSource () == this )
        {
            // Updating style on orientation change
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
        final boolean ltr = getComponentOrientation ().isLeftToRight ();
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
                    if ( isGroup () && isNeighbourSupportsPartialDecoration ( gridSize, col, row, TOP ) )
                    {
                        decoration.setPaintTop ( false );
                        decoration.setPaintTopLine ( false );
                    }
                    else if ( isAttachTop () && row == 0 )
                    {
                        decoration.setPaintTop ( false );
                        decoration.setPaintTopLine ( false );
                    }
                    else
                    {
                        decoration.setPaintTop ( true );
                        decoration.setPaintTopLine ( false );
                    }
                    if ( isGroup () && isNeighbourSupportsPartialDecoration ( gridSize, col, row, ltr ? LEFT : RIGHT ) )
                    {
                        decoration.setPaintLeft ( false );
                        decoration.setPaintLeftLine ( false );
                    }
                    else if ( isAttachLeft () && col == ( ltr ? 0 : gridSize.columns - 1 ) )
                    {
                        decoration.setPaintLeft ( false );
                        decoration.setPaintLeftLine ( false );
                    }
                    else
                    {
                        decoration.setPaintLeft ( true );
                        decoration.setPaintLeftLine ( false );
                    }
                    if ( isGroup () && isNeighbourSupportsPartialDecoration ( gridSize, col, row, BOTTOM ) )
                    {
                        decoration.setPaintBottom ( false );
                        decoration.setPaintBottomLine ( true );
                    }
                    else if ( isAttachBottom () && row == gridSize.rows - 1 )
                    {
                        decoration.setPaintBottom ( false );
                        decoration.setPaintBottomLine ( false );
                    }
                    else
                    {
                        decoration.setPaintBottom ( true );
                        decoration.setPaintBottomLine ( false );
                    }
                    if ( isGroup () && isNeighbourSupportsPartialDecoration ( gridSize, col, row, ltr ? RIGHT : LEFT ) )
                    {
                        decoration.setPaintRight ( false );
                        decoration.setPaintRightLine ( true );
                    }
                    else if ( isAttachRight () && col == ( ltr ? gridSize.columns - 1 : 0 ) )
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