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

package com.alee.extended.link;

import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.managers.icon.Icons;
import com.alee.utils.WebUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Custom link action opening specified URL in default system internet browser.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 */

public class UrlLinkAction extends AsyncLinkAction implements IconSupport, TitleSupport
{
    /**
     * URL to be opened.
     */
    private final String url;

    /**
     * Constructs new {@link com.alee.extended.link.UrlLinkAction}.
     *
     * @param url URL to be opened
     */
    public UrlLinkAction ( final String url )
    {
        super ();
        this.url = url;
    }

    @Override
    public Icon getIcon ()
    {
        // todo Probably retrieve favicon from the site?
        return Icons.globe;
    }

    @Override
    public String getTitle ()
    {
        // todo Probably remove protocol if it exists?
        return url;
    }

    @Override
    protected void asyncLinkExecuted ( final ActionEvent event )
    {
        WebUtils.browseSiteSafely ( url );
    }
}