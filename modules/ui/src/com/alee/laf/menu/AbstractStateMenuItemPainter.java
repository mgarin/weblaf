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

package com.alee.laf.menu;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.button.AbstractButtonPainter;
import com.alee.laf.checkbox.ButtonStatePainter;
import com.alee.laf.checkbox.IButtonStatePainter;
import com.alee.painter.DefaultPainter;
import com.alee.painter.SectionPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.util.List;

/**
 * Abstract painter for stateful {@link JMenuItem} implementations.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public abstract class AbstractStateMenuItemPainter<C extends JMenuItem, U extends MenuItemUI, D extends IDecoration<C, D>>
        extends AbstractButtonPainter<C, U, D> implements IAbstractMenuItemPainter<C, U>
{
    /**
     * Client property for {@link JMenuItem} state {@link Icon}.
     */
    public static final String STATE_ICON_PROPERTY = "menuItemStateIcon";

    /**
     * Menu item change listener.
     */
    protected transient MenuItemChangeListener menuItemChangeListener;

    /**
     * State icon painter.
     */
    @Nullable
    @DefaultPainter ( ButtonStatePainter.class )
    protected IButtonStatePainter stateIconPainter;

    @Nullable
    @Override
    protected List<SectionPainter<C, U>> getSectionPainters ()
    {
        return asList ( stateIconPainter );
    }

    @Override
    public void install ( @NotNull final C c, @NotNull final U ui )
    {
        super.install ( c, ui );
        installStateIcon ();
    }

    @Override
    public void uninstall ( @NotNull final C c, @NotNull final U ui )
    {
        uninstallStateIcon ();
        super.uninstall ( c, ui );
    }

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installMenuItemChangeListener ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallMenuItemChangeListener ();
        super.uninstallPropertiesAndListeners ();
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.isSelected () )
        {
            states.add ( DecorationState.checked );
        }
        return states;
    }

    @Override
    protected boolean isSelected ()
    {
        return component.isEnabled () && component.getModel ().isArmed ();
    }

    /**
     * Installs state {@link Icon} if current button {@link Icon} is {@code null} or {@link UIResource}.
     */
    protected void installStateIcon ()
    {
        if ( stateIconPainter != null )
        {
            // todo Workaround to allow disabling icon, replace with omit in style later
            final Dimension size = stateIconPainter.getPreferredSize ();
            if ( size.width > 0 && size.height > 0 )
            {
                component.putClientProperty ( STATE_ICON_PROPERTY, createIcon () );
            }
        }
    }

    /**
     * Uninstalls state {@link Icon} if current button {@link Icon} is {@link UIResource}.
     */
    protected void uninstallStateIcon ()
    {
        if ( stateIconPainter != null )
        {
            // todo Workaround to allow disabling icon, replace with omit in style later
            final Dimension size = stateIconPainter.getPreferredSize ();
            if ( size.width > 0 && size.height > 0 )
            {
                component.putClientProperty ( STATE_ICON_PROPERTY, null );
            }
        }
    }

    /**
     * Installs {@link MenuItemChangeListener} into {@link JMenuItem}.
     */
    protected void installMenuItemChangeListener ()
    {
        menuItemChangeListener = MenuItemChangeListener.install ( component );
    }

    /**
     * Uninstalls {@link MenuItemChangeListener} from {@link JMenuItem}.
     */
    protected void uninstallMenuItemChangeListener ()
    {
        menuItemChangeListener = MenuItemChangeListener.uninstall ( component, menuItemChangeListener );
    }

    /**
     * Creates and returns component state icon.
     *
     * @return component state icon
     */
    @NotNull
    protected Icon createIcon ()
    {
        return new StateIcon ();
    }

    /**
     * Custom state icon.
     */
    protected class StateIcon implements Icon, UIResource
    {
        @Override
        public void paintIcon ( @NotNull final Component c, @NotNull final Graphics g, final int x, final int y )
        {
            if ( stateIconPainter != null )
            {
                final Graphics2D g2d = ( Graphics2D ) g;
                final Object aa = GraphicsUtils.setupAntialias ( g2d );
                paintSection ( stateIconPainter, g2d, new Rectangle ( new Point ( x, y ), getSize () ) );
                GraphicsUtils.restoreAntialias ( g2d, aa );
            }
        }

        /**
         * Returns icon size.
         *
         * @return icon size
         */
        @NotNull
        protected Dimension getSize ()
        {
            return stateIconPainter != null ? stateIconPainter.getPreferredSize () : new Dimension ( 16, 16 );
        }

        @Override
        public int getIconWidth ()
        {
            return getSize ().width;
        }

        @Override
        public int getIconHeight ()
        {
            return getSize ().height;
        }
    }
}