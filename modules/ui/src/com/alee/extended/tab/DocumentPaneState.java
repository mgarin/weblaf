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

package com.alee.extended.tab;

import com.alee.utils.general.Pair;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * Object used to store WebDocumentPane documents structure state.
 *
 * @author Mikle Garin
 * @see WebDocumentPane#getDocumentPaneState()
 * @see WebDocumentPane#setDocumentPaneState(DocumentPaneState)
 */

@XStreamAlias ( "DocumentPaneState" )
public class DocumentPaneState implements Serializable
{
    /**
     * Whether or not this document pane structure level represents split.
     */
    @XStreamAsAttribute
    private Boolean split;

    /**
     * Selected document ID on this document pane structure level.
     * Specified only when this is not split.
     */
    @XStreamAsAttribute
    private String selectedId;

    /**
     * Document IDs opened on this document pane structure level.
     * Specified only when this is not split.
     */
    @XStreamImplicit
    private List<String> documentIds;

    /**
     * Split orientation on this document pane structure level.
     * Specified only when this is split.
     */
    @XStreamAsAttribute
    private Integer splitOrientation;

    /**
     * Split divider location on this document pane structure level.
     * We have to save only proportions, otherwise it will be incorrect in case split size changes.
     * Specified only when this is split.
     */
    @XStreamAsAttribute
    private Double dividerLocation;

    /**
     * Split side states on this document pane structure level.
     * Specified only when this is split.
     */
    private Pair<DocumentPaneState, DocumentPaneState> splitState;

    public DocumentPaneState ()
    {
        super ();
    }

    public DocumentPaneState ( final String selectedId, final List<String> documentIds )
    {
        super ();
        this.split = false;
        this.selectedId = selectedId;
        this.documentIds = documentIds;
    }

    public DocumentPaneState ( final int orientation, final double dividerLocation,
                               final Pair<DocumentPaneState, DocumentPaneState> splitState )
    {
        super ();
        this.split = true;
        this.splitOrientation = orientation;
        this.dividerLocation = dividerLocation;
        this.splitState = splitState;
    }

    public Boolean isSplit ()
    {
        return split != null && split;
    }

    public void setSplit ( final Boolean split )
    {
        this.split = split;
    }

    public String getSelectedId ()
    {
        return selectedId;
    }

    public void setSelectedId ( final String selectedId )
    {
        this.selectedId = selectedId;
    }

    public List<String> getDocumentIds ()
    {
        return documentIds;
    }

    public void setDocumentIds ( final List<String> documentIds )
    {
        this.documentIds = documentIds;
    }

    public Integer getSplitOrientation ()
    {
        return splitOrientation;
    }

    public void setSplitOrientation ( final Integer orientation )
    {
        this.splitOrientation = orientation;
    }

    public Double getDividerLocation ()
    {
        return dividerLocation;
    }

    public void setDividerLocation ( final Double location )
    {
        this.dividerLocation = location;
    }

    public Pair<DocumentPaneState, DocumentPaneState> getSplitState ()
    {
        return splitState;
    }

    public void setSplitState ( final Pair<DocumentPaneState, DocumentPaneState> splitState )
    {
        this.splitState = splitState;
    }
}