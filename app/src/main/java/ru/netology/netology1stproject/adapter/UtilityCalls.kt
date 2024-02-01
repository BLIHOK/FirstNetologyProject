package ru.netology.netology1stproject.adapter

class UtilityCalls {
    fun changeCountersImagin(counter: Int): String {

        return when (counter) {
            in 0..999 -> counter.toString()
            in 1000..9999 ->
                if ((counter % 1000) == 0) {
                    (counter / 1000).toString() + "K"
                } else if (counter % 1000 < 100) {
                    (counter / 1000).toString() + "K"
                } else (counter / 1000).toString() + "." + ((counter / 100) % 10).toString() + "K"

            in 10000..999999 -> (counter / 1000).toString() + "K"

            in 1000000..10000000 ->
                if ((counter % 1000000) == 0) {
                    (counter / 1000000).toString() + "M"
                } else if (counter % 1000000 < 100000) {
                    (counter / 1000000).toString() + "M"
                } else (counter / 1000000).toString() + "." + ((counter / 100000) % 10).toString() + "M"

            else -> "0"
        }
    }
}