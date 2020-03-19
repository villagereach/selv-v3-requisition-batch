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

package org.openlmis.requisition.batch.service.summary;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.openlmis.requisition.batch.repository.RequisitionQueryLineItem;
import org.openlmis.requisition.batch.repository.custom.impl.RequisitionSummaryRepositoryCustomImpl;
import org.openlmis.requisition.batch.service.referencedata.PermissionService;
import org.openlmis.requisition.batch.service.referencedata.PermissionStringDto;
import org.openlmis.requisition.batch.service.referencedata.PermissionStrings;
import org.openlmis.requisition.batch.service.referencedata.UserDto;
import org.openlmis.requisition.batch.util.AuthenticationHelper;
import org.openlmis.requisition.batch.web.summary.RequisitionSummariesSearchParams;
import org.openlmis.requisition.batch.web.summary.RequisitionSummaryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequisitionSummaryService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequisitionSummaryService.class);

  private static final String REQUISITION_APPROVE_RIGHT = "REQUISITION_APPROVE";

  @Autowired
  private PermissionService permissionService;

  @Autowired
  private AuthenticationHelper authenticationHelper;

  @Autowired
  private RequisitionSummaryBuilder requisitionSummaryBuilder;

  @Autowired
  private RequisitionSummaryRepositoryCustomImpl requisitionSummaryRepository;

  /**
   * Fetches requisition summary data from db and builds response dto.
   * Uses user permissions to filter requisition that user can approve.
   *
   * @param  params query params of a request
   * @return        build requisition summary dto
   */
  public RequisitionSummaryDto getRequisitionSummary(RequisitionSummariesSearchParams params) {

    Profiler profiler = new Profiler("REQUISITION_SUMMARY_SERVICE_GET");
    profiler.setLogger(LOGGER);

    profiler.start("GET_AUTHENTICATED_USER");
    UserDto user = authenticationHelper.getCurrentUser();

    profiler.start("GET_PERMISSION_STRINGS");
    PermissionStrings.Handler handler = permissionService.getPermissionStrings(user.getId());
    Set<PermissionStringDto> permissionStrings = handler.get();

    profiler.start("FILTER_SUPERVISED_FACILITIES");
    Set<UUID> supervisedFacilitiesIds = permissionStrings.stream()
        .filter(permission -> permission.getRightName().equals(REQUISITION_APPROVE_RIGHT))
        .filter(permission -> permission.getProgramId().equals(params.getProgramId()))
        .map(PermissionStringDto::getFacilityId)
        .collect(toSet());

    profiler.start("FIND_SUMMARY_IN_REPOSITORY");
    List<RequisitionQueryLineItem> requisitionSummaries = requisitionSummaryRepository
        .getRequisitionSummaries(
            params.getProcessingPeriodId(),
            params.getProgramId(),
            supervisedFacilitiesIds);

    profiler.start("BUILD_RESPONSE");
    RequisitionSummaryDto result = requisitionSummaryBuilder.build(
        requisitionSummaries,
        params.getProgramId(),
        params.getProcessingPeriodId());

    profiler.stop().log();
    return result;
  }
}
