package com.ivasio.bachelor_thesis.shared.serialization;

import com.ivasio.bachelor_thesis.shared.models.Point;

import java.time.OffsetDateTime;
import java.util.UUID;


public class SourcedPoint extends Point {
    private UUID sourceId;

    public SourcedPoint(float longitude, float latitude, OffsetDateTime timestamp, UUID sourceId) {
        super(longitude, latitude, timestamp);
        this.sourceId = sourceId;
    }

    public SourcedPoint(long id, float longitude, float latitude, OffsetDateTime timestamp, UUID sourceId) {
        super(id, longitude, latitude, timestamp);
        this.sourceId = sourceId;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public Point toPoint() {
        return new Point(getLongitude(), getLatitude(), getTimestamp());
    }

}
