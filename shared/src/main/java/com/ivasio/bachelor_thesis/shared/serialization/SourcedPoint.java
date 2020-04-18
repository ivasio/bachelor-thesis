package com.ivasio.bachelor_thesis.shared.serialization;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.ivasio.bachelor_thesis.shared.models.Point;


public class SourcedPoint extends Point {
    private UUID sourceId;

    public SourcedPoint(long id, float longitude, float latitude, OffsetDateTime timestamp, UUID sourceId) {
        super(id, longitude, latitude, timestamp);
        this.sourceId = sourceId;
    }

    public UUID getSourceId() {
        return sourceId;
    }


}
