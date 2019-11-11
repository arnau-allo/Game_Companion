package com.example.gamecompanion


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initUI()
    }

    private fun initUI(){
        if(FirebaseAuth.getInstance().currentUser ==null){
            logoutButton.visibility = View.GONE
            registerButton.visibility = View.VISIBLE
            registerButton.setOnClickListener {
                startActivity(Intent(requireContext(), RegisterActivity::class.java))
            }
        }
        else{
            registerButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE

            logoutButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                initUI()
            }

        }
        registerButton.setOnClickListener {
            //TODO: Go to register
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
        }
        //else: show profile
    }

}
