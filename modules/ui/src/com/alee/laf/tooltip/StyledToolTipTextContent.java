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

package com.alee.laf.tooltip;

import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.ContentPropertyListener;
import com.alee.painter.decoration.content.SimpleStyledTextContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

/**
 * Abstract tooltip styled text content implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

@XStreamAlias ( "StyledToolTipText" )
public class StyledToolTipTextContent<E extends JToolTip, D extends IDecoration<E, D>, I extends StyledToolTipTextContent<E, D, I>>
        extends SimpleStyledTextContent<E, D, I>
{
    /**
     * Component property change listener.
     */
    protected transient ContentPropertyListener<E, D> listener;

    @Override
    public void activate ( final E c, final D d )
    {
        // Performing default actions
        super.activate ( c, d );

        // Adding text change listener
        listener = new ContentPropertyListener<E, D> ( c, d )
        {
            @Override
            public void propertyChange ( final E c, final D d, final String property, final Object oldValue, final Object newValue )
            {
                updateContentCache ( c, d );
            }
        };
        c.addPropertyChangeListener ( WebLookAndFeel.TIP_TEXT_PROPERTY, listener );
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        // Removing text change listener
        c.removePropertyChangeListener ( WebLookAndFeel.TIP_TEXT_PROPERTY, listener );
        listener = null;

        // Performing default actions
        super.deactivate ( c, d );
    }

    @Override
    protected boolean isHtmlText ( final E c, final D d )
    {
        return c.getClientProperty ( BasicHTML.propertyKey ) != null;
    }

    @Override
    protected View getHtml ( final E c, final D d )
    {
        return ( View ) c.getClientProperty ( BasicHTML.propertyKey );
    }

    @Override
    protected String getComponentText ( final E c, final D d )
    {
        return c.getTipText ();
    }

    @Override
    protected int getComponentMnemonicIndex ( final E c, final D d )
    {
        return -1;
    }
}