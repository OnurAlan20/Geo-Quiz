package com.onuralan.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.onuralan.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate(savedInstanceState: Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        updateQuestion()

        //challenge 1
        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.trueButton.setOnClickListener {
            if (quizViewModel.questionBank[quizViewModel.currentIndex].isAnswerPicked == false){
                quizViewModel.questionBank[quizViewModel.currentIndex].isAnswerPicked = true
                quizViewModel.questionBank[quizViewModel.currentIndex].userAnswer = true
                val messageResId = checkAnswer(true)
                val snack = Snackbar.make(it,messageResId,Snackbar.LENGTH_LONG)
                snack.show()
            }

        }

        binding.falseButton.setOnClickListener {
            if (quizViewModel.questionBank[quizViewModel.currentIndex].isAnswerPicked == false){
                quizViewModel.questionBank[quizViewModel.currentIndex].isAnswerPicked = true
                val messageResId = checkAnswer(false)
                val snack = Snackbar.make(it,messageResId,Snackbar.LENGTH_LONG)
                snack.show()
            }

        }

        binding.previousButton.setOnClickListener {
            if (quizViewModel.currentIndex == 0){
                quizViewModel.currentIndex = quizViewModel.questionBank.size - 1
                updateQuestion()
            }else{
                quizViewModel.currentIndex = (quizViewModel.currentIndex-1) % quizViewModel.questionBank.size
                updateQuestion()
            }

        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
    }
    //Life Cycle of the Activity
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    private  fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionTextResId
        binding.questionTextView.setText(questionTextResId)


        var isEveryAnswerPicked = false
        for (i in quizViewModel.questionBank){
            if(i.isAnswerPicked == true){
                isEveryAnswerPicked = true
            }else{
                isEveryAnswerPicked = false
            }
        }

        if (isEveryAnswerPicked){
            var trueValue:Float = 0f

            for(i in quizViewModel.questionBank){
                if (i.isAnswerTrue == true){
                    trueValue++

                }
            }
            val percent:Float = trueValue/quizViewModel.questionBank.size.toFloat()
            Toast.makeText(this,"this is your percent = ${percent*100}",Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkAnswer(userAnswer: Boolean):Int{
        val correctAnswer = quizViewModel.currentQuestionRealAnswer
        quizViewModel.questionBank[quizViewModel.currentIndex].userAnswer = userAnswer

        val messageResId:Int?

        if (userAnswer == correctAnswer) {
            messageResId= R.string.correct_toast
            quizViewModel.questionBank[quizViewModel.currentIndex].isAnswerTrue = true
        } else {
            messageResId= R.string.incorrect_toast
            quizViewModel.questionBank[quizViewModel.currentIndex].isAnswerTrue = false
        }
        return messageResId

    }
}

