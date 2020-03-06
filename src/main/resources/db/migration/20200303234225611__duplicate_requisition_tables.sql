-- Table: requisitionbatch.batch_requisitions

-- DROP TABLE requisitionbatch.batch_requisitions;

CREATE TABLE requisitionbatch.batch_requisitions
(
    id uuid NOT NULL,
    createddate timestamp with time zone,
    modifieddate timestamp with time zone,
    draftstatusmessage text COLLATE pg_catalog."default",
    emergency boolean NOT NULL,
    facilityid uuid NOT NULL,
    numberofmonthsinperiod integer NOT NULL,
    processingperiodid uuid NOT NULL,
    programid uuid NOT NULL,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    supervisorynodeid uuid,
    supplyingfacilityid uuid,
    templateid uuid NOT NULL,
    datephysicalstockcountcompleted date,
    version bigint DEFAULT 0,
    reportonly boolean,
    extradata jsonb,
    CONSTRAINT batch_requisitions_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisitionbatch.batch_requisitions
    OWNER to postgres;

-- Index: batch_req_prod_fac_per

-- DROP INDEX requisitionbatch.batch_req_prod_fac_per;

CREATE UNIQUE INDEX batch_req_prod_fac_per
    ON requisitionbatch.batch_requisitions USING btree
    (programid, facilityid, processingperiodid)
    TABLESPACE pg_default
    WHERE emergency = false AND supervisorynodeid IS NULL;

-- Index: batch_req_prod_fac_per_node

-- DROP INDEX requisitionbatch.batch_req_prod_fac_per_node;

CREATE UNIQUE INDEX batch_req_prod_fac_per_node
    ON requisitionbatch.batch_requisitions USING btree
    (programid, facilityid, processingperiodid, supervisorynodeid)
    TABLESPACE pg_default
    WHERE emergency = false AND supervisorynodeid IS NOT NULL;

-- Table: requisitionbatch.batch_requisition_line_items

-- DROP TABLE requisitionbatch.batch_requisition_line_items;

CREATE TABLE requisitionbatch.batch_requisition_line_items
(
    id uuid NOT NULL,
    adjustedconsumption integer,
    approvedquantity integer,
    averageconsumption integer,
    beginningbalance integer,
    calculatedorderquantity integer,
    maxperiodsofstock numeric(19,2),
    maximumstockquantity integer,
    nonfullsupply boolean NOT NULL,
    numberofnewpatientsadded integer,
    orderableid uuid,
    packstoship bigint,
    priceperpack numeric(19,2),
    remarks character varying(250) COLLATE pg_catalog."default",
    requestedquantity integer,
    requestedquantityexplanation character varying(255) COLLATE pg_catalog."default",
    skipped boolean,
    stockonhand integer,
    total integer,
    totalconsumedquantity integer,
    totalcost numeric(19,2),
    totallossesandadjustments integer,
    totalreceivedquantity integer,
    totalstockoutdays integer,
    requisitionid uuid,
    idealstockamount integer,
    calculatedorderquantityisa integer,
    additionalquantityrequired integer,
    CONSTRAINT batch_requisition_line_items_pkey PRIMARY KEY (id),
    CONSTRAINT fk_batch_4sg1naierwgt9avsjcm76a2yl FOREIGN KEY (requisitionid)
        REFERENCES requisitionbatch.batch_requisitions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisitionbatch.batch_requisition_line_items
    OWNER to postgres;

-- Index: batch_requisition_line_items_requisitionid_idx

-- DROP INDEX requisitionbatch.batch_requisition_line_items_requisitionid_idx;

CREATE INDEX batch_requisition_line_items_requisitionid_idx
    ON requisitionbatch.batch_requisition_line_items USING btree
    (requisitionid)
    TABLESPACE pg_default;

ALTER TABLE requisitionbatch.batch_requisition_line_items
    CLUSTER ON batch_requisition_line_items_requisitionid_idx;

-- Table: requisitionbatch.batch_status_changes

-- DROP TABLE requisitionbatch.batch_status_changes;

CREATE TABLE requisitionbatch.batch_status_changes
(
    id uuid NOT NULL,
    createddate timestamp with time zone,
    modifieddate timestamp with time zone,
    authorid uuid,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    requisitionid uuid NOT NULL,
    supervisorynodeid uuid,
    CONSTRAINT batch_status_changes_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisitionbatch.batch_status_changes
    OWNER to postgres;

-- Index: batch_status_changes_requisitionid_idx

-- DROP INDEX requisitionbatch.batch_status_changes_requisitionid_idx;

CREATE INDEX batch_status_changes_requisitionid_idx
    ON requisitionbatch.batch_status_changes USING btree
    (requisitionid)
    TABLESPACE pg_default;

CREATE OR REPLACE FUNCTION batch_unique_status_changes() returns trigger LANGUAGE plpgsql AS $$
BEGIN
  DROP TABLE IF EXISTS batch_status_change_data;
  CREATE TEMP TABLE batch_status_change_data AS
  (
      SELECT status, supervisorynodeid
      FROM requisitionbatch.batch_status_changes
      WHERE requisitionid = NEW.requisitionid
      ORDER BY createddate DESC
      LIMIT 2
  );

  IF EXISTS (SELECT 1 FROM batch_status_change_data GROUP BY status, supervisoryNodeId HAVING COUNT(*) > 1)
  THEN
    RAISE 'Duplicate status change: % at supervisory node: % ', NEW.status, NEW.supervisoryNodeId USING ERRCODE = 'unique_violation';
  ELSE
    RETURN NEW;
  END IF;

END $$;

-- Trigger: batch_check_status_changes

-- DROP TRIGGER batch_check_status_changes ON requisitionbatch.batch_status_changes;

CREATE CONSTRAINT TRIGGER batch_check_status_changes
    AFTER INSERT
    ON requisitionbatch.batch_status_changes
    DEFERRABLE INITIALLY DEFERRED    FOR EACH ROW
    EXECUTE PROCEDURE requisitionbatch.batch_unique_status_changes();

-- Table: requisitionbatch.batch_status_messages

-- DROP TABLE requisitionbatch.batch_status_messages;

CREATE TABLE requisitionbatch.batch_status_messages
(
    id uuid NOT NULL,
    createddate timestamp with time zone,
    modifieddate timestamp with time zone,
    authorfirstname character varying(255) COLLATE pg_catalog."default",
    authorid uuid,
    authorlastname character varying(255) COLLATE pg_catalog."default",
    body text COLLATE pg_catalog."default" NOT NULL,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    requisitionid uuid NOT NULL,
    statuschangeid uuid NOT NULL,
    CONSTRAINT batch_status_messages_pkey PRIMARY KEY (id),
    CONSTRAINT batch_status_change_id_unique UNIQUE (statuschangeid)

        DEFERRABLE INITIALLY DEFERRED,
    CONSTRAINT fk_batch_hp6wryw9250cf3jhceddvmn5b FOREIGN KEY (requisitionid)
        REFERENCES requisitionbatch.batch_requisitions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_batch_status_messages_status_change_id FOREIGN KEY (statuschangeid)
        REFERENCES requisitionbatch.batch_status_changes (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisitionbatch.batch_status_messages
    OWNER to postgres;

-- Table: requisitionbatch.batch_stock_adjustments

-- DROP TABLE requisitionbatch.batch_stock_adjustments;

CREATE TABLE requisitionbatch.batch_stock_adjustments
(
    id uuid NOT NULL,
    quantity integer NOT NULL,
    reasonid uuid NOT NULL,
    requisitionlineitemid uuid,
    CONSTRAINT batch_stock_adjustments_pkey PRIMARY KEY (id),
    CONSTRAINT fk_batch_9nqi8imo7ty6jafeijhviynrt FOREIGN KEY (requisitionlineitemid)
        REFERENCES requisitionbatch.batch_requisition_line_items (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisitionbatch.batch_stock_adjustments
    OWNER to postgres;

-- Index: batch_req_line_reason

-- DROP INDEX requisitionbatch.batch_req_line_reason;

CREATE UNIQUE INDEX batch_req_line_reason
    ON requisitionbatch.batch_stock_adjustments USING btree
    (reasonid, requisitionlineitemid)
    TABLESPACE pg_default;

