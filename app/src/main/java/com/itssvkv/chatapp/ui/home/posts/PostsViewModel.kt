package com.itssvkv.chatapp.ui.home.postsimport android.content.Contextimport android.util.Logimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport androidx.lifecycle.viewModelScopeimport com.google.gson.Gsonimport com.itssvkv.chatapp.data.local.repository.FirebaseRepositoryimport com.itssvkv.chatapp.data.local.repository.SharedPrefRepositoryimport com.itssvkv.chatapp.models.UserDataInfoimport com.itssvkv.chatapp.utils.Common.TAGimport com.itssvkv.chatapp.utils.sharedpref.SharedPrefCommon.CURRENT_USER_INFOimport dagger.hilt.android.lifecycle.HiltViewModelimport kotlinx.coroutines.launchimport javax.inject.Inject@HiltViewModelclass PostsViewModel @Inject constructor(    private val firebaseRepository: FirebaseRepository,    private val sharedPrefRepository: SharedPrefRepository) : ViewModel() {    private val _currentUserDataInfo = MutableLiveData<UserDataInfo>()    val currentUserDataInfo: LiveData<UserDataInfo>        get() = _currentUserDataInfo    fun getCurrentUserInfo(context: Context) {        viewModelScope.launch {            firebaseRepository.currentUserDetails().get().addOnSuccessListener {                viewModelScope.launch {                    val userData = it.toObject(UserDataInfo::class.java)                    sharedPrefRepository.saveToPref(                        context = context,                        key = CURRENT_USER_INFO,                        value = Gson().toJson(userData)                    )                    Log.d(TAG, "getCurrentUserInfo: $userData")                }            }.addOnFailureListener {                Log.d(TAG, "getCurrentUserInfo: $it")            }        }        getCurrentUserFromSharedPref(context = context)    }    private fun getCurrentUserFromSharedPref(context: Context) {        viewModelScope.launch {            val currentUserInfo = sharedPrefRepository.getFromPref(                context = context,                key = CURRENT_USER_INFO,                defValue = ""            ) as String            _currentUserDataInfo.postValue(                Gson().fromJson(                    currentUserInfo,                    UserDataInfo::class.java                )            )        }    }}