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

package com.alee.extended.label;

import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.ContentPropertyListener;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Styled label text content implementation.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Alexandr Zernov
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 */
@XStreamAlias ( "StyledLabelText" )
public class StyledLabelText<C extends WebStyledLabel, D extends IDecoration<C, D>, I extends StyledLabelText<C, D, I>>
        extends AbstractStyledTextContent<C, D, I>
{
    /**
     * Component property change listener.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient ContentPropertyListener<C, D> listener;

    @Override
    public void activate ( final C c, final D d )
    {
        // Performing default actions
        super.activate ( c, d );

        // Adding style ranges change listener
        listener = new ContentPropertyListener<C, D> ( c, d )
        {
            @Override
            public void propertyChange ( final C c, final D d, final String property, final Object oldValue, final Object newValue )
            {
                buildTextRanges ( c, d );
                c.repaint ();
            }
        };
        c.addPropertyChangeListener ( WebStyledLabel.STYLE_RANGES_PROPERTY, listener );
    }

    @Override
    public void deactivate ( final C c, final D d )
    {
        // Removing style ranges change listener
        c.removePropertyChangeListener ( WebStyledLabel.STYLE_RANGES_PROPERTY, listener );
        listener = null;

        // Performing default actions
        super.deactivate ( c, d );
    }

    @Override
    protected List<StyleRange> getStyleRanges ( final C c, final D d )
    {
        return c.getStyleRanges ();
    }

    @Override
    protected TextWrap getWrapType ( final C c, final D d )
    {
        return c.getWrap ();
    }

    @Override
    protected int getMaximumRows ( final C c, final D d )
    {
        return c.getMaximumRows ();
    }

    @Override
    protected String getText ( final C c, final D d )
    {
        return c.getText ();
    }

    @Override
    protected int getMnemonicIndex ( final C c, final D d )
    {
        return c.getDisplayedMnemonicIndex ();
    }

    @Override
    protected int getHorizontalAlignment ( final C c, final D d )
    {
        return halign != null ? halign : c.getHorizontalTextAlignment ();
    }

    @Override
    protected int getVerticalAlignment ( final C c, final D d )
    {
        return valign != null ? valign : c.getVerticalTextAlignment ();
    }
}