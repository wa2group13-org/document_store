package it.polito.wa2.g13.document_store.util

/**
 * [Result] is a type that represents either success ([Ok]) or failure ([Err]).
 */
@Suppress("unused")
sealed class Result<T, E> {

    fun isOk(): Boolean = this is Ok

    fun isErr(): Boolean = this is Err

    fun option(): T? = when (this) {
        is Ok -> this.t
        is Err -> null
    }

    fun <R> map(f: (T) -> R): Result<R, E> = when (this) {
        is Ok -> Ok(f(this.t))
        is Err -> Err(this.err)
    }

    /**
     * Returns [res] if the result is [Ok], otherwise returns the [Err] value of `this`.
     *
     * Arguments passed to and are eagerly evaluated; if you are passing the result of a function call,
     * it is recommended to use [andThen], which is lazily evaluated.
     */
    fun <R> and(res: Result<R, E>): Result<R, E> = when (this) {
        is Ok -> res
        is Err -> Err(this.err)
    }

    /**
     * Calls [f] if the result is [Ok], otherwise returns the [Err] value of this.
     *
     * This function can be used for control flow based on [Result] values.
     */
    fun <R> andThen(f: (T) -> Result<R, E>): Result<R, E> = when (this) {
        is Ok -> f(this.t)
        is Err -> Err(this.err)
    }

    /**
     * Returns [res] if the result is [Err], otherwise returns the [Ok] value of this.
     *
     * Arguments passed to [or] are eagerly evaluated; if you are passing the result of a function call,
     * it is recommended to use [orElse], which is lazily evaluated.
     */
    fun <F> or(res: Result<T, F>): Result<T, F> = when (this) {
        is Ok -> Ok(this.t)
        is Err -> res
    }

    /**
     * Calls [f] if the result is [Err], otherwise returns the [Ok] value of this.
     *
     * This function can be used for control flow based on result values.
     */
    fun <F> orElse(f: (E) -> Result<T, F>): Result<T, F> = when (this) {
        is Ok -> Ok(this.t)
        is Err -> f(this.err)
    }


    /**
     * Returns the contained [Ok] value or a provided [default].
     *
     * Arguments passed to [unwrapOr] are eagerly evaluated; if you are passing the result of a function call,
     * it is recommended to use [unwrapOrElse], which is lazily evaluated.
     */
    fun unwrapOr(default: T): T = when (this) {
        is Ok -> this.t
        is Err -> default
    }

    /**
     * Returns the contained [Ok] value or computes it from a closure.
     */
    fun unwrapOrElse(f: (E) -> T): T = when (this) {
        is Ok -> this.t
        is Err -> f(this.err)
    }
}

/**
 * Contains the success value
 */
data class Ok<T, E>(val t: T) : Result<T, E>()

/**
 * Contains the error value
 */
data class Err<T, E>(val err: E) : Result<T, E>()
