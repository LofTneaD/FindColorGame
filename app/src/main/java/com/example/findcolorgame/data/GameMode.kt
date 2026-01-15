enum class GameMode {
    CLASSIC,      // 3 жизни, без таймера
    TIME_ATTACK,  // 60 секунд
    SURVIVAL;      // одна ошибка = конец игры

    fun displayName(): String = when (this) {
        CLASSIC -> "Классический"
        TIME_ATTACK -> "На время"
        SURVIVAL -> "Выживание"
    }

    fun String.toGameMode(): GameMode? =
        runCatching { GameMode.valueOf(this) }.getOrNull()
}
