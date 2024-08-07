/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package common

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * This reduce is a fancy function just to inform that the calculation process is involving some other elements.
 * This function will be beneficial in the debugging and refactoring process - giving clear information (visually) of it's dependencies.
 * Example:
 * ```
 *     val isABlackHole = reduce(
 *         mass,
 *         radius,
 *     ) { m, r ->
 *          val G = 6.6743e-11 // Gravitational constant in m^3/(kg*s^2)
 *          val c = 299792458 // Speed of light in m/s
 *          val SchwarzschildRadius = 2*G*m/(c*c) // BlackHole Radius
 *
 *          return r <= SchwarzschildRadius
 *     }
 * ```
 * In that example, we can see that the calculation process requires 2 elements: userSubmitIntentErrorMessage and agreeToTermAndAgreement.
 * When it used properly, the `bloc` body will not colored purple (indicating that the function is totally pure).
 */
@OptIn(ExperimentalContracts::class)
inline fun <T1, T2, R> reduce(
    e1: T1,
    e2: T2,
    bloc: (T1, T2) -> R,
): R {
    contract {
        callsInPlace(bloc, InvocationKind.EXACTLY_ONCE)
    }
    return bloc.invoke(e1, e2)
}

/**
 * This reduce is a fancy function just to inform that the calculation process is involving some other elements.
 * This function will be beneficial in the debugging and refactoring process - giving clear information (visually) of it's dependencies.
 * Example:
 * ```
 *     val isABlackHole = reduce(
 *         mass,
 *         radius,
 *     ) { m, r ->
 *          val G = 6.6743e-11 // Gravitational constant in m^3/(kg*s^2)
 *          val c = 299792458 // Speed of light in m/s
 *          val SchwarzschildRadius = 2*G*m/(c*c) // BlackHole Radius
 *
 *          return r <= SchwarzschildRadius
 *     }
 * ```
 * In that example, we can see that the calculation process requires 2 elements: userSubmitIntentErrorMessage and agreeToTermAndAgreement.
 * When it used properly, the `bloc` body will not colored purple (indicating that the function is totally pure).
 */
@OptIn(ExperimentalContracts::class)
inline fun <T1, T2, T3, R> reduce(
    e1: T1,
    e2: T2,
    e3: T3,
    bloc: (T1, T2, T3) -> R,
): R {
    contract {
        callsInPlace(bloc, InvocationKind.EXACTLY_ONCE)
    }
    return bloc.invoke(e1, e2, e3)
}

/**
 * This reduce is a fancy function just to inform that the calculation process is involving some other elements.
 * This function will be beneficial in the debugging and refactoring process - giving clear information (visually) of it's dependencies.
 * Example:
 * ```
 *     val isABlackHole = reduce(
 *         mass,
 *         radius,
 *     ) { m, r ->
 *          val G = 6.6743e-11 // Gravitational constant in m^3/(kg*s^2)
 *          val c = 299792458 // Speed of light in m/s
 *          val SchwarzschildRadius = 2*G*m/(c*c) // BlackHole Radius
 *
 *          return r <= SchwarzschildRadius
 *     }
 * ```
 * In that example, we can see that the calculation process requires 2 elements: userSubmitIntentErrorMessage and agreeToTermAndAgreement.
 * When it used properly, the `bloc` body will not colored purple (indicating that the function is totally pure).
 */
@OptIn(ExperimentalContracts::class)
inline fun <T1, T2, T3, T4, R> reduce(
    e1: T1,
    e2: T2,
    e3: T3,
    e4: T4,
    bloc: (T1, T2, T3, T4) -> R,
): R {
    contract {
        callsInPlace(bloc, InvocationKind.EXACTLY_ONCE)
    }
    return bloc.invoke(e1, e2, e3, e4)
}

/**
 * This reduce is a fancy function just to inform that the calculation process is involving some other elements.
 * This function will be beneficial in the debugging and refactoring process - giving clear information (visually) of it's dependencies.
 * Example:
 * ```
 *     val SingularityUniverse = reduce(
 *         mass,
 *         spaces,
 *         cosmologicalConstant,
 *         entropy,
 *         quantumFluctuation,
 *     ) { args ->
 *
 *         val d1 = args[0] as BigInteger
 *         val d2 = args[1] as BigInteger
 *         val d3 = args[2] as Double
 *         val d4 = args[3] as X
 *         val d5 = args[4] as Y
 *
 *         ....
 *     }
 * ```
 * In that example, we can see that the calculation process requires 2 elements: userSubmitIntentErrorMessage and agreeToTermAndAgreement.
 */
@OptIn(ExperimentalContracts::class)
inline fun <R> reduce(
    vararg entries: Any,
    bloc: (entries: List<Any>) -> R,
): R {
    contract {
        callsInPlace(bloc, InvocationKind.EXACTLY_ONCE)
    }
    return bloc.invoke(entries.toList())
}
