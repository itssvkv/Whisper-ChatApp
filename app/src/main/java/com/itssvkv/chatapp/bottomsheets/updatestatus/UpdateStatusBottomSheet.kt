package com.itssvkv.chatapp.bottomsheets.updatestatusimport android.os.Bundleimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport com.google.android.material.bottomsheet.BottomSheetDialogFragmentimport com.itssvkv.chatapp.Rimport com.itssvkv.chatapp.databinding.UpdateStatusBottomSheetBindingimport com.itssvkv.chatapp.models.UserDataInfoimport dagger.hilt.android.AndroidEntryPointimport javax.inject.Inject@AndroidEntryPointclass UpdateStatusBottomSheet : BottomSheetDialogFragment() {    private var _binding: UpdateStatusBottomSheetBinding? = null    private val binding get() = _binding!!    private var currentUserInfo: UserDataInfo? = null    @Inject    lateinit var bundle: Bundle    init {        setStyle(STYLE_NORMAL, R.style.DialogStyle)    }    override fun onCreateView(        inflater: LayoutInflater,        container: ViewGroup?,        savedInstanceState: Bundle?    ): View? {        _binding = UpdateStatusBottomSheetBinding.inflate(inflater, container, false)        currentUserInfo = bundle.getSerializable("currentUserInfo") as UserDataInfo?        init()        return _binding?.root    }    private fun init() {        setupUserInfo()        openKeyboard()        initClicks()    }    private fun initClicks() {        binding.cancel.setOnClickListener {            this@UpdateStatusBottomSheet.dismiss()        }    }    private fun setupUserInfo() {        binding.enterAboutEt.setText(currentUserInfo?.name as String)    }    private fun openKeyboard() {        binding.enterAboutEt.requestFocus()    }}