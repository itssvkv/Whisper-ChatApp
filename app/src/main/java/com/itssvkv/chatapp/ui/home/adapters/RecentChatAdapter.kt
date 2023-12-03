package com.itssvkv.chatapp.ui.home.adaptersimport android.annotation.SuppressLintimport android.util.Logimport android.view.LayoutInflaterimport android.view.ViewGroupimport androidx.recyclerview.widget.DiffUtilimport androidx.recyclerview.widget.ListAdapterimport androidx.recyclerview.widget.RecyclerViewimport com.bumptech.glide.Glideimport com.itssvkv.chatapp.data.local.repository.FirebaseRepositoryimport com.itssvkv.chatapp.databinding.RecentChatRecyclerBindingimport com.itssvkv.chatapp.models.ChatRoomimport com.itssvkv.chatapp.models.UserDataInfoimport com.itssvkv.chatapp.utils.Common.TAGimport javax.inject.Injectclass RecentChatAdapter @Inject constructor(    private val firebaseRepository: FirebaseRepository) : ListAdapter<ChatRoom, RecentChatAdapter.RecentChatViewHolder>(    Comparator) {    var onChatClickedListener: ((UserDataInfo) -> Unit)? = null    private var otherUserInfo: UserDataInfo? = null    inner class RecentChatViewHolder(val binding: RecentChatRecyclerBinding) :        RecyclerView.ViewHolder(binding.root) {        @SuppressLint("SetTextI18n")        fun bind(item: ChatRoom) {            firebaseRepository.getOtherUserFromChatRoom(item.userIds!!).get()                .addOnSuccessListener {                    Log.d(TAG, "bind: $it")                    if (item.lastMessageSenderId == firebaseRepository.currentUserId()) {                        binding.lastMessage.text = "You: ${item.lastMessage}"                    } else {                        binding.lastMessage.text = item.lastMessage                    }                    otherUserInfo = it.toObject(UserDataInfo::class.java)                    binding.usernameTv.text = otherUserInfo?.name                    Glide.with(binding.userPhotoIv.context).load(otherUserInfo?.profilePhoto)                        .into(binding.userPhotoIv)                    binding.lastMessageTime.text =                        firebaseRepository.changeTimestampToString(item.lastMessageTimestamp)                }        }    }    private object Comparator : DiffUtil.ItemCallback<ChatRoom>() {        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {            return oldItem == newItem        }        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {            return oldItem == newItem        }    }    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentChatViewHolder {        val binding =            RecentChatRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)        return RecentChatViewHolder(binding)    }    override fun onBindViewHolder(holder: RecentChatViewHolder, position: Int) {        val oneChat = getItem(position)        oneChat?.let {            holder.bind(it)        }        holder.itemView.setOnClickListener {            onChatClickedListener?.invoke(otherUserInfo!!)        }    }}