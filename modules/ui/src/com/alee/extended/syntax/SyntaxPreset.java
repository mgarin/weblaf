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

import com.alee.utils.ColorUtils;
import com.alee.utils.FileUtils;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import java.awt.*;
import java.io.File;
import java.util.Locale;

/**
 * Custom settings presets for WebSyntaxArea.
 *
 * @author Mikle Garin
 */

public enum SyntaxPreset
{
    base ( PresetType.settings )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setAntiAliasingEnabled ( true );
                    syntaxArea.setUseFocusableTips ( true );
                    syntaxArea.setTabSize ( 4 );
                    syntaxArea.setCodeFoldingEnabled ( true );
                    syntaxArea.setPaintTabLines ( false );
                    syntaxArea.setWhitespaceVisible ( false );
                    syntaxArea.setEOLMarkersVisible ( false );
                }
            },

    size ( PresetType.style )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setRows ( 4 );
                    syntaxArea.setColumns ( 1 );
                }
            },

    margin ( PresetType.style )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setMargin ( new Insets ( 5, 5, 5, 5 ) );
                }
            },

    historyLimit ( PresetType.settings )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.getUndoManager ().setLimit ( 50 );
                }
            },

    viewable ( PresetType.settings )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setEditable ( false );
                }
            },
    editable ( PresetType.settings )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setEditable ( true );
                }
            },

    hideMenu ( PresetType.settings )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setPopupMenu ( null );
                }
            },

    transparent ( PresetType.style )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setBackground ( ColorUtils.transparent () );
                    syntaxArea.setOpaque ( false );
                }
            },
    opaque ( PresetType.style )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setBackground ( Color.WHITE );
                    syntaxArea.setOpaque ( true );
                }
            },

    java ( PresetType.syntax )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_JAVA );
                }
            },
    xml ( PresetType.syntax )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_XML );
                }
            },
    html ( PresetType.syntax )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_HTML );
                }
            },
    css ( PresetType.syntax )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_CSS );
                }
            },
    js ( PresetType.syntax )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT );
                }
            },
    php ( PresetType.syntax )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_PHP );
                }
            },
    sql ( PresetType.syntax )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_SQL );
                }
            },
    none ( PresetType.syntax )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    syntaxArea.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_NONE );
                }
            },

    ideaTheme ( PresetType.theme )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    SyntaxTheme.idea.apply ( syntaxArea );
                }
            },
    darkTheme ( PresetType.theme )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    SyntaxTheme.dark.apply ( syntaxArea );
                }
            },
    vsTheme ( PresetType.theme )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    SyntaxTheme.vs.apply ( syntaxArea );
                }
            },
    eclipseTheme ( PresetType.theme )
            {
                @Override
                public void apply ( final WebSyntaxArea syntaxArea )
                {
                    SyntaxTheme.eclipse.apply ( syntaxArea );
                }
            };

    /**
     * SyntaxPreset type.
     */
    private final PresetType type;

    /**
     * Constructs new enumeration.
     *
     * @param type SyntaxPreset type
     */
    private SyntaxPreset ( final PresetType type )
    {
        this.type = type;
    }

    /**
     * Returns SyntaxPreset type.
     *
     * @return SyntaxPreset type
     */
    public PresetType getType ()
    {
        return type;
    }

    /**
     * Applies syntax preset to the specified WebSyntaxArea.
     *
     * @param syntaxArea WebSyntaxArea
     */
    public abstract void apply ( WebSyntaxArea syntaxArea );

    /**
     * Returns syntax preset for the specified file.
     *
     * @param file file
     * @return syntax preset for the specified file
     */
    public static SyntaxPreset getSyntaxPreset ( final File file )
    {
        return getSyntaxPreset ( file.getName () );
    }

    /**
     * Returns syntax preset for the specified file name.
     *
     * @param file file name
     * @return syntax preset for the specified file name
     */
    public static SyntaxPreset getSyntaxPreset ( final String file )
    {
        final String fe = FileUtils.getFileExtPart ( file, false );
        final String ext = fe.length () > 0 ? fe.toLowerCase ( Locale.ROOT ) : file;
        if ( ext.contains ( "java" ) )
        {
            return java;
        }
        else if ( ext.contains ( "xml" ) )
        {
            return xml;
        }
        else if ( ext.contains ( "htm" ) )
        {
            return html;
        }
        else if ( ext.contains ( "css" ) )
        {
            return css;
        }
        else if ( ext.contains ( "js" ) || ext.contains ( "javascript" ) )
        {
            return js;
        }
        else if ( ext.contains ( "php" ) )
        {
            return php;
        }
        else if ( ext.contains ( "sql" ) )
        {
            return sql;
        }
        else
        {
            return none;
        }
    }
}