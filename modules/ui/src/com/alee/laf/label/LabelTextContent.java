package com.alee.laf.label;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractTextContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

/**
 * Label text content implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Alexandr Zernov
 */

@XStreamAlias ( "LabelText" )
public class LabelTextContent<E extends JLabel, D extends IDecoration<E, D>, I extends LabelTextContent<E, D, I>>
        extends AbstractTextContent<E, D, I>
{
    @Override
    protected boolean isHtmlText ( final E c, final D d )
    {
        return c.getClientProperty ( BasicHTML.propertyKey ) != null;
    }

    @Override
    protected View getHtml ( final E c, final D d )
    {
        return ( View ) c.getClientProperty ( BasicHTML.propertyKey );
    }

    @Override
    protected String getText ( final E c, final D d )
    {
        return c.getText ();
    }

    @Override
    protected int getMnemonicIndex ( final E c, final D d )
    {
        return c.getDisplayedMnemonicIndex ();
    }
}
