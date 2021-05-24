package com.spacex.mvvm

import android.app.Activity
import android.content.Context
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.spacex.application.MyApplication
import com.spacex.database.entity.Launch
import com.spacex.placeholder.JsonPlaceholder
import com.test.spacex.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class InitViewModel(private val app: MyApplication) : AndroidViewModel(app) {
    private val checkString = app.getString(R.string.shared_preference_check)
    private val preferences = "myPreferences"

    private var check:Boolean? =  loadCheckFromSharedPreference(app.applicationContext)

    private val errorMessageLive = MutableLiveData<String>()

    private var takenOnlyFromDatabase: Boolean = false

    fun saveDataToDbAsync(launches: List<Launch>){
        viewModelScope.launch(Dispatchers.IO) {
            run {
                app.database.getDao().insertLaunches(launches)
            }
        }
    }

    fun saveLastUpdateDateToPreferences(){
        val dateMillis : Long = System.currentTimeMillis()
        saveDateToSharedPreference(app, dateMillis)
    }

    private fun loadCheckFromSharedPreference(context: Context): Boolean? {
        val sharedPreference = context.getSharedPreferences(
            context.getString(R.string.shared_preference_time), Context.MODE_PRIVATE)
        return sharedPreference?.getBoolean(checkString, false)
    }

    fun saveCheckToSharedPreference(context: Context, check: Boolean){
        val sharedPreference = context.getSharedPreferences(preferences, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(checkString, check)
        editor.apply()
    }

    fun saveTimeToSharedPreference(context: Context?){
        val sharedPreference = context!!.getSharedPreferences(preferences, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putLong(context.getString(R.string.shared_preference_date_millis), System.currentTimeMillis())
        editor.apply()
    }

    private fun getDate(milliSeconds: Long, dateFormat: String): MutableLiveData<String> {
        val stringDateLiveData = MutableLiveData<String>()

        val formatter = SimpleDateFormat(dateFormat, Locale.US)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds

        stringDateLiveData.value = formatter.format(calendar.time)
        return stringDateLiveData
    }

    private fun saveDateToSharedPreference(context: Context, dateMillis : Long){
        val sharedPreference = context.getSharedPreferences(
            context.getString(R.string.shared_preference_time), Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putLong(context.getString(R.string.shared_preference_date_millis), dateMillis)
        editor.apply()
    }

    fun observeDataIdDB(): LiveData<List<Launch>> {
        return app.database.getDao().getLaunches()
    }

    fun getTakenFromDatabaseValue(): LiveData<Boolean> {
        val takenFromDatabaseValueLiveData = MutableLiveData<Boolean>()
        takenFromDatabaseValueLiveData.value = takenOnlyFromDatabase
        return takenFromDatabaseValueLiveData
    }

    fun saveLaunchesToDB(
        activity: Activity
    ){
        JsonPlaceholder
            .api
            .getLaunches()
            .enqueue(object : Callback, retrofit2.Callback<List<Launch>> {
                override fun onFailure(
                    call: retrofit2.Call<List<Launch>>,
                    t: Throwable
                ) {
                    takenOnlyFromDatabase = true

                    if(check!!) {
                        check = false
                        saveCheckToSharedPreference(activity, check!!)
                    }
                }

                override fun onResponse(
                    call: retrofit2.Call<List<Launch>>,
                    response: retrofit2.Response<List<Launch>>
                ) {
                    saveLastUpdateDateToPreferences()

                    val body : List<Launch>? = response.body()
                    val bodyNotNull : List<Launch> = body!!

                    saveDataToDbAsync(bodyNotNull)

                    takenOnlyFromDatabase = false

                    check =true
                    saveCheckToSharedPreference(activity, check!!)

                    saveTimeToSharedPreference(activity)
                }

                override fun onFailure(
                    call: Call,
                    e: IOException) {
                    errorMessageLive.value = e.message

                    takenOnlyFromDatabase = true

                    if(check!!) {
                        activity.findNavController(R.id.data_updated_time)
                            .navigate(R.id.errorFragment)
                        check = false
                        saveCheckToSharedPreference(activity, check!!)
                    }
                }

                override fun onResponse(
                    call: Call,
                    response: Response
                ) {
                    check = true
                    saveCheckToSharedPreference(activity, check!!)
                }
            })
    }

    fun loadTimeFromSharedPreference(context: Context?): MutableLiveData<String> {
        val sharedPreference = context?.getSharedPreferences(
            context.getString(R.string.shared_preference_time), Context.MODE_PRIVATE)
        val dateMillis : Long = sharedPreference?.getLong(context.getString(R.string.shared_preference_date_millis), 0)!!
        return getDate(dateMillis, context.getString(R.string.date_format))
    }
}
