/*
 * Copyright 2019 ABSA Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package za.co.absa.spline.gateway.rest

import java.util.Arrays.asList

import org.apache.commons.configuration.{CompositeConfiguration, EnvironmentConfiguration, SystemConfiguration}
import za.co.absa.spline.common.config.ConfTyped

object CorsConfig
  extends CompositeConfiguration(asList(
    new SystemConfiguration,
    new EnvironmentConfiguration))
    with ConfTyped {
  override val rootPrefix: String = "spline"

  object CorsFilterConf extends Conf("corsFilter") {
    val allowedOrigin: String = getString(Prop("allowedOrigin"))
    val allowedHeader: Array[String] = getStringArray(Prop("allowedHeader"))
  }

}

