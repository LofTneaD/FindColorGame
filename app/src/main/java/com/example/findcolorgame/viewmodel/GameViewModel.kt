package com.example.findcolorgame.viewmodel

import GameMode
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findcolorgame.data.FirebaseRepository
import com.example.findcolorgame.data.GamePreferencesManager
import com.example.findcolorgame.data.GameState
import com.example.findcolorgame.data.PlayerResult

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class GameViewModel(
    private val prefsManager: GamePreferencesManager,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    var state by mutableStateOf(GameState(mode = GameMode.CLASSIC))
        private set

    private var _playerName by mutableStateOf("")
    val playerName: String get() = _playerName

    fun setPlayerName(name: String) {
        _playerName = name.take(12)
    }

    private val baseColors = listOf(
        Color.Red, Color.Green, Color.Blue,
        Color.Magenta, Color.Cyan, Color.Yellow,
        Color(0xFFFFA500),
        Color(0xFF673AB7),
        Color(0xFF800080),
        Color(0xFF008080),
        Color(0xFFCDDC39),
        Color(0xFF3F51B5),
        Color(0xCDFF6B6B),
        Color(0xFFF3B488),
        Color(0xFF78F352),
        Color(0xFF4B0808),
        Color(0xFF0B174B),
        Color(0xFF5B4683),
        Color(0xFFC7BBBB),
        Color(0xFF312424),
        Color(0xFFADEEEC),
        Color(0xFFD2C3F8),
        Color(0xFF2E8B57)
    )
    private var timerJob: Job? = null

    fun startGame(mode: GameMode) {
        timerJob?.cancel()
        state = when (mode) {
            GameMode.CLASSIC -> GameState(mode = mode, lives = 3, gameOver = false)
            GameMode.TIME_ATTACK -> GameState(mode = mode, timeLeft = 60, gameOver = false)
            GameMode.SURVIVAL -> GameState(mode = mode, lives = 1, gameOver = false)
        }
        Log.d("GameViewModel", "startGame state: $state")
        generateGrid()
        if (mode == GameMode.TIME_ATTACK) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (state.timeLeft > 0 && !state.gameOver) {
                delay(1000)
                state = state.copy(timeLeft = state.timeLeft - 1)
            }
            if (state.timeLeft == 0) finishGame()
        }
    }

    fun onCellClick(index: Int) {
        Log.d("GameViewModel", "onCellClick: index=$index, correct=${state.correctIndex}, gameOver=${state.gameOver}")
        if (state.gameOver) return

        if (index == state.correctIndex) {
            nextLevel()
        } else {
            handleMistake()
        }
    }

    private fun nextLevel() {
        state = state.copy(level = state.level + 1)
        generateGrid()
    }

    private fun handleMistake() {
        Log.d("GameViewModel", "handleMistake called, mode=${state.mode}")
        when (state.mode) {
            GameMode.CLASSIC -> {
                val livesLeft = state.lives - 1  // уменьшаем на 1
                if (livesLeft <= 0) {
                    Log.d("GameViewModel", "No lives left, finishing game")
                    finishGame()
                }
                else state = state.copy(lives = livesLeft)
            }
            GameMode.SURVIVAL -> {
                Log.d("GameViewModel", "Survival mode mistake, finishing game")
                finishGame()
            }
            GameMode.TIME_ATTACK -> {
                Log.d("GameViewModel", "Time attack mistake, ignoring")
            }
        }
    }

    private fun finishGame() {
        timerJob?.cancel()
        state = state.copy(gameOver = true)
        saveRecord()
    }

    private fun saveRecord() {
//        viewModelScope.launch {
//            val score = state.level
//            prefsManager.saveBestScore(state.mode, score)
        viewModelScope.launch {
            //firebaseRepository.clearAllResults() //TODO: убрать очистку перед сборкой
            firebaseRepository.saveResult(
                PlayerResult(
                    name = playerName,
                    mode = state.mode.name,
                    score = state.level
                )
            )
        }
    }

    private fun generateGrid() {
        val size = when {
            state.level < 5 -> 2
            state.level < 9 -> 3
            state.level < 14 -> 4
            state.level < 20 -> 5
            else -> 6
        }

        val (base, special) = generateColors(state.level)

        state = state.copy(
            gridSize = size,
            correctIndex = (0 until size * size).random(),
            baseColor = base,
            specialColor = special
        )
    }

    private fun generateColors(level: Int): Pair<Color, Color> {
        val base = baseColors.random()

        val maxLevel = 30
        val clampedLevel = level.coerceIn(1, maxLevel)
        val rawFactor = clampedLevel / maxLevel.toFloat()

        // Медленное нарастание сложности (корень)
        val difficultyFactor = kotlin.math.sqrt(rawFactor)

        // Разница яркости: на низких уровнях большая, на высоких — маленькая, но не 0
        val maxLightnessDiff = 20f
        val minLightnessDiff = 1f
        val lightnessDiff = maxLightnessDiff - difficultyFactor * (maxLightnessDiff - minLightnessDiff)

        val hsl = base.toHsl()
        val baseH = hsl[0]
        val s = hsl[1]
        val l = hsl[2]

        // Спец-цвет — уменьшаем яркость на lightnessDiff, гарантируя в пределах [0..100]
        val special = hslToColor(baseH, s, (l - lightnessDiff).coerceAtLeast(0f))

        return base to special
    }
    fun hslToColor(h: Float, s: Float, l: Float): Color {
        val c = (1 - kotlin.math.abs(2 * l / 100f - 1)) * (s / 100f)
        val x = c * (1 - kotlin.math.abs((h / 60f) % 2 - 1))
        val m = l / 100f - c / 2f

        val (r1, g1, b1) = when {
            h < 60 -> Triple(c, x, 0f)
            h < 120 -> Triple(x, c, 0f)
            h < 180 -> Triple(0f, c, x)
            h < 240 -> Triple(0f, x, c)
            h < 300 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        val r = (r1 + m).coerceIn(0f, 1f)
        val g = (g1 + m).coerceIn(0f, 1f)
        val b = (b1 + m).coerceIn(0f, 1f)

        return Color(r, g, b, 1f)
    }

    fun Color.toHsl(): FloatArray {
        val r = red
        val g = green
        val b = blue

        val max = max(r, max(g, b))
        val min = min(r, min(g, b))
        val delta = max - min

        var h = 0f
        var s: Float
        val l = (max + min) / 2f

        if (delta == 0f) {
            h = 0f
            s = 0f
        } else {
            s = if (l < 0.5f) delta / (max + min) else delta / (2f - max - min)

            h = when (max) {
                r -> ((g - b) / delta + (if (g < b) 6 else 0))
                g -> ((b - r) / delta + 2)
                else -> ((r - g) / delta + 4)
            }

            h /= 6f
        }

        return floatArrayOf(h * 360f, s * 100f, l * 100f)
    }
}