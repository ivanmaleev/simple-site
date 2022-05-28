            drop SCHEMA IF EXISTS simple CASCADE;

            create EXTENSION IF NOT EXISTS hstore SCHEMA public;

            create SCHEMA simple authorization postgres;
            grant USAGE ON SCHEMA public TO postgres;