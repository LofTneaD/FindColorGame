package com.example.findcolorgame.data

import GameMode
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val firestore = Firebase.firestore
    private val resultsRef = firestore.collection("results")

    fun saveResult(result: PlayerResult) {
        resultsRef.add(result)
    }

    fun getTopResults(mode: GameMode): Flow<List<PlayerResult>> = callbackFlow {
        val listener = resultsRef
            .whereEqualTo("mode", mode.name)
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val list = snapshot?.documents
                    ?.mapNotNull { it.toObject(PlayerResult::class.java) }
                    ?: emptyList()
                Log.d(
                    "Firestore",
                    "fromCache = ${snapshot?.metadata?.isFromCache}"
                )
                trySend(list)
            }

        awaitClose { listener.remove() }
    }

    suspend fun clearAllResults() {
        val snapshot = firestore.collection("results").get().await()
        snapshot.documents.forEach { doc ->
            firestore.collection("results").document(doc.id).delete()
        }
    }
}