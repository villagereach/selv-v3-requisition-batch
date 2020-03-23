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

import org.openlmis.requisition.batch.service.summary.RequisitionSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequisitionSummariesController {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RequisitionSummariesController.class);

  @Autowired
  private RequisitionSummaryService requisitionSummaryService;

  /**
   * Returns requisition data for given period and program aggregated by geographic zones.
   * Response is prepared and filtered using query params and user permissions.
   *
   * @param  queryParams request parameters
   * @return             build requisition summary
   */
  @GetMapping("api/requisitionSummaries")
  public RequisitionSummaryDto getRequisitionSummary(
      @RequestParam MultiValueMap<String, String> queryParams) {
    Profiler profiler = new Profiler("REQUISITION_SUMMARY_CONTROLLER_GET");
    profiler.setLogger(LOGGER);

    profiler.start("GET_QUERY_PARAMS");
    RequisitionSummariesSearchParams params =
        new RequisitionSummariesSearchParams(queryParams);

    profiler.start("GET_RESPONSE_FROM_SERVICE");
    RequisitionSummaryDto result = requisitionSummaryService.getRequisitionSummary(params);

    profiler.stop().log();
    return result;
  }
}
