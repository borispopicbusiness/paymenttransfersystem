CREATE TABLE accounts (
    account_number VARCHAR(34) NOT NULL UNIQUE PRIMARY KEY,
    balance NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    registered TIMESTAMP NOT NULL,
    owner VARCHAR(255) NOT NULL,
    account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('personal', 'commercial', 'internal_cash')),
    comment TEXT,

    CONSTRAINT iban_format CHECK (
        account_type NOT IN ('personal', 'commercial')
            OR account_number ~ '^[A-Z]{2}[0-9]{2}[A-Z0-9]{10,30}$'
    )
);