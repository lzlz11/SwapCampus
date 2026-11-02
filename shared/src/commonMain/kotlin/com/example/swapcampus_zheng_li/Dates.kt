package com.example.swapcampus_zheng_li

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

fun todayDate(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
