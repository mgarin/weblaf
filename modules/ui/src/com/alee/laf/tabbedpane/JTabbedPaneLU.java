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

package com.alee.laf.tabbedpane;

import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageUpdater;
import com.alee.managers.language.ToolTipLU;

import javax.swing.*;

/**
 * Custom {@link LanguageUpdater} for {@link JTabbedPane}.
 * This class provides language updates for {@link JTabbedPane}.
 * By default it uses provided language key and tab indices to determine single tab translation key.
 * Basically if you provide "my.tab" language key for tabbed pane, first tab translation should have "my.tab.0" key.
 * It can also depend on tab component name instead of the tab index if configured to do so.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public class JTabbedPaneLU extends ToolTipLU<JTabbedPane>
{
    /**
     * Whether should use tab component name to determine tab translation key or not.
     */
    protected boolean useComponentNames;

    /**
     * Constructs new LanguageUpdater for {@link JTabbedPane} component with default settings.
     */
    public JTabbedPaneLU ()
    {
        this ( false );
    }

    /**
     * Constructs new LanguageUpdater for {@link JTabbedPane} component with specified settings.
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
    public Class getComponentClass ()
    {
        return JTabbedPane.class;
    }

    @Override
    public void update ( final JTabbedPane component, final Language language, final String key, final Object... data )
    {
        super.update ( component, language, key, data );

        // Running through all tabs
        for ( int i = 0; i < component.getTabCount (); i++ )
        {
            // Updating tab text and mnemonic
            // todo Use tab information from WebDocumentPane in case this tabbed pane is part of it
            final String tabKey = key + "." + ( useComponentNames ? component.getComponentAt ( i ).getName () : "" + i );
            if ( language.containsText ( tabKey ) )
            {
                component.setTitleAt ( i, language.get ( tabKey, data ) );
                component.setMnemonicAt ( i, language.getMnemonic ( tabKey ) );
            }
        }
    }
}