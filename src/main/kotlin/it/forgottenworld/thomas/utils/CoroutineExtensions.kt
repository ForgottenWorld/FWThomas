package it.forgottenworld.thomas.utils

import com.github.shynixn.mccoroutine.asyncDispatcher
import com.github.shynixn.mccoroutine.launch
import com.github.shynixn.mccoroutine.launchAsync
import com.github.shynixn.mccoroutine.minecraftDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun launch(f: suspend CoroutineScope.() -> Unit) = thomasPlugin.launch(f)

fun launchAsync(f: suspend CoroutineScope.() -> Unit) = thomasPlugin.launchAsync(f)

val Dispatchers.minecraft get() = thomasPlugin.minecraftDispatcher

val Dispatchers.async get() = thomasPlugin.asyncDispatcher