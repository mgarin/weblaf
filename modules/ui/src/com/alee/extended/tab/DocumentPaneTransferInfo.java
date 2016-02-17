package com.alee.extended.tab;

import java.io.Serializable;

/**
 * @author nsofronov
 */

public class DocumentPaneTransferInfo implements Serializable
{
    protected final String documentPaneId;
    protected final Boolean dragBetweenPanesEnabled;

    public DocumentPaneTransferInfo ( final String documentPaneId, final Boolean dragBetweenPanesEnabled )
    {
        this.documentPaneId = documentPaneId;
        this.dragBetweenPanesEnabled = dragBetweenPanesEnabled;
    }

    public String getDocumentPaneId ()
    {
        return documentPaneId;
    }

    public Boolean getDragBetweenPanesEnabled ()
    {
        return dragBetweenPanesEnabled;
    }
}