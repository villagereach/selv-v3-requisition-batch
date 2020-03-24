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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.openlmis.requisition.batch.dto.summary.OrderableVersionSummaryDto;
import org.openlmis.requisition.batch.dto.summary.OrderableZonalSummaryDto;

public class OrderableZonalSummaryDtoDataBuilder {

  private static int instanceNumber = 0;

  private String districtName;
  private List<OrderableVersionSummaryDto> orderableVersions;
  private Set<UUID> requisitionIds;

  /**
   * Used for creating new instance of {@link OrderableZonalSummaryDto}.
   */
  public OrderableZonalSummaryDtoDataBuilder() {
    instanceNumber++;

    districtName = "District " + instanceNumber;
    orderableVersions = new ArrayList<>();
    orderableVersions.add(new OrderableVersionSummaryDtoDataBuilder().build());
    requisitionIds = new HashSet<>();
    requisitionIds.add(UUID.randomUUID());
  }

  public OrderableZonalSummaryDto build() {
    return new OrderableZonalSummaryDto(districtName, orderableVersions, requisitionIds);
  }

  public OrderableZonalSummaryDtoDataBuilder withDistrictName(String districtName) {
    this.districtName = districtName;
    return this;
  }

  public OrderableZonalSummaryDtoDataBuilder withOrderableVersions(
      List<OrderableVersionSummaryDto> orderableVersions) {
    this.orderableVersions = orderableVersions;
    return this;
  }

  public OrderableZonalSummaryDtoDataBuilder withRequisitionIds(Set<UUID> requisitionIds) {
    this.requisitionIds = requisitionIds;
    return this;
  }
}
