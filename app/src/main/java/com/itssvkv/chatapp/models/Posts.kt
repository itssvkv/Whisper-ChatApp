package com.itssvkv.chatapp.modelsimport com.google.firebase.Timestampdata class Posts(    val postId: String = "",    val caption: String = "",    val postPhoto: String = "",    val userWhoCreateThePost: UserDataInfo? = null,    val timestamp: Timestamp? =null)