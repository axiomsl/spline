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

package za.co.absa.spline.consumer.service.repo

import com.arangodb.ArangoDatabaseAsync
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import za.co.absa.spline.consumer.service.model.{Operation, OperationDetails}

import scala.concurrent.{ExecutionContext, Future}

@Repository
class OperationRepositoryImpl @Autowired()(db: ArangoDatabaseAsync) extends OperationRepository {
  import za.co.absa.spline.persistence.ArangoImplicits._

  override def findById(operationId: Operation.Id)(implicit ec: ExecutionContext): Future[OperationDetails] = {
    db.queryOne[OperationDetails](
      """
      FOR ope IN operation
        FILTER ope._key == @operationId

        LET inputs = (
          FOR v IN 1..1
          OUTBOUND ope follows
          RETURN v.outputSchema.attributes
        )

        LET readsFrom = FIRST(
          (
            FOR v IN 1..1
            OUTBOUND ope readsFrom
            RETURN v.uri
          )
        )

        LET writesTo = FIRST(
          (
            FOR v IN 1..1
            OUTBOUND ope writesTo
            RETURN v.uri
          )
        )

        LET output = [ope.outputSchema.attributes]

        LET dataTypes = (
          FOR v IN 1..9999
          INBOUND ope follows, readsFrom, writesTo, executes
          FILTER CONTAINS(v._id, "execution")
          RETURN v.dataTypes
        )

        LET schemas = APPEND(inputs, output)

        RETURN {
          "operation" : MERGE(KEEP(ope, "_type", "name"), {"_id": ope._key }, {"readsFrom" : readsFrom}, {"writesTo" : writesTo}),
          "dataTypes": FIRST(dataTypes),
          "schemas" : schemas,
          "inputs": LENGTH(inputs) > 0 ? RANGE(0, LENGTH(inputs)-1) : [],
          "output": LENGTH(schemas)-1
        }
      """,
      Map("operationId" -> operationId)
    )
  }
}
