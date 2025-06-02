package com.example.tictactoe

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

// PUBLIC_INTERFACE
class MainActivity : AppCompatActivity() {
    // The 3x3 board; null = empty, "X" or "O" = marked.
    private val board = Array(3) { arrayOfNulls<String>(3) }
    private var xTurn = true
    private lateinit var gridButtons: Array<Array<Button>>
    private lateinit var playerIndicator: TextView
    private lateinit var resetBtn: Button

    private val WINNING_PATTERNS = arrayOf(
        // Rows
        arrayOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)),
        arrayOf(Pair(1, 0), Pair(1, 1), Pair(1, 2)),
        arrayOf(Pair(2, 0), Pair(2, 1), Pair(2, 2)),
        // Columns
        arrayOf(Pair(0, 0), Pair(1, 0), Pair(2, 0)),
        arrayOf(Pair(0, 1), Pair(1, 1), Pair(2, 1)),
        arrayOf(Pair(0, 2), Pair(1, 2), Pair(2, 2)),
        // Diagonals
        arrayOf(Pair(0, 0), Pair(1, 1), Pair(2, 2)),
        arrayOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use custom minimalist layout and colors.
        setContentView(R.layout.activity_main)
        // Initialize references to UI components.
        playerIndicator = findViewById(R.id.player_indicator)
        resetBtn = findViewById(R.id.reset_button)
        val gridLayout = findViewById<GridLayout>(R.id.tictactoe_grid)

        // Minimalist, borderless grid of buttons
        gridButtons = Array(3) { i ->
            Array(3) { j ->
                val btn = gridLayout.getChildAt(i * 3 + j) as Button
                btn.setOnClickListener { onCellClick(i, j) }
                btn
            }
        }

        // Reset logic
        resetBtn.setOnClickListener {
            resetGame()
        }

        resetGame()
    }

    // PUBLIC_INTERFACE
    private fun onCellClick(row: Int, col: Int) {
        if (board[row][col] != null || getWinnerOrDraw() != null) return
        board[row][col] = if (xTurn) "X" else "O"
        updateBoardUI()
        val state = getWinnerOrDraw()
        if (state == null) {
            xTurn = !xTurn
            updatePlayerIndicator()
        } else {
            showResult(state)
        }
    }

    // PUBLIC_INTERFACE
    private fun updateBoardUI() {
        for (i in 0..2) {
            for (j in 0..2) {
                val cell = board[i][j]
                gridButtons[i][j].text = cell ?: ""
                gridButtons[i][j].isEnabled = (cell == null) && (getWinnerOrDraw() == null)
            }
        }
    }

    // PUBLIC_INTERFACE
    private fun updatePlayerIndicator() {
        // Show whose turn if not won/draw. Otherwise, handled by showResult.
        val state = getWinnerOrDraw()
        if (state == null) {
            playerIndicator.text = "Turn: ${if (xTurn) "X" else "O"}"
            playerIndicator.setTextColor(ContextCompat.getColor(this, R.color.black))
            playerIndicator.setTypeface(null, Typeface.BOLD)
        }
    }

    // PUBLIC_INTERFACE
    private fun showResult(result: String) {
        // result is "X", "O" or "Draw"
        when (result) {
            "X" -> {
                playerIndicator.text = "X Wins!"
                playerIndicator.setTextColor(ContextCompat.getColor(this, R.color.accent))
            }
            "O" -> {
                playerIndicator.text = "O Wins!"
                playerIndicator.setTextColor(ContextCompat.getColor(this, R.color.accent))
            }
            "Draw" -> {
                playerIndicator.text = "Draw Game"
                playerIndicator.setTextColor(ContextCompat.getColor(this, R.color.secondary))
            }
        }
        playerIndicator.setTypeface(null, Typeface.BOLD)
        // Disable remaining buttons
        for (i in 0..2)
            for (j in 0..2)
                gridButtons[i][j].isEnabled = false
    }

    // PUBLIC_INTERFACE
    private fun resetGame() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = null
                gridButtons[i][j].text = ""
                gridButtons[i][j].isEnabled = true
            }
        }
        xTurn = true
        updatePlayerIndicator()
    }

    // PUBLIC_INTERFACE
    private fun getWinnerOrDraw(): String? {
        // Check if X or O wins
        for (pattern in WINNING_PATTERNS) {
            val v1 = board[pattern[0].first][pattern[0].second]
            val v2 = board[pattern[1].first][pattern[1].second]
            val v3 = board[pattern[2].first][pattern[2].second]
            if (v1 != null && v1 == v2 && v2 == v3) {
                return v1
            }
        }
        // Draw if no empty and no winner
        if (board.all { row -> row.all { it != null } }) {
            return "Draw"
        }
        return null
    }
}
