package com.itssvkv.chatapp.ui.createpostimport android.content.Contextimport android.net.Uriimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport androidx.lifecycle.viewModelScopeimport com.google.firebase.Timestampimport com.google.firebase.storage.StorageReferenceimport com.google.gson.Gsonimport com.itssvkv.chatapp.data.local.repository.FirebaseRepositoryimport com.itssvkv.chatapp.data.local.repository.SharedPrefRepositoryimport com.itssvkv.chatapp.models.Postsimport com.itssvkv.chatapp.models.UserDataInfoimport com.itssvkv.chatapp.utils.sharedpref.SharedPrefCommon.CURRENT_USER_INFOimport dagger.hilt.android.lifecycle.HiltViewModelimport kotlinx.coroutines.launchimport java.util.UUIDimport javax.inject.Inject@HiltViewModelclass CreatePostViewModel @Inject constructor(    private val sharedPrefRepository: SharedPrefRepository,    private val storageReference: StorageReference,    private val firebaseRepository: FirebaseRepository) : ViewModel() {    private val _currentUserInfoLiveData = MutableLiveData<UserDataInfo>()    val currentUserDataInfoLiveData: LiveData<UserDataInfo>        get() = _currentUserInfoLiveData    private val _imageUriLiveData = MutableLiveData<Uri>()    val imageUriLiveData: LiveData<Uri>        get() = _imageUriLiveData    private var posts = Posts()    var closeCreatePostFragment: (() -> Unit)? = null    var makeToast: (() -> Unit)? = null    fun getCurrentUserInfoFormSharedPref(context: Context) {        viewModelScope.launch {            val currentUserInfo = sharedPrefRepository.getFromPref(                context = context,                key = CURRENT_USER_INFO,                defValue = ""            ) as String            _currentUserInfoLiveData.postValue(                Gson().fromJson(currentUserInfo, UserDataInfo::class.java)            )        }    }    fun uploadImageToFirebase(image: Uri) {        val imageRef = storageReference.child(UUID.randomUUID().toString())        imageRef.putFile(image).addOnSuccessListener {            imageRef.downloadUrl.addOnSuccessListener {                _imageUriLiveData.postValue(it)            }        }    }    fun setPostDataToFirebase(caption: String) {        posts = posts.copy(            postId = UUID.randomUUID().toString(),            caption = caption,            postPhoto = _imageUriLiveData.value.toString(),            userWhoCreateThePost = _currentUserInfoLiveData.value,            timestamp = Timestamp.now()        )        viewModelScope.launch {            firebaseRepository.createPostOnFirebase().set(posts).addOnSuccessListener {                closeCreatePostFragment?.invoke()                makeToast?.invoke()            }        }    }}