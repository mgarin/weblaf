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

package com.alee.extended.breadcrumb;

import com.alee.api.annotations.NotNull;
import com.alee.extended.WebContainer;
import com.alee.extended.breadcrumb.element.BreadcrumbElementData;
import com.alee.extended.breadcrumb.element.BreadcrumbElementPainter;
import com.alee.extended.button.WebSplitButton;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.date.WebDateField;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.link.WebLink;
import com.alee.managers.style.ComponentException;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom breadcrumb component.
 * It allows positioning multiple elements within itself and applies custom style to them.
 * Each element can also display a progress bar on its background if it is a supported breadcrumb element.
 * It is most commonly used to create navigation menus or to display some process steps, but not limited to that.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see BreadcrumbDescriptor
 * @see WBreadcrumbUI
 * @see WebBreadcrumbUI
 * @see IBreadcrumbPainter
 * @see BreadcrumbPainter
 * @see WebContainer
 */
public class WebBreadcrumb extends WebContainer<WebBreadcrumb, WBreadcrumbUI>
{
    /**
     * Component properties.
     */
    public static final String FORCE_STYLE_PROPERTY = "forceStyle";

    /**
     * Whether or not style should be forced onto the added elements if possible.
     */
    protected boolean forceStyle;

    /**
     * {@link Map} containing {@link BreadcrumbElementData} for all elements currently added to this {@link WebBreadcrumb}.
     */
    protected transient Map<JComponent, BreadcrumbElementData> data;

    /**
     * Constructs new {@link WebBreadcrumb}.
     */
    public WebBreadcrumb ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link WebBreadcrumb}.
     *
     * @param id style ID
     */
    public WebBreadcrumb ( final StyleId id )
    {
        this.forceStyle = true;
        this.data = new HashMap<JComponent, BreadcrumbElementData> ( 5 );
        updateUI ();
        setStyleId ( id );
    }

    @Override
    public BreadcrumbLayout getLayout ()
    {
        return ( BreadcrumbLayout ) super.getLayout ();
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.breadcrumb;
    }

    /**
     * Returns whether or not style should be forced onto the added elements if possible.
     *
     * @return {@code true} if style should be forced onto the added elements if possible, {@code false} otherwise
     */
    public boolean isForceStyle ()
    {
        return forceStyle;
    }

    /**
     * Sets whether or not style should be forced onto the added elements if possible.
     *
     * @param forceStyle whether or not style should be forced onto the added elements if possible
     */
    public void setForceStyle ( final boolean forceStyle )
    {
        final boolean previous = this.forceStyle;
        this.forceStyle = forceStyle;
        firePropertyChange ( FORCE_STYLE_PROPERTY, previous, forceStyle );
    }

    @Override
    protected void addImpl ( final Component component, final Object constraints, final int index )
    {
        // todo Better way to present supported components
        if ( isSupported ( component ) )
        {
            data.put ( ( JComponent ) component, new BreadcrumbElementData () );
            if ( component instanceof JPanel )
            {
                StyleManager.setStyleId ( ( JPanel ) component, StyleId.breadcrumbPanel.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof WebLink )
            {
                StyleManager.setStyleId ( ( WebLink ) component, StyleId.breadcrumbLink.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof WebStyledLabel )
            {
                StyleManager.setStyleId ( ( WebStyledLabel ) component, StyleId.breadcrumbStyledLabel.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof JLabel )
            {
                StyleManager.setStyleId ( ( JLabel ) component, StyleId.breadcrumbLabel.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof WebTristateCheckBox )
            {
                StyleManager.setStyleId ( ( WebTristateCheckBox ) component, StyleId.breadcrumbTristateCheckBox.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof JCheckBox )
            {
                StyleManager.setStyleId ( ( JCheckBox ) component, StyleId.breadcrumbCheckBox.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof JRadioButton )
            {
                StyleManager.setStyleId ( ( JRadioButton ) component, StyleId.breadcrumbRadioButton.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof WebSplitButton )
            {
                StyleManager.setStyleId ( ( WebSplitButton ) component, StyleId.breadcrumbSplitButton.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof JToggleButton )
            {
                StyleManager.setStyleId ( ( JToggleButton ) component, StyleId.breadcrumbToggleButton.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof JButton )
            {
                StyleManager.setStyleId ( ( JButton ) component, StyleId.breadcrumbButton.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof JFormattedTextField )
            {
                StyleManager.setStyleId ( ( JFormattedTextField ) component,
                        StyleId.breadcrumbFormattedTextField.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof JPasswordField )
            {
                StyleManager.setStyleId ( ( JPasswordField ) component, StyleId.breadcrumbPasswordField.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof JTextField )
            {
                StyleManager.setStyleId ( ( JTextField ) component, StyleId.breadcrumbTextField.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof JComboBox )
            {
                StyleManager.setStyleId ( ( JComboBox ) component, StyleId.breadcrumbComboBox.at ( WebBreadcrumb.this ) );
            }
            else if ( component instanceof WebDateField )
            {
                StyleManager.setStyleId ( ( WebDateField ) component, StyleId.breadcrumbDateField.at ( WebBreadcrumb.this ) );
            }
            else
            {
                throw new ComponentException ( "Unsupported breadcrumb element: " + component );
            }
        }

        super.addImpl ( component, constraints, index );

        final int actualIndex = getComponentZOrder ( component );
        DecorationUtils.fireStatesChanged ( ( JComponent ) component );
        if ( actualIndex > 0 )
        {
            final Component previous = getComponent ( actualIndex - 1 );
            if ( PainterSupport.isDecoratable ( previous ) )
            {
                DecorationUtils.fireStatesChanged ( ( JComponent ) previous );
            }
        }
        if ( actualIndex < getComponentCount () - 1 )
        {
            final Component next = getComponent ( actualIndex + 1 );
            if ( PainterSupport.isDecoratable ( next ) )
            {
                DecorationUtils.fireStatesChanged ( ( JComponent ) next );
            }
        }
    }

    /**
     * Returns whether or not specified {@link Component} is supported as element of {@link WebBreadcrumb}.
     *
     * @param component {@link Component} to check
     * @return {@code true} if specified {@link Component} is supported as element of {@link WebBreadcrumb}, {@code false} otherwise
     */
    protected boolean isSupported ( final Component component )
    {
        // todo Better way to present supported components
        return component instanceof JPanel ||
                component instanceof JLabel ||
                component instanceof WebStyledLabel ||
                component instanceof WebLink ||
                component instanceof JButton ||
                component instanceof JToggleButton ||
                component instanceof WebSplitButton ||
                component instanceof JComboBox ||
                component instanceof WebDateField ||
                component instanceof JCheckBox ||
                component instanceof WebTristateCheckBox ||
                component instanceof JRadioButton ||
                component instanceof JTextField ||
                component instanceof JFormattedTextField ||
                component instanceof JPasswordField;
    }

    @Override
    public void remove ( final int index )
    {
        final Component component = getComponent ( index );
        if ( isApplied ( component ) )
        {
            data.remove ( component );
        }

        super.remove ( index );

        DecorationUtils.fireStatesChanged ( ( JComponent ) component );
        if ( index > 0 )
        {
            final Component previous = getComponent ( index - 1 );
            if ( PainterSupport.isDecoratable ( previous ) )
            {
                DecorationUtils.fireStatesChanged ( ( JComponent ) previous );
            }
        }
        if ( index < getComponentCount () )
        {
            final Component next = getComponent ( index );
            if ( PainterSupport.isDecoratable ( next ) )
            {
                DecorationUtils.fireStatesChanged ( ( JComponent ) next );
            }
        }
    }

    /**
     * Returns whether or not specified {@link Component} is supported as element of {@link WebBreadcrumb}.
     *
     * @param component {@link Component} to check
     * @return {@code true} if specified {@link Component} is supported as element of {@link WebBreadcrumb}, {@code false} otherwise
     */
    protected boolean isApplied ( final Component component )
    {
        final boolean applied;
        if ( component instanceof JComponent )
        {
            // todo Better way to retrieve painter
            final ComponentUI ui = LafUtils.getUI ( ( JComponent ) component );
            final Painter painter = ReflectUtils.getFieldValueSafely ( ui, "painter" );
            applied = painter instanceof BreadcrumbElementPainter;
        }
        else
        {
            applied = false;
        }
        return applied;
    }

    /**
     * Returns whether or not specified {@link Component} is first within this {@link WebBreadcrumb}.
     *
     * @param component {@link Component} to check position for
     * @return {@code true} if specified {@link Component} is first within this {@link WebBreadcrumb}, {@code false} otherwise
     */
    public boolean isFirst ( final Component component )
    {
        return component == getFirstComponent ();
    }

    /**
     * Returns whether or not specified {@link Component} is last within this {@link WebBreadcrumb}.
     *
     * @param component {@link Component} to check position for
     * @return {@code true} if specified {@link Component} is last within this {@link WebBreadcrumb}, {@code false} otherwise
     */
    public boolean isLast ( final Component component )
    {
        return component == getLastComponent ();
    }

    /**
     * Returns displayed progress type for the specified {@link JComponent}.
     *
     * @param component {@link JComponent}
     * @return displayed progress type for the specified {@link JComponent}
     */
    public BreadcrumbElementData.ProgressType getProgressType ( final JComponent component )
    {
        return data ( component ).getProgressType ();
    }

    /**
     * Sets displayed progress type for the specified {@link JComponent}.
     *
     * @param component {@link JComponent}
     * @param type      new displayed progress type for the specified {@link JComponent}
     */
    public void setProgressType ( final JComponent component, final BreadcrumbElementData.ProgressType type )
    {
        data ( component ).setProgressType ( type );
        DecorationUtils.fireStatesChanged ( component );
        repaint ();
    }

    /**
     * Returns breadcrumb element progress for the specified {@link JComponent}.
     *
     * @param component {@link JComponent}
     * @return breadcrumb element progress for the specified {@link JComponent}
     */
    public double getProgress ( final JComponent component )
    {
        return data ( component ).getProgress ();
    }

    /**
     * Sets breadcrumb element progress for the specified {@link JComponent}.
     *
     * @param component {@link JComponent}
     * @param progress  breadcrumb element progress for the specified {@link JComponent}
     */
    public void setProgress ( final JComponent component, final double progress )
    {
        data ( component ).setProgress ( progress );
        DecorationUtils.fireStatesChanged ( component );
        repaint ();
    }

    /**
     * Returns {@link BreadcrumbElementData} on the specified {@link JComponent}.
     * If {@link BreadcrumbElementData} is not available {@link ComponentException} is thrown.
     *
     * @param component {@link JComponent} to retrieve {@link BreadcrumbElementData} for
     * @return {@link BreadcrumbElementData} on the specified {@link JComponent}
     */
    protected BreadcrumbElementData data ( final JComponent component )
    {
        final BreadcrumbElementData data = this.data.get ( component );
        if ( data == null )
        {
            throw new ComponentException ( "Specified JComponent is representing breadcrumb element: " + component );
        }
        return data;
    }

    /**
     * Returns the LaF object that renders this component.
     *
     * @return LabelUI object
     */
    public WBreadcrumbUI getUI ()
    {
        return ( WBreadcrumbUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WBreadcrumbUI}
     */
    public void setUI ( final WBreadcrumbUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @NotNull
    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}