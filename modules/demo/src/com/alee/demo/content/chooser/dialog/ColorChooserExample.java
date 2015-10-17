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

package com.alee.demo.content.chooser.dialog;

import com.alee.demo.api.AbstractExample;
import com.alee.demo.api.AbstractStylePreview;
import com.alee.demo.api.FeatureState;
import com.alee.demo.api.Preview;
import com.alee.laf.button.WebButton;
import com.alee.laf.colorchooser.WebColorChooser;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class ColorChooserExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "colorchooser";
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList ( new ColorChooserDialog ( StyleId.textfield ) );
    }

    /**
     * Color chooser dialog preview.
     */
    protected class ColorChooserDialog extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id preview style ID
         */
        public ColorChooserDialog ( final StyleId id )
        {
            super ( ColorChooserExample.this, "basic", FeatureState.updated, id );
        }

        @Override
        protected JComponent createPreviewContent ( final StyleId id )
        {
            return new WebButton ( "Show color chooser", new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    WebColorChooser.showDialog ( ( Component ) e.getSource () );
                }
            } );
        }
    }
}