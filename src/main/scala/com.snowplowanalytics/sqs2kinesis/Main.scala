/*
 * Copyright (c) 2020-2020 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package com.snowplowanalytics.sqs2kinesis

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import com.typesafe.config.ConfigFactory
//import io.sentry.Sentry

object Main extends App with LazyLogging {

  val sqs2KinesisConfig = {
    // lack of one of those settings should throw an exception and stop the application
    val conf              = ConfigFactory.load().getConfig("sqs2kinesis")
    val sqsQueue          = conf.getString("sqs-queue")
    val kinesisStreamName = conf.getString("kinesis-stream-name")
//    val sentryDsn         = conf.getString("sentry-dsn")

    val config = Sqs2KinesisConfig(
      sqsQueue,
      kinesisStreamName
//      sentryDsn
    )
    logger.info(s"config: $config")
    config
  }

//  Sentry.init(sqs2KinesisConfig.sentryDsn)

  implicit val system: ActorSystem = ActorSystem()

  EventsStreamModule.runStream(sqs2KinesisConfig)
  HttpModule.runHttpServer("0.0.0.0", 8080) // HTTP server for health check

}
