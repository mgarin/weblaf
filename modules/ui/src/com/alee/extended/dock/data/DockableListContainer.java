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

package com.alee.extended.dock.data;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.Orientation;
import com.alee.api.jdk.Objects;
import com.alee.extended.dock.WebDockablePane;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link com.alee.extended.dock.data.DockableContainer} representing either horizontal or vertical list of elements.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 */
@XStreamAlias ( "DockableList" )
public class DockableListContainer extends AbstractDockableElement implements DockableContainer
{
    /**
     * List orientation.
     */
    @NotNull
    @XStreamAsAttribute
    protected Orientation orientation;

    /**
     * List of structure elements.
     */
    @Nullable
    @XStreamImplicit
    protected List<DockableElement> elements;

    /**
     * Constructs new elements list.
     *
     * @param orientation list orientation
     * @param elements    elements to add
     */
    public DockableListContainer ( @NotNull final Orientation orientation, @NotNull final DockableElement... elements )
    {
        this ( orientation, new Dimension ( 0, 0 ), elements );
    }

    /**
     * Constructs new elements list.
     *
     * @param orientation list orientation
     * @param size        container size
     * @param elements    elements to add
     */
    public DockableListContainer ( @NotNull final Orientation orientation, @NotNull final Dimension size,
                                   @NotNull final DockableElement... elements )
    {
        super ( TextUtils.generateId ( "EL" ), size );
        this.orientation = orientation;
        for ( final DockableElement element : elements )
        {
            add ( element );
        }
    }

    @Override
    public void added ( @Nullable final DockableContainer parent )
    {
        super.added ( parent );

        // Initializing children
        if ( elements != null )
        {
            for ( final DockableElement element : elements )
            {
                element.added ( this );
            }
        }
    }

    @Override
    public boolean isContent ()
    {
        boolean isContent = false;
        if ( elements != null )
        {
            for ( final DockableElement element : elements )
            {
                if ( element.isContent () )
                {
                    isContent = true;
                    break;
                }
            }
        }
        return isContent;
    }

    @NotNull
    @Override
    public Orientation getOrientation ()
    {
        return orientation;
    }

    @Override
    public void setOrientation ( @NotNull final Orientation orientation )
    {
        this.orientation = orientation;
    }

    @Override
    public int getElementCount ()
    {
        return elements != null ? elements.size () : 0;
    }

    @NotNull
    @Override
    public <E extends DockableElement> E get ( @NotNull final String id )
    {
        final DockableElement element = find ( id );
        if ( element == null )
        {
            throw new RuntimeException ( "Unable to find element with identifier: " + id );
        }
        return ( E ) element;
    }

    @Nullable
    @Override
    public <E extends DockableElement> E find ( @NotNull final String id )
    {
        DockableElement element = null;
        if ( elements != null )
        {
            for ( final DockableElement e : elements )
            {
                if ( Objects.equals ( id, e.getId () ) )
                {
                    element = e;
                    break;
                }
                if ( e instanceof DockableContainer )
                {
                    element = ( ( DockableContainer ) e ).find ( id );
                    if ( element != null )
                    {
                        break;
                    }
                }
            }
        }
        return ( E ) element;
    }

    @Override
    public boolean contains ( @NotNull final String id )
    {
        return find ( id ) != null;
    }

    @Override
    public int indexOf ( @NotNull final DockableElement element )
    {
        return elements != null ? elements.indexOf ( element ) : -1;
    }

    @NotNull
    @Override
    public DockableElement get ( final int index )
    {
        final DockableElement element = elements != null ? elements.get ( index ) : null;
        if ( element == null )
        {
            throw new RuntimeException ( "Unable to find element at index: " + index );
        }
        return element;
    }

    @Override
    public void add ( @NotNull final DockableElement element )
    {
        add ( getElementCount (), element );
    }

    @Override
    public void add ( final int index, @NotNull final DockableElement element )
    {
        // Ensure elements list is created
        if ( elements == null )
        {
            elements = new ArrayList<DockableElement> ( 2 );
        }

        // Add element
        elements.add ( index, element );
        element.added ( this );
    }

    @Override
    public void remove ( final DockableElement element )
    {
        if ( elements != null )
        {
            elements.remove ( element );
            element.removed ( this );
        }
    }

    @Override
    public boolean isVisible ( @NotNull final WebDockablePane dockablePane )
    {
        boolean isVisible = false;
        if ( elements != null )
        {
            for ( final DockableElement element : elements )
            {
                if ( element.isVisible ( dockablePane ) )
                {
                    isVisible = true;
                    break;
                }
            }
        }
        return isVisible;
    }

    @Override
    public void layout ( @NotNull final WebDockablePane dockablePane, @NotNull final Rectangle bounds,
                         @NotNull final List<ResizeData> resizeableAreas )
    {
        // Saving bounds
        setBounds ( bounds );

        // Placing elements
        if ( elements != null )
        {
            // Settings
            final boolean horizontal = orientation.isHorizontal ();

            // Calculating existing sizes
            int summ = 0;
            int minSumm = 0;
            int cindex = -1;
            final List<DockableElement> visible = new ArrayList<DockableElement> ( elements.size () );
            final List<Integer> sizes = new ArrayList<Integer> ( elements.size () );
            for ( final DockableElement element : elements )
            {
                if ( element.isVisible ( dockablePane ) )
                {
                    final int length;
                    if ( element.isContent () )
                    {
                        length = 0;
                        cindex = visible.size ();
                    }
                    else
                    {
                        final Dimension minimumSize = element.getMinimumSize ( dockablePane );
                        length = horizontal ?
                                Math.max ( element.getSize ().width, minimumSize.width ) :
                                Math.max ( element.getSize ().height, minimumSize.height );
                        final int minLength = horizontal ? minimumSize.width : minimumSize.height;
                        summ += Math.max ( length, minLength );
                        minSumm += minLength;
                    }
                    visible.add ( element );
                    sizes.add ( length );
                }
            }

            // Continue only if there are visible elements
            if ( visible.size () > 0 )
            {
                // Minimum content length
                final int minContentLength = cindex == -1 ? 0 : horizontal ?
                        visible.get ( cindex ).getMinimumSize ( dockablePane ).width :
                        visible.get ( cindex ).getMinimumSize ( dockablePane ).height;

                // Calculating required and minimum container length
                final int spacing = dockablePane.getContentSpacing ();
                final int spacings = spacing * ( visible.size () - 1 );
                final int requiredLength = summ + minContentLength + spacings;
                final int minimumLength = minSumm + minContentLength + spacings;

                // Calculating total available space
                // We do not want to scale below minimum required space, so we set it as minimum
                final int availableSpace = Math.max ( horizontal ? bounds.width : bounds.height, minimumLength );

                // Adjusting sizes if they do not fit into available area
                if ( requiredLength > availableSpace )
                {
                    // Available space minus spacings and minimum content size
                    final int available = availableSpace - minContentLength - spacings;

                    // Shrinking all elements according to their size
                    for ( int i = 0; i < visible.size (); i++ )
                    {
                        if ( i != cindex )
                        {
                            // Shrinked element size
                            // todo Spread possible remaining pixels
                            sizes.set ( i, ( int ) Math.floor ( ( float ) available * sizes.get ( i ) / summ ) );
                        }
                        else
                        {
                            // Minimum content size
                            sizes.set ( i, minContentLength );
                        }
                    }
                }
                else
                {
                    if ( cindex != -1 )
                    {
                        // Filling all available space with content
                        // We leave all the rest of the sizes intact as they fit into available space
                        sizes.set ( cindex, availableSpace - summ - spacings );
                    }
                    else
                    {
                        // Stretching elements across the area since there is no content in this container
                        final int available = availableSpace - spacings;
                        for ( int i = 0; i < visible.size (); i++ )
                        {
                            // todo Spread possible remaining pixels
                            sizes.set ( i, ( int ) Math.floor ( ( float ) available * sizes.get ( i ) / summ ) );
                        }
                    }
                }

                // Placing structure elements
                int x = bounds.x;
                int y = bounds.y;
                for ( int i = 0; i < visible.size (); i++ )
                {
                    final DockableElement element = visible.get ( i );

                    // Calculating resulting element sizes
                    final int width = horizontal ? sizes.get ( i ) : bounds.width;
                    final int height = horizontal ? bounds.height : sizes.get ( i );

                    // Updating sizes for non-content areas
                    if ( i != cindex )
                    {
                        // We only want to update width for horizontal orientation and height for vertical
                        // The opposite size is handled by the parent container, we don't want to record it's size changes
                        final Dimension oldSize = element.getSize ();
                        element.setSize ( new Dimension (
                                horizontal ? sizes.get ( i ) : oldSize.width,
                                horizontal ? oldSize.height : sizes.get ( i )
                        ) );
                    }

                    // Placing element
                    element.layout (
                            dockablePane,
                            new Rectangle ( x, y, width, height ),
                            resizeableAreas
                    );

                    // Adding resize element bounds
                    if ( i < visible.size () - 1 )
                    {
                        final int rg = dockablePane.getResizeGripperWidth ();
                        final int rgx = horizontal ? x + width + spacing / 2 - rg / 2 : x;
                        final int rgy = horizontal ? y : y + height + spacing / 2 - rg / 2;
                        final int rgw = horizontal ? rg : width;
                        final int rgh = horizontal ? height : rg;
                        final Rectangle rb = new Rectangle ( rgx, rgy, rgw, rgh );
                        resizeableAreas.add ( new ResizeData ( rb, orientation, element.getId (), visible.get ( i + 1 ).getId () ) );
                    }

                    // Incrementing coordinate
                    if ( horizontal )
                    {
                        x += width + spacing;
                    }
                    else
                    {
                        y += height + spacing;
                    }
                }
            }
        }
    }

    @NotNull
    @Override
    public Dimension getMinimumSize ( @NotNull final WebDockablePane dockablePane )
    {
        // Base minimum size
        Dimension min = dockablePane.getMinimumElementSize ();

        // Children-dictated minimum size
        if ( dockablePane.isOccupyMinimumSizeForChildren () )
        {
            final Dimension mc = new Dimension ( 0, 0 );
            if ( elements != null )
            {
                final int spacing = dockablePane.getContentSpacing ();
                for ( final DockableElement element : elements )
                {
                    final Dimension minElement = element.getMinimumSize ( dockablePane );
                    if ( orientation.isHorizontal () )
                    {
                        mc.width += minElement.width + spacing;
                        mc.height = Math.max ( minElement.height, mc.height );
                    }
                    else
                    {
                        mc.width = Math.max ( minElement.width, mc.width );
                        mc.height += minElement.height + spacing;
                    }
                }
                if ( orientation.isHorizontal () )
                {
                    mc.width -= spacing;
                }
                else
                {
                    mc.height -= spacing;
                }
            }
            min = new Dimension ( Math.max ( min.width, mc.width ), Math.max ( min.height, mc.height ) );
        }

        // Validating size
        // This is made here to optimize performance
        if ( size.width < min.width || size.height < min.height )
        {
            setSize ( new Dimension ( Math.max ( size.width, min.width ), Math.max ( size.height, min.height ) ) );
        }

        return min;
    }
}