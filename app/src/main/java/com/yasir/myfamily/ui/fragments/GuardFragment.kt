package com.yasir.myfamily.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfamily.databinding.FragmentGuardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yasir.myfamily.ui.adapters.InviteMailAdapter


class GuardFragment : Fragment(),InviteMailAdapter.OnActionClick {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private lateinit var binding: FragmentGuardBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuardBinding.inflate(inflater, container, false)

        binding.sendInvite.setOnClickListener {
            sendInvite()
        }

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInvites()
    }

    private fun getInvites() {
        val firestore = Firebase.firestore
        firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .collection("Invites").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val list: ArrayList<String> = ArrayList()
                    for (item in it.result) {
                        if (item.get("invite_status") == 0L) {
                            list.add(item.id)
                        }
                    }

                    Log.d("invite89", "getInvites: $list")

                    val adapter = InviteMailAdapter(list, this)
                    binding.inviteRecycler.layoutManager = LinearLayoutManager(mContext)
                    binding.inviteRecycler.adapter = adapter

                }
            }
    }
        private fun sendInvite() {
            val mail = binding.inviteMail.text.toString()

            val firestore = Firebase.firestore

            val data = hashMapOf(
                "InviteStatus" to 0
            )
            val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

            firestore.collection("users")
                .document(mail)
                .collection("invites")
                .document(senderMail).set(data)
                .addOnSuccessListener {

                }.addOnFailureListener {

                }
        }

    override fun onAcceptClick(mail: String) {
        val firestore = Firebase.firestore

        val data = hashMapOf(
            "InviteStatus" to 1
        )
        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    override fun onDenyClick(mail: String) {
        val firestore = Firebase.firestore

        val data = hashMapOf(
            "InviteStatus" to -1
        )
        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }


}