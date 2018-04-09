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

package com.alee.managers.language.updaters;

import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageState;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.tooltip.WebCustomTooltip;

import javax.swing.*;

/**
 * Custom {@link LanguageUpdater} for any {@link JComponent} that provides tooltips only.
 * Most of default predefined language updaters extend this class.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public class ToolTipLU<C extends JComponent> implements LanguageUpdater<C>, LanguageState
{
    @Override
    public Class getComponentClass ()
    {
        return JComponent.class;
    }

    @Override
    public void update ( final C component, final Language language, final String key, final Object... data )
    {
        // Updating Swing component tooltips
        final LanguageUpdater swingTooltipUpdater = UILanguageManager.getLanguageUpdater ( JToolTip.class );
        if ( swingTooltipUpdater != null )
        {
            swingTooltipUpdater.update ( component, language, key, data );
        }

        // Updating custom WebLaF component tooltips
        final LanguageUpdater customTooltipUpdater = UILanguageManager.getLanguageUpdater ( WebCustomTooltip.class );
        if ( customTooltipUpdater != null )
        {
            customTooltipUpdater.update ( component, language, key, data );
        }
    }
}