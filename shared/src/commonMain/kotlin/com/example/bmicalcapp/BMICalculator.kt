package com.example.bmicalcapp

class BMIcalculator {

    fun calculateBMI(heightCMS: Float, weightKGS: Float ): String {

        val heightMtr = heightCMS / 100.0
        val bmiScore = weightKGS / (heightMtr * heightMtr)

        return getBMIStatus(bmiScore)
    }

    fun getBMIStatus ( bmiScore : Double) : String {
        return when(bmiScore) {
            in 0.0..16.0 -> "Very Thin"
            in 16.0..17.0 -> "Thin"
            in 17.0..18.5 -> "Under Weight"
            in 18.5..24.9 -> "Healthy"
            in 24.9..30.0 -> "Overweight"
            in 30.0..100.0 -> "Obesity"
            else -> "Wrong value"
        }
    }

}