package com.example.gamecompanion.fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gamecompanion.R
import com.example.gamecompanion.activity.RegisterActivity
import com.example.gamecompanion.models.UserModel
import com.example.gamecompanion.utils.COLECTION_USERS
import com.example.gamecompanion.utils.SharedPreferencesManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.io.File


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

    override fun onResume(){
        super.onResume()
        initUI()
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
            showUser()
            takePicture.setOnClickListener {
                takePicture()
            }
        }

        //else: show profile
    }
    private fun showUser(){

        SharedPreferencesManager().getUsername(requireContext())
            ?.let { username ->
            }
            ?:run{

            }

        val username = SharedPreferencesManager().getUsername(requireContext())

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?:""

        FirebaseFirestore.getInstance()
            .collection(COLECTION_USERS)
            .document(userId)
            .get()
            .addOnSuccessListener {documentSnapshot ->
                val userProfile = documentSnapshot.toObject(UserModel::class.java)
                usernameTextView.text=userProfile?.username
                SharedPreferencesManager().setUsername(requireContext(),userProfile?.username)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }

    private fun takePicture(){
        val imageIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageIntent.resolveActivity(requireActivity().packageManager)
        startActivityForResult(imageIntent,1)

    }

    private fun createImageFile(){
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.toString() + "/" + "avatarImage.jpeg")
    }

    private fun saveImageToCloud(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        val storageReference = FirebaseStorage.getInstance().reference
        val avatarsFoldersReference = storageReference.child("public/avatars/")
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val timestamp = System.currentTimeMillis()
        val avatarImageReference = avatarsFoldersReference.child("avatar_${userId}_${timestamp}.jpeg")

        avatarImageReference.putBytes(imageBytes)
            .addOnSuccessListener {
                avatarImageReference.downloadUrl
                    .addOnSuccessListener {url->
                    Log.i("ProfileFragment", "Success uploading image $url")
                        FirebaseFirestore.getInstance()
                            .collection(COLECTION_USERS)
                            .document(userId)
                            .update("avatarUrl",url.toString())
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener{

                            }
                    }
            }
            .addOnFailureListener {
                Log.w("ProfileFragment", "Error uploading image")
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageBitmap?.let {
                avatar.setImageBitmap(it)
                saveImageToCloud(it)
            }
        }
    }
}
