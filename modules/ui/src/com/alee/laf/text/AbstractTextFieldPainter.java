package com.alee.laf.text;

import com.alee.laf.text.AbstractTextEditorPainter;
import com.alee.laf.text.IAbstractTextFieldPainter;
import com.alee.laf.text.TextFieldLayout;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public abstract class AbstractTextFieldPainter<E extends JTextComponent, U extends BasicTextUI, D extends IDecoration<E, D>>
        extends AbstractTextEditorPainter<E, U, D> implements IAbstractTextFieldPainter<E, U>, SwingConstants
{
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Installing custom layout for leading/trailing components
        component.setLayout ( new TextFieldLayout () );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Uninstalling custom layout for leading/trailing components
        component.setLayout ( null );

        super.uninstall ( c, ui );
    }

    @Override
    public Insets getBorders ()
    {
        final Insets base = super.getBorders ();
        final Component lc = getLeadingComponent ();
        final Component tc = getTrailingComponent ();
        if ( lc != null || tc != null )
        {
            return i ( base, 0, lc != null ? lc.getPreferredSize ().width : 0, 0, tc != null ? tc.getPreferredSize ().width : 0 );
        }
        else
        {
            return base;
        }
    }
}