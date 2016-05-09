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

import com.alee.utils.CompareUtils;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * @author Mikle Garin
 */

public class FieldEditorKit extends DefaultEditorKit
{
    /**
     * Field editor kit.
     */
    protected static EditorKit fieldEditorKit;

    /**
     * Editor kit actions.
     */
    protected Action[] actions;

    /**
     * Constructs new field editor kit.
     */
    protected FieldEditorKit ()
    {
        super ();

        // Creating copy of default actions array
        final Action[] a = super.getActions ();
        actions = Arrays.copyOf ( a, a.length );

        // Looking for action indices
        for ( int i = 0; i < actions.length; i++ )
        {
            final Action action = actions[ i ];
            final Object name = action.getValue ( Action.NAME );

            // Replacing begin line action
            if ( CompareUtils.equals ( name, DefaultEditorKit.beginLineAction ) )
            {
                actions[ i ] = new TextAction ( DefaultEditorKit.beginLineAction )
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        final JTextComponent target = getTextComponent ( e );
                        if ( target != null )
                        {
                            target.setCaretPosition ( 0 );
                        }
                    }
                };
            }

            // Replacing end line action
            if ( CompareUtils.equals ( name, DefaultEditorKit.endLineAction ) )
            {
                actions[ i ] = new TextAction ( DefaultEditorKit.endLineAction )
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        final JTextComponent target = getTextComponent ( e );
                        if ( target != null )
                        {
                            target.setCaretPosition ( target.getDocument ().getLength () );
                        }
                    }
                };
            }
        }
    }

    @Override
    public Action[] getActions ()
    {
        return actions;
    }

    /**
     * Returns static editor kit instance.
     *
     * @return static editor kit instance
     */
    public synchronized static EditorKit get ()
    {
        if ( fieldEditorKit == null )
        {
            fieldEditorKit = new FieldEditorKit ();
        }
        return fieldEditorKit;
    }
}