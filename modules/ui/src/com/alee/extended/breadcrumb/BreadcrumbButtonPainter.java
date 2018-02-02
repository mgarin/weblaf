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

import com.alee.laf.button.WebButtonUI;
import com.alee.managers.style.Bounds;
import com.alee.painter.AbstractPainter;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

/**
 * Custom painter for WebBreadcrumbButton component.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public class BreadcrumbButtonPainter<C extends WebBreadcrumbButton, U extends WebButtonUI> extends AbstractPainter<C, U>
{
    /**
     * Listeners.
     */
    protected ContainerAdapter containerAdapter;
    protected AncestorAdapter ancestorAdapter;

    /**
     * Runtime variables.
     */
    protected WebBreadcrumb breadcrumb = null;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();

        containerAdapter = new ContainerAdapter ()
        {
            @Override
            public void componentAdded ( final ContainerEvent e )
            {
                if ( e.getChild () != component )
                {
                    updateAll ();
                }
            }

            @Override
            public void componentRemoved ( final ContainerEvent e )
            {
                if ( e.getChild () != component )
                {
                    updateAll ();
                }
            }
        };

        ancestorAdapter = new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                final Container container = component.getParent ();
                if ( container instanceof WebBreadcrumb )
                {
                    breadcrumb = ( WebBreadcrumb ) container;
                    breadcrumb.addContainerListener ( containerAdapter );
                }
                updateAll ();
            }

            @Override
            public void ancestorRemoved ( final AncestorEvent event )
            {
                removeBreadcrumbAdapter ();
                updateAll ();
            }
        };
        component.addAncestorListener ( ancestorAdapter );
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        removeBreadcrumbAdapter ();
        containerAdapter = null;
        component.removeAncestorListener ( ancestorAdapter );
        ancestorAdapter = null;

        super.uninstallPropertiesAndListeners ();
    }

    /**
     * Removes ContainerAdapter from parent breadcrumb if it exists.
     */
    protected void removeBreadcrumbAdapter ()
    {
        if ( breadcrumb != null )
        {
            breadcrumb.removeContainerListener ( containerAdapter );
            breadcrumb = null;
        }
    }

    @Override
    public Boolean isOpaque ()
    {
        return false;
    }

    @Override
    protected Insets getBorder ()
    {
        return BreadcrumbUtils.getElementMargin ( component );
    }

    @Override
    public void paint ( final Graphics2D g2d, final C c, final U ui, final Bounds bounds )
    {
        // Painting background
        BreadcrumbUtils.paintElementBackground ( g2d, c );
    }
}