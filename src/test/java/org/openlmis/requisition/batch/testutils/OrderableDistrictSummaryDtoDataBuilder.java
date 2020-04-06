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
import org.openlmis.requisition.batch.dto.summary.OrderableDistrictSummaryDto;
import org.openlmis.requisition.batch.dto.summary.OrderableVersionSummaryDto;

public class OrderableDistrictSummaryDtoDataBuilder {

  private List<OrderableVersionSummaryDto> orderableVersions;

  /**
   * Used for creating new instance of {@link OrderableDistrictSummaryDto}.
   */
  public OrderableDistrictSummaryDtoDataBuilder() {
    orderableVersions = new ArrayList<>();
    orderableVersions.add(new OrderableVersionSummaryDtoDataBuilder().build());
  }

  public OrderableDistrictSummaryDto build() {
    return new OrderableDistrictSummaryDto(orderableVersions);
  }

  public OrderableDistrictSummaryDtoDataBuilder withOrderableVersions(
      List<OrderableVersionSummaryDto> orderableVersions) {
    this.orderableVersions = orderableVersions;
    return this;
  }
}
