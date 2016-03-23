package crosses_zeroes;
/**
 * contains constants, that are used in other classes
 *
 */
public interface Constants {
  static final int CROSS_MARK = 1;
  static final int ZERO_MARK = 2;
  static final int EMPTY_MARK = 0;
  static final int ZEROS_WIN = 2;   // current game state
  static final int CROSSES_WIN = 1;
  static final int DRAW = 0;
  static final int GAME_IS_NOT_OVER = -1;
  static final int EASY = 1;        // difficulty
  static final int MEDIUM = 3;
  static final int HARD = 5;
  static final int NO_RESULT = -1;  // if no cell number to return
  static final int NOTHING_TO_PREVENT = -1;
  static final int NO_POSSIBILITY = -1;
  static final int MAX_FIELD_SIZE = 4;
  static final int STOP_AUTO_GAME = 1;
}
