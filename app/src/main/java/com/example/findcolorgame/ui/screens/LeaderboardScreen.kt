import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.findcolorgame.data.FirebaseRepository
import com.example.findcolorgame.data.PlayerResult

@Composable
fun LeaderboardScreen(
    repository: FirebaseRepository,
    currentPlayerName: String,
    onBack: () -> Unit
) {
    var selectedMode by remember { mutableStateOf(GameMode.CLASSIC) }

    val topPlayers by repository
        .getTopResults(selectedMode)
        .collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center
    ) {
        Text(
            "Таблица рекордов",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        // Переключение режимов
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GameMode.values().forEach { mode ->
                Button(
                    onClick = { selectedMode = mode },
                    modifier = Modifier.weight(1f),
                    enabled = selectedMode != mode,
                    shape = RoundedCornerShape(50),
                ) {
                    Text(
                        text = mode.displayName(),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        LazyColumn {
            itemsIndexed(topPlayers) { index, player ->
                LeaderboardItem(
                    position = index + 1,
                    player = player,
                    isCurrentPlayer = player.name == currentPlayerName
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}

@Composable
fun LeaderboardItem(
    position: Int,
    player: PlayerResult,
    isCurrentPlayer: Boolean
) {
    val backgroundColor = if (isCurrentPlayer) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (isCurrentPlayer) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isCurrentPlayer) "⭐ #$position ${player.name}" else "#$position ${player.name}",
                color = textColor
            )
            Text(
                text = " - уровень: ${player.score}",
                color = textColor
            )
        }
    }
}