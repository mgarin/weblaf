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

package com.alee.demo.content.window;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.laf.button.WebButton;
import com.alee.managers.language.LM;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ExceptionUtils;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JOptionPaneExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "joptionpane";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "optionpane";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Make Dialogs", "dialog" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        final List<Preview> previews = new ArrayList<Preview> ();
        for ( final Type type : Type.values () )
        {
            previews.add ( new OptionPanePreview ( type ) );
        }
        return previews;
    }

    /**
     * Message option pane.
     */
    protected class OptionPanePreview extends AbstractStylePreview
    {
        /**
         * Option pane dialog type.
         */
        private final Type type;

        /**
         * Constructs new style preview.
         *
         * @param type option pane dialog type
         */
        public OptionPanePreview ( final Type type )
        {
            super ( JOptionPaneExample.this, type.name (), FeatureState.updated, StyleId.optionpane );
            this.type = type;
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebButton system = createOptionPaneButton ( Decoration.system );
            final WebButton decorated = createOptionPaneButton ( Decoration.decorated );
            return CollectionUtils.asList ( system, decorated );
        }

        /**
         * Returns option pane display button.
         *
         * @param decoration option pane dialog decoration type
         * @return option pane display button
         */
        protected WebButton createOptionPaneButton ( final Decoration decoration )
        {
            final WebButton button = new WebButton ( getExampleLanguagePrefix () + "show." + decoration );
            button.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    // Setup dialog decoration if needed
                    final boolean dd = JDialog.isDefaultLookAndFeelDecorated ();
                    if ( decoration == Decoration.decorated )
                    {
                        JDialog.setDefaultLookAndFeelDecorated ( true );
                    }

                    // Display option pane
                    final Window parent = CoreSwingUtils.getWindowAncestor ( button );
                    final String title = LM.get ( getPreviewLanguagePrefix () + "title" );
                    final String message = LM.get ( getPreviewLanguagePrefix () + "message" );
                    final String notification = LM.get ( getPreviewLanguagePrefix () + "notification" );
                    final String closed = LM.get ( getExampleLanguagePrefix () + "closed" );
                    final WebNotification ntf;
                    if ( type == Type.input )
                    {
                        final int type = JOptionPane.WARNING_MESSAGE;
                        final String result = JOptionPane.showInputDialog ( parent, message, title, type );
                        if ( result != null )
                        {
                            ntf = NotificationManager.showNotification ( parent, String.format ( notification, result ) );
                        }
                        else
                        {
                            ntf = NotificationManager.showNotification ( parent, closed );
                        }
                    }
                    else if ( type == Type.choice )
                    {
                        final int type = JOptionPane.ERROR_MESSAGE;
                        final String e1 = NullPointerException.class.getCanonicalName ();
                        final String e2 = IllegalArgumentException.class.getCanonicalName ();
                        final String e3 = ClassCastException.class.getCanonicalName ();
                        final String e4 = ArrayIndexOutOfBoundsException.class.getCanonicalName ();
                        final String e5 = ObjectStreamException.class.getCanonicalName ();
                        final String e6 = FileNotFoundException.class.getCanonicalName ();
                        final String[] options = { e1, e2, e3, e4, e5, e6 };
                        final String result = ( String ) JOptionPane.showInputDialog ( parent, message, title, type, null, options, e1 );
                        if ( result != null )
                        {
                            final Throwable exception = ReflectUtils.createInstanceSafely ( result );
                            final Throwable nonNull = exception != null ? exception : new NullPointerException ();
                            final String stackTrace = ExceptionUtils.getStackTrace ( nonNull );
                            ntf = NotificationManager.showNotification ( parent, String.format ( notification, stackTrace ) );
                        }
                        else
                        {
                            ntf = NotificationManager.showNotification ( parent, closed );
                        }
                    }
                    else if ( type == Type.confirm )
                    {
                        final int type = JOptionPane.QUESTION_MESSAGE;
                        final int result = JOptionPane.showConfirmDialog ( parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION, type );
                        if ( result != JOptionPane.CLOSED_OPTION )
                        {
                            ntf = NotificationManager.showNotification ( parent, String.format ( notification, result ) );
                        }
                        else
                        {
                            ntf = NotificationManager.showNotification ( parent, closed );
                        }
                    }
                    else
                    {
                        final int type = JOptionPane.INFORMATION_MESSAGE;
                        JOptionPane.showMessageDialog ( parent, message, title, type );
                        ntf = NotificationManager.showNotification ( parent, closed );
                    }
                    ntf.setDisplayTime ( 2000 );

                    // Restore dialog decoration if needed
                    if ( decoration == Decoration.decorated )
                    {
                        JDialog.setDefaultLookAndFeelDecorated ( dd );
                    }
                }
            } );
            return button;
        }
    }

    /**
     * Option pane type.
     */
    protected enum Type
    {
        message,
        input,
        choice,
        confirm
    }

    /**
     * Option pane dialog decoraton.
     */
    protected enum Decoration
    {
        system,
        decorated
    }
}