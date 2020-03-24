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
import java.util.UUID;
import org.openlmis.requisition.batch.dto.ObjectReferenceDto;
import org.openlmis.requisition.batch.dto.summary.RequisitionSummaryDto;
import org.openlmis.requisition.batch.dto.summary.RequisitionSummaryLineItemDto;

public class RequisitionSummaryDtoDataBuilder {

  private ObjectReferenceDto program;
  private ObjectReferenceDto processingPeriod;
  private List<RequisitionSummaryLineItemDto> lineItems;

  /**
   * Used for creating new instance of {@link RequisitionSummaryDto}.
   */
  public RequisitionSummaryDtoDataBuilder() {
    program = new ObjectReferenceDtoDataBuilder()
        .withPath("programs")
        .buildAsDto();
    processingPeriod = new ObjectReferenceDtoDataBuilder()
        .withPath("processingPeriods")
        .buildAsDto();
    lineItems = new ArrayList<>();
    lineItems.add(new RequisitionSummaryLineItemDtoDataBuilder().build());
  }

  public RequisitionSummaryDto build() {
    return new RequisitionSummaryDto(program, processingPeriod, lineItems);
  }

  /**
   * Sets processing period id.
   *
   * @param processingPeriodId period id
   * @return builder object
   */
  public RequisitionSummaryDtoDataBuilder withProcessingPeriodId(UUID processingPeriodId) {
    processingPeriod = new ObjectReferenceDtoDataBuilder()
        .withPath("processingPeriods")
        .withId(processingPeriodId)
        .buildAsDto();
    return this;
  }

  /**
   * Sets program id.
   *
   * @param programId program id
   * @return builder object
   */
  public RequisitionSummaryDtoDataBuilder withProgramId(UUID programId) {
    program = new ObjectReferenceDtoDataBuilder()
        .withPath("programs")
        .withId(programId)
        .buildAsDto();
    return this;
  }

  public RequisitionSummaryDtoDataBuilder withLineItems(List<RequisitionSummaryLineItemDto> items) {
    lineItems = items;
    return this;
  }
}
