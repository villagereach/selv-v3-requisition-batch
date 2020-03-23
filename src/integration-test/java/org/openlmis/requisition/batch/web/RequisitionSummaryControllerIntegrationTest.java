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

package org.openlmis.requisition.batch.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;

import guru.nidi.ramltester.junit.RamlMatchers;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.openlmis.requisition.batch.service.summary.RequisitionSummaryService;
import org.openlmis.requisition.batch.testutils.RequisitionSummaryDtoDataBuilder;
import org.openlmis.requisition.batch.web.summary.RequisitionSummariesSearchParams;
import org.openlmis.requisition.batch.web.summary.RequisitionSummaryDto;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class RequisitionSummaryControllerIntegrationTest extends BaseWebIntegrationTest {

  private static final String RESOURCE_URL = "/api/requisitionSummaries";
  private static final String PROCESSING_PERIOD_ID = "processingPeriodId";
  private static final String PROGRAM_ID = "programId";

  private UUID programId = UUID.randomUUID();
  private UUID processingPeriodId = UUID.randomUUID();
  private MultiValueMap<String, String> paramsMap;
  private RequisitionSummariesSearchParams params;
  private RequisitionSummaryDto requisitionSummary = new RequisitionSummaryDtoDataBuilder().build();

  @MockBean
  private RequisitionSummaryService requisitionSummaryService;

  @Before
  public void setUp() {
    paramsMap = new LinkedMultiValueMap<>();
    paramsMap.add(PROCESSING_PERIOD_ID, processingPeriodId.toString());
    paramsMap.add(PROGRAM_ID, programId.toString());
    params = new RequisitionSummariesSearchParams(paramsMap);

    given(requisitionSummaryService.getRequisitionSummary(eq(params)))
        .willReturn(requisitionSummary);
  }

  @Test
  public void shouldReturnUnauthorizedWithoutAuthorizationForCreateServiceAccountEndpoint() {
    restAssured
        .given()
        .queryParameter(PROGRAM_ID, programId)
        .queryParameter(PROCESSING_PERIOD_ID, processingPeriodId)
        .when()
        .get(RESOURCE_URL)
        .then()
        .statusCode(401);

    assertThat(RAML_ASSERT_MESSAGE, restAssured.getLastReport(), RamlMatchers.hasNoViolations());
  }

  @Test
  public void shouldRetrieveRequisitionSummary() {
    RequisitionSummaryDto response = restAssured
        .given()
        .header(HttpHeaders.AUTHORIZATION, getTokenHeader())
        .queryParameter(PROGRAM_ID, programId)
        .queryParameter(PROCESSING_PERIOD_ID, processingPeriodId)
        .when()
        .get(RESOURCE_URL)
        .then()
        .statusCode(200)
        .extract().as(RequisitionSummaryDto.class);

    assertThat(requisitionSummary, is(response));
  }

  @Test
  public void shouldReturnBadRequestWhenProgramIdWasNotProvided() {
    restAssured
        .given()
        .header(HttpHeaders.AUTHORIZATION, getTokenHeader())
        .queryParameter(PROCESSING_PERIOD_ID, processingPeriodId)
        .when()
        .get(RESOURCE_URL)
        .then()
        .statusCode(400);
  }

  @Test
  public void shouldReturnBadRequestWhenProcessingPeriodIdWasNotProvided() {
    restAssured
        .given()
        .header(HttpHeaders.AUTHORIZATION, getTokenHeader())
        .queryParameter(PROGRAM_ID, programId)
        .when()
        .get(RESOURCE_URL)
        .then()
        .statusCode(400);
  }
}
