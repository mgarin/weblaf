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

package com.alee.demo.content;

import com.alee.api.annotations.NotNull;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.FormLayout;
import com.alee.extended.panel.AlignPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Utility class providing various sample UI elements for {@link com.alee.demo.DemoApplication} examples.
 *
 * @author Mikle Garin
 */
public final class SampleInterface
{
    /**
     * Returns sample auth form UI.
     *
     * @return sample auth form UI
     */
    @NotNull
    public static JComponent createAuthForm ()
    {
        final WebPanel form = new WebPanel ( StyleId.panelDecorated, new FormLayout ( 10, 10 ) );
        form.setPadding ( 15, 25, 15, 25 );

        form.add ( new WebStyledLabel ( "demo.sample.interface.auth.title", SwingConstants.CENTER ), FormLayout.LINE );

        form.add ( new WebLabel ( "demo.sample.interface.auth.login", SwingConstants.RIGHT ) );
        form.add ( new WebTextField ( 15 ) );

        form.add ( new WebLabel ( "demo.sample.interface.auth.password", SwingConstants.RIGHT ) );
        form.add ( new WebPasswordField ( 15 ) );

        form.add ( new AlignPanel (
                new GroupPanel (
                        5, true,
                        new WebButton ( "demo.sample.interface.auth.buttons.login" ).setPreferredWidth ( 100 ),
                        new WebButton ( "demo.sample.interface.auth.buttons.cancel" ).setPreferredWidth ( 100 )
                ),
                AlignPanel.CENTER,
                AlignPanel.CENTER
        ), FormLayout.LINE );

        return form;
    }
}