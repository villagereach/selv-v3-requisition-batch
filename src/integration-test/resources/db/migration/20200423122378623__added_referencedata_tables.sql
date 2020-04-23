CREATE TABLE referencedata.geographic_levels
(
    id uuid NOT NULL,
    code text COLLATE pg_catalog."default" NOT NULL,
    levelnumber integer NOT NULL,
    name text COLLATE pg_catalog."default",
    CONSTRAINT geographic_levels_pkey PRIMARY KEY (id),
    CONSTRAINT uk_by9o3bl6rafeuane589514s2v UNIQUE (code)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE referencedata.geographic_levels
    OWNER to postgres;

CREATE TABLE referencedata.geographic_zones
(
    id uuid NOT NULL,
    catchmentpopulation integer,
    code text COLLATE pg_catalog."default" NOT NULL,
    latitude numeric(8,5),
    longitude numeric(8,5),
    name text COLLATE pg_catalog."default",
    levelid uuid NOT NULL,
    parentid uuid,
    boundary geometry,
    extradata jsonb,
    CONSTRAINT geographic_zones_pkey PRIMARY KEY (id),
    CONSTRAINT uk_jpns3ahywgm4k52rdfm08m9k0 UNIQUE (code),
    CONSTRAINT fk3wu6tbyjf7re179s3v57d0hqw FOREIGN KEY (levelid)
        REFERENCES referencedata.geographic_levels (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT geographic_zones_parentid_fkey FOREIGN KEY (parentid)
        REFERENCES referencedata.geographic_zones (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE referencedata.geographic_zones
    OWNER to postgres;

CREATE TABLE referencedata.facility_operators
(
    id uuid NOT NULL,
    code text COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    displayorder integer,
    name text COLLATE pg_catalog."default",
    CONSTRAINT facility_operators_pkey PRIMARY KEY (id),
    CONSTRAINT uk_g7ooo22v3vokh2qrqbxw7uaps UNIQUE (code)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE referencedata.facility_operators
    OWNER to postgres;

CREATE TABLE referencedata.facility_types
(
    id uuid NOT NULL,
    active boolean,
    code text COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    displayorder integer,
    name text COLLATE pg_catalog."default",
    CONSTRAINT facility_types_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE referencedata.facility_types
    OWNER to postgres;

CREATE UNIQUE INDEX unq_facility_type_code
    ON referencedata.facility_types USING btree
    (lower(code) COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE TABLE referencedata.facilities
(
    id uuid NOT NULL,
    active boolean NOT NULL,
    code text COLLATE pg_catalog."default" NOT NULL,
    comment text COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    enabled boolean NOT NULL,
    godowndate date,
    golivedate date,
    name text COLLATE pg_catalog."default",
    openlmisaccessible boolean,
    geographiczoneid uuid NOT NULL,
    operatedbyid uuid,
    typeid uuid NOT NULL,
    extradata jsonb,
    location geometry,
    CONSTRAINT facilities_pkey PRIMARY KEY (id),
    CONSTRAINT fk2vn7d69cbm7cl3m4rte8cy6ja FOREIGN KEY (geographiczoneid)
        REFERENCES referencedata.geographic_zones (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkm12nqtk6paxb7b20m5rklm12w FOREIGN KEY (operatedbyid)
        REFERENCES referencedata.facility_operators (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkpc0soanvqabccyg5br9aexoc1 FOREIGN KEY (typeid)
        REFERENCES referencedata.facility_types (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE referencedata.facilities
    OWNER to postgres;

CREATE INDEX facilities_geographiczoneid_idx
    ON referencedata.facilities USING btree
    (geographiczoneid ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX facilities_location_idx
    ON referencedata.facilities USING gist
    (location)
    TABLESPACE pg_default;

CREATE INDEX facilities_operatedbyid_idx
    ON referencedata.facilities USING btree
    (operatedbyid ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX facilities_typeid_idx
    ON referencedata.facilities USING btree
    (typeid ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE UNIQUE INDEX unq_facility_code
    ON referencedata.facilities USING btree
    (lower(code) COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
