# <div align="center">Isolation Minimax</div>

An implementation of the Isolation board game using the [Minimax](https://en.wikipedia.org/wiki/Minimax) AI algorithm.

---

### What is Minimax

Minimax is a artificial intelligence algorithm applied in two player games, such as tic-tac-toe, checkers, chess etc. These games are known as zero-sum games, because in a mathematical representation: one player wins `(+100)` and other player loses `(-100)` or no one wins `(0)`.

### How does it work?

The algorithm searches recursively the best move that leads the Max player to win or not lose (draw). It considers the current state of the game and the available moves in that state, then for each valid move it plays (alternating min and max) until it finds a terminal state (win, draw or lose). Then it applies the best considered move.

### Run

---

In project root,
``` thymeleafspringsecurityextras
./gradlew build && java -jar build/libs/Isolation-0.0.1-SNAPSHOT.jar
```

## <div align="center">Game Board</div>

<p align="center">
<img height=400 src="https://github.com/AlexandrosAlexiou/Isolation-minimax/blob/main/src/main/resources/board.png" alt="board"/>
</p>
