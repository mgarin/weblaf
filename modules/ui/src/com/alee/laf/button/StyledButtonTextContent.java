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

package com.alee.laf.button;

import com.alee.extended.label.StyleRange;
import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractStyledTextContent;
import com.alee.painter.decoration.content.ContentPropertyListener;
import com.alee.painter.decoration.content.StyledText;
import com.alee.painter.decoration.content.TextWrap;
import com.alee.utils.CompareUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.util.Collections;
import java.util.List;

/**
 * Abstract button styled text content implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

@XStreamAlias ( "StyledButtonText" )
public class StyledButtonTextContent<E extends AbstractButton, D extends IDecoration<E, D>, I extends StyledButtonTextContent<E, D, I>>
        extends AbstractStyledTextContent<E, D, I>
{
    /**
     * Text wrapping type.
     */
    @XStreamAsAttribute
    protected TextWrap wrap;

    /**
     * Maximum amount of rows.
     */
    @XStreamAsAttribute
    protected Integer maximumRows;

    /**
     * Styled text used for rendering.
     */
    protected transient StyledText styledText;

    /**
     * Mnemonic index within plain text.
     */
    protected transient Integer mnemonicIndex;

    /**
     * Component property change listener.
     */
    protected transient ContentPropertyListener<E, D> listener;

    @Override
    public void activate ( final E c, final D d )
    {
        // Updating runtime variables
        styledText = new StyledText ( c.getText () );
        mnemonicIndex = getMnemonicIndex ( styledText.getPlainText (), c.getMnemonic () );

        // Performing default actions
        super.activate ( c, d );

        // Adding text change listener
        listener = new ContentPropertyListener<E, D> ( c, d )
        {
            @Override
            public void propertyChange ( final E component, final D decoration, final String property, final Object oldValue,
                                         final Object newValue )
            {
                if ( CompareUtils.equals ( property, WebLookAndFeel.TEXT_PROPERTY ) )
                {
                    // Updating runtime variables
                    styledText = new StyledText ( c.getText () );
                    mnemonicIndex = getMnemonicIndex ( styledText.getPlainText (), c.getMnemonic () );

                    // Rebuilding actual text ranges
                    buildTextRanges ( component, decoration );
                }
            }
        };
        c.addPropertyChangeListener ( listener );
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        // Removing text change listener
        c.removePropertyChangeListener ( listener );
        listener = null;

        // Performing default actions
        super.deactivate ( c, d );

        // Cleaning up runtime variables
        mnemonicIndex = null;
        styledText = null;
    }

    /**
     * Returns index of the first occurrence of {@code mnemonic} within string {@code text}.
     * Matching algorithm is not case-sensitive.
     *
     * @param text     The text to search through, may be {@code null}
     * @param mnemonic The mnemonic to find the character for.
     * @return index into the string if exists, otherwise -1
     */
    protected int getMnemonicIndex ( final String text, final int mnemonic )
    {
        if ( text == null || mnemonic == '\0' )
        {
            return -1;
        }

        final char uc = Character.toUpperCase ( ( char ) mnemonic );
        final char lc = Character.toLowerCase ( ( char ) mnemonic );

        final int uci = text.indexOf ( uc );
        final int lci = text.indexOf ( lc );

        if ( uci == -1 )
        {
            return lci;
        }
        else if ( lci == -1 )
        {
            return uci;
        }
        else
        {
            return ( lci < uci ) ? lci : uci;
        }
    }

    @Override
    protected List<StyleRange> getStyleRanges ( final E c, final D d )
    {
        return styledText != null ? styledText.getStyleRanges () : Collections.<StyleRange>emptyList ();
    }

    @Override
    protected int getMaximumRows ( final E c, final D d )
    {
        return maximumRows != null ? maximumRows : 0;
    }

    @Override
    protected TextWrap getWrapType ( final E c, final D d )
    {
        return wrap != null ? wrap : TextWrap.none;
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
    protected String getText ( final E c, final D d )
    {
        return styledText != null ? styledText.getPlainText () : c.getText ();
    }

    @Override
    protected int getMnemonicIndex ( final E c, final D d )
    {
        return mnemonicIndex != null ? mnemonicIndex : c.getDisplayedMnemonicIndex ();
    }

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        wrap = content.wrap != null ? content.wrap : wrap;
        maximumRows = content.maximumRows != null ? content.maximumRows : maximumRows;
        return ( I ) this;
    }
}