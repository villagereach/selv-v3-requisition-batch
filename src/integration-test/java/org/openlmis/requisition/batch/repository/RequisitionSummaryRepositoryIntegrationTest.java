/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.requisition.batch.repository;

import static org.hibernate.validator.internal.util.CollectionHelper.asSet;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlmis.requisition.batch.repository.custom.impl.RequisitionSummaryRepositoryCustomImpl;
import org.openlmis.requisition.batch.testutils.RequisitionQueryLineItemDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RequisitionSummaryRepositoryIntegrationTest {

  @Autowired
  private RequisitionSummaryRepositoryCustomImpl requisitionSummaryRepository;

  private static final String DISTRICT = "TEST_DISTRICT_NAME";
  private static final UUID SUPERVISORY_NODE_ID_1 =
          UUID.fromString("7dbcd317-3e50-4964-ab11-d6acf0537d2d");
  private static final UUID SUPERVISORY_NODE_ID_2 =
          UUID.fromString("0a85b106-888f-11ea-bc55-0242ac130003");
  private static final UUID PROCESSING_PERIOD_ID =
          UUID.fromString("5c196f55-d38f-449b-b7dd-5636d640cf22");
  private static final UUID PROGRAM_ID = UUID.fromString("fabfd914-1bb1-470c-9e5d-f138b3ce70b8");
  private static final UUID RANDOM_PROGRAM_ID =
          UUID.fromString("3879701a-89ee-11ea-bc55-0242ac130003");
  private static final UUID ORDERABLE_ID = UUID.fromString("37cead00-608f-41ef-928e-3d79e8701c36");
  private static final UUID REQUISITION_ID_1 =
          UUID.fromString("afcea43d-31e9-49fe-ba74-e5b0dc2c47c4");
  private static final UUID REQUISITION_ID_2 =
          UUID.fromString("5d56ed06-888e-11ea-bc55-0242ac130003");
  private static final UUID REQUISITION_ID_APPROVED =
          UUID.fromString("07964b80-89e9-11ea-bc55-0242ac130003");
  private List<UUID> supervisoryNodeIds = new ArrayList<>();
  private List<UUID> requisitionIds = new ArrayList<>();
  private RequisitionQueryLineItem expectedRequisitionQueryLineItem;
  private List<RequisitionQueryLineItem> requisitionQueryLineItemList;

  @Before
  public void setUp() {
    supervisoryNodeIds.add(SUPERVISORY_NODE_ID_1);
    supervisoryNodeIds.add(SUPERVISORY_NODE_ID_2);
    requisitionIds.add(REQUISITION_ID_1);
    requisitionIds.add(REQUISITION_ID_2);

    expectedRequisitionQueryLineItem =
            new RequisitionQueryLineItemDataBuilder()
                    .withDistrictName(DISTRICT)
                    .withOrderableId(ORDERABLE_ID)
                    .withStockOnHand(40)
                    .withPacksToShip(10)
                    .withRequestedQuantity(200)
                    .withSupervisoryNodeIds(supervisoryNodeIds)
                    .withRequisitionIds(requisitionIds)
                    .build();
  }

  @Test
  public void shouldGetRequisitionQueryLineItems() {
    requisitionQueryLineItemList = requisitionSummaryRepository.getRequisitionSummaries(
                    PROCESSING_PERIOD_ID, PROGRAM_ID,
                    asSet(SUPERVISORY_NODE_ID_1, SUPERVISORY_NODE_ID_2));

    assertEquals(1, requisitionQueryLineItemList.size());
    assertEquals(expectedRequisitionQueryLineItem, requisitionQueryLineItemList.get(0));
    assertEquals(2, requisitionQueryLineItemList.get(0).getRequisitionIds().size());
  }

  @Test
  public void shouldGetOneRequisitionInRequisitionQueryLineItems() {
    requisitionQueryLineItemList = requisitionSummaryRepository.getRequisitionSummaries(
                    PROCESSING_PERIOD_ID, PROGRAM_ID,
                    asSet(SUPERVISORY_NODE_ID_1));

    assertEquals(1, requisitionQueryLineItemList.size());
    assertEquals(1, requisitionQueryLineItemList.get(0).getRequisitionIds().size());
  }

  @Test
  public void shouldNotReturnRequisitionsWithApprovedStatus() {
    requisitionQueryLineItemList = requisitionSummaryRepository.getRequisitionSummaries(
                    PROCESSING_PERIOD_ID, PROGRAM_ID,
                    asSet(SUPERVISORY_NODE_ID_1, SUPERVISORY_NODE_ID_2));

    assertEquals(requisitionQueryLineItemList.get(0).getRequisitionIds()
            .contains(REQUISITION_ID_APPROVED),false);
  }

  @Test
  public void shouldNotReturnRequisitionQueryLineItemsForRandomProgamId() {
    requisitionQueryLineItemList = requisitionSummaryRepository.getRequisitionSummaries(
                    PROCESSING_PERIOD_ID, RANDOM_PROGRAM_ID,
                    asSet(SUPERVISORY_NODE_ID_1, SUPERVISORY_NODE_ID_2));

    assertEquals(requisitionQueryLineItemList.isEmpty(), true);
  }
}
