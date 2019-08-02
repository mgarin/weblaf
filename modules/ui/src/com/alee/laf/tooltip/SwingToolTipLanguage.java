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

import com.alee.api.jdk.Objects;
import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageState;
import com.alee.managers.language.LanguageUpdater;
import com.alee.managers.language.ToolTipLU;
import com.alee.managers.language.data.Text;
import com.alee.managers.language.AbstractToolTipLanguage;

import javax.swing.*;

/**
 * This class provides language updates for Swing tooltips of any component.
 * This {@link LanguageUpdater} shouldn't be used directly for {@link JToolTip} but instead it is used by {@link ToolTipLU}.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public class SwingToolTipLanguage<C extends JComponent> extends AbstractToolTipLanguage<C>
{
    /**
     * Swing {@link JToolTip}.
     */
    public static final String TYPE = "SWING";

    /**
     * Special marker for {@link JComponent}s where this {@link LanguageUpdater} have added tooltips.
     */
    protected static final String SWING_TOOLTIP_MARKER = "swing.tooltip.marker";

    @Override
    public Class getComponentClass ()
    {
        return JToolTip.class;
    }

    @Override
    public void update ( final C component, final Language language, final String key, final Object... data )
    {
        // todo Data that is passed in this method cannot be used
        // todo Some new multi-data system is required to do that

        final Text toolTipText = getToolTipText ( language, key );
        if ( toolTipText != null )
        {
            // Updating tooltip text
            component.setToolTipText ( toolTipText.getText () );
            component.putClientProperty ( SWING_TOOLTIP_MARKER, true );
        }
        else if ( component.getClientProperty ( SWING_TOOLTIP_MARKER ) != null )
        {
            // Removing tooltip
            component.setToolTipText ( null );
            component.putClientProperty ( SWING_TOOLTIP_MARKER, null );
        }
    }

    /**
     * Returns Swing tooltip {@link Text} or {@code null} if it shouldn't be added.
     *
     * @param language {@link Language}
     * @param key      language key
     * @return Swing tooltip {@link Text} or {@code null} if it shouldn't be added
     */
    protected Text getToolTipText ( final Language language, final String key )
    {
        // Checking custom state that has priority
        Text tooltip = language.getText ( key, LanguageState.SWING_TOOLTIP_TEXT );

        // Checking default state if custom state was not detected
        if ( tooltip == null && Objects.equals ( getDefaultToolTipType (), TYPE ) )
        {
            tooltip = language.getText ( key, LanguageState.TOOLTIP_TEXT );
        }

        return tooltip;
    }
}