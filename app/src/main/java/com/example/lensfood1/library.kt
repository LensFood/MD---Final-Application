package com.example.lensfood1


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lensfood1.databinding.FragmentLibraryBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class library : Fragment() {

    private lateinit var binding: FragmentLibraryBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val rootView = binding.root

        // Inisialisasi Google Sign-In Options dan GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Set onClickListener untuk tombol logout
        binding.logout.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                auth.signOut()
                val intent = Intent(activity, signin::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        return rootView
    }
}
