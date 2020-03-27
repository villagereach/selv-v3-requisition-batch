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
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.openlmis.requisition.batch.dto.summary.RequisitionSummaryDto;
import org.openlmis.requisition.batch.repository.RequisitionQueryLineItem;
import org.openlmis.requisition.batch.testutils.OrderableVersionSummaryDtoDataBuilder;
import org.openlmis.requisition.batch.testutils.OrderableZonalSummaryDtoDataBuilder;
import org.openlmis.requisition.batch.testutils.RequisitionQueryLineItemDataBuilder;
import org.openlmis.requisition.batch.testutils.RequisitionSummaryDtoDataBuilder;
import org.openlmis.requisition.batch.testutils.RequisitionSummaryLineItemDtoDataBuilder;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class RequisitionSummaryBuilderTest {

  @InjectMocks
  private RequisitionSummaryBuilder requisitionSummaryBuilder;

  private UUID programId = UUID.randomUUID();
  private UUID processingPeriodId = UUID.randomUUID();
  private String district1 = "District 1";
  private String district2 = "District 2";
  private UUID orderable1 = UUID.randomUUID();
  private UUID orderable2 = UUID.randomUUID();

  private RequisitionQueryLineItem item1 = new RequisitionQueryLineItemDataBuilder()
      .withDistrictName(district1)
      .withOrderableId(orderable1)
      .withOrderableVersionNumber(1)
      .build();
  private RequisitionQueryLineItem item2 = new RequisitionQueryLineItemDataBuilder()
      .withDistrictName(district1)
      .withOrderableId(orderable1)
      .withOrderableVersionNumber(1)
      .build();
  private RequisitionQueryLineItem item3 = new RequisitionQueryLineItemDataBuilder()
      .withDistrictName(district1)
      .withOrderableId(orderable1)
      .withOrderableVersionNumber(2)
      .build();
  private RequisitionQueryLineItem item4 = new RequisitionQueryLineItemDataBuilder()
      .withDistrictName(district1)
      .withOrderableId(orderable2)
      .withOrderableVersionNumber(1)
      .build();
  private RequisitionQueryLineItem item5 = new RequisitionQueryLineItemDataBuilder()
      .withDistrictName(district2)
      .withOrderableId(orderable1)
      .withOrderableVersionNumber(1)
      .build();
  private RequisitionQueryLineItem item6 = new RequisitionQueryLineItemDataBuilder()
      .withDistrictName(district2)
      .withOrderableId(orderable1)
      .withOrderableVersionNumber(1)
      .build();
  private List<RequisitionQueryLineItem> requisitionQueryLineItems = newArrayList(
      item1, item2, item3, item4, item5, item6);

  private RequisitionSummaryDto summary = new RequisitionSummaryDtoDataBuilder()
      .withProcessingPeriodId(processingPeriodId)
      .withProgramId(programId)
      .withLineItems(newArrayList(
          new RequisitionSummaryLineItemDtoDataBuilder()
              .withOrderableId(orderable1)
              .withZoneSummaries(newArrayList(
                  new OrderableZonalSummaryDtoDataBuilder()
                      .withDistrictName(district1)
                      .withOrderableVersions(newArrayList(
                          new OrderableVersionSummaryDtoDataBuilder()
                              .withVersionNumber(1)
                              .withRequestedQuantity(item1.getRequestedQuantity()
                                  + item2.getRequestedQuantity())
                              .withStockOnHand(item1.getStockOnHand() + item2.getStockOnHand())
                              .withPacksToShip(item1.getPacksToShip() + item2.getPacksToShip())
                              .build(),
                          new OrderableVersionSummaryDtoDataBuilder()
                              .withVersionNumber(2)
                              .withRequestedQuantity(item3.getRequestedQuantity())
                              .withStockOnHand(item3.getStockOnHand())
                              .withPacksToShip(item3.getPacksToShip())
                              .build()))
                      .withRequisitionIds(toSet(item1.getRequisitionIds(),
                          item2.getRequisitionIds(), item3.getRequisitionIds()))
                      .build(),
                  new OrderableZonalSummaryDtoDataBuilder()
                      .withDistrictName(district2)
                      .withOrderableVersions(newArrayList(
                          new OrderableVersionSummaryDtoDataBuilder()
                              .withVersionNumber(1)
                              .withRequestedQuantity(item5.getRequestedQuantity()
                                  + item6.getRequestedQuantity())
                              .withStockOnHand(item5.getStockOnHand() + item6.getStockOnHand())
                              .withPacksToShip(item5.getPacksToShip() + item6.getPacksToShip())
                              .build()))
                      .withRequisitionIds(toSet(item5.getRequisitionIds(),
                          item6.getRequisitionIds()))
                      .build()))
              .build(),
          new RequisitionSummaryLineItemDtoDataBuilder()
              .withOrderableId(orderable2)
              .withZoneSummaries(newArrayList(
                  new OrderableZonalSummaryDtoDataBuilder()
                      .withDistrictName(district1)
                      .withOrderableVersions(newArrayList(
                          new OrderableVersionSummaryDtoDataBuilder()
                              .withVersionNumber(1)
                              .withRequestedQuantity(item4.getRequestedQuantity())
                              .withStockOnHand(item4.getStockOnHand())
                              .withPacksToShip(item4.getPacksToShip())
                              .build()))
                      .withRequisitionIds(toSet(item4.getRequisitionIds()))
                      .build()))
              .build()))
      .build();

  @Before
  public void setUp() {
    ReflectionTestUtils.setField(requisitionSummaryBuilder, "serviceUrl", "https://openlmis/");
  }

  @Test
  public void shouldBuildRequisitionSummary() {
    RequisitionSummaryDto result = requisitionSummaryBuilder
        .build(requisitionQueryLineItems, programId, processingPeriodId);

    result.getLineItems().sort(Comparator.comparing(item -> item.getOrderable().getId()));
    summary.getLineItems().sort(Comparator.comparing(item -> item.getOrderable().getId()));

    assertThat(summary, is(result));
  }

  @Test
  public void shouldBuildWithNullSoh() {
    requisitionSummaryBuilder.build(
        newArrayList(new RequisitionQueryLineItemDataBuilder()
            .withDistrictName(district1)
            .withOrderableId(orderable1)
            .withOrderableVersionNumber(1)
            .withStockOnHand(null)
            .build()),
        programId,
        processingPeriodId);
  }

  @Test
  public void shouldBuildWithNullRequestedQuantity() {
    requisitionSummaryBuilder.build(
        newArrayList(new RequisitionQueryLineItemDataBuilder()
            .withDistrictName(district1)
            .withOrderableId(orderable1)
            .withOrderableVersionNumber(1)
            .withRequestedQuantity(null)
            .build()),
        programId,
        processingPeriodId);
  }

  @Test
  public void shouldBuildWithNullPacksToShip() {
    requisitionSummaryBuilder.build(
        newArrayList(new RequisitionQueryLineItemDataBuilder()
            .withDistrictName(district1)
            .withOrderableId(orderable1)
            .withOrderableVersionNumber(1)
            .withPacksToShip(null)
            .build()),
        programId,
        processingPeriodId);
  }

  private Set<UUID> toSet(List<UUID>... lists) {
    Set<UUID> result = new HashSet<>();
    Arrays.stream(lists).forEach(result::addAll);
    return result;
  }
}
