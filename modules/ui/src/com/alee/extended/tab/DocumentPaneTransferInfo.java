package com.alee.extended.tab;

import java.io.Serializable;

/**
 * @author nsofronov
 */
public class DocumentPaneTransferInfo implements Serializable
{
    private final String documentPanelId;
    private final Boolean dragBetweenPanesEnabled;

    public DocumentPaneTransferInfo ( final String documentPanelId, final Boolean dragBetweenPanesEnabled )
    {
        this.documentPanelId = documentPanelId;
        this.dragBetweenPanesEnabled = dragBetweenPanesEnabled;
    }

    public String getDocumentPanelId ()
    {
        return documentPanelId;
    }

    public Boolean getDragBetweenPanesEnabled ()
    {
        return dragBetweenPanesEnabled;
    }
}
