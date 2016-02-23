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

package com.alee.managers.style.skin.web.data.decoration;

import com.alee.utils.MergeUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.xml.ListToStringConverter;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
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
    @XStreamConverter ( ListToStringConverter.class )
    protected List<String> states;

    /**
     * Whether or not decoration should be displayed.
     * This doesn't affect anything painted outside, for example by any {@link com.alee.painter.SpecificPainter}.
     * In case this decoration is used by {@link com.alee.painter.SectionPainter} this only affects section visibility.
     */
    @XStreamAsAttribute
    protected Boolean visible = true;

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

    @Override
    public String getId ()
    {
        return states != null ? TextUtils.listToString ( states, STATES_SEPARATOR ) : defaultStateId;
    }

    @Override
    public List<String> getStates ()
    {
        return states;
    }

    @Override
    public boolean isVisible ()
    {
        return visible == null || visible;
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

    @Override
    public Dimension getPreferredSize ()
    {
        return size != null ? size : null;
    }

    @Override
    public I merge ( final I state )
    {
        if ( state.visible != null )
        {
            visible = state.visible;
        }
        if ( state.size != null )
        {
            size = state.size;
        }
        if ( state.opacity != null )
        {
            opacity = state.opacity;
        }
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