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

import com.alee.api.annotations.NotNull;
import com.alee.managers.icon.Icons;
import com.alee.utils.WebUtils;

import java.awt.event.ActionEvent;

/**
 * Custom link action opening new email composition window for the specified email in default system mail agent.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 */
public class EmailLinkAction extends AsyncLinkAction
{
    /**
     * Constructs new {@link com.alee.extended.link.UrlLinkAction}.
     *
     * @param email recipient email
     */
    public EmailLinkAction ( @NotNull final String email )
    {
        super ( Icons.email, email );
    }

    @Override
    protected void asyncLinkExecuted ( @NotNull final ActionEvent event )
    {
        final String email = getText ();
        if ( email == null )
        {
            throw new RuntimeException ( "E-mail cannot be null" );
        }
        WebUtils.writeEmailSafely ( email );
    }
}