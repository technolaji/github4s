/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

package github4s.free.domain

case class Config(
                   content_type: String,
                   insecure_ssl: String,
                   url: String
                 )

case class LastResponse(
                         code: Int,
                         status: String,
                         message: String
                       )

//case class Webhook(
//                    `type`: String,
//                    id: Int,
//                    name: String,
//                    active: Boolean,
//                    events: Seq[String],
//                    config: Option[Config],
//                    updated_at: String,
//                    created_at: String,
//                    url: String,
//                    test_url: String,
//                    ping_url: String,
//                    last_response: Option[LastResponse]
//                  )
case class Webhook(
                    `type`: String,
                    id: Int,
                    name: String,
                    active: Boolean,
                    events: Seq[String],
                    config: Option[Config],
                    updated_at: String,
                    created_at: String,
                    url: String,
                    test_url: String,
                    ping_url: String,
                    last_response: Option[LastResponse]
                  )

