CREATE TABLE ledger_entries (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    entry_date DATE NOT NULL,

    CONSTRAINT fk_transaction_id FOREIGN KEY (transaction_id)
        REFERENCES transactions(id),

    CONSTRAINT chk_entry_type2 CHECK (type IN ('DEBIT', 'CREDIT'))
);