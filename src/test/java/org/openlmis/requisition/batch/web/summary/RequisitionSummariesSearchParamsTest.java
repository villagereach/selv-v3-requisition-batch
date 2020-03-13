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

import static org.junit.Assert.assertEquals;
import static org.openlmis.requisition.batch.i18n.MessageKeys.ERROR_PROGRAM_ID_NOT_PROVIDED;
import static org.openlmis.requisition.batch.i18n.MessageKeys.ERROR_QUERY_INVALID_PARAMS;

import be.joengenduvel.java.verifiers.ToStringVerifier;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openlmis.requisition.batch.exception.ValidationMessageException;
import org.openlmis.requisition.batch.testutils.ToStringContractTest;
import org.springframework.util.LinkedMultiValueMap;

public class RequisitionSummariesSearchParamsTest
    extends ToStringContractTest<RequisitionSummariesSearchParams> {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  private static final String PROGRAM_ID = "programId";
  private static final String PROCESSING_PERIOD_ID = "processingPeriodId";

  private LinkedMultiValueMap<String, String> queryMap;
  private UUID id1 = UUID.randomUUID();
  private UUID id2 = UUID.randomUUID();

  @Before
  public void setUp() {
    queryMap = new LinkedMultiValueMap<>();
    queryMap.add(PROGRAM_ID, id1.toString());
    queryMap.add(PROCESSING_PERIOD_ID, id2.toString());
  }

  @Override
  protected Class<RequisitionSummariesSearchParams> getTestClass() {
    return RequisitionSummariesSearchParams.class;
  }

  @Test
  public void shouldThrowExceptionIfThereIsUnknownParameterInParameters() {
    exception.expect(ValidationMessageException.class);
    exception.expectMessage(ERROR_QUERY_INVALID_PARAMS);

    queryMap.add("some-param", "some-value");
    new RequisitionSummariesSearchParams(queryMap);
  }

  @Test
  public void shouldReturnValueForKeyProgramId() {
    queryMap.add(PROGRAM_ID, id2.toString());
    RequisitionSummariesSearchParams searchParams =
        new RequisitionSummariesSearchParams(queryMap);

    assertEquals(id1, searchParams.getProgramId());
  }

  @Test
  public void shouldThrowExceptionIfParameterHasNoUuidFormat() {
    queryMap.add(PROGRAM_ID, "test");
    RequisitionSummariesSearchParams searchParams =
        new RequisitionSummariesSearchParams(queryMap);

    assertEquals(id1, searchParams.getProgramId());
  }

  @Test
  public void shouldThrowExceptionIfProgramIdKeyIsNotPresent() {
    exception.expect(ValidationMessageException.class);
    exception.expectMessage(ERROR_PROGRAM_ID_NOT_PROVIDED);

    queryMap.remove(PROGRAM_ID);
    new RequisitionSummariesSearchParams(queryMap);
  }

  protected void prepare(ToStringVerifier<RequisitionSummariesSearchParams> verifier) {
    verifier.ignore("PROGRAM_ID", "PROCESSING_PERIOD_ID", "ALL_PARAMETERS");
  }
}
