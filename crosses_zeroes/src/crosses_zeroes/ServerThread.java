package crosses_zeroes;

/**
 * class is used to send and receive info to/from server thread
 */
public class ServerThread extends Thread implements Constants {
  private GameLogic field;
  private int posX;
  private int posY;
  private int result;
  private int serverMode = PLAY_MODE;
  private boolean stopServer = false;

  /**
   * stops server
   */
  protected void finalize() {
    stopServer = true;
  }

  public ServerThread(GameLogic field) {
    this.field = field;
  }

  public void run() {
    while (true) {
      try { // wait notify from client
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
      /*try { // client starts to wait slower (after) then server notifies him
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }*/
      synchronized (this) { // notify waiting client thread
        notifyAll();
      }
    }
  }

  public void setPosition(int posX,int posY) {
    this.posX = posX;
    this.posY = posY;
  }

  public int getResult() {
    return result;
  }

  public void setMode(int mode) {
    serverMode = mode;
  }
}
