package com.lastaoutdoor.lasta.data

import android.app.Activity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.util.concurrent.Executor

class MockTask<T : Any> : Task<T>() {
  override fun addOnFailureListener(p0: OnFailureListener): Task<T> {
    return this
  }

  override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<T> {
    return this
  }

  override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<T> {
    return this
  }

  override fun getException(): Exception? {
    return null
  }

  override fun getResult(): T {
    return this.getResult()
  }

  override fun <X : Throwable?> getResult(p0: Class<X>): T {
    return this.getResult()
  }

  override fun isCanceled(): Boolean {
    return false
  }

  override fun isComplete(): Boolean {
    return true
  }

  override fun isSuccessful(): Boolean {
    return true
  }

  override fun addOnSuccessListener(p0: Executor, p1: OnSuccessListener<in T>): Task<T> {
    return this
  }

  override fun addOnSuccessListener(p0: Activity, p1: OnSuccessListener<in T>): Task<T> {
    return this
  }

  override fun addOnSuccessListener(p0: OnSuccessListener<in T>): Task<T> {
    return this
  }

  fun await(): T {
    return getResult()
  }
}
