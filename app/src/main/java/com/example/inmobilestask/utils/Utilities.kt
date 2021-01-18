package com.example.inmobilestask.utils

import com.example.inmobilestask.data.models.Items

class Utilities {
// loop around each item, compares them to other items by a nested loop
    fun ascOrderSorting(items: MutableList<Items>): MutableList<Items> {
// sorts list in ASC order based on ID value
        val sortedList = items.toMutableList()
        for (pass in 0 until (sortedList.size - 1)) {
            for (currentPosition in 0 until (items.size - 1)) {
                if (sortedList[currentPosition].id!! > sortedList[currentPosition + 1].id!!) {
                    val tmp = sortedList[currentPosition]
                    sortedList[currentPosition] = sortedList[currentPosition + 1]
                    sortedList[currentPosition + 1] = tmp
                }
            }
        }

        return sortedList
    }
}