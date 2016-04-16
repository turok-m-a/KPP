package crosses_zeroes;

/**
 * class is used to send and receive info to/from server thread
 */
public class ServerThread extends Thread implements Constants {
  public GameLogic field;
  public int posX;
  public int posY;
  public int result;
  public int serverMode = PLAY_MODE;
  private boolean stopServer = false;

  /**
   * stops server
   */
  protected void finalize() {
    stopServer = true;
  }

  public void run() {
    while (true) { // wait notify from client
      try {
        synchronized (this) {
          this.wait();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (stopServer) { // stop this thread
        break;
      }
      switch (serverMode) {
        case PLAY_MODE: {
          result = field.checkCell(posX, posY);
        };
          break;
        case REPLAY_MODE: {
          if (field.loadTurnFromReplay() == false) {
            result = NO_RESULT;
          } else {
            result = 0; // != NO_RESULT
          }
        }
          break;
      }
      synchronized (this) { // notify waiting client thread
        notify();
      }
    }
  }
}
