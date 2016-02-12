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
 * Demo Application styles.
 *
 * @author Mikle Garin
 */

public final class DemoStyles
{
    /**
     * General.
     */
    public static final StyleId filterField = StyleId.of ( "filter" );
    public static final StyleId resourceLink = StyleId.of ( "resource" );
    public static final StyleId emptycontentPanel = StyleId.of ( "emptycontent" );
    public static final ChildStyleId emptycontentLabel = ChildStyleId.of ( "emptycontent" );
    public static final StyleId stretchedDocumentPane = StyleId.of ( "stretched" );

    /**
     * Examples frame.
     */
    public static final StyleId examplesTree = StyleId.of ( "examples-tree" );

    /**
     * Example panel.
     */
    public static final StyleId toolBar = StyleId.of ( "tool" );
    public static final StyleId toolLabel = StyleId.of ( "tool" );
    public static final StyleId toolSeparator = StyleId.of ( "tool" );
    public static final StyleId infoBar = StyleId.of ( "info-bar" );
    public static final StyleId previewsPanel = StyleId.of ( "previews" );

    /**
     * Preview panel.
     */
    public static final StyleId previewLightPanel = StyleId.of ( "preview-light" );
    public static final StyleId previewDarkPanel = StyleId.of ( "preview-dark" );
    public static final ChildStyleId previewTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId previewSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId previewContent = ChildStyleId.of ( "content" );

    /**
     * Style code preview panel.
     */
    public static final StyleId skinSelectorsPanel = StyleId.of ( "skin-selectors" );
    public static final ChildStyleId skinSelectorButton = ChildStyleId.of ( "skin-selector" );
}