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

import com.alee.managers.icon.Icons;
import com.alee.utils.WebUtils;

import java.awt.event.ActionEvent;

/**
 * Custom link action opening specified URL in default system internet browser.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 */
public class UrlLinkAction extends AsyncLinkAction
{
    /**
     * todo 1. Retrieve favicon from the site?
     * todo 2. Remove protocol from URL?
     */

    /**
     * Constructs new {@link com.alee.extended.link.UrlLinkAction}.
     *
     * @param url URL to be opened
     */
    public UrlLinkAction ( final String url )
    {
        super ( Icons.globe, url );
    }

    @Override
    protected void asyncLinkExecuted ( final ActionEvent event )
    {
        WebUtils.browseSiteSafely ( getText () );
    }
}