package com.alee.laf.grouping;

import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;
import com.alee.utils.swing.UnselectableButtonGroup;

import javax.swing.*;
import java.awt.*;

/**
 * Special container that can be used to visually group up various components.
 * It will force component to paint decoration according to its position in this container.
 * Note that this container will control some of the styling settings provided by skin to make components look better.
 *
 * @author Mikle Garin
 */
public class GroupPane extends WebPanel implements SwingConstants
{
    /**
     * Constructs new group pane.
     */
    public GroupPane ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param components components to group
     */
    public GroupPane ( final Component... components )
    {
        this ( StyleId.auto, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param orientation components flow orientation
     * @param components  components to group
     */
    public GroupPane ( final int orientation, final Component... components )
    {
        this ( StyleId.auto, orientation, components );
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
        this ( StyleId.auto, columns, rows, components );
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
        this ( StyleId.auto, orientation, columns, rows, components );
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
        this ( id, true, orientation, columns, rows, components );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param groupButtons whether or not should group toggle state elements
     */
    public GroupPane ( final boolean groupButtons )
    {
        this ( StyleId.auto, groupButtons, SwingConstants.HORIZONTAL, Integer.MAX_VALUE, 1 );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param id           style ID
     * @param groupButtons whether or not should group toggle state elements
     */
    public GroupPane ( final StyleId id, final boolean groupButtons )
    {
        this ( id, groupButtons, SwingConstants.HORIZONTAL, Integer.MAX_VALUE, 1 );
    }

    /**
     * Constructs new group pane with specified components inside.
     *
     * @param id           style ID
     * @param groupButtons whether or not should group toggle state elements
     * @param orientation  components flow orientation
     * @param columns      amount of columns used to place components
     * @param rows         amount of rows used to place components
     * @param components   components to group
     */
    public GroupPane ( final StyleId id, final boolean groupButtons, final int orientation, final int columns, final int rows,
                       final Component... components )
    {
        super ( id, new GroupPaneLayout ( orientation, columns, rows ) );
        setGroupButtons ( groupButtons );
        add ( components );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.grouppane;
    }

    /**
     * Overridden to inform when incorrect layout manager is provided.
     * Only {@link com.alee.laf.grouping.GroupPaneLayout} instances can be used.
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
    public boolean isGrouping ()
    {
        return getLayout ().isGrouping ();
    }

    /**
     * Sets whether or not should visually group provided children components.
     *
     * @param group whether or not should visually group provided children components
     */
    public void setGroup ( final boolean group )
    {
        getLayout ().setGroup ( group );
    }

    /**
     * Returns whether or not should group toggle state elements.
     *
     * @return true if should group toggle state elements, false otherwise
     */
    public boolean isGroupButtons ()
    {
        return getLayout ().isGroupButtons ();
    }

    /**
     * Sets whether or not should group toggle state elements.
     * Changing this flag will automatically group or ungroup toggle state elements in this pane.
     *
     * @param group whether or not should group toggle state elements
     */
    public void setGroupButtons ( final boolean group )
    {
        getLayout ().setGroupButtons ( group );
    }

    /**
     * Returns whether or not this button group should allow empty selection state.
     *
     * @return true if this button group should allow empty selection state, false otherwise
     */
    public boolean isUnselectableGrouping ()
    {
        return getLayout ().isUnselectableGrouping ();
    }

    /**
     * Sets whether or not this button group should allow empty selection state.
     *
     * @param unselectable whether or not this button group should allow empty selection state
     */
    public void setUnselectableGrouping ( final boolean unselectable )
    {
        getLayout ().setUnselectableGrouping ( unselectable );
    }

    /**
     * Returns button group used to group toggle state elements placed within this group pane.
     *
     * @return button group used to group toggle state elements placed within this group pane
     */
    public UnselectableButtonGroup getButtonGroup ()
    {
        return getLayout ().getButtonGroup ();
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
        revalidate ();
    }

    /**
     * Returns whether or not top side should be decorated.
     *
     * @return true if top side should be decorated, false otherwise
     */
    public boolean isPaintTop ()
    {
        return getLayout ().isPaintTop ();
    }

    /**
     * Returns whether or not left side should be decorated.
     *
     * @return true if left side should be decorated, false otherwise
     */
    public boolean isPaintLeft ()
    {
        return getLayout ().isPaintLeft ();
    }

    /**
     * Returns whether or not bottom side should be decorated.
     *
     * @return true if bottom side should be decorated, false otherwise
     */
    public boolean isPaintBottom ()
    {
        return getLayout ().isPaintBottom ();
    }

    /**
     * Returns whether or not right side should be decorated.
     *
     * @return true if right side should be decorated, false otherwise
     */
    public boolean isPaintRight ()
    {
        return getLayout ().isPaintRight ();
    }

    /**
     * Sets whether or not right side should be decorated.
     *
     * @param top    whether or not top side should be decorated
     * @param left   whether or not bottom side should be decorated
     * @param bottom whether or not bottom side should be decorated
     * @param right  whether or not right side should be decorated
     */
    public void setPaintSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        getLayout ().setPaintSides ( top, left, bottom, right );
        revalidate ();
        repaint ();
    }
}