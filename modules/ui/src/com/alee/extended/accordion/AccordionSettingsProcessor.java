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

package com.alee.extended.accordion;

import com.alee.api.annotations.NotNull;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

/**
 * {@link SettingsProcessor} implementation that handles {@link WebAccordion} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionPane
 * @see AccordionModel
 * @see WebAccordionModel
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class AccordionSettingsProcessor extends SettingsProcessor<WebAccordion, AccordionState, Configuration<AccordionState>>
{
    /**
     * {@link AccordionListener} for tracking {@link WebAccordion} state changes.
     */
    protected transient AccordionListener accordionListener;

    /**
     * Constructs new {@link AccordionSettingsProcessor}.
     *
     * @param accordion     {@link WebAccordion} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public AccordionSettingsProcessor ( final WebAccordion accordion, final Configuration configuration )
    {
        super ( accordion, configuration );
    }

    @Override
    protected void register ( final WebAccordion accordion )
    {
        accordionListener = new AccordionAdapter ()
        {
            @Override
            public void expanding ( @NotNull final WebAccordion accordion, @NotNull final AccordionPane pane )
            {
                save ();
            }

            @Override
            public void collapsing ( @NotNull final WebAccordion accordion, @NotNull final AccordionPane pane )
            {
                save ();
            }
        };
        accordion.addAccordionListener ( accordionListener );
    }

    @Override
    protected void unregister ( final WebAccordion accordion )
    {
        accordion.removeAccordionListener ( accordionListener );
        accordionListener = null;
    }

    @Override
    protected void loadSettings ( final WebAccordion accordion )
    {
        final AccordionState state = loadSettings ();
        if ( state != null )
        {
            accordion.setAccordionState ( state );
        }
    }

    @Override
    protected void saveSettings ( final WebAccordion accordion )
    {
        saveSettings ( accordion.getAccordionState () );
    }
}