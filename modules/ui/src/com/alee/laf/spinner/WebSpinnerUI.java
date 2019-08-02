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

import com.alee.api.jdk.Consumer;
import com.alee.laf.button.WebButton;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
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
public class WebSpinnerUI extends BasicSpinnerUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( SpinnerPainter.class )
    protected ISpinnerPainter painter;

    /**
     * Returns an instance of the {@link WebSpinnerUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebSpinnerUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebSpinnerUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( spinner );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( spinner );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected LayoutManager createLayout ()
    {
        return new WebSpinnerLayout ();
    }

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
    protected void configureEditorContainer ( final JSpinner.DefaultEditor container, final JSpinner spinner )
    {
        // Installing proper styling
        StyleId.spinnerEditorContainer.at ( spinner ).set ( container );
    }

    /**
     * Configures spinner editor.
     *
     * @param field   spinner editor
     * @param spinner spinner
     */
    protected void configureEditor ( final JTextComponent field, final JSpinner spinner )
    {
        // Installing proper styling
        StyleId.spinnerEditor.at ( spinner ).set ( field );
    }

    @Override
    protected Component createNextButton ()
    {
        final WebButton nextButton = new WebButton ( StyleId.spinnerNextButton.at ( spinner ), Icons.upSmall );
        nextButton.setName ( "Spinner.nextButton" );
        installNextButtonListeners ( nextButton );
        return nextButton;
    }

    @Override
    protected Component createPreviousButton ()
    {
        final WebButton prevButton = new WebButton ( StyleId.spinnerPreviousButton.at ( spinner ), Icons.downSmall );
        prevButton.setName ( "Spinner.previousButton" );
        installPreviousButtonListeners ( prevButton );
        return prevButton;
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( spinner, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( spinner, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( spinner, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( spinner );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( spinner, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( spinner );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( spinner, padding );
    }

    /**
     * Returns spinner painter.
     *
     * @return spinner painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets spinner painter.
     * Pass null to remove spinner painter.
     *
     * @param painter new spinner painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( spinner, new Consumer<ISpinnerPainter> ()
        {
            @Override
            public void accept ( final ISpinnerPainter newPainter )
            {
                WebSpinnerUI.this.painter = newPainter;
            }
        }, this.painter, painter, ISpinnerPainter.class, AdaptiveSpinnerPainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        // return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
        return null;
    }
}