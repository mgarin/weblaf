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

package com.alee.demo.skin;

import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;

/**
 * {@link com.alee.demo.DemoApplication} styles.
 *
 * @author Mikle Garin
 */
public final class DemoStyles
{
    /**
     * General.
     */
    public static final StyleId toolButton = StyleId.of ( "tool" );
    public static final StyleId toolIconButton = StyleId.of ( "tool-icon" );
    public static final StyleId toolCombobox = StyleId.of ( "tool" );
    public static final StyleId toolLangCombobox = StyleId.of ( "tool-lang" );
    public static final StyleId filterfield = StyleId.of ( "filter" );
    public static final StyleId resourceLink = StyleId.of ( "resource" );
    public static final StyleId fieldInner = StyleId.of ( "field-inner" );
    public static final StyleId placeholderLabel = StyleId.of ( "placeholder" );

    /**
     * Examples frame.
     */
    public static final StyleId examplesTree = StyleId.of ( "examples" );

    /**
     * Central pane.
     */
    public static final StyleId emptycontentPanel = StyleId.of ( "emptycontent" );
    public static final ChildStyleId emptycontentInfoLabel = ChildStyleId.of ( "emptycontent-info" );
    public static final ChildStyleId emptycontentWarnLabel = ChildStyleId.of ( "emptycontent-warn" );
    public static final StyleId expamplesPane = StyleId.of ( "examples" );

    /**
     * Example panel.
     */
    public static final StyleId exampleToolbar = StyleId.of ( "example" );
    public static final StyleId wikiLabel = StyleId.of ( "wiki" );
    public static final StyleId wikiLink = StyleId.of ( "wiki" );
    public static final StyleId previewsPanel = StyleId.of ( "previews" );

    /**
     * Preview panel.
     */
    public static final StyleId previewPanel = StyleId.of ( "preview" );
    public static final ChildStyleId previewTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId previewSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId previewContent = ChildStyleId.of ( "content" );

    /**
     * Custom example styles.
     */
    public static final StyleId easingEastBar = StyleId.of ( "easing-east-bar" );
    public static final StyleId easingSouthBar = StyleId.of ( "easing-south-bar" );
    public static final StyleId shapedetectionTitle = StyleId.of ( "shape-detection-title" );
    public static final StyleId shapedetectionSubTitle = StyleId.of ( "shape-detection-sub-title" );
    public static final StyleId shapedetectionTestPanel = StyleId.of ( "shape-detection-test-panel" );
    public static final StyleId shapedetectionTestPanelLeft = StyleId.of ( "shape-detection-test-panel-left" );
    public static final StyleId shapedetectionTestPanelRight = StyleId.of ( "shape-detection-test-panel-right" );
    public static final StyleId shapedetectionBoundsPanel = StyleId.of ( "shape-detection-bounds-panel" );
    public static final StyleId shapedetectionButton = StyleId.of ( "shape-detection-button" );
    public static final StyleId shapedetectionTextArea = StyleId.of ( "shape-detection-textarea" );
    public static final StyleId shapedetectionDecoratedPanel = StyleId.of ( "shape-detection-decorated-panel" );

    /**
     * Example styles.
     */
    public static final StyleId leadingImage = StyleId.of ( "leading" );
    public static final StyleId trailingImage = StyleId.of ( "trailing" );
}