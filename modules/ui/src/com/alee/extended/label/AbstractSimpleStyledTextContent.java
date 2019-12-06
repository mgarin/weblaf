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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.ContentPropertyListener;
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
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 */
public abstract class AbstractSimpleStyledTextContent<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractSimpleStyledTextContent<C, D, I>>
        extends AbstractStyledTextContent<C, D, I>
{
    /**
     * Text wrapping type.
     */
    @Nullable
    @XStreamAsAttribute
    protected TextWrap wrap;

    /**
     * Maximum amount of displayed rows.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer maximumRows;

    /**
     * Styled text used for rendering.
     */
    @Nullable
    protected transient StyleRanges styleRanges;

    /**
     * Mnemonic index within plain text.
     */
    @Nullable
    protected transient Integer mnemonicIndex;

    /**
     * Component property change listener.
     */
    @Nullable
    protected transient ContentPropertyListener<C, D> contentListener;

    @Override
    public void activate ( @NotNull final C c, @NotNull final D d )
    {
        // Initializing caches
        initializeContentCache ( c, d );

        // Performing default actions
        super.activate ( c, d );

        // Installing content property listener
        installContentPropertyListener ( c, d );
    }

    @Override
    public void deactivate ( @NotNull final C c, @NotNull final D d )
    {
        // Uninstalling content property listener
        uninstallContentPropertyListener ( c, d );

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
    protected void initializeContentCache ( @NotNull final C c, @NotNull final D d )
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
    protected void destroyContentCache ( @NotNull final C c, @NotNull final D d )
    {
        // Resetting mnemonic index
        mnemonicIndex = null;

        // Resetting style ranges
        styleRanges = null;
    }

    /**
     * Installs content property listener.
     *
     * @param c painted component
     * @param d painted decoration state
     */
    protected void installContentPropertyListener ( @NotNull final C c, @NotNull final D d )
    {
        // Adding text change listener
        final String property = getStyledTextProperty ();
        if ( property != null )
        {
            contentListener = new ContentPropertyListener<C, D> ( c, d )
            {
                @Override
                public void propertyChange ( @NotNull final C c, @NotNull final D d, @NotNull final String property,
                                             @Nullable final Object oldValue, @Nullable final Object newValue )
                {
                    updateContentCache ( c, d );
                }
            };
            c.addPropertyChangeListener ( property, contentListener );
        }
    }

    /**
     * Uninstalls content property listener.
     *
     * @param c painted component
     * @param d painted decoration state
     */
    protected void uninstallContentPropertyListener ( @NotNull final C c, @NotNull final D d )
    {
        // Removing text change listener
        final String property = getStyledTextProperty ();
        if ( property != null )
        {
            c.removePropertyChangeListener ( property, contentListener );
            contentListener = null;
        }
    }

    /**
     * Returns name of the property that contains styled text.
     * It is used for registering content property listener that updates style caches.
     *
     * @return name of the property that contains styled text
     */
    @Nullable
    protected abstract String getStyledTextProperty ();

    /**
     * Performs style caches update.
     *
     * @param c painted component
     * @param d painted decoration state
     */
    protected void updateContentCache ( @NotNull final C c, @NotNull final D d )
    {
        // Re-initializing content cache
        initializeContentCache ( c, d );

        // Rebuilding actual text ranges
        buildTextRanges ( c, d );
    }

    @NotNull
    @Override
    protected List<StyleRange> getStyleRanges ( @NotNull final C c, @NotNull final D d )
    {
        return styleRanges != null ? styleRanges.getStyleRanges () : Collections.<StyleRange>emptyList ();
    }

    @NotNull
    @Override
    protected TextWrap getWrapType ( @NotNull final C c, @NotNull final D d )
    {
        return wrap != null ? wrap : TextWrap.none;
    }

    @Override
    protected int getMaximumRows ( @NotNull final C c, @NotNull final D d )
    {
        return maximumRows != null ? maximumRows : 0;
    }

    @Nullable
    @Override
    protected String getText ( @NotNull final C c, @NotNull final D d )
    {
        return styleRanges != null ? styleRanges.getPlainText () : getComponentText ( c, d );
    }

    @Override
    protected int getMnemonicIndex ( @NotNull final C c, @NotNull final D d )
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
    @Nullable
    protected abstract String getComponentText ( @NotNull C c, @NotNull D d );

    /**
     * Returns actual component mnemonic or {@code -1} if it shouldn't be displayed.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return actual component mnemonic or {@code -1} if it shouldn't be displayed
     */
    protected abstract int getComponentMnemonic ( @NotNull C c, @NotNull D d );
}