CREATE TABLE logging.error_log_notification
(
    id               SERIAL PRIMARY KEY,
    timestamp        TIMESTAMP    NOT NULL,
    log_level        VARCHAR(10) NOT NULL,
    log_time         TIMESTAMP    NOT NULL,
    application_name VARCHAR(255) NOT NULL,
    logger_class     VARCHAR(255) NOT NULL,
    message          TEXT,
    stack_trace      TEXT,
);
