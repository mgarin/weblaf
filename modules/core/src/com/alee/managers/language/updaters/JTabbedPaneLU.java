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

import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.Value;

import javax.swing.*;

/**
 * This class provides language default updates for JTabbedPane component.
 * By default it uses provided language key and tab indices to determine single tab translation key.
 * Basically if you provide "my.tab" language key for tabbed pane, first tab translation should have "my.tab.0" key.
 * It can also depend on tab component name instead of the tab index if configured to do so.
 *
 * @author Mikle Garin
 */

public class JTabbedPaneLU extends DefaultLanguageUpdater<JTabbedPane>
{
    /**
     * Whether should use tab component name to determine tab translation key or not.
     */
    protected boolean useComponentNames = LanguageUpdaterSettings.useTabComponentNames;

    /**
     * Constructs new LanguageUpdater for JTabbedPane component with default settings.
     */
    public JTabbedPaneLU ()
    {
        super ();
    }

    /**
     * Constructs new LanguageUpdater for JTabbedPane component with specified settings.
     *
     * @param useComponentNames whether should use tab component name to determine tab translation key or not
     */
    public JTabbedPaneLU ( final boolean useComponentNames )
    {
        super ();
        this.useComponentNames = useComponentNames;
    }

    /**
     * Returns whether should use tab component name to determine tab translation key or not.
     *
     * @return true if should use tab component name to determine tab translation key, false otherwise
     */
    public boolean isUseComponentNames ()
    {
        return useComponentNames;
    }

    /**
     * Sets whether should use tab component name to determine tab translation key or not.
     *
     * @param useComponentNames whether should use tab component name to determine tab translation key or not
     */
    public void setUseComponentNames ( final boolean useComponentNames )
    {
        this.useComponentNames = useComponentNames;
    }

    @Override
    public void update ( final JTabbedPane c, final String key, final Value value, final Object... data )
    {
        // Running through all tabs
        for ( int i = 0; i < c.getTabCount (); i++ )
        {
            // Updating tab text and mnemonic
            final String tabKey = useComponentNames ? c.getComponentAt ( i ).getName () : "" + i;
            final Value tabValue = LanguageManager.getNotNullValue ( c, key, tabKey );
            final String text = getDefaultText ( tabValue, data );
            c.setTitleAt ( i, text != null ? text : null );
            c.setMnemonicAt ( i, text != null && value.getMnemonic () != null ? value.getMnemonic () : 0 );
        }
    }
}