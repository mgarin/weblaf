package com.alee.extended.label;

import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractStyledTextContent;
import com.alee.painter.decoration.content.TextWrap;
import com.alee.utils.CompareUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Styled label text content implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Alexandr Zernov
 */

@XStreamAlias ("StyledLabelText")
public class StyledLabelTextContent<E extends WebStyledLabel, D extends IDecoration<E, D>, I extends StyledLabelTextContent<E, D, I>>
        extends AbstractStyledTextContent<E, D, I> implements PropertyChangeListener
{
    protected transient E component;
    protected transient D decoration;

    @Override
    public void activate ( final E c, final D d )
    {
        super.activate ( c, d );

        component = c;
        decoration = d;

        component.addPropertyChangeListener ( this );
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        component.removePropertyChangeListener ( this );

        decoration = null;
        component = null;

        super.deactivate ( c, d );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( CompareUtils.equals ( property, WebLookAndFeel.TEXT_PROPERTY, WebStyledLabel.PROPERTY_STYLE_RANGE ) )
        {
            buildTextRanges ( component, decoration, textRanges );
        }
    }

    @Override
    protected List<StyleRange> getStyleRanges ( final E c, final D d )
    {
        return c.getStyleRanges ();
    }

    @Override
    protected int getMaximumRows ( final E c, final D d )
    {
        return c.getMaximumRows ();
    }

    @Override
    protected int getRowGap ( final E c, final D d )
    {
        return rowGap != null ? rowGap : c.getRowGap ();
    }

    @Override
    protected TextWrap getWrapType ( final E c, final D d )
    {
        return c.getWrap ();
    }

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

    @Override
    protected int getHorizontalTextAlignment ( final E c, final D d )
    {
        return horizontalTextAlignment != null ? horizontalTextAlignment : c.getHorizontalAlignment ();
    }

    @Override
    protected int getVerticalTextAlignment ( final E c, final D d )
    {
        return verticalTextAlignment != null ? verticalTextAlignment : c.getVerticalAlignment ();
    }
}
