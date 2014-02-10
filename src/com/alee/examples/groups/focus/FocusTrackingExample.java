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

package com.alee.examples.groups.focus;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.text.WebTextField;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;

import javax.swing.*;
import java.awt.*;

/**
 * Focus tracking example.
 *
 * @author Mikle Garin
 */

public class FocusTrackingExample extends DefaultExample
{
    /**
     * Focus tracker strong references.
     */
    private DefaultFocusTracker focusTracker1;
    private DefaultFocusTracker focusTracker2;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Focus tracking";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Tacking component and its childs focus";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Tracked forms
        final Component form1 = createForm ();
        final Component form2 = createForm ();

        // Output area
        final TitledBorderPainter painter = new TitledBorderPainter ( "Focus changes:" );
        painter.setTitleAlignment ( SwingConstants.CENTER );
        final WebPanel panel = new WebPanel ( painter );
        final WebTextArea info = new WebTextArea ();
        info.setEditable ( false );
        info.setRows ( 1 );
        info.setMargin ( 5 );
        final WebScrollPane infoScroll = new WebScrollPane ( info );
        infoScroll.setPreferredWidth ( 150 );
        panel.add ( infoScroll );

        // Form #1 focus tracker
        // It tracks only focus loss/gain by form overall and doesn't inform about focus changes within the form
        focusTracker1 = new DefaultFocusTracker ( true )
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                updateInfo ( focused, info, "form1" );
            }
        };
        FocusManager.addFocusTracker ( form1, focusTracker1 );

        // Form #2 focus tracker
        // It tracks only focus loss/gain by form overall and doesn't inform about focus changes within the form
        focusTracker2 = new DefaultFocusTracker ( true )
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                updateInfo ( focused, info, "form2" );
            }
        };
        FocusManager.addFocusTracker ( form2, focusTracker2 );

        return new GroupPanel ( 10, new GroupPanel ( 10, false, form1, form2 ), panel ).setMargin ( 5 );
    }

    /**
     * Adds focus changes information into specified text area.
     *
     * @param focused whether form is focused or not
     * @param info    information text area
     * @param form    tracked form title
     */
    private void updateInfo ( final boolean focused, final WebTextArea info, final String form )
    {
        final String s = info.getText ().trim ().length () > 0 ? ( info.getText () + "\n" ) : "";
        info.setText ( s + ( focused ? form + " gained focus" : form + " lost focus" ) );
    }

    /**
     * Returns custom form for focus tracking.
     *
     * @return custom form for focus tracking
     */
    private Component createForm ()
    {
        final TitledBorderPainter painter = new TitledBorderPainter ( "Form 1" );
        painter.setTitleAlignment ( SwingConstants.CENTER );

        final WebPanel panel = new WebPanel ( painter );
        panel.setMargin ( 5 );
        final double[][] constraints = { { TableLayout.PREFERRED, TableLayout.FILL }, { TableLayout.PREFERRED, TableLayout.PREFERRED } };
        final TableLayout layout = new TableLayout ( constraints );
        layout.setHGap ( 5 );
        layout.setVGap ( 5 );
        panel.setLayout ( layout );

        panel.add ( new WebLabel ( "Field 1:" ), "0,0" );
        panel.add ( new WebTextField (), "1,0" );

        panel.add ( new WebLabel ( "Field 2:" ), "0,1" );
        panel.add ( new WebCheckBox ( "Some check" ), "1,1" );

        return panel;
    }
}