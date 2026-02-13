package com.vikas.guhyagyan.apiState

interface ApiState<T> {
    val data: T?
    val errorMessage: String?

    class Loading<T> : ApiState<T> {
        override val data: T? = null
        override val errorMessage: String? = null
    }

    class Success<T>(override val data: T?) : ApiState<T> {
        override val errorMessage: String? = null
    }

    class Error<T>(override val errorMessage: String) : ApiState<T> {
        override val data: T? = null
    }

}