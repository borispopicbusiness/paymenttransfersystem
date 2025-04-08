CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    amount NUMERIC(19, 2) NOT NULL,
    source_account_id VARCHAR(34) NOT NULL,
    destination_account_id VARCHAR(34) NOT NULL,
    entry_date DATE NOT NULL,
    comment TEXT,

    CONSTRAINT fk_source_account FOREIGN KEY (source_account_id)
        REFERENCES accounts(account_number),
    CONSTRAINT fk_destination_account FOREIGN KEY (destination_account_id)
        REFERENCES accounts(account_number)
);