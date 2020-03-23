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

package org.openlmis.requisition.batch.i18n;

import java.util.Arrays;

public abstract class MessageKeys {
  private static final String DELIMITER = ".";

  private static final String SERVICE_PREFIX = "requisitionBatch";
  private static final String ERROR_PREFIX = join(SERVICE_PREFIX, "error");

  private static final String AUTHENTICATION_ERROR = join(ERROR_PREFIX, "authentication");
  public static final String ERROR_USER_NOT_FOUND = join(AUTHENTICATION_ERROR, "userCanNotBeFound");

  private static final String SERVICE_ERROR = join(ERROR_PREFIX, "service");
  public static final String ERROR_SERVICE_REQUIRED = join(SERVICE_ERROR, "required");
  public static final String ERROR_SERVICE_OCCURRED = join(SERVICE_ERROR, "errorOccurred");

  private static final String VALIDATION_ERROR = join(ERROR_PREFIX, "validation");
  public static final String ERROR_INVALID_DATE_FORMAT = join(VALIDATION_ERROR,
      "invalidDateFormat");
  public static final String ERROR_INVALID_BOOLEAN_FORMAT = join(VALIDATION_ERROR,
      "invalidBooleanFormat");
  public static final String ERROR_INVALID_UUID_FORMAT = join(VALIDATION_ERROR,
      "invalidUuidFormat");
  public static final String ERROR_SUMMARY_QUERY_INVALID_PARAMS = join(VALIDATION_ERROR,
      "query", "summary", "invalidParams");
  public static final String ERROR_PROGRAM_ID_NOT_PROVIDED = join(VALIDATION_ERROR,
      "query", "programId", "notProvided");
  public static final String ERROR_PROCESSING_PERIOD_ID_NOT_PROVIDED = join(VALIDATION_ERROR,
      "query", "processingPeriodId", "notProvided");

  private static final String PERMISSION_ERROR = join(ERROR_PREFIX, "permission");
  public static final String ERROR_NO_APPROVE_PERMISSION = join(PERMISSION_ERROR, "approve");

  private MessageKeys() {
    throw new UnsupportedOperationException();
  }

  private static String join(String... params) {
    return String.join(DELIMITER, Arrays.asList(params));
  }
}
