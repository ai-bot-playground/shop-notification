CREATE TABLE sent_notifications (
    event_id   VARCHAR(64)  PRIMARY KEY,
    channel    VARCHAR(32)  NOT NULL,
    event_type VARCHAR(64)  NOT NULL,
    order_id   VARCHAR(64)  NOT NULL,
    message    VARCHAR(256) NOT NULL,
    sent_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);
