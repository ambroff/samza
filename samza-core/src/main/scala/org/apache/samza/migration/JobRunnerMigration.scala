/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.samza.migration

import org.apache.samza.config.Config
import org.apache.samza.util.{Util, Logging}
import org.apache.samza.SamzaException


object JobRunnerMigration {
  val CHECKPOINTMIGRATION = "old.checkpoint.KafkaCheckpointMigration"
  def apply(config: Config) = {
    val migration = new JobRunnerMigration
    migration.checkpointMigration(config)
  }
}

class JobRunnerMigration extends Logging {

  def checkpointMigration(config: Config) = {
    val checkpointFactory = Option(config.get("task.checkpoint.factory"))
    checkpointFactory match {
      case Some("org.apache.samza.checkpoint.kafka.KafkaCheckpointManagerFactory") =>
        info("Performing checkpoint migration")
        val checkpointMigrationPlan = Util.getObj[MigrationPlan](JobRunnerMigration.CHECKPOINTMIGRATION)
        checkpointMigrationPlan.migrate(config)
      case None =>
        info("No task.checkpoint.factory defined, not performing any checkpoint migration")
      case _ =>
        val errorMsg = "Auto checkpoint migration for 0.10.0 upgrade is only supported for Kafka checkpointing system, " +
          "for everything else, please use the checkpoint tool and remove task.checkpoint.factory configuration"
        error(errorMsg)
        throw new SamzaException(errorMsg)
    }
  }
}
