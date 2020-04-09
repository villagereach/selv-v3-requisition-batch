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

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openlmis.requisition.batch.dto.summary.RequisitionSummaryDto;
import org.openlmis.requisition.batch.exception.PermissionMessageException;
import org.openlmis.requisition.batch.repository.RequisitionQueryLineItem;
import org.openlmis.requisition.batch.repository.custom.impl.RequisitionSummaryRepositoryCustomImpl;
import org.openlmis.requisition.batch.service.referencedata.DetailedRoleAssignmentDto;
import org.openlmis.requisition.batch.service.referencedata.RightType;
import org.openlmis.requisition.batch.service.referencedata.UserDto;
import org.openlmis.requisition.batch.service.referencedata.UserReferenceDataService;
import org.openlmis.requisition.batch.testutils.DetailedRoleAssignmentDataBuilder;
import org.openlmis.requisition.batch.testutils.RequisitionQueryLineItemDataBuilder;
import org.openlmis.requisition.batch.testutils.RequisitionSummaryDtoDataBuilder;
import org.openlmis.requisition.batch.testutils.RightDtoDataBuilder;
import org.openlmis.requisition.batch.testutils.RoleDtoDataBuilder;
import org.openlmis.requisition.batch.testutils.UserDtoDataBuilder;
import org.openlmis.requisition.batch.util.AuthenticationHelper;
import org.openlmis.requisition.batch.web.summary.RequisitionSummariesSearchParams;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(MockitoJUnitRunner.class)
public class RequisitionSummaryServiceTest {

  private static final String REQUISITION_APPROVE_RIGHT = "REQUISITION_APPROVE";
  private static final String PROCESSING_PERIOD_ID = "processingPeriodId";
  private static final String PROGRAM_ID = "programId";

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Mock
  private AuthenticationHelper authenticationHelper;

  @Mock
  private UserReferenceDataService userReferenceDataService;

  @Mock
  private RequisitionSummaryBuilder requisitionSummaryBuilder;

  @Mock
  private RequisitionSummaryRepositoryCustomImpl requisitionSummaryRepository;

  @InjectMocks
  private RequisitionSummaryService requisitionSummaryService;

  private UserDto user = new UserDtoDataBuilder().buildAsDto();
  private UUID supervisoryNodeId = UUID.randomUUID();
  private UUID programId = UUID.randomUUID();
  private UUID processingPeriodId = UUID.randomUUID();
  private List<RequisitionQueryLineItem> queryResult = newArrayList(
      new RequisitionQueryLineItemDataBuilder().build());
  private RequisitionSummaryDto requisitionSummary = new RequisitionSummaryDtoDataBuilder().build();
  private RequisitionSummariesSearchParams params;
  private List<DetailedRoleAssignmentDto> roleAssignments = newArrayList(
      new DetailedRoleAssignmentDataBuilder()
          .withProgramId(programId)
          .withSupervisoryNodeId(supervisoryNodeId)
          .withRole(new RoleDtoDataBuilder()
              .withRights(asSet(
                  new RightDtoDataBuilder()
                      .withName(REQUISITION_APPROVE_RIGHT)
                      .withType(RightType.SUPERVISION)
                      .build()))
              .build())
          .build()
  );

  @Before
  public void setUp() {
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    paramsMap.add(PROCESSING_PERIOD_ID, processingPeriodId.toString());
    paramsMap.add(PROGRAM_ID, programId.toString());
    params = new RequisitionSummariesSearchParams(paramsMap);

    when(authenticationHelper.getCurrentUser()).thenReturn(user);
    when(userReferenceDataService.getRoleAssignments(eq(user.getId())))
        .thenReturn(roleAssignments);
    when(requisitionSummaryRepository.getRequisitionSummaries(
        eq(processingPeriodId), eq(programId), eq(asSet(supervisoryNodeId))))
        .thenReturn(queryResult);
    when(requisitionSummaryBuilder.build(eq(queryResult), eq(programId), eq(processingPeriodId)))
        .thenReturn(requisitionSummary);
  }

  @Test
  public void shouldReturnRequisitionSummaryForParams() {
    assertThat(requisitionSummary, is(requisitionSummaryService.getRequisitionSummary(params)));
  }

  @Test
  public void shouldThrowPermissionExceptionWhenUserIsLackingApproveRight() {
    exception.expect(PermissionMessageException.class);

    when(userReferenceDataService.getRoleAssignments(eq(user.getId()))).thenReturn(newArrayList());

    requisitionSummaryService.getRequisitionSummary(params);
  }
}
