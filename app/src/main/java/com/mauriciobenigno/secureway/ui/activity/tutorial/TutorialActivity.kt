package com.mauriciobenigno.secureway.ui.activity.tutorial

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.ui.activity.PrincipalActivity
import com.mauriciobenigno.secureway.util.Preferences
import java.lang.StringBuilder

@RequiresApi(Build.VERSION_CODES.M)
class TutorialActivity : AppIntro2()  {


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura Slides

        slideInicial()

        slideExplicacao()

        slidePermissao()

        slideFinalizar()

        // configura permissão necessária
        askForPermissions(permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), slideNumber = 3, required = true)

        // configura esquema de cores e status bar
        showStatusBar(true)
        setStatusBarColor(Color.parseColor(this.getString(R.color.colorPrimaryDark)))
        setStatusBarColorRes(R.color.colorPrimaryDark)
        isWizardMode = true
        isSystemBackButtonLocked = true

    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        Preferences(this).completouTutorial = true
        val intent = Intent(this, PrincipalActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun slideInicial() {
        val texto = StringBuilder()
        texto.append("SecureWay foi desenvolvido como uma plataforma colaborativa")
        texto.append(" onde as pessoas ajudam umas as outras, fornecendo informações")
        texto.append(" sobre a segurança de diversas localidades.")

        addSlide(AppIntroFragment.newInstance(
            title = "Olá, bem vindo ao SecureWay",
            description = texto.toString() ,
            imageDrawable = R.drawable.image_shield,
            titleColor = Color.WHITE,
            descriptionColor = Color.WHITE,
            backgroundColor = getColor(R.color.colorPrimary),
        ))
    }

    private fun slideExplicacao(){
        val texto = StringBuilder()
        texto.append("Saber informações com antecedencia pode ajudar a escolher um lugar")
        texto.append(" mais seguro para morar, colocar crianças na escola e afins.")
        texto.append(" Também é possível colaborar, fornecendo informaçoes para ajudar outras pessoas.")

        addSlide(AppIntroFragment.newInstance(
            title = "Por que?",
            description = texto,
            imageDrawable = R.drawable.ponto_interrogacao,
            titleColor = Color.WHITE,
            descriptionColor = Color.WHITE,
            backgroundColor = getColor(R.color.colorPrimary),
        ))
    }

    private fun slidePermissao(){
        val texto = StringBuilder()
        texto.append("Para garantir o funcionamento correto do SecureWay ")
        texto.append("é necessário fornecer permissão de acesso a localização. ")
        texto.append("Com essa permissão o aplicativo pode identificar sua localização ")
        texto.append("durante o uso e fornecer informações de sua localidade.")

        addSlide(AppIntroFragment.newInstance(
            title = "Acesso ao GPS",
            description = texto,
            imageDrawable = R.drawable.img_telefone_gps,
            titleColor = Color.WHITE,
            descriptionColor = Color.WHITE,
            backgroundColor = getColor(R.color.colorPrimary),
        ))
    }


    private fun slideFinalizar(){
        val texto = StringBuilder()
        texto.append(" Tudo certo, agora é só concluir para poder buscar ")
        texto.append(" localidades e fornecer informações.")

        addSlide(AppIntroFragment.newInstance(
            title = "Tudo pronto!!",
            description = texto,
            imageDrawable = R.drawable.ic_emoji_positivo,
            titleColor = Color.WHITE,
            descriptionColor = Color.WHITE,
            backgroundColor = getColor(R.color.colorPrimary),
        ))
    }

}