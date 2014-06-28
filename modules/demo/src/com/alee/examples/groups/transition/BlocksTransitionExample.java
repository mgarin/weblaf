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
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.TransitionListener;
import com.alee.extended.transition.effects.Direction;
import com.alee.extended.transition.effects.blocks.BlockType;
import com.alee.extended.transition.effects.blocks.BlocksTransitionEffect;
import com.alee.laf.button.WebButton;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 13.11.12 Time: 14:04
 */

public class BlocksTransitionExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Blocks transition";
    }

    @Override
    public String getDescription ()
    {
        return "Blocks transition effect";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Transition panel
        final ComponentTransition componentTransition = new ComponentTransition ();

        // Transition effect
        final BlocksTransitionEffect effect = new BlocksTransitionEffect ();
        effect.setDirection ( Direction.random );
        effect.setType ( BlockType.cascade );
        effect.setSize ( 40 );
        effect.setSpeed ( 4 );
        effect.setFade ( false );
        componentTransition.setTransitionEffect ( effect );

        // Effects
        final WebImage image1 = new WebImage ( loadIcon ( "pictures/1.jpg" ) );
        final WebImage image2 = new WebImage ( loadIcon ( "pictures/2.jpg" ) );
        final WebImage image3 = new WebImage ( loadIcon ( "pictures/3.jpg" ) );

        // Initial transition panel state
        componentTransition.setContent ( image1 );
        componentTransition.setPreferredSize ( SwingUtils.max ( image1, image2 ) );

        // Images switch button
        final WebButton switchView = new WebButton ( loadIcon ( "switch.png" ) );
        switchView.setDrawFocus ( false );
        switchView.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( componentTransition.getContent () == image1 )
                {
                    componentTransition.performTransition ( image2 );
                }
                else if ( componentTransition.getContent () == image2 )
                {
                    componentTransition.performTransition ( image3 );
                }
                else
                {
                    componentTransition.performTransition ( image1 );
                }
            }
        } );
        componentTransition.addTransitionListener ( new TransitionListener ()
        {
            @Override
            public void transitionStarted ()
            {
                switchView.setEnabled ( false );
            }

            @Override
            public void transitionFinished ()
            {
                switchView.setEnabled ( true );
            }
        } );

        return new GroupPanel ( 10, componentTransition, new CenterPanel ( switchView ) );
    }
}