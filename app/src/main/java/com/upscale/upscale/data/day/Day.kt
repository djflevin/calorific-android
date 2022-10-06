package com.upscale.upscale.data.day

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.format.DateTimeFormatter

@Entity
data class Day (
    @PrimaryKey val date: String = DateTimeFormatter.ISO_DATE.format(Instant.now())
)