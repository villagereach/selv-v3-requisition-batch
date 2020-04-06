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

package org.openlmis.requisition.batch.testutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.openlmis.requisition.batch.repository.RequisitionQueryLineItem;

public class RequisitionQueryLineItemDataBuilder {

  private static int instanceNumber = 0;

  private String districtName;
  private UUID orderableId;
  private Integer orderableVersionNumber;
  private Integer requestedQuantity;
  private Integer packsToShip;
  private Integer stockOnHand;
  private List<UUID> requisitionIds;
  private List<UUID> supervisoryNodeIds;

  /**
   * Used for creating new instance of {@link RequisitionQueryLineItem}.
   */
  public RequisitionQueryLineItemDataBuilder() {
    instanceNumber++;

    districtName = "District " + instanceNumber;
    orderableId = UUID.randomUUID();
    orderableVersionNumber = 1;
    requestedQuantity = new Random().nextInt(100);
    packsToShip = new Random().nextInt(100);
    stockOnHand = new Random().nextInt(100);
    requisitionIds = new ArrayList<>();
    requisitionIds.add(UUID.randomUUID());
    supervisoryNodeIds = new ArrayList<>();
    supervisoryNodeIds.add(UUID.randomUUID());
  }

  public RequisitionQueryLineItem build() {
    return new RequisitionQueryLineItem(districtName, orderableId, orderableVersionNumber,
        requestedQuantity, stockOnHand, packsToShip, requisitionIds, supervisoryNodeIds);
  }

  public RequisitionQueryLineItemDataBuilder withDistrictName(String districtName) {
    this.districtName = districtName;
    return this;
  }

  public RequisitionQueryLineItemDataBuilder withOrderableId(UUID orderableId) {
    this.orderableId = orderableId;
    return this;
  }

  public RequisitionQueryLineItemDataBuilder withOrderableVersionNumber(Integer number) {
    this.orderableVersionNumber = number;
    return this;
  }

  public RequisitionQueryLineItemDataBuilder withStockOnHand(Integer stockOnHand) {
    this.stockOnHand = stockOnHand;
    return this;
  }

  public RequisitionQueryLineItemDataBuilder withRequestedQuantity(Integer requestedQuantity) {
    this.requestedQuantity = requestedQuantity;
    return this;
  }

  public RequisitionQueryLineItemDataBuilder withPacksToShip(Integer packsToShip) {
    this.packsToShip = packsToShip;
    return this;
  }
}
