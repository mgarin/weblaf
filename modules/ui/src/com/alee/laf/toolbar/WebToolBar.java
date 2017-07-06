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

package com.alee.laf.toolbar;

import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.swing.extensions.ContainerMethods;
import com.alee.utils.swing.extensions.ContainerMethodsImpl;
import com.alee.utils.swing.extensions.SizeMethods;
import com.alee.utils.swing.extensions.SizeMethodsImpl;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * {@link JToolBar} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JToolBar
 * @see WebToolBarUI
 * @see ToolBarPainter
 */

public class WebToolBar extends JToolBar
        implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods, ContainerMethods<WebToolBar>, SizeMethods<WebToolBar>
{
    /**
     * Constructs new toolbar.
     */
    public WebToolBar ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new toolbar.
     *
     * @param orientation toolbar orientation
     */
    public WebToolBar ( final int orientation )
    {
        this ( StyleId.auto, orientation );
    }

    /**
     * Constructs new toolbar.
     *
     * @param name toolbar name
     */
    public WebToolBar ( final String name )
    {
        this ( StyleId.auto, name );
    }

    /**
     * Constructs new toolbar.
     *
     * @param name        toolbar name
     * @param orientation toolbar orientation
     */
    public WebToolBar ( final String name, final int orientation )
    {
        this ( StyleId.auto, name, orientation );
    }

    /**
     * Constructs new toolbar.
     *
     * @param id style ID
     */
    public WebToolBar ( final StyleId id )
    {
        this ( id, null, HORIZONTAL );
    }

    /**
     * Constructs new toolbar.
     *
     * @param id          style ID
     * @param orientation toolbar orientation
     */
    public WebToolBar ( final StyleId id, final int orientation )
    {
        this ( id, null, orientation );
    }

    /**
     * Constructs new toolbar.
     *
     * @param id   style ID
     * @param name toolbar name
     */
    public WebToolBar ( final StyleId id, final String name )
    {
        this ( id, name, HORIZONTAL );
    }

    /**
     * Constructs new toolbar.
     *
     * @param id          style ID
     * @param name        toolbar name
     * @param orientation toolbar orientation
     */
    public WebToolBar ( final StyleId id, final String name, final int orientation )
    {
        super ( name, orientation );
        setStyleId ( id );
    }

    /**
     * Adds specified {@link Component} under {@link ToolbarLayout#MIDDLE} constraints.
     *
     * @param component {@link Component} to add
     */
    public void addToMiddle ( final Component component )
    {
        add ( component, ToolbarLayout.MIDDLE );
    }

    /**
     * Adds specified {@link Component} under {@link ToolbarLayout#FILL} constraints.
     *
     * @param component {@link Component} to add
     */
    public void addFill ( final Component component )
    {
        add ( component, ToolbarLayout.FILL );
    }

    /**
     * Adds specified {@link Component} under {@link ToolbarLayout#END} constraints.
     *
     * @param component {@link Component} to add
     */
    public void addToEnd ( final Component component )
    {
        add ( component, ToolbarLayout.END );
    }

    /**
     * Adds new {@link WebToolBarSeparator} under {@link ToolbarLayout#START} constraints.
     */
    @Override
    public void addSeparator ()
    {
        addSeparator ( ToolbarLayout.START );
    }

    /**
     * Adds new {@link WebToolBarSeparator} under {@link ToolbarLayout#MIDDLE} constraints.
     *
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparatorToMiddle ()
    {
        return addSeparator ( ToolbarLayout.MIDDLE );
    }

    /**
     * Adds new {@link WebToolBarSeparator} under {@link ToolbarLayout#END} constraints.
     *
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparatorToEnd ()
    {
        return addSeparator ( ToolbarLayout.END );
    }

    /**
     * Adds new {@link WebToolBarSeparator} under specified constraints.
     *
     * @param constraints constraints for {@link WebToolBarSeparator}
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparator ( final String constraints )
    {
        return addSeparator ( constraints, StyleId.toolbarseparator );
    }

    /**
     * Adds new {@link WebToolBarSeparator} with the specified {@link StyleId} under {@link ToolbarLayout#START} constraints.
     *
     * @param id {@link StyleId} for {@link WebToolBarSeparator}
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparator ( final StyleId id )
    {
        return addSeparator ( ToolbarLayout.START, id );
    }

    /**
     * Adds new {@link WebToolBarSeparator} with the specified {@link StyleId} under {@link ToolbarLayout#END} constraints.
     *
     * @param id {@link StyleId} for {@link WebToolBarSeparator}
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparatorToEnd ( final StyleId id )
    {
        return addSeparator ( ToolbarLayout.END, id );
    }

    /**
     * Adds new {@link WebToolBarSeparator} with the specified {@link StyleId} under the specified constraints.
     *
     * @param constraints constraints for {@link WebToolBarSeparator}
     * @param id          {@link StyleId} for {@link WebToolBarSeparator}
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparator ( final String constraints, final StyleId id )
    {
        final WebToolBarSeparator separator = new WebToolBarSeparator ( id );
        add ( separator, constraints );
        return separator;
    }

    /**
     * Adds {@link List} of {@link Component}s under the specified constraints.
     *
     * @param components  {@link List} of {@link Component}s to add
     * @param constraints constraints to add {@link Component}s under
     */
    public void add ( final List<? extends Component> components, final String constraints )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                add ( component, constraints );
            }
        }
    }

    /**
     * Adds {@link Component}s at the specified Z-index.
     *
     * @param index      Z-index to add {@link Component}s at
     * @param components {@link Component}s to add
     */
    public void add ( final int index, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( int i = 0; i < components.length; i++ )
            {
                add ( components[ i ], index + i );
            }
        }
    }

    /**
     * Adds {@link Component}s under the specified constraints.
     *
     * @param constraints constraints to add {@link Component}s under
     * @param components  {@link Component}s to add
     */
    public void add ( final String constraints, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( final Component component : components )
            {
                add ( component, constraints );
            }
        }
    }

    /**
     * Adds spacing between components.
     */
    public void addSpacing ()
    {
        addSpacing ( 2 );
    }

    /**
     * Adds spacing between components.
     *
     * @param spacing spacing size
     */
    public void addSpacing ( final int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.START );
    }

    /**
     * Adds spacing between components at the end.
     */
    public void addSpacingToEnd ()
    {
        addSpacingToEnd ( 2 );
    }

    /**
     * Adds spacing between components at the end.
     *
     * @param spacing spacing size
     */
    public void addSpacingToEnd ( final int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.END );
    }

    /**
     * Adds spacing between components at the specified constraints.
     *
     * @param spacing     spacing size
     * @param constraints layout constraints
     */
    public void addSpacing ( final int spacing, final String constraints )
    {
        // todo Add layout implementation instead of wasted component
        add ( new WhiteSpace ( spacing ), constraints );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.toolbar;
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( this );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( this, id );
    }

    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( this );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( this );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Override
    public Map<String, Painter> getCustomPainters ()
    {
        return StyleManager.getCustomPainters ( this );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter getCustomPainter ( final String id )
    {
        return StyleManager.getCustomPainter ( this, id );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public Painter setCustomPainter ( final String id, final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, id, painter );
    }

    @Override
    public boolean resetPainter ()
    {
        return StyleManager.resetPainter ( this );
    }

    @Override
    public Shape getShape ()
    {
        return ShapeMethodsImpl.getShape ( this );
    }

    @Override
    public Insets getMargin ()
    {
        return MarginMethodsImpl.getMargin ( this );
    }

    @Override
    public void setMargin ( final int margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        MarginMethodsImpl.setMargin ( this, top, left, bottom, right );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PaddingMethodsImpl.getPadding ( this );
    }

    @Override
    public void setPadding ( final int padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    @Override
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        PaddingMethodsImpl.setPadding ( this, top, left, bottom, right );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the {@link WebToolBarUI} object that renders this component
     */
    @Override
    public WebToolBarUI getUI ()
    {
        return ( WebToolBarUI ) super.getUI ();
    }

    /**
     * Sets the L&amp;F object that renders this component.
     *
     * @param ui {@link WebToolBarUI}
     */
    public void setUI ( final WebToolBarUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }

    @Override
    public boolean contains ( final Component component )
    {
        return ContainerMethodsImpl.contains ( this, component );
    }

    @Override
    public WebToolBar add ( final List<? extends Component> components )
    {
        return ContainerMethodsImpl.add ( this, components );
    }

    @Override
    public WebToolBar add ( final List<? extends Component> components, final int index )
    {
        return ContainerMethodsImpl.add ( this, components, index );
    }

    @Override
    public WebToolBar add ( final List<? extends Component> components, final Object constraints )
    {
        return ContainerMethodsImpl.add ( this, components, constraints );
    }

    @Override
    public WebToolBar add ( final Component component1, final Component component2 )
    {
        return ContainerMethodsImpl.add ( this, component1, component2 );
    }

    @Override
    public WebToolBar add ( final Component... components )
    {
        return ContainerMethodsImpl.add ( this, components );
    }

    @Override
    public WebToolBar remove ( final List<? extends Component> components )
    {
        return ContainerMethodsImpl.remove ( this, components );
    }

    @Override
    public WebToolBar remove ( final Component... components )
    {
        return ContainerMethodsImpl.remove ( this, components );
    }

    @Override
    public WebToolBar removeAll ( final Class<? extends Component> componentClass )
    {
        return ContainerMethodsImpl.removeAll ( this, componentClass );
    }

    @Override
    public Component getFirstComponent ()
    {
        return ContainerMethodsImpl.getFirstComponent ( this );
    }

    @Override
    public Component getLastComponent ()
    {
        return ContainerMethodsImpl.getLastComponent ( this );
    }

    @Override
    public WebToolBar equalizeComponentsWidth ()
    {
        return ContainerMethodsImpl.equalizeComponentsWidth ( this );
    }

    @Override
    public WebToolBar equalizeComponentsHeight ()
    {
        return ContainerMethodsImpl.equalizeComponentsHeight ( this );
    }

    @Override
    public WebToolBar equalizeComponentsSize ()
    {
        return ContainerMethodsImpl.equalizeComponentsSize ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public WebToolBar setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebToolBar setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebToolBar setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebToolBar setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebToolBar setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebToolBar setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebToolBar setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }
}