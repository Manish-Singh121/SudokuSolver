package com.example.sudokusolver

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var gameBoard: SudokuBoard
    private var gameBoardSolver: Solver? = null
    private var solveBTN: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameBoard = findViewById(R.id.SudokuBoard)
        gameBoardSolver = gameBoard.solver
        solveBTN = findViewById(R.id.solveButton)
    }

    fun btnOnePress(@Suppress("UNUSED_PARAMETER") view: View) {
        gameBoardSolver!!.setNumberPos(1)
        gameBoard.invalidate()
    }

    fun btnTwoPress(@Suppress("UNUSED_PARAMETER") view: View) {
        gameBoardSolver!!.setNumberPos(2)
        gameBoard.invalidate()
    }

    fun btnThreePress(@Suppress("UNUSED_PARAMETER") view: View) {
        gameBoardSolver!!.setNumberPos(3)
        gameBoard.invalidate()
    }

    fun btnFourPress(@Suppress("UNUSED_PARAMETER") view: View) {
        gameBoardSolver!!.setNumberPos(4)
        gameBoard.invalidate()
    }

    fun btnFivePress(@Suppress("UNUSED_PARAMETER") view: View) {
        gameBoardSolver!!.setNumberPos(5)
        gameBoard.invalidate()
    }

    fun btnSixPress(@Suppress("UNUSED_PARAMETER") view: View) {
        gameBoardSolver!!.setNumberPos(6)
        gameBoard.invalidate()
    }

    fun btnSevenPress(@Suppress("UNUSED_PARAMETER") view: View) {
        gameBoardSolver!!.setNumberPos(7)
        gameBoard.invalidate()
    }

    fun btnEightPress(@Suppress("UNUSED_PARAMETER") view: View) {
        gameBoardSolver!!.setNumberPos(8)
        gameBoard.invalidate()
    }

    fun btnNinePress(@Suppress("UNUSED_PARAMETER") view: View) {
        gameBoardSolver!!.setNumberPos(9)
        gameBoard.invalidate()
    }

    internal inner class SolveBoardThread : Runnable {
        override fun run() {
            gameBoardSolver!!.solve(gameBoard)
        }
    }

    fun solve(@Suppress("UNUSED_PARAMETER") view: View) {
        if (solveBTN!!.text.toString() == getString(R.string.solve)) {
            solveBTN!!.text = getString(R.string.clear)
            gameBoardSolver!!.emptyBoxIndexes
            val solveBoardThread = SolveBoardThread()
            Thread(solveBoardThread).start()
            gameBoard.invalidate()
        } else {
            solveBTN!!.text = getString(R.string.solve)
            gameBoardSolver!!.resetBoard()
            gameBoard.invalidate()
        }
    }
}