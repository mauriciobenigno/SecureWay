package com.mauriciobenigno.secureway.util

class ScoreUtils {

    companion object {

        fun obterAvaliacao(pontuacao: Double) : String {
            val resultado = if(pontuacao<= 200 )  "Região Segura"
            else if(pontuacao in 201.0..400.0)  "Região um pouco insegura"
            else if(pontuacao in 401.0..600.0)  "Região insegura"
            else if(pontuacao in 601.0..800.0)  "Região um pouco perigosa "
            else if(pontuacao >= 801)  "Região Perigosa"
            else "Região sem avaliação"

            return resultado
        }

    }

}