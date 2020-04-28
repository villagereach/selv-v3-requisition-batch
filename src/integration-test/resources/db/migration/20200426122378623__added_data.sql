INSERT INTO referencedata.geographic_levels
(id, code, levelnumber, name)
VALUES('75968df1-2daf-4e7f-823c-64688059ecb7', 'country', 1, 'Nacional');

INSERT INTO referencedata.geographic_levels
(id, code, levelnumber, name)
VALUES('4d2b948e-a20b-483d-bfda-fc8c6b126c33', 'provincia', 2, 'Província');

INSERT INTO referencedata.geographic_levels
(id, code, levelnumber, name)
VALUES('2be873a6-6ddc-4cbb-ba4c-7337eb74c24a', 'distrito', 3, 'Distrito');

INSERT INTO referencedata.geographic_zones
(id, code, name, levelid)
VALUES('5ea01a9e-ceb4-4920-84ed-2729691c9deb','1','Test', '75968df1-2daf-4e7f-823c-64688059ecb7');

INSERT INTO referencedata.geographic_zones
(id, code, name, levelid, parentid)
VALUES('9538a868-2bc5-4cb4-baab-07ea79e00bba','102','Test','4d2b948e-a20b-483d-bfda-fc8c6b126c33','5ea01a9e-ceb4-4920-84ed-2729691c9deb');

INSERT INTO referencedata.geographic_zones
(id, code, name, levelid, parentid)
VALUES('095f6fc0-7ecb-4860-a3ff-23e5d9b36e7a', '102506', 'DISTRICT', '2be873a6-6ddc-4cbb-ba4c-7337eb74c24a', '9538a868-2bc5-4cb4-baab-07ea79e00bba');

INSERT INTO referencedata.facility_types
(id, active, code, name)
VALUES('e03972ea-dead-4c5c-aaf1-6f2531eea53a', 'true', 'test', 'Test');

INSERT INTO referencedata.facility_operators
(id, code, description, displayorder, name)
VALUES('de8b4b03-1b31-441b-8138-0bafddcfba86', 'Test',	'Test', 1, 'Test');

INSERT INTO referencedata.facilities
(id, active, code, enabled, name, openlmisaccessible, geographiczoneid, operatedbyid, typeid)
VALUES('784be047-7d37-4840-893d-68534b2b80a9',	'true',	'1020612', 'true', 'Test', 'true',	'095f6fc0-7ecb-4860-a3ff-23e5d9b36e7a',	'de8b4b03-1b31-441b-8138-0bafddcfba86',	'e03972ea-dead-4c5c-aaf1-6f2531eea53a');

INSERT INTO referencedata.facilities
(id, active, code, enabled, name, openlmisaccessible, geographiczoneid, operatedbyid, typeid)
VALUES('35bb7cf8-888e-11ea-bc55-0242ac130003',	'true',	'8880612', 'true', 'facility 2', 'true',	'095f6fc0-7ecb-4860-a3ff-23e5d9b36e7a',	'de8b4b03-1b31-441b-8138-0bafddcfba86',	'e03972ea-dead-4c5c-aaf1-6f2531eea53a');

INSERT INTO requisition.requisitions
(id, createddate, modifieddate, emergency, facilityid, numberofmonthsinperiod, processingperiodid, programid, status, supervisorynodeid, templateid, version, reportonly)
VALUES('afcea43d-31e9-49fe-ba74-e5b0dc2c47c4', '2020-02-13 11:02:36', '2020-02-21 17:38:02', 'false', '784be047-7d37-4840-893d-68534b2b80a9',	1, '5c196f55-d38f-449b-b7dd-5636d640cf22',
'fabfd914-1bb1-470c-9e5d-f138b3ce70b8', 'AUTHORIZED', '7dbcd317-3e50-4964-ab11-d6acf0537d2d', '7ad8e784-170a-4db9-845f-5bb551bc635d', 5, 'false');

INSERT INTO requisition.requisition_line_items
(id, adjustedconsumption, approvedquantity, averageconsumption, beginningbalance, calculatedorderquantity,  maximumstockquantity, orderableid, packstoship, requestedquantity, requestedquantityexplanation, skipped, stockonhand, total, totalconsumedquantity, totalcost, totallossesandadjustments, totalreceivedquantity, totalstockoutdays, requisitionid,   orderableversionnumber, facilitytypeapprovedproductid, facilitytypeapprovedproductversionnumber)
VALUES('a76fbd99-b3ab-4092-b726-f2e5653ec7e6',	0, 100,	25, 20,	5, 25, '37cead00-608f-41ef-928e-3d79e8701c36', 5, 100, 'Test', false, 20, 20, 0, 0.00, 0, 0, 0, 'afcea43d-31e9-49fe-ba74-e5b0dc2c47c4', 66, '735cf24f-8d15-49eb-9020-b55e481a0fe4', 2);

INSERT INTO requisition.requisitions
(id, createddate, modifieddate, emergency, facilityid, numberofmonthsinperiod, processingperiodid, programid, status, supervisorynodeid, templateid, version, reportonly)
VALUES('5d56ed06-888e-11ea-bc55-0242ac130003', '2020-02-13 11:02:36', '2020-02-21 17:38:02', 'false', '35bb7cf8-888e-11ea-bc55-0242ac130003',	1, '5c196f55-d38f-449b-b7dd-5636d640cf22',
'fabfd914-1bb1-470c-9e5d-f138b3ce70b8', 'AUTHORIZED', '0a85b106-888f-11ea-bc55-0242ac130003', '7ad8e784-170a-4db9-845f-5bb551bc635d', 5, 'false');

INSERT INTO requisition.requisition_line_items
(id, adjustedconsumption, approvedquantity, averageconsumption, beginningbalance, calculatedorderquantity,  maximumstockquantity, orderableid, packstoship, requestedquantity, requestedquantityexplanation, skipped, stockonhand, total, totalconsumedquantity, totalcost, totallossesandadjustments, totalreceivedquantity, totalstockoutdays, requisitionid,   orderableversionnumber, facilitytypeapprovedproductid, facilitytypeapprovedproductversionnumber)
VALUES('6dd4115e-888e-11ea-bc55-0242ac130003',	0, 100,	25, 20,	5, 25, '37cead00-608f-41ef-928e-3d79e8701c36', 5, 100, 'Test', false, 20, 20, 0, 0.00, 0, 0, 0, '5d56ed06-888e-11ea-bc55-0242ac130003', 66, '735cf24f-8d15-49eb-9020-b55e481a0fe4', 2);