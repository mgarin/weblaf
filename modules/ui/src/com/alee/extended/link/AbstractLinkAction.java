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

import com.alee.api.annotations.Nullable;

import javax.swing.*;

/**
 * Abstract {@link LinkAction} that doesn't have custom {@link Icon} or text.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 * @see LinkAction
 * @see AsyncLinkAction
 * @see UrlLinkAction
 * @see EmailLinkAction
 * @see FileLinkAction
 */
public abstract class AbstractLinkAction implements LinkAction
{
    /**
     * {@link LinkAction} icon.
     */
    @Nullable
    protected final Icon icon;

    /**
     * {@link LinkAction} text.
     */
    @Nullable
    protected final String text;

    /**
     * Constructs new {@link AbstractLinkAction}.
     */
    public AbstractLinkAction ()
    {
        this ( null, null );
    }

    /**
     * Constructs new {@link AbstractLinkAction}.
     *
     * @param icon {@link LinkAction} icon
     */
    public AbstractLinkAction ( @Nullable final Icon icon )
    {
        this ( icon, null );
    }

    /**
     * Constructs new {@link AbstractLinkAction}.
     *
     * @param text {@link LinkAction} text
     */
    public AbstractLinkAction ( @Nullable final String text )
    {
        this ( null, text );
    }

    /**
     * Constructs new {@link AbstractLinkAction}.
     *
     * @param icon {@link LinkAction} icon
     * @param text {@link LinkAction} text
     */
    public AbstractLinkAction ( @Nullable final Icon icon, @Nullable final String text )
    {
        this.icon = icon;
        this.text = text;
    }

    @Nullable
    @Override
    public Icon getIcon ()
    {
        return icon;
    }

    @Nullable
    @Override
    public String getText ()
    {
        return text;
    }
}