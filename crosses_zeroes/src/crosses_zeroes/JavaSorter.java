package crosses_zeroes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;

public class JavaSorter implements Constants {
  final String path = "autogen100k/";
  final String sortedPath = "autogenSorted/";
  final LinkedList<byte[]> listOfFiles = new LinkedList<byte[]>();
  private int firstNumber;
  private int lastNumber;

  public JavaSorter(int firstNumber, int lastNumber) {
    this.firstNumber = firstNumber;
    this.lastNumber = lastNumber;
  }

  public void sort(int SortMode) {
    for (int i = firstNumber; i <= lastNumber; i++) {
      if (i % 50000 == 0) {
        System.gc();
      }
      Path sourcePath = Paths.get(path + Integer.toString(i));
      try { //read replays
        byte[] buffer = Files.readAllBytes(sourcePath);
        listOfFiles.add(buffer);
      } catch (IOException e) {
        System.out.println("cannot open file: " + Integer.toString(i));
      }
    }
    if (SortMode == SORT_BY_POSITIONS) { //sort replays
      Collections.sort(listOfFiles, ReplayComparator::compare);
    } else {
      Collections.sort(listOfFiles, TurnsComparator::compare);
    }
    for (int i = firstNumber; i <= lastNumber; i++) {
      if (i % 50000 == 0) {
        System.gc();
      } //save sorted replays
      File outputFile = new File(sortedPath + Integer.toString(i));
      try (FileOutputStream outputStream = (new FileOutputStream(outputFile));) {
        outputStream.write(listOfFiles.get(i));
        outputStream.close();
      } catch (IOException e) {
        System.out.println("error: cannot write to file" + Integer.toString(i));
      }
    }
    File outputFile = new File("worst"); //save worst replay
    try (FileOutputStream outputStream = (new FileOutputStream(outputFile));) {
      outputStream.write(listOfFiles.get(lastNumber));
      outputStream.close();
    } catch (IOException e) {
      System.out.println("error: cannot write to file");
    }
    outputFile = new File("best"); //save best replay
    try (FileOutputStream outputStream = (new FileOutputStream(outputFile));) {
      outputStream.write(listOfFiles.get(firstNumber));
      outputStream.close();
    } catch (IOException e) {
      System.out.println("error: cannot write to file");
    }
  }

}
