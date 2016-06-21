package com.alee.laf.label;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.layout.IconTextContentLayout;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;

/**
 * Label layout implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Alexandr Zernov
 */
@XStreamAlias ( "LabelLayout" )
public class LabelContentLayout<E extends JLabel, D extends IDecoration<E, D>, I extends IconTextContentLayout<E, D, I>>
        extends IconTextContentLayout<E, D, I>
{
    @Override
    protected int getIconTextGap ( final E c, final D d )
    {
        return gap != null ? gap : c.getIconTextGap ();
    }

    @Override
    protected int getHorizontalAlignment ( final E c, final D d )
    {
        return halign != null ? halign.getValue () : c.getHorizontalAlignment ();
    }

    @Override
    protected int getVerticalAlignment ( final E c, final D d )
    {
        return valign != null ? valign.getValue () : c.getVerticalAlignment ();
    }

    @Override
    protected int getHorizontalTextPosition ( final E c, final D d )
    {
        return hpos != null ? hpos.getValue () : c.getHorizontalTextPosition ();
    }

    @Override
    protected int getVerticalTextPosition ( final E c, final D d )
    {
        return vpos != null ? vpos.getValue () : c.getVerticalTextPosition ();
    }
}
