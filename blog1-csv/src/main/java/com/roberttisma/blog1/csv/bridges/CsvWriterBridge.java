package com.roberttisma.blog1.csv.bridges;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface CsvWriterBridge<T> extends Closeable {

  default void write(List<? extends T> objects) throws IOException {
    for (T object :objects){
      this.write(object);
    }
  }

  void write(T object) throws IOException;

  void writeHeader() throws  IOException;

}
