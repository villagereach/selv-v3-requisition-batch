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

import java.util.UUID;
import org.openlmis.requisition.batch.service.referencedata.DetailedRoleAssignmentDto;
import org.openlmis.requisition.batch.service.referencedata.RoleDto;

public class DetailedRoleAssignmentDataBuilder {

  private static int instanceNumber = 0;

  private RoleDto role;
  private String programCode;
  private String supervisoryNodeCode;
  private String warehouseCode;
  private UUID programId;
  private UUID supervisoryNodeId;
  private UUID warehouseId;

  /**
   * Creates builder for creating new instance of {@link DetailedRoleAssignmentDto}.
   */
  public DetailedRoleAssignmentDataBuilder() {
    instanceNumber++;

    this.role = new RoleDtoDataBuilder().build();
    this.programCode = "program" + instanceNumber;
    this.supervisoryNodeCode = "node" + instanceNumber;
    this.warehouseCode = "warehouse" + instanceNumber;
    this.programId = UUID.randomUUID();
    this.supervisoryNodeId = UUID.randomUUID();
    this.warehouseId = UUID.randomUUID();
  }

  public DetailedRoleAssignmentDto build() {
    return new DetailedRoleAssignmentDto(role, programCode, supervisoryNodeCode, warehouseCode,
        programId, supervisoryNodeId, warehouseId);
  }

  public DetailedRoleAssignmentDataBuilder withRole(RoleDto role) {
    this.role = role;
    return this;
  }

  public DetailedRoleAssignmentDataBuilder withProgramId(UUID programId) {
    this.programId = programId;
    return this;
  }

  public DetailedRoleAssignmentDataBuilder withSupervisoryNodeId(UUID supervisoryNodeId) {
    this.supervisoryNodeId = supervisoryNodeId;
    return this;
  }
}
