package com.example.travappupd.data.entities

import kotlin.random.Random

    fun generateLocalId(): Long =
        (System.currentTimeMillis() * 1000) + Random.nextInt(1000)

