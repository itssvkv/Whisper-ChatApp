package com.itssvkv.chatapp.ui.roomchatimport android.util.Logimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport androidx.lifecycle.viewModelScopeimport com.google.firebase.Timestampimport com.google.firebase.firestore.QuerySnapshotimport com.google.firebase.firestore.SetOptionsimport com.itssvkv.chatapp.data.local.repository.FirebaseRepositoryimport com.itssvkv.chatapp.models.ChatMessageimport com.itssvkv.chatapp.models.ChatRoomimport com.itssvkv.chatapp.utils.Common.TAGimport dagger.hilt.android.lifecycle.HiltViewModelimport kotlinx.coroutines.launchimport java.util.UUIDimport javax.inject.Inject@HiltViewModelclass RoomChatViewModel @Inject constructor(    private val firebaseRepository: FirebaseRepository) : ViewModel() {    private var _chatRoomModel: ChatRoom? = null    val chatRoomModel: ChatRoom?        get() = _chatRoomModel    private var _chatMessageModel: ChatMessage? = null    val chatMessageModel: ChatMessage?        get() = _chatMessageModel    private var firstUserId: String = ""    private var secondUserId: String = ""    var clearEditText: (() -> Unit)? = null    private var _query = MutableLiveData<QuerySnapshot>()    val query: LiveData<QuerySnapshot>        get() = _query    init {        viewModelScope.launch {            this@RoomChatViewModel.firstUserId = firebaseRepository.currentUserId()!!        }    }    suspend fun getOrCreateChatRoom(chatRoomId: String) {        Log.d(TAG, "getOrCreateChatRoom: $firstUserId")        firebaseRepository.getChatRoomReference(chatRoomId = chatRoomId).get()            .addOnCompleteListener { task ->                Log.d(TAG, "getOrCreateChatRoom: Succ")                if (task.isSuccessful) {                    _chatRoomModel = task.result.toObject(ChatRoom::class.java)                    Log.d(TAG, "getOrCreateChatRoom: ${_chatRoomModel?.userIds}")                    if (_chatRoomModel == null) {                        _chatRoomModel =                            ChatRoom(                                chatRoomId = chatRoomId,                                userIds = listOf(                                    firstUserId,                                    secondUserId                                ),                                lastMessageTimestamp = Timestamp.now(),                                lastMessageSenderId = ""                            )                        viewModelScope.launch {                            firebaseRepository.getChatRoomReference(chatRoomId = chatRoomId)                                .set(_chatRoomModel!!)                        }                    }                }            }    }    suspend fun getChatRoomId(secondUserId: String): String {        this.secondUserId = secondUserId        return firebaseRepository.getChatRoomId(            firstUserId = firebaseRepository.currentUserId()!!,            secondUserId = secondUserId        )    }    suspend fun sendMessageToUser(userMessage: String, chatRoomId: String) {        viewModelScope.launch {            _chatRoomModel?.lastMessageTimestamp = Timestamp.now()            _chatRoomModel?.lastMessageSenderId = firebaseRepository.currentUserId()!!            _chatRoomModel?.lastMessage = userMessage            firebaseRepository                .getChatRoomReference(chatRoomId = chatRoomId)                .set(_chatRoomModel!!, SetOptions.merge())            _chatMessageModel = ChatMessage(                message = userMessage,                senderId = firebaseRepository.currentUserId()!!,                timestamp = Timestamp.now(),                id = UUID.randomUUID().toString()            )            firebaseRepository.getChatRoomMessageReference(chatRoomId = chatRoomId)                .add(_chatMessageModel!!)                .addOnCompleteListener { task ->                    if (task.isSuccessful) {                        clearEditText?.invoke()                    }                }        }    }    suspend fun getAllMessageOfTwoUsers(chatRoomId: String) =        firebaseRepository.getAllMessageOfTwoUsers(chatRoomId).addSnapshotListener { value, _ ->            _query.postValue(value)        }}