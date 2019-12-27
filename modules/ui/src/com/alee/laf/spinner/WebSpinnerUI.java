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

package com.alee.laf.spinner;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.button.WebButton;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.*;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Custom UI for {@link JSpinner} component.
 *
 * @author Mikle Garin
 */
public class WebSpinnerUI extends BasicSpinnerUI
{
    /**
     * Returns an instance of the {@link WebSpinnerUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebSpinnerUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebSpinnerUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( spinner );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( spinner );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @NotNull
    @Override
    protected LayoutManager createLayout ()
    {
        return new WebSpinnerLayout ();
    }

    @NotNull
    @Override
    protected JComponent createEditor ()
    {
        final JComponent editor = spinner.getEditor ();
        editor.setInheritsPopupMenu ( true );
        if ( editor instanceof JTextComponent )
        {
            configureEditor ( ( JTextComponent ) editor, spinner );
        }
        else if ( editor instanceof JSpinner.DefaultEditor )
        {
            final JSpinner.DefaultEditor container = ( JSpinner.DefaultEditor ) editor;
            configureEditorContainer ( container, spinner );
            configureEditor ( container.getTextField (), spinner );
        }
        return editor;
    }

    /**
     * Configures spinner editor container.
     *
     * @param container spinner editor container
     * @param spinner   spinner
     */
    protected void configureEditorContainer ( @NotNull final JSpinner.DefaultEditor container, @NotNull final JSpinner spinner )
    {
        StyleId.spinnerEditorContainer.at ( spinner ).set ( container );
    }

    /**
     * Configures spinner editor.
     *
     * @param field   spinner editor
     * @param spinner spinner
     */
    protected void configureEditor ( @NotNull final JTextComponent field, @NotNull final JSpinner spinner )
    {
        StyleId.spinnerEditor.at ( spinner ).set ( field );
    }

    @NotNull
    @Override
    protected Component createNextButton ()
    {
        final WebButton nextButton = new WebButton ( StyleId.spinnerNextButton.at ( spinner ), Icons.upSmall );
        nextButton.setName ( "Spinner.nextButton" );
        installNextButtonListeners ( nextButton );
        return nextButton;
    }

    @NotNull
    @Override
    protected Component createPreviousButton ()
    {
        final WebButton prevButton = new WebButton ( StyleId.spinnerPreviousButton.at ( spinner ), Icons.downSmall );
        prevButton.setName ( "Spinner.previousButton" );
        installPreviousButtonListeners ( prevButton );
        return prevButton;
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return null;
    }
}