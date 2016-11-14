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

import com.alee.utils.CollectionUtils;
import com.alee.utils.MergeUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.swing.CursorType;
import com.alee.utils.xml.ListToStringConverter;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Abstract component state decoration providing basic settings.
 *
 * @param <E> component type
 * @param <I> decoration type
 * @author Mikle Garin
 */

public abstract class AbstractDecoration<E extends JComponent, I extends AbstractDecoration<E, I>> implements IDecoration<E, I>
{
    /**
     * Default component state ID.
     * Added to avoid default state ID being null.
     */
    public static final String defaultStateId = "default";

    /**
     * Component states this decoration is describing separated by comma.
     * Various components might use multiple states to describe their decoration.
     */
    @XStreamAsAttribute
    @XStreamConverter (ListToStringConverter.class)
    protected List<String> states;

    /**
     * Whether or not this decoration should overwrite previous one when merged.
     */
    @XStreamAsAttribute
    protected Boolean overwrite;

    /**
     * Whether or not decoration should be displayed.
     * This doesn't affect anything painted outside, for example by any {@link com.alee.painter.SpecificPainter}.
     * In case this decoration is used by {@link com.alee.painter.SectionPainter} this only affects section visibility.
     */
    @XStreamAsAttribute
    protected Boolean visible;

    /**
     * Decoration size.
     * It can be specified to provide fixed decoration size.
     */
    @XStreamAsAttribute
    protected Dimension size;

    /**
     * Decoration opacity.
     * This doesn't affect anything painted outside, for example by any {@link com.alee.painter.SpecificPainter}.
     * In case this decoration is used by {@link com.alee.painter.SectionPainter} this opacity is only applied to the section.
     */
    @XStreamAsAttribute
    protected Float opacity;

    /**
     * Custom component cursor for this decoration state.
     */
    @XStreamAsAttribute
    protected CursorType cursor;

    /**
     * Whether or not this decoration is applied only to a section of the component.
     * Provided explicitely by the painted using this decoration.
     */
    protected transient Boolean section;

    /**
     * Previously set cursor.
     * Saved to avoid default component cursor removal.
     */
    protected transient Cursor previousCursor;

    @Override
    public String getId ()
    {
        return states != null ? TextUtils.listToString ( states, STATES_SEPARATOR ) : defaultStateId;
    }

    @Override
    public void activate ( final E c )
    {
        final Cursor customCursor = getCursor ();
        if ( customCursor != null )
        {
            previousCursor = c.getCursor ();
            c.setCursor ( customCursor );
        }
    }

    @Override
    public void deactivate ( final E c )
    {
        final Cursor customCursor = getCursor ();
        if ( customCursor != null )
        {
            c.setCursor ( previousCursor );
            previousCursor = null;
        }
    }

    @Override
    public List<String> getStates ()
    {
        return states;
    }

    @Override
    public void updateStates ( final List<String> states )
    {
        this.states = states;
    }

    @Override
    public boolean usesState ( final String state )
    {
        // todo Add negation syntax usage [ #387 ]
        return states != null && states.contains ( state );
    }

    @Override
    public boolean isApplicableTo ( final List<String> states )
    {
        if ( CollectionUtils.isEmpty ( this.states ) )
        {
            return true;
        }
        else if ( CollectionUtils.isEmpty ( states ) )
        {
            return false;
        }
        else
        {
            for ( final String state : this.states )
            {
                if ( !states.contains ( state ) )
                {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean isOverwrite ()
    {
        return overwrite != null && overwrite;
    }

    @Override
    public boolean isVisible ()
    {
        return visible == null || visible;
    }

    @Override
    public boolean isSection ()
    {
        return section != null && section;
    }

    @Override
    public void setSection ( final boolean section )
    {
        this.section = section;
    }

    /**
     * Returns decoration opacity.
     *
     * @return decoration opacity
     */
    public float getOpacity ()
    {
        return opacity != null ? opacity : 1f;
    }

    /**
     * Returns custom component cursor for this decoration state or {@code null} if none provided.
     *
     * @return custom component cursor for this decoration state or {@code null} if none provided
     */
    public Cursor getCursor ()
    {
        return cursor != null ? cursor.getCursor () : null;
    }

    @Override
    public int getBaseline ( final E c, final int width, final int height )
    {
        return -1;
    }

    @Override
    public Dimension getPreferredSize ( final E c )
    {
        return size != null ? size : null;
    }

    @Override
    public I merge ( final I decoration )
    {
        overwrite = overwrite != null && overwrite || decoration.overwrite != null && decoration.overwrite;
        visible = decoration.isOverwrite () || decoration.visible != null ? decoration.visible : visible;
        size = decoration.isOverwrite () || decoration.size != null ? decoration.size : size;
        opacity = decoration.isOverwrite () || decoration.opacity != null ? decoration.opacity : opacity;
        cursor = decoration.isOverwrite () || decoration.cursor != null ? decoration.cursor : cursor;
        return ( I ) this;
    }

    @Override
    public I clone ()
    {
        return ( I ) MergeUtils.cloneByFieldsSafely ( this );
    }

    @Override
    public String toString ()
    {
        return ReflectUtils.getClassName ( this ) + " [ id=" + getId () + "; visible=" + isVisible () + " ]";
    }
}