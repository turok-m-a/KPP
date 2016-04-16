package crosses_zeroes;
/**
 * class is used to send and receive info to/from server thread
 */
public class ServerThread extends Thread {
  public GameLogic field;
  public int posX;
  public int posY;
  public int result;
  private boolean stopServer = false;
  /**
   * stops server
   */
  protected void finalize() {
    stopServer = true;
  }
  public void run() {
    while (true) {  // wait notify from client
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
      result = field.checkCell(posX, posY);
      synchronized (this) { // notify waiting client thread
        notify();
      }
    }
  }
}
