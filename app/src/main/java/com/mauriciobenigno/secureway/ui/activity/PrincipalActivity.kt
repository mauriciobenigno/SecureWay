package com.mauriciobenigno.secureway.ui.activity

import android.R.attr
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.ui.MapViewFragment
import com.mauriciobenigno.secureway.ui.activity.autenticacao.AutenticacaoActivity
import com.mauriciobenigno.secureway.ui.fragment.creditos.CreditosFragment
import com.mauriciobenigno.secureway.ui.fragment.faq.FaqFragment
import com.mauriciobenigno.secureway.ui.fragment.report.ReportListFragment
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.IOException


class PrincipalActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var REQUEST_REPORT_CREATE = 998
        var REQUEST_REPORT_EDIT = 999
        private val REQUEST_IMAGE_CAPTURE = 1000
        private val REQUEST_TAKE_PHOTO = 1001
    }

    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var fragmentMap: MapViewFragment? = null
    private var fragmentAtual: Fragment? = null

    // Drawer
    private var ll_logado: LinearLayout? = null
    private var ll_deslogado: LinearLayout? = null
    private var img_perfil: ImageView? = null
    private var tv_drawer_apelido: TextView? = null
    private var tv_drawer_numero: TextView? = null

    // URI para foto de perfil
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout!!.addDrawerListener(toggle)

        toggle.syncState()

        navigationView = findViewById(R.id.navView)
        navigationView!!.setNavigationItemSelectedListener(this)

        val header: View = navigationView!!.getHeaderView(0)
        ll_logado = header.findViewById<View>(R.id.ll_drawer_info_logado) as LinearLayout
        ll_deslogado = header.findViewById<View>(R.id.ll_drawer_info_deslogado) as LinearLayout

        ll_deslogado?.setOnClickListener {
            val intent = Intent(this, AutenticacaoActivity::class.java)
            startActivity(intent)
        }

        img_perfil = header.findViewById<View>(R.id.img_perfil) as ImageView
        tv_drawer_apelido = header.findViewById<View>(R.id.tv_drawer_apelido) as TextView
        tv_drawer_numero = header.findViewById<View>(R.id.tv_drawer_numero) as TextView

        img_perfil?.clipToOutline = true
        img_perfil?.setRotation(90F)

        img_perfil?.setOnClickListener {
            capturarFoto()
        }

        // Inicializar Framents
        fragmentMap = MapViewFragment()
        fragmentAtual = fragmentMap


        if (fragmentAtual != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame, fragmentAtual!!)
            ft.commit()
        }
    }

    private fun configurarContaDrawer(){
        if(Firebase.auth.currentUser != null){
            ll_logado?.visibility  = View.VISIBLE
            ll_deslogado?.visibility  = View.GONE
            tv_drawer_apelido!!.text = Firebase.auth.currentUser!!.displayName
            tv_drawer_numero!!.text = Firebase.auth.currentUser!!.phoneNumber

            if(Firebase.auth.currentUser!!.photoUrl != null && Firebase.auth.currentUser!!.photoUrl.toString().isNotEmpty() ){;
                CarregarFoto()
            }
        } else {
            ll_logado?.visibility  = View.GONE
            ll_deslogado?.visibility  = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        configurarContaDrawer()

        if(fragmentAtual is ReportListFragment){
            fragmentAtual = ReportListFragment()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame, fragmentAtual!!)
            ft.commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_map -> {
                fragmentAtual = fragmentMap
                Toast.makeText(this, "Menu 1 - mapa", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_item_reports -> {
                fragmentAtual = ReportListFragment()
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.replace(R.id.container_frame, fragmentAtual!!)
                ft.commit()
            }
            R.id.nav_item_faq -> {
                fragmentAtual = FaqFragment()
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.replace(R.id.container_frame, fragmentAtual!!)
                ft.commit()
            }
            R.id.nav_item_info -> {
                fragmentAtual = CreditosFragment()
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.replace(R.id.container_frame, fragmentAtual!!)
                ft.commit()
            }
            R.id.nav_item_sair_conta -> {
                if(Firebase.auth.currentUser != null){
                    Firebase.auth.signOut()
                    configurarContaDrawer()
                    Toast.makeText(this, "Saindo da conta", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Você não está logado!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_item_sair -> {
                exibirDialogSaida()
            }
            else -> {
                Toast.makeText(this, "Menu default", Toast.LENGTH_SHORT).show()
            }
        }
        if (fragmentAtual != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame, fragmentAtual!!)
            ft.commit()
        }
        drawerLayout?.closeDrawer(GravityCompat.START)

        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === REQUEST_REPORT_CREATE && resultCode === RESULT_OK) {
            (fragmentAtual as MapViewFragment).loadHeatMap(false)
        }
        else  if (requestCode === REQUEST_REPORT_EDIT && resultCode === RESULT_OK) {
            (fragmentAtual as ReportListFragment).configurarAdapter()
        }
        else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            if(currentPhotoPath != null && currentPhotoPath.isNotEmpty()){
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(currentPhotoPath))
                    .build()
                Firebase.auth.currentUser!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            CarregarFoto()
                        }
                        else{
                            Toast.makeText(this, "Ocorreu um erro ao salvar foto no perfil", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            exibirDialogSaida()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun exibirDialogSaida(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Você deseja sair do aplicativo?")
            .setNegativeButton("Não", null)
            .setPositiveButton("Sim") { _, _ ->
                super.onBackPressed()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun capturarFoto() {
        try{
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    val photoFile: File? = try {
                        gravarFoto()
                    } catch (ex: IOException) {
                        null
                    }

                    currentPhotoPath = photoFile?.absolutePath.toString()
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(this, "${this.application.packageName}.fileprovider", it)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }
        }
        catch (e: Exception){
            Log.e("FotoPerfil", e.message!!)
        }

    }

    private fun gravarFoto(): File {
        return File.createTempFile("perfilSW", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            .apply {
                currentPhotoPath = absolutePath
            }
    }


    private fun CarregarFoto() {
        doAsync{
            try{
                val targetW = 640
                val targetH = 480

                val bmOptions = BitmapFactory.Options().apply {
                    // Get the dimensions of the bitmap
                    inJustDecodeBounds = true

                    BitmapFactory.decodeFile(Firebase.auth.currentUser!!.photoUrl!!.toString(), this)

                    val photoW: Int = outWidth
                    val photoH: Int = outHeight

                    // Determine how much to scale down the image
                    val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

                    // Decode the image file into a Bitmap sized to fill the View
                    inJustDecodeBounds = false
                    inSampleSize = scaleFactor
                    inPurgeable = true
                }
                BitmapFactory.decodeFile(Firebase.auth.currentUser!!.photoUrl!!.toString(), bmOptions)?.also { bitmap ->
                    uiThread {
                        img_perfil!!.setImageBitmap(bitmap)
                    }
                }
            }catch (e: Exception){
                Log.e("FotoPerfil - carregar ", e.message!!)
            }
        }

    }
}