/*
 * Copyright 2019 Lo-Ellen Robotics
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

package frc.team4069.saturn.lib.debug

import frc.team4069.saturn.lib.nt.SaturnNetworkTable
import frc.team4069.saturn.lib.nt.delegate
import frc.team4069.saturn.lib.nt.get

/**
 * Singleton Object representing the network table for the Live Dashboard
 */
object LiveDashboard {
    val liveDashboardTable = SaturnNetworkTable.getTable("Live_Dashboard")

    var robotX by liveDashboardTable["robotX"].delegate(0.0)
    var robotY by liveDashboardTable["robotY"].delegate(0.0)
    var robotHeading by liveDashboardTable["robotHeading"].delegate(0.0)

    var isFollowingPath by liveDashboardTable["isFollowingPath"].delegate(false)
    var pathX by liveDashboardTable["pathX"].delegate(0.0)
    var pathY by liveDashboardTable["pathY"].delegate(0.0)
    var pathHeading by liveDashboardTable["pathHeading"].delegate(0.0)
}