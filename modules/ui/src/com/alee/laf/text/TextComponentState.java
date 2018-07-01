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

package com.alee.laf.text;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.text.JTextComponent;
import java.io.Serializable;

/**
 * {@link JTextComponent} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see TextComponentSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "TextComponentState" )
public class TextComponentState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link JTextComponent} text.
     */
    @XStreamAsAttribute
    protected final String text;

    /**
     * {@link JTextComponent} caret position.
     */
    @XStreamAsAttribute
    protected final Integer caretPosition;

    /**
     * Constructs default {@link TextComponentState}.
     */
    public TextComponentState ()
    {
        this ( "", null );
    }

    /**
     * Constructs new {@link TextComponentState} with settings from {@link JTextComponent}.
     *
     * @param textComponent {@link JTextComponent} to retrieve settings from
     */
    public TextComponentState ( final JTextComponent textComponent )
    {
        this ( textComponent.getText (), textComponent.getCaretPosition () );
    }

    /**
     * Constructs new {@link TextComponentState} with specified settings.
     *
     * @param text          {@link JTextComponent} text
     * @param caretPosition {@link JTextComponent} caret position
     */
    public TextComponentState ( final String text, final Integer caretPosition )
    {
        this.caretPosition = caretPosition;
        this.text = text;
    }

    /**
     * Returns {@link JTextComponent} text.
     *
     * @return {@link JTextComponent} text
     */
    public String text ()
    {
        return text;
    }

    /**
     * Returns {@link JTextComponent} caret position.
     *
     * @return {@link JTextComponent} caret position
     */
    public Integer caretPosition ()
    {
        return caretPosition;
    }

    /**
     * Applies this {@link TextComponentState} to the specified {@link JTextComponent}.
     *
     * @param textComponent {@link JTextComponent} to apply this {@link TextComponentState} to
     */
    public void apply ( final JTextComponent textComponent )
    {
        textComponent.setText ( text );
        if ( caretPosition != null )
        {
            textComponent.setCaretPosition ( caretPosition );
        }
    }
}