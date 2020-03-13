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

package org.openlmis.requisition.batch.web.summary;

import static java.util.Arrays.asList;
import static org.openlmis.requisition.batch.i18n.MessageKeys.ERROR_PROCESSING_PERIOD_ID_NOT_PROVIDED;
import static org.openlmis.requisition.batch.i18n.MessageKeys.ERROR_PROGRAM_ID_NOT_PROVIDED;
import static org.openlmis.requisition.batch.i18n.MessageKeys.ERROR_QUERY_INVALID_PARAMS;

import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.openlmis.requisition.batch.exception.ValidationMessageException;
import org.openlmis.requisition.batch.util.Message;
import org.openlmis.requisition.batch.web.SearchParams;
import org.springframework.util.MultiValueMap;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
public final class RequisitionSummariesSearchParams {

  private static final String PROGRAM_ID = "programId";
  private static final String PROCESSING_PERIOD_ID = "processingPeriodId";

  private static final List<String> ALL_PARAMETERS = asList(PROGRAM_ID, PROCESSING_PERIOD_ID);

  private SearchParams queryParams;

  /**
   * Wraps map of query params into an object.
   */
  public RequisitionSummariesSearchParams(MultiValueMap<String, String> queryMap) {
    queryParams = new SearchParams(queryMap);
    validate();
  }

  /**
   * Gets {@link UUID} for "programId" key from params.
   *
   * @return UUID value of program id or null if params doesn't contain "programId" key.
   */
  public UUID getProgramId() {
    if (!queryParams.containsKey(PROGRAM_ID)) {
      return null;
    }
    return queryParams.getUuid(PROGRAM_ID);
  }

  /**
   * Gets {@link UUID} for "processingPeriodId" key from params.
   *
   * @return UUID value of processingPeriod id
   *          or null if params doesn't contain "processingPeriodId" key.
   */
  public UUID getProcessingPeriodId() {
    if (!queryParams.containsKey(PROCESSING_PERIOD_ID)) {
      return null;
    }
    return queryParams.getUuid(PROCESSING_PERIOD_ID);
  }

  /**
   * Checks if query params are valid. Returns false if any provided param is not on supported list.
   */
  public void validate() {
    if (!ALL_PARAMETERS.containsAll(queryParams.keySet())) {
      throw new ValidationMessageException(new Message(ERROR_QUERY_INVALID_PARAMS));
    }
    if (!queryParams.containsKey(PROGRAM_ID)) {
      throw new ValidationMessageException(new Message(ERROR_PROGRAM_ID_NOT_PROVIDED));
    }
    if (!queryParams.containsKey(PROCESSING_PERIOD_ID)) {
      throw new ValidationMessageException(new Message(ERROR_PROCESSING_PERIOD_ID_NOT_PROVIDED));
    }
  }
}
