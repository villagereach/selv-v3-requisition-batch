-- Cleanup, as in test context it might be needed
DROP SCHEMA IF EXISTS requisition CASCADE;
DROP SCHEMA IF EXISTS referencedata CASCADE;

CREATE SCHEMA requisition;
CREATE SCHEMA referencedata;
