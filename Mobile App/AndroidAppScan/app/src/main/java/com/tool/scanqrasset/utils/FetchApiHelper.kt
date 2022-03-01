package com.tool.scanqrasset.utils

import android.widget.Toast
import com.tool.scanqrasset.MainApplication
import com.tool.scanqrasset.api.UserService
import com.tool.scanqrasset.inf.FetchApiAction
import kotlinx.coroutines.*

class FetchApiHelper {
    private object Holder {
        val INSTANCE = FetchApiHelper()
    }

    companion object {
        @JvmStatic
        fun getInstance(): FetchApiHelper {
            return Holder.INSTANCE
        }

        private val fetchJob = SupervisorJob()
        private val userService: UserService = UserService()

//        val errorHandler = CoroutineExceptionHandler { _, throwable ->
//            Toast.makeText(
//                MainApplication.getInstance().applicationContext,
//                throwable.message,
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }

        val scope = CoroutineScope(fetchJob + Dispatchers.Main)
    }

    fun fetchAssetsApi(id: String, fetchApiAction: FetchApiAction, errorHandler:CoroutineExceptionHandler) {
        scope.launch(errorHandler) {
            val asset = userService.getAsset(id)

            // do something
            fetchApiAction.doAction(asset)
        }
    }


}