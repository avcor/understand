package com.example.understand.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.understand.R

class FragmentLifecycleActivity : AppCompatActivity() {

    companion object {
        private const val TAG = com.example.understand.TAG + "Act"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        setContentView(R.layout.activity_fragment_lifecycle2)

        // Example 1: commit() - Standard asynchronous commit
        // - Executes asynchronously on the main thread (posted to message queue)
        // - WILL throw IllegalStateException if called after onSaveInstanceState()
        // - Safe to use in normal lifecycle methods (onCreate, onStart, onResume)
//        demonstrateCommit()

        // Example 2: commitNow() - Synchronous commit
        // - Executes IMMEDIATELY and synchronously
        // - WILL throw IllegalStateException if called after onSaveInstanceState()
        // - Use when you need the fragment transaction to complete before next line of code
        // - Cannot be used with addToBackStack() - will throw IllegalStateException
//         demonstrateCommitNow()

        // Example 3: commitAllowingStateLoss() - Commit with state loss allowed
        // - Executes asynchronously like commit()
        // - WILL NOT throw exception after onSaveInstanceState()
        // - Use when transaction is not critical and can be lost after state save
         demonstrateCommitAllowingStateLoss()
    }

    /**
     * COMMIT() - Asynchronous execution
     * The transaction is scheduled to execute but doesn't happen immediately
     */
    private fun demonstrateCommit() {
        Log.d(TAG, "--- demonstrateCommit() START ---")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Fragment1(), "fragment1")
            .addToBackStack("fragment1_transaction")
            .commit()

        Log.d(TAG, "commit() called - but Fragment1 may not be attached yet")

        // Fragment1 might not be attached yet at this point
        // because commit() is asynchronous
        val fragment = supportFragmentManager.findFragmentByTag("fragment1")
        Log.d(TAG, "Fragment1 immediately after commit(): ${if (fragment?.isAdded == true) "ADDED" else "NOT ADDED YET"}")

        Log.d(TAG, "--- demonstrateCommit() END ---")
    }

    /**
     * COMMITNOW() - Synchronous execution
     * The transaction executes immediately before the next line
     */
    private fun demonstrateCommitNow() {
        Log.d(TAG, "--- demonstrateCommitNow() START ---")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Fragment2(), "fragment2")
            // NOTE: Cannot use addToBackStack() with commitNow()!
            // .addToBackStack("fragment2_transaction") // This would throw IllegalStateException
            .commitNow()

        Log.d(TAG, "commitNow() called - Fragment2 WILL be attached now")

        // Fragment2 WILL be attached at this point because commitNow() is synchronous
        val fragment = supportFragmentManager.findFragmentByTag("fragment2")
        Log.d(TAG, "Fragment2 immediately after commitNow(): ${if (fragment?.isAdded == true) "ADDED" else "NOT ADDED"}")

        Log.d(TAG, "--- demonstrateCommitNow() END ---")
    }

    /**
     * COMMITALLOWINGSTATELOSS() - Asynchronous with state loss allowed
     * Use when the transaction is not critical and can be lost
     */
    private fun demonstrateCommitAllowingStateLoss() {
        Log.d(TAG, "--- demonstrateCommitAllowingStateLoss() START ---")

        // Simulating a delayed operation that might happen after onSaveInstanceState()
        // This is common in async callbacks (network requests, database queries, etc.)
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, Fragment1(), "fragment_delayed")
                    .commitAllowingStateLoss()

                Log.d(TAG, "commitAllowingStateLoss() succeeded - even if called after onSaveInstanceState()")
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
            }
        }, 5000) // 2 second delay

        Log.d(TAG, "--- demonstrateCommitAllowingStateLoss() END ---")
    }

    /**
     * Example showing the difference in a practical scenario
     */
    private fun practicalExample() {
        // Scenario 1: Normal fragment addition - use commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Fragment1())
            .addToBackStack(null)
            .commit() // Safe and standard

        // Scenario 2: Need to execute code that depends on fragment being added
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Fragment2())
            .commitNow() // Fragment2 is guaranteed to be added after this line

        // Now we can safely access Fragment2
        val fragment2 = supportFragmentManager.findFragmentById(R.id.fragment_container) as? Fragment2
        fragment2?.let {
            // Do something with fragment2
            Log.d(TAG, "Fragment2 is definitely added and accessible")
        }

        // Scenario 3: Async callback (e.g., from network request)
        // User might have navigated away, so we use commitAllowingStateLoss
        Handler(Looper.getMainLooper()).postDelayed({
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Fragment1())
                .commitAllowingStateLoss() // Won't crash if activity state already saved
        }, 3000)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")

        // Force execute any pending transactions
        supportFragmentManager.executePendingTransactions()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState - after this, commit() and commitNow() will throw exception")

        // After this point:
        // - commit() -> IllegalStateException ❌
        // - commitNow() -> IllegalStateException ❌
        // - commitAllowingStateLoss() -> Works fine ✓
    }
}
