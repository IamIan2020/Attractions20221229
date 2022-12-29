package com.taipei.attractions.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taipei.attractions.AppConfig
import com.taipei.attractions.SingleLiveEvent
import com.taipei.attractions.model.AttractionsAllModel
import com.taipei.attractions.network.NetworkResult
import kotlinx.coroutines.*
import retrofit2.HttpException

class CommentsViewModel : ViewModel() {
    var job: Job? = null

    var attractionAllResponse = SingleLiveEvent<NetworkResult<AttractionsAllModel>>()


//    init {
//        getNewAttractionsAll1("zh-tw", 1)
//    }

    fun getNewAttractionsAll1(language: String, pageIndex: Int) {

        attractionAllResponse.postValue(NetworkResult.Loading(true))
        job = CoroutineScope(Dispatchers.IO).launch {
            try{
                val comment = AppConfig.getApi().getAttractionsAll(language, pageIndex)
                withContext(Dispatchers.Main) {
                    delay(300L)
                    attractionAllResponse.postValue(NetworkResult.Success(comment))

                }
            }catch (e: HttpException){

                attractionAllResponse.postValue(NetworkResult.Failure(e.message ?: "Unknown Error"))
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        viewModelScope.cancel()
    }
}