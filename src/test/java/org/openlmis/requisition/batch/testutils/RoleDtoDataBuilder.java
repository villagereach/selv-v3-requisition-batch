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

import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

import java.util.Set;
import org.openlmis.requisition.batch.service.referencedata.RightDto;
import org.openlmis.requisition.batch.service.referencedata.RoleDto;

public class RoleDtoDataBuilder {

  private static int instanceNumber = 0;

  private String name;
  private String description;
  private Set<RightDto> rights;

  /**
   * Creates builder for creating new instance of {@link RoleDto}.
   */
  public RoleDtoDataBuilder() {
    instanceNumber++;

    this.name = "role" + instanceNumber;
    this.description = "role desc" + instanceNumber;
    this.rights = asSet(new RightDtoDataBuilder().build());
  }

  public RoleDto build() {
    return new RoleDto(name, description, rights);
  }

  public RoleDtoDataBuilder withRights(Set<RightDto> rights) {
    this.rights = rights;
    return this;
  }
}
