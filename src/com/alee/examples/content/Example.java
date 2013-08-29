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

package com.alee.examples.content;

import com.alee.examples.WebLookAndFeelDemo;

import java.awt.*;
import java.util.List;

/**
 * This interface provides base methods for WebLaF demo example classes managed by ExampleManager.
 *
 * @author Mikle Garin
 */

public interface Example
{
    /**
     * Returns example title.
     * It might be used in examples tree or list.
     *
     * @return example title
     */
    public String getTitle ();

    /**
     * Returns short example description.
     * It might be used in some additional information fields.
     *
     * @return short example description
     */
    public String getDescription ();

    /**
     * Returns list of resources used in example.
     * There might be anything - images, files, urls, classes or something else.
     * ExampleManager might provide preview components for some of their types.
     *
     * @return list of resources used in example
     */
    public List<Object> getResources ();

    /**
     * Returns whether live presentation is available for this example or not.
     * Live presentation should guide user through the example, displaying its possible usage ways.
     *
     * @return true if live presentation is available for this example, false otherwise
     */
    public boolean isPresentationAvailable ();

    /**
     * Starts live presentation.
     */
    public void startPresentation ();

    /**
     * Forces live presentation to proceed to next step.
     */
    public void nextPresentationStep ();

    /**
     * Stops live presentation.
     */
    public void stopPresentation ();

    /**
     * Sets a runnable that should be executed at the end of the presentation.
     * You might want to cleanup some variables or components here.
     *
     * @param runnable runnable
     */
    public void doWhenPresentationFinished ( Runnable runnable );

    /**
     * Returns current state of the component development.
     *
     * @return current state of the component development
     */
    public FeatureState getFeatureState ();

    /**
     * Returns whether the example component should fill all available width or not.
     *
     * @return true if the example component should fill all available width, false otherwise
     */
    public boolean isFillWidth ();

    /**
     * Returns preview component for this example.
     * There might be any possible component here, but make sure it fits the demo application.
     *
     * @param owner demo application main frame
     * @return preview component
     */
    public Component getPreview ( WebLookAndFeelDemo owner );
}