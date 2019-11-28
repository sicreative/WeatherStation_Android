package sc.azsphere.weatherstation

import android.net.NetworkInfo


interface JsonCallBack<T> {

    var jsonReading:Boolean


    fun startHttpReading()

    /**
     * Indicates that the callback handler needs to update its appearance or information based on
     * the result of the task. Expected to be called from the main thread.
     */
    fun jsonDecording(result: T?)



    /**
     * Indicates that the download operation has finished. This method is called even if the
     * download hasn't completed successfully.
     */
    fun finishJsonParsing()
}