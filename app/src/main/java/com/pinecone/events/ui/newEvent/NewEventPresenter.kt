package com.pinecone.events.ui.newEvent

import com.google.firebase.auth.FirebaseAuth
import com.pinecone.events.service.EventService
import com.pinecone.events.service.PlacesService
import com.pinecone.events.util.DateUtil.createDate

class NewEventPresenter(val view: Contract.View) : Contract.Presenter {
    override var date: String? = null
    override var time: String? = null
    override var placeId: String? = null
    override fun getPlaces() {
        view.onPlacesLoaded(PlacesService.getPlaces())
    }

    override fun addEvent() {
        when (checkErrors(placeId, date, time)) {
            true -> {
                val dateTime = createDate("$date $time")
                view.onAddEvent(EventService.addEvent(FirebaseAuth.getInstance().currentUser!!.uid,
                        placeId!!, dateTime.millis))
            }
        }
    }

    override fun checkErrors(placeId: String?, date: String?, time: String?): Boolean {
        return when {
            (placeId.isNullOrEmpty()) -> {
                view.onMissingInfo("Informe o local")
                return false
            }
            (time.isNullOrEmpty()) -> {
                view.onMissingInfo("Informe o horário")
                return false
            }
            (date.isNullOrEmpty()) -> {
                view.onMissingInfo("Informe a data")
                return false
            }
            else -> true
        }
    }

    override fun onEventAdded() {
        view.onEventAdded()
    }

}