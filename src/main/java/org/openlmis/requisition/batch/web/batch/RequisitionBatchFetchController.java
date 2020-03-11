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

package org.openlmis.requisition.batch.web.batch;

import static java.util.stream.Collectors.toList;

import org.openlmis.requisition.batch.service.referencedata.PermissionService;
import org.openlmis.requisition.batch.service.referencedata.PermissionStringDto;
import org.openlmis.requisition.batch.service.referencedata.PermissionStrings;
import org.openlmis.requisition.batch.service.referencedata.UserDto;
import org.openlmis.requisition.batch.util.AuthenticationHelper;
import org.openlmis.requisition.batch.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("requisitionSummaries")
public class RequisitionBatchFetchController extends BaseController {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RequisitionBatchFetchController.class);

  @Autowired
  private PermissionService permissionService;

  @Autowired
  private AuthenticationHelper authenticationHelper;

  /**
   * Returns requisition data for given period and program
   * aggregated by geographic zones of given level.
   */
  @GetMapping
  public RequisitionSummaryDto getRequisitionSummary(
      @RequestParam MultiValueMap<String, String> queryParams) {

    Profiler profiler = new Profiler("REQUISITION_SERVICE_SEARCH");
    profiler.setLogger(LOGGER);

    profiler.start("GET_QUERY_PARAMS");
    new RequisitionBatchSummarySearchParams(queryParams);

    profiler.start("GET_USER");
    UserDto user = authenticationHelper.getCurrentUser();

    profiler.start("GET_PERM_STRINGS");
    PermissionStrings.Handler handler = permissionService.getPermissionStrings(user.getId());
    handler.get()
        .stream()
        .map(PermissionStringDto::toString)
        .collect(toList());

    return new RequisitionSummaryDto();
  }
}
