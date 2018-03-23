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

import com.alee.api.ui.IconBridge;
import com.alee.api.ui.TextBridge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.EventListener;

/**
 * Base interface for {@link WebLink} actions.
 * Implementations of this interface can also implement {@link IconBridge} and
 * {@link TextBridge} for providing initial {@link WebLink} icon and text.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 * @see AbstractLinkAction
 * @see AsyncLinkAction
 * @see UrlLinkAction
 * @see EmailLinkAction
 * @see FileLinkAction
 */

public interface LinkAction extends EventListener
{
    /**
     * Returns {@link Icon} for this {@link LinkAction}.
     * Return {@code null} to avoid affecting {@link WebLink}.
     *
     * @return {@link Icon} for this {@link LinkAction}
     */
    public Icon getIcon ();

    /**
     * Returns text for this {@link LinkAction}.
     * Return {@code null} to avoid affecting {@link WebLink}.
     *
     * @return text for this {@link LinkAction}
     */
    public String getText ();

    /**
     * Invoked when link is being executed.
     *
     * @param event link execution event
     */
    public void linkExecuted ( ActionEvent event );
}