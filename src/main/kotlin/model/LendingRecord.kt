package model

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class LendingRecord(
    val id: Int,
    val bookId: Int,
    val borrowerName: String,
    val checkoutDate: String,
    val returnDate: String? = null
)
