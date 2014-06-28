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

package com.alee.examples.groups.transition;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.image.WebImage;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.TransitionAdapter;
import com.alee.extended.transition.effects.fade.FadeTransitionEffect;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: mgarin Date: 13.11.12 Time: 12:39
 */

public class FadeTransitionExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Fade transition";
    }

    @Override
    public String getDescription ()
    {
        return "Fade transition effect";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Image and loader lists
        final List<WebImage> images = CollectionUtils.copy ( createImage ( "1.png" ), createImage ( "2.png" ), createImage ( "3.png" ) );
        final List<WebLabel> loaders = CollectionUtils.copy ( createLoader (), createLoader (), createLoader () );

        // Images panel
        final WebPanel imagesPanel = new WebPanel ( new HorizontalFlowLayout ( 5, false ) );
        imagesPanel.addAncestorListener ( new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( AncestorEvent event )
            {
                // Initial transition on first panel appearance
                imagesPanel.removeAncestorListener ( this );
                for ( int i = 0; i < imagesPanel.getComponentCount (); i++ )
                {
                    // Delayed image fade-in
                    ComponentTransition componentTransition = ( ComponentTransition ) imagesPanel.getComponent ( i );
                    componentTransition.delayTransition ( 1000 + i * 500, images.get ( i ) );
                }
            }
        } );

        // States switch buttons
        final WebButton reloadView = new WebButton ( loadIcon ( "switch.png" ) );
        reloadView.setEnabled ( false );
        reloadView.setDrawFocus ( false );
        reloadView.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                reloadView.setEnabled ( false );
                for ( int i = 0; i < imagesPanel.getComponentCount (); i++ )
                {
                    // Switching to loader first
                    ComponentTransition componentTransition = ( ComponentTransition ) imagesPanel.getComponent ( i );
                    componentTransition.performTransition ( loaders.get ( i ) );

                    // Delayed image fade-in
                    componentTransition.delayTransition ( 1000 + i * 500, images.get ( i ) );
                }
            }
        } );

        // Transition panels
        for ( int i = 0; i < images.size (); i++ )
        {
            // Single transition panel with loader as initial component
            final ComponentTransition transition = new ComponentTransition ( loaders.get ( i ), new FadeTransitionEffect () );
            imagesPanel.add ( transition );

            if ( i == images.size () - 1 )
            {
                // Transition listener for reload button enabling
                transition.addTransitionListener ( new TransitionAdapter ()
                {
                    @Override
                    public void transitionFinished ()
                    {
                        if ( transition.getContent () instanceof WebImage )
                        {
                            reloadView.setEnabled ( true );
                        }
                    }
                } );
            }
        }

        return new GroupPanel ( 10, imagesPanel, new CenterPanel ( reloadView ) );
    }

    private WebImage createImage ( String src )
    {
        return new WebImage ( loadIcon ( "images/" + src ) );
    }

    private WebLabel createLoader ()
    {
        WebLabel loader = new WebLabel ( loadIcon ( "loader.gif" ) );
        loader.setPreferredSize ( new Dimension ( 48, 48 ) );
        loader.setOpaque ( true );
        return loader;
    }
}