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

package com.alee.painter.decoration.content;

import com.alee.extended.label.StyleRange;
import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * Simple styled text content implementation.
 * It keeps style ranges while component only stores its text containing style syntax.
 * All other common settings can be specified in the style referencing this content.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

public abstract class SimpleStyledTextContent<E extends JComponent, D extends IDecoration<E, D>, I extends SimpleStyledTextContent<E, D, I>>
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

    @Override
    public void activate ( final E c, final D d )
    {
        // Updating runtime variables
        initializeContentCache ( c, d );

        // Performing default actions
        super.activate ( c, d );
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        // Performing default actions
        super.deactivate ( c, d );

        // Cleaning up runtime variables
        mnemonicIndex = null;
        styledText = null;
    }

    /**
     * Performs style caches update.
     *
     * @param c painted component
     * @param d painted decoration state
     */
    protected void initializeContentCache ( final E c, final D d )
    {
        // Updating styled text
        styledText = new StyledText ( getComponentText ( c, d ) );

        // Updating painted mnemonic index
        mnemonicIndex = getMnemonicIndex ( styledText.getPlainText (), getComponentMnemonicIndex ( c, d ) );
    }

    /**
     * Performs style caches update.
     *
     * @param c painted component
     * @param d painted decoration state
     */
    protected void updateContentCache ( final E c, final D d )
    {
        // Re-initializing content cache
        initializeContentCache ( c, d );

        // Rebuilding actual text ranges
        buildTextRanges ( c, d );
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
    protected String getText ( final E c, final D d )
    {
        return styledText != null ? styledText.getPlainText () : getComponentText ( c, d );
    }

    /**
     * Returns actual component text.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text to be painted
     */
    protected abstract String getComponentText ( E c, D d );

    @Override
    protected int getMnemonicIndex ( final E c, final D d )
    {
        return mnemonicIndex != null ? mnemonicIndex : getComponentMnemonicIndex ( c, d );
    }

    /**
     * Returns actual component mnemonic index or {@code -1} if it shouldn't be displayed.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return actual component mnemonic index or {@code -1} if it shouldn't be displayed
     */
    protected abstract int getComponentMnemonicIndex ( E c, D d );

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
    public I merge ( final I content )
    {
        super.merge ( content );
        wrap = content.wrap != null ? content.wrap : wrap;
        maximumRows = content.maximumRows != null ? content.maximumRows : maximumRows;
        return ( I ) this;
    }
}