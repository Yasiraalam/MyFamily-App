package com.yasir.myfamily.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfamily.R
import com.example.myfamily.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.yasir.myfamily.constants.Constants
import com.yasir.myfamily.sharedPrefrences.SharedPref
import com.yasir.myfamily.ui.adapters.InviteAdapter
import com.yasir.myfamily.ui.adapters.MemberAdapter
import com.yasir.myfamily.ui.dataClasses.ContactModel
import com.yasir.myfamily.ui.dataClasses.MemberModel
import com.yasir.myfamily.ui.databaseClass.MyFamilyDatabase
import com.yasir.myfamily.ui.logIn.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



@SuppressLint("ResourceType")
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var inviteAdapter: InviteAdapter
    lateinit var mContext:Context
    private val listContacts :ArrayList<ContactModel> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    //upper items family
        val listMember = listOf<MemberModel>(
            MemberModel("yasir", "hyderpora", "67%", "122"),
            MemberModel("kabir", "bandipora", "88%", "234"),
            MemberModel("Faisal", "Anatnag", "97%", "990"),
            MemberModel("huzaif", "kulgam", "30%", "100"),
            MemberModel("Rehan", "Srinagar", "18%", "10")
        )
        val adapter = MemberAdapter(listMember)

        binding.recyclerMember.layoutManager = LinearLayoutManager(mContext)
        binding.recyclerMember.adapter = adapter
        //DB

        inviteAdapter = InviteAdapter( listContacts)
        fetchDatabaseContacts()
        //coroutine apply and storing fetched contacts into db
        CoroutineScope(Dispatchers.IO).launch {
            insertDatabaseContacts(fetchContacts())
        }

        binding.recyclerInvite.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerInvite.adapter = inviteAdapter

        //logout logic
        binding.iconThreeDots.setOnClickListener {
            SharedPref.putBoolean(Constants.IS_USER_LOGED_IN,false)
//            FirebaseAuth.getInstance().signOut()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            activity?.let { it1 ->
                GoogleSignIn.getClient(it1,gso).signOut()
            }
            startActivity(Intent(activity,LoginActivity::class.java))

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private  fun fetchDatabaseContacts() {
        val database = MyFamilyDatabase.getDatabase(mContext)
       database.contactDao().getAllContacts().observe(viewLifecycleOwner){
           listContacts.clear()
           listContacts.addAll(it)
           inviteAdapter.notifyDataSetChanged()
       }
    }

    //fetching from database contacts
    @SuppressLint("SuspiciousIndentation")
    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {
       val database = MyFamilyDatabase.getDatabase(mContext)
        database.contactDao().insertAll(listContacts)
    }

    private fun fetchContacts(): ArrayList<ContactModel> {
       val contentResolver = requireActivity().contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
        val listContacts :ArrayList<ContactModel> = ArrayList()
        if(cursor!=null && cursor.count >0){
            while (cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if(hasPhoneNumber >0){
                    val phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",
                        arrayOf(id),
                        ""

                        )
                    if(phoneCursor!=null && phoneCursor.count>0){
                            while (phoneCursor.moveToNext()){
                                val phoneNum = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                listContacts.add(ContactModel(name,phoneNum))
                            }
                        phoneCursor.close()
                    }
                }
            }
            cursor.close()
        }
   return listContacts
    }
}

