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

package com.alee.extended.syntax;

import com.alee.api.annotations.NotNull;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;

/**
 * Custom themes for WebSyntaxArea.
 *
 * @author Mikle Garin
 */
public enum SyntaxTheme
{
    /**
     * IntelliJ IDEA theme.
     */
    idea,

    /**
     * Dark theme.
     */
    dark,

    /**
     * Visual Studio theme.
     */
    vs,

    /**
     * Eclipse theme.
     */
    eclipse;

    /**
     * Returns theme icon.
     *
     * @return theme icon
     */
    @NotNull
    public Icon getIcon ()
    {
        return new ImageIcon ( SyntaxTheme.class.getResource ( "icons/" + this + ".png" ) );
    }

    /**
     * Returns theme name.
     *
     * @return theme name
     */
    @NotNull
    public String getName ()
    {
        final String name;
        switch ( this )
        {
            default:
            case idea:
                name = "IntelliJ IDEA";
                break;

            case dark:
                name = "Dark";
                break;

            case vs:
                name = "Visual Studio";
                break;

            case eclipse:
                name = "Eclipse";
                break;
        }
        return name;
    }

    /**
     * Applies theme to {@link WebSyntaxArea}.
     *
     * @param syntaxArea {@link WebSyntaxArea}
     */
    public void apply ( @NotNull final RSyntaxTextArea syntaxArea )
    {
        try
        {
            Theme.load ( SyntaxTheme.class.getResourceAsStream ( "themes/" + this + ".xml" ) ).apply ( syntaxArea );
        }
        catch ( final IOException e )
        {
            LoggerFactory.getLogger ( SyntaxTheme.class ).error ( e.toString (), e );
        }
    }
}