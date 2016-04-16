package crosses_zeroes;
/**
 * contains constants, that are used in other classes
 *
 */
public interface Constants {
  final int CROSS_MARK = 1;
  final int ZERO_MARK = 2;
  final int EMPTY_MARK = 0;
  final int ZEROS_WIN = 2;   // current game state
  final int CROSSES_WIN = 1;
  final int DRAW = 0;
  final int GAME_IS_NOT_OVER = -1;
  final int EASY = 1;        // difficulty
  final int MEDIUM = 3;
  final int HARD = 5;
  final int NO_RESULT = -1;  // if no cell number to return
  final int NOTHING_TO_PREVENT = -1;
  final int NO_POSSIBILITY = -1;
  final int MAX_FIELD_SIZE = 4;
  final int STOP_AUTO_GAME = 1;
  final int BELONGS_TO_WIN_COMBINATION = 1;
  final int DOESNT_BELONG_TO_WIN_COMBINATION = 0;
  final int STOP_REPLAY = 1;
  final int SECOND_TURN = 2;
  final int THIRD_TURN = 3;
  final int WINDOW_LENGTH = 450; // window size
  final int WINDOW_HEIGTH = 130;
  final int DEFAULT_FIELD_SIZE = 3;
}
