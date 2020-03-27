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

import java.util.Random;
import org.openlmis.requisition.batch.dto.summary.OrderableVersionSummaryDto;

public class OrderableVersionSummaryDtoDataBuilder {

  private Integer versionNumber;
  private Integer stockOnHand;
  private Integer requestedQuantity;
  private Integer packsToShip;

  /**
   * Used for creating new instance of {@link OrderableVersionSummaryDto}.
   */
  public OrderableVersionSummaryDtoDataBuilder() {
    versionNumber = 0;
    stockOnHand = new Random().nextInt(100);
    requestedQuantity = new Random().nextInt(100);
    packsToShip = new Random().nextInt(100);
  }

  public OrderableVersionSummaryDto build() {
    return new OrderableVersionSummaryDto(
        versionNumber, stockOnHand, requestedQuantity, packsToShip);
  }

  public OrderableVersionSummaryDtoDataBuilder withVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
    return this;
  }

  public OrderableVersionSummaryDtoDataBuilder withStockOnHand(int stockOnHand) {
    this.stockOnHand = stockOnHand;
    return this;
  }

  public OrderableVersionSummaryDtoDataBuilder withRequestedQuantity(int requestedQuantity) {
    this.requestedQuantity = requestedQuantity;
    return this;
  }

  public OrderableVersionSummaryDtoDataBuilder withPacksToShip(int packsToShip) {
    this.packsToShip = packsToShip;
    return this;
  }
}
