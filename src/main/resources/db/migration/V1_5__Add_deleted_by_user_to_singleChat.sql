ALTER TABLE single_chat
    ADD COLUMN "deleted_by_user_one" BOOLEAN DEFAULT FALSE,
    ADD COLUMN "deleted_by_user_two" BOOLEAN DEFAULT FALSE;