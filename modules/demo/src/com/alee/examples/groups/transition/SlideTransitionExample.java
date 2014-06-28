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
import com.alee.extended.panel.WebButtonGroup;
import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.TransitionListener;
import com.alee.extended.transition.effects.Direction;
import com.alee.extended.transition.effects.slide.SlideTransitionEffect;
import com.alee.extended.transition.effects.slide.SlideType;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: mgarin Date: 12.11.12 Time: 18:13
 */

public class SlideTransitionExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Slide transition";
    }

    @Override
    public String getDescription ()
    {
        return "Slide transition effect";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Images list
        final List<ImageIcon> images =
                CollectionUtils.copy ( loadIcon ( "images/1.png" ), loadIcon ( "images/2.png" ), loadIcon ( "images/3.png" ) );

        // Images panel
        final WebPanel imagesPanel = new WebPanel ( new HorizontalFlowLayout ( 5, false ) );

        // States switch buttons
        final WebButton slideLeft = new WebButton ( loadIcon ( "left.png" ) );
        slideLeft.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // Changing image positions in lsit
                ImageIcon first = images.remove ( 0 );
                images.add ( first );

                // Performing transitions
                performTransitions ( imagesPanel, images, Direction.left );
            }
        } );
        final WebButton slideRight = new WebButton ( loadIcon ( "right.png" ) );
        slideRight.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // Changing image positions in lsit
                ImageIcon last = images.remove ( images.size () - 1 );
                images.add ( 0, last );

                // Performing transitions
                performTransitions ( imagesPanel, images, Direction.right );
            }
        } );
        final WebButtonGroup buttonGroup = new WebButtonGroup ( slideLeft, slideRight );
        buttonGroup.setButtonsDrawFocus ( false );

        // Transition panels
        boolean first = true;
        for ( ImageIcon image : images )
        {
            final ComponentTransition transition = new ComponentTransition ( new WebImage ( image ), createEffect () );
            imagesPanel.add ( transition );

            if ( first )
            {
                first = false;
                transition.addTransitionListener ( new TransitionListener ()
                {
                    @Override
                    public void transitionStarted ()
                    {
                        slideLeft.setEnabled ( false );
                        slideRight.setEnabled ( false );
                    }

                    @Override
                    public void transitionFinished ()
                    {
                        slideLeft.setEnabled ( true );
                        slideRight.setEnabled ( true );
                    }
                } );
            }
        }

        return new GroupPanel ( 10, imagesPanel, new CenterPanel ( buttonGroup ) );
    }

    private void performTransitions ( WebPanel imagesPanel, List<ImageIcon> images, Direction direction )
    {
        for ( int i = 0; i < imagesPanel.getComponentCount (); i++ )
        {
            ComponentTransition componentTransition = ( ComponentTransition ) imagesPanel.getComponent ( i );
            ( ( SlideTransitionEffect ) componentTransition.getTransitionEffect () ).setDirection ( direction );
            componentTransition.performTransition ( new WebImage ( images.get ( i ) ) );
        }
    }

    private SlideTransitionEffect createEffect ()
    {
        // Slide effect
        SlideTransitionEffect effect = new SlideTransitionEffect ();
        effect.setType ( SlideType.moveBoth );
        effect.setSpeed ( 6 );
        return effect;
    }
}