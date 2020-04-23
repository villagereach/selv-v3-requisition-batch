CREATE TABLE requisition.requisitions
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
    CONSTRAINT requisitions_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisition.requisitions
    OWNER to postgres;

CREATE UNIQUE INDEX req_prod_fac_per
    ON requisition.requisitions USING btree
    (programid, facilityid, processingperiodid)
    TABLESPACE pg_default
    WHERE emergency = false AND supervisorynodeid IS NULL;

CREATE UNIQUE INDEX req_prod_fac_per_node
    ON requisition.requisitions USING btree
    (programid, facilityid, processingperiodid, supervisorynodeid)
    TABLESPACE pg_default
    WHERE emergency = false AND supervisorynodeid IS NOT NULL;

CREATE OR REPLACE FUNCTION requisition.unique_status_changes() returns trigger LANGUAGE plpgsql AS $$
BEGIN
  DROP TABLE IF EXISTS status_change_data;
  CREATE TEMP TABLE status_change_data AS
  (
      SELECT status, supervisorynodeid
      FROM requisition.status_changes
      WHERE requisitionid = NEW.requisitionid
      ORDER BY createddate DESC
      LIMIT 2
  );

  IF EXISTS (SELECT 1 FROM status_change_data GROUP BY status, supervisoryNodeId HAVING COUNT(*) > 1)
  THEN
    RAISE 'Duplicate status change: % at supervisory node: % ', NEW.status, NEW.supervisoryNodeId USING ERRCODE = 'unique_violation';
  ELSE
    RETURN NEW;
  END IF;

END $$;

CREATE TABLE requisition.requisition_line_items
(
    id uuid NOT NULL,
    adjustedconsumption integer,
    approvedquantity integer,
    averageconsumption integer,
    beginningbalance integer,
    calculatedorderquantity integer,
    maxperiodsofstock numeric(19,2),
    maximumstockquantity integer,
    nonfullsupply boolean,
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
    orderableversionnumber bigint,
    facilitytypeapprovedproductid uuid,
    facilitytypeapprovedproductversionnumber bigint,
    CONSTRAINT requisition_line_items_pkey PRIMARY KEY (id),
    CONSTRAINT fk_4sg1naierwgt9avsjcm76a2yl FOREIGN KEY (requisitionid)
        REFERENCES requisition.requisitions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisition.requisition_line_items
    OWNER to postgres;

CREATE INDEX requisition_line_items_requisitionid_idx
    ON requisition.requisition_line_items USING btree
    (requisitionid)
    TABLESPACE pg_default;

ALTER TABLE requisition.requisition_line_items
    CLUSTER ON requisition_line_items_requisitionid_idx;

CREATE TABLE requisition.status_changes
(
    id uuid NOT NULL,
    createddate timestamp with time zone,
    modifieddate timestamp with time zone,
    authorid uuid,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    requisitionid uuid NOT NULL,
    supervisorynodeid uuid,
    CONSTRAINT status_changes_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisition.status_changes
    OWNER to postgres;

CREATE INDEX status_changes_requisitionid_idx
    ON requisition.status_changes USING btree
    (requisitionid)
    TABLESPACE pg_default;

CREATE CONSTRAINT TRIGGER check_status_changes
    AFTER INSERT
    ON requisition.status_changes
    DEFERRABLE INITIALLY DEFERRED    FOR EACH ROW
    EXECUTE PROCEDURE requisition.unique_status_changes();

CREATE TABLE requisition.status_messages
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
    CONSTRAINT status_messages_pkey PRIMARY KEY (id),
    CONSTRAINT status_change_id_unique UNIQUE (statuschangeid)

        DEFERRABLE INITIALLY DEFERRED,
    CONSTRAINT fk_hp6wryw9250cf3jhceddvmn5b FOREIGN KEY (requisitionid)
        REFERENCES requisition.requisitions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_status_messages_status_change_id FOREIGN KEY (statuschangeid)
        REFERENCES requisition.status_changes (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisition.status_messages
    OWNER to postgres;

CREATE TABLE requisition.stock_adjustments
(
    id uuid NOT NULL,
    quantity integer NOT NULL,
    reasonid uuid NOT NULL,
    requisitionlineitemid uuid,
    CONSTRAINT stock_adjustments_pkey PRIMARY KEY (id),
    CONSTRAINT fk_9nqi8imo7ty6jafeijhviynrt FOREIGN KEY (requisitionlineitemid)
        REFERENCES requisition.requisition_line_items (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE requisition.stock_adjustments
    OWNER to postgres;

CREATE UNIQUE INDEX req_line_reason
    ON requisition.stock_adjustments USING btree
    (reasonid, requisitionlineitemid)
    TABLESPACE pg_default;
