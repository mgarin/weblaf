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

import com.alee.managers.style.StyleId;
import com.alee.laf.panel.WebPanel;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import static com.alee.extended.syntax.SyntaxPreset.*;

/**
 * Styled WebSyntaxArea extension.
 * This is mostly usable for in-line code display within application.
 *
 * @author Mikle Garin
 */
public class WebSyntaxPanel extends WebPanel
{
    /**
     * WebSyntaxArea that performs code highlight.
     */
    private final WebSyntaxArea syntaxArea;

    /**
     * Constructs new WebSyntaxPanel.
     *
     * @param presets presets to apply
     */
    public WebSyntaxPanel ( final SyntaxPreset... presets )
    {
        this ( StyleId.syntaxpanel, "", presets );
    }

    /**
     * Constructs new WebSyntaxPanel.
     *
     * @param code    displayed code
     * @param presets presets to apply
     */
    public WebSyntaxPanel ( final String code, final SyntaxPreset... presets )
    {
        this ( StyleId.syntaxpanel, code, presets );
    }

    /**
     * Constructs new WebSyntaxPanel.
     *
     * @param id      style ID
     * @param presets presets to apply
     */
    public WebSyntaxPanel ( final StyleId id, final SyntaxPreset... presets )
    {
        this ( id, "", presets );
    }

    /**
     * Constructs new WebSyntaxPanel.
     *
     * @param id      style ID
     * @param code    displayed code
     * @param presets presets to apply
     */
    public WebSyntaxPanel ( final StyleId id, final String code, final SyntaxPreset... presets )
    {
        super ( id );

        // Syntax area
        syntaxArea = new WebSyntaxArea ( code );
        syntaxArea.applyPresets ( base, viewable, hideMenu, ideaTheme, transparent );
        syntaxArea.applyPresets ( presets );
        syntaxArea.setText ( code );

        // Additional focus loss listener that resets caret position
        // This is required to drop any highlights made due to caret position changes
        // This also disables current line highlight for non-focused areas
        syntaxArea.setHighlightCurrentLine ( false );
        syntaxArea.addFocusListener ( new FocusAdapter ()
        {
            @Override
            public void focusGained ( final FocusEvent e )
            {
                syntaxArea.setHighlightCurrentLine ( shouldHighlightCurrentLine () );
            }

            @Override
            public void focusLost ( final FocusEvent e )
            {
                syntaxArea.setHighlightCurrentLine ( false );
                syntaxArea.setCaretPosition ( 0 );
            }
        } );

        add ( syntaxArea );
    }

    /**
     * Sets displayed text.
     *
     * @param text displayed text
     */
    public void setText ( final String text )
    {
        syntaxArea.setText ( text );
        syntaxArea.setHighlightCurrentLine ( shouldHighlightCurrentLine () );
    }

    /**
     * Returns whether should highlight current line or not.
     *
     * @return true if should highlight current line, false otherwise
     */
    protected boolean shouldHighlightCurrentLine ()
    {
        return syntaxArea.getLineCount () > 1;
    }

    /**
     * Applies presets to underlying WebSyntaxArea.
     *
     * @param presets presets to apply
     */
    public void applyPresets ( final SyntaxPreset... presets )
    {
        syntaxArea.applyPresets ( presets );
    }

    /**
     * Applies presets to underlying WebSyntaxArea.
     *
     * @param presets presets to apply
     */
    public void applyPresets ( final List<SyntaxPreset> presets )
    {
        syntaxArea.applyPresets ( presets );
    }

    /**
     * Returns underlying WebSyntaxArea that performs code highlight.
     *
     * @return underlying WebSyntaxArea that performs code highlight
     */
    public WebSyntaxArea getSyntaxArea ()
    {
        return syntaxArea;
    }
}