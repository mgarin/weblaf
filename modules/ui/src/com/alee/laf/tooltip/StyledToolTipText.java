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

import com.alee.extended.label.AbstractSimpleStyledTextContent;
import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;

/**
 * Abstract tooltip styled text content implementation.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "StyledToolTipText" )
public class StyledToolTipText<C extends JToolTip, D extends IDecoration<C, D>, I extends StyledToolTipText<C, D, I>>
        extends AbstractSimpleStyledTextContent<C, D, I>
{
    @Override
    protected String getStyledTextProperty ()
    {
        return WebLookAndFeel.TIP_TEXT_PROPERTY;
    }

    @Override
    protected String getComponentText ( final C c, final D d )
    {
        return c.getTipText ();
    }

    @Override
    protected int getComponentMnemonic ( final C c, final D d )
    {
        return -1;
    }
}