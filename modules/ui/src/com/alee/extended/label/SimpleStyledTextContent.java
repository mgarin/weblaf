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

import com.alee.painter.decoration.IDecoration;
import com.alee.utils.SwingUtils;
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
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 */

@SuppressWarnings ( "UnusedParameters" )
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
    protected transient StyleRanges styleRanges;

    /**
     * Mnemonic index within plain text.
     */
    protected transient Integer mnemonicIndex;

    @Override
    public void activate ( final E c, final D d )
    {
        // Initializing caches
        initializeContentCache ( c, d );

        // Performing default actions
        super.activate ( c, d );
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        // Performing default actions
        super.deactivate ( c, d );

        // Cleaning-up caches
        destroyContentCache ( c, d );
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
        styleRanges = new StyleRanges ( getComponentText ( c, d ) );

        // Updating painted mnemonic index
        // We cannot use button mnemonic index since it doesn't exclude style syntax from calculations
        mnemonicIndex = SwingUtils.getMnemonicIndex ( styleRanges.getPlainText (), getComponentMnemonic ( c, d ) );
    }

    /**
     * Performs style caches cleanup.
     *
     * @param c painted component
     * @param d painted decoration state
     */
    protected void destroyContentCache ( final E c, final D d )
    {
        // Resetting mnemonic index
        mnemonicIndex = null;

        // Resetting style ranges
        styleRanges = null;
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
        return styleRanges != null ? styleRanges.getStyleRanges () : Collections.<StyleRange>emptyList ();
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
        return styleRanges != null ? styleRanges.getPlainText () : getComponentText ( c, d );
    }

    @Override
    protected int getMnemonicIndex ( final E c, final D d )
    {
        return mnemonicIndex != null ? mnemonicIndex : -1;
    }

    /**
     * Returns actual component text.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text to be painted
     */
    protected abstract String getComponentText ( E c, D d );

    /**
     * Returns actual component mnemonic or {@code -1} if it shouldn't be displayed.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return actual component mnemonic or {@code -1} if it shouldn't be displayed
     */
    protected abstract int getComponentMnemonic ( E c, D d );

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        wrap = content.isOverwrite () || content.wrap != null ? content.wrap : wrap;
        maximumRows = content.isOverwrite () || content.maximumRows != null ? content.maximumRows : maximumRows;
        return ( I ) this;
    }
}