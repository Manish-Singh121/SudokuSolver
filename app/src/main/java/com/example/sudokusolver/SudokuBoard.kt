package com.example.sudokusolver

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.ceil
import kotlin.math.min

class SudokuBoard(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var boardColor = 0
    private var cellFillColor = 0
    private var cellsHighlightColor = 0
    private var letterColor = 0
    private var letterColorSolve = 0

    private val boardColorPaint = Paint()
    private val cellFillColorPaint = Paint()
    private val cellsHighlightColorPaint = Paint()
    private val letterPaint = Paint()

    private val letterPaintBounds = Rect()

    private var cellSize = 0

    val solver = Solver()


    override fun onMeasure(width: Int, height: Int) {
        super.onMeasure(width, height)

        val height2 = MeasureSpec.getSize(height)
        val width2 = MeasureSpec.getSize(width)
        val dimension = min(width2, height2) - 2

        cellSize = dimension / 9

        setMeasuredDimension(dimension, dimension)
    }

    override fun onDraw(canvas: Canvas) {
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 20f
        boardColorPaint.color = boardColor
        boardColorPaint.isAntiAlias = true

        cellFillColorPaint.style = Paint.Style.FILL
        cellFillColorPaint.isAntiAlias = true
        cellFillColorPaint.color = cellFillColor

        cellsHighlightColorPaint.style = Paint.Style.FILL
        cellsHighlightColorPaint.isAntiAlias = true
        cellsHighlightColorPaint.color = cellsHighlightColor

        letterPaint.style = Paint.Style.FILL
        letterPaint.isAntiAlias = true
        letterPaint.color = letterColor

        colorCell(canvas, solver.selectedRow, solver.selectedColumn)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), boardColorPaint)

        drawBoard(canvas)
        drawNumbers(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val isValid: Boolean
        val x = event.x
        val y = event.y
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            ceil((y / cellSize).toDouble()).toInt().also { solver.selectedRow = it }
            ceil((x / cellSize).toDouble()).toInt().also { solver.selectedColumn = it }
            isValid = true
        } else {
            isValid = false
        }
        return isValid
    }

    private fun drawNumbers(canvas: Canvas) {
        letterPaint.textSize = (cellSize * 0.8).toFloat()

        for (r in 0..8) {
            for (c in 0..8) {
                if (solver.board[r][c] != 0) {
                    val text = solver.board[r][c].toString()
                    letterPaint.getTextBounds(text, 0, text.length, letterPaintBounds)
                    val width: Float = letterPaint.measureText(text)
                    val height: Float = letterPaintBounds.height().toFloat()
                    canvas.drawText(
                        text,
                        c * cellSize + (cellSize - width) / 2,
                        r * cellSize + cellSize - (cellSize - height) / 2,
                        letterPaint
                    )
                }
            }
        }

        letterPaint.color = letterColorSolve
        for (letter in solver.emptyBoxIndex) {
            val r = letter[0] as Int
            val c = letter[1] as Int
            val text = solver.board[r][c].toString()
            letterPaint.getTextBounds(text, 0, text.length, letterPaintBounds)
            val width: Float = letterPaint.measureText(text)
            val height: Float = letterPaintBounds.height().toFloat()

            canvas.drawText(
                text,
                c * cellSize + (cellSize - width) / 2,
                r * cellSize + cellSize - (cellSize - height) / 2,
                letterPaint
            )
        }
    }

    private fun colorCell(canvas: Canvas, r: Int, c: Int) {
        if (solver.selectedColumn != -1 && solver.selectedRow != -1) {
            canvas.drawRect(
                ((c - 1) * cellSize).toFloat(),
                0f,
                (c * cellSize).toFloat(),
                (cellSize * 9).toFloat(),
                cellsHighlightColorPaint
            )
            canvas.drawRect(
                0f,
                ((r - 1) * cellSize).toFloat(),
                (cellSize * 9).toFloat(),
                (r * cellSize).toFloat(),
                cellsHighlightColorPaint
            )
            canvas.drawRect(
                ((c - 1) * cellSize).toFloat(),
                ((r - 1) * cellSize).toFloat(),
                (c * cellSize).toFloat(),
                (r * cellSize).toFloat(),
                cellsHighlightColorPaint
            )
        }
        invalidate()
    }

    private fun drawThickLine() {
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 10f
        boardColorPaint.color = boardColor
    }

    private fun drawThinLine() {
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 4f
        boardColorPaint.color = boardColor
    }

    private fun drawBoard(canvas: Canvas) {
        for (c in 0..9) {
            if (c % 3 == 0) {
                drawThickLine()
            } else {
                drawThinLine()
            }
            canvas.drawLine(
                (cellSize * c).toFloat(),
                0f,
                (cellSize * c).toFloat(),
                width.toFloat(),
                boardColorPaint
            )
        }
        for (r in 0..9) {
            if (r % 3 == 0) {
                drawThickLine()
            } else {
                drawThinLine()
            }
            canvas.drawLine(
                0f,
                (cellSize * r).toFloat(),
                width.toFloat(),
                (cellSize * r).toFloat(),
                boardColorPaint
            )
        }
    }

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SudokuBoard, 0, 0)
        try {
            boardColor = a.getInteger(R.styleable.SudokuBoard_boardColor, 0)
            cellFillColor = a.getInteger(R.styleable.SudokuBoard_cellFillColor, 0)
            cellsHighlightColor = a.getInteger(R.styleable.SudokuBoard_cellsHighlightColor, 0)
            letterColor = a.getInteger(R.styleable.SudokuBoard_letterColor, 0)
            letterColorSolve = a.getInteger(R.styleable.SudokuBoard_letterColorSolve, 0)
        } finally {
            a.recycle()
        }
    }
}