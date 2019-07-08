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