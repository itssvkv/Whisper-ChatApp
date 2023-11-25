package com.itssvkv.chatapp.bottomsheetsimport android.content.Contextimport android.net.Uriimport android.util.Logimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport androidx.lifecycle.viewModelScopeimport com.google.firebase.storage.StorageReferenceimport com.itssvkv.chatapp.data.local.repository.FirebaseRepositoryimport com.itssvkv.chatapp.data.local.repository.SharedPrefRepositoryimport com.itssvkv.chatapp.models.UserDataInfoimport com.itssvkv.chatapp.utils.Common.TAGimport com.itssvkv.chatapp.utils.sharedpref.SharedPrefCommon.CURRENT_USER_IDimport com.itssvkv.chatapp.utils.sharedpref.SharedPrefCommon.IS_USERimport dagger.hilt.android.lifecycle.HiltViewModelimport kotlinx.coroutines.launchimport javax.inject.Inject@HiltViewModelclass CreateUserSheetViewModel @Inject constructor(    private val repo: SharedPrefRepository,    private val storageReference: StorageReference,    private val firebaseRepository: FirebaseRepository) : ViewModel() {    private val _setInProgress = MutableLiveData<Boolean>()    val setInProgress: LiveData<Boolean>        get() = _setInProgress    private val _imageUriLiveData = MutableLiveData<Uri>()    val imageUriLiveData: LiveData<Uri>        get() = _imageUriLiveData    var dismissBottomSheet: (() -> Unit)? = null    var goToNextFragment: (() -> Unit)? = null    suspend fun uploadImage(imageUri: Uri) {        val imageRef = storageReference.child(firebaseRepository.currentUserId()!!)        imageRef.putFile(imageUri).addOnSuccessListener {            imageRef.downloadUrl.addOnSuccessListener { storageUri ->                _imageUriLiveData.postValue(storageUri)                Log.d(TAG, "uploadImage: $_imageUriLiveData")            }        }    }    suspend fun setDataToFirebase(context: Context, userInfo: UserDataInfo) {        _setInProgress.postValue(true)        firebaseRepository.currentUserDetails().set(userInfo).addOnCompleteListener { task ->            _setInProgress.postValue(false)            if (task.isSuccessful) {                viewModelScope.launch {                    repo.saveToPref(context = context, key = IS_USER, value = true)                    repo.saveToPref(                        context = context,                        key = CURRENT_USER_ID,                        value = userInfo.id                    )                }                dismissBottomSheet?.invoke()                goToNextFragment?.invoke()            }        }    }    fun getUserId() =        firebaseRepository.currentUserId()}