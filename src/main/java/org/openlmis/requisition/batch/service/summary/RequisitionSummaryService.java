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

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.openlmis.requisition.batch.i18n.MessageKeys.ERROR_NO_APPROVE_PERMISSION;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.openlmis.requisition.batch.dto.summary.RequisitionSummaryDto;
import org.openlmis.requisition.batch.exception.PermissionMessageException;
import org.openlmis.requisition.batch.repository.RequisitionQueryLineItem;
import org.openlmis.requisition.batch.repository.custom.impl.RequisitionSummaryRepositoryCustomImpl;
import org.openlmis.requisition.batch.service.referencedata.DetailedRoleAssignmentDto;
import org.openlmis.requisition.batch.service.referencedata.UserDto;
import org.openlmis.requisition.batch.service.referencedata.UserReferenceDataService;
import org.openlmis.requisition.batch.util.AuthenticationHelper;
import org.openlmis.requisition.batch.util.Message;
import org.openlmis.requisition.batch.web.summary.RequisitionSummariesSearchParams;
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
  private AuthenticationHelper authenticationHelper;

  @Autowired
  private UserReferenceDataService userReferenceDataService;

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
    Set<UUID> nodeIds = userReferenceDataService.getRoleAssignments(user.getId()).stream()
        .filter(roleAssignment -> roleAssignment.getProgramId().equals(params.getProgramId()))
        .filter(roleAssignment -> roleAssignment.getRole().getRights().stream()
            .anyMatch(right -> right.getName().equals(REQUISITION_APPROVE_RIGHT)))
        .map(DetailedRoleAssignmentDto::getSupervisoryNodeId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    if (isEmpty(nodeIds)) {
      throw new PermissionMessageException(new Message(ERROR_NO_APPROVE_PERMISSION));
    }

    profiler.start("FIND_SUMMARY_IN_REPOSITORY");
    List<RequisitionQueryLineItem> requisitionSummaries = requisitionSummaryRepository
        .getRequisitionSummaries(
            params.getProcessingPeriodId(),
            params.getProgramId(),
            nodeIds);

    profiler.start("BUILD_RESPONSE");
    RequisitionSummaryDto result = requisitionSummaryBuilder.build(
        requisitionSummaries,
        params.getProgramId(),
        params.getProcessingPeriodId());

    profiler.stop().log();
    return result;
  }
}
