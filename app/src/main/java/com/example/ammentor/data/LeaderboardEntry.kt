package com.example.ammentor.data

//data class LeaderboardEntry(
//    val id: String = "",
//    val name: String = "",
//    val badge: String = "",
//    val progress: Float = 0f
//)




data class LeaderboardEntry(
    val id: Int,
    val name: String,
    val progress: Float,
    val badge: String,
    val taskProgress: List<Float>
)
val leaderboardData = listOf(
    LeaderboardEntry(1, "Nishita", 0.95f, "🥇 Grandmaster", List(9) { 1f }),
    LeaderboardEntry(2, "Adarsh", 0.89f, "🥈 Expert", List(9) { 0.8f }),
    LeaderboardEntry(3, "Rohith", 0.75f, "🥉 Advanced", List(9) { 0.6f }),
    LeaderboardEntry(4,"Karthik",0.95f,"🥉Advanced",List(8){0.65f})
)
