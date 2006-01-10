/*
 * Copyright (c) 2000-2004 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.compiler;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CompilerIOUtil {
  private final static byte[] BUFF = new byte[1024];

  private CompilerIOUtil() {}

  public static String readString(DataInput stream) throws IOException {
    final int length = stream.readInt();
    if (length == -1) {
      return null;
    }

    if (length == 0) {
      return "";
    }

    char[] chars = new char[length];
    int charsRead = 0;

    while (charsRead < length) {
      synchronized (BUFF) {
        final byte[] buff = BUFF;
        final int bytesRead = Math.min((length - charsRead) * 2, buff.length);
        stream.readFully(buff, 0, bytesRead);
        for (int i = 0 ; i < bytesRead; i += 2) {
          chars[charsRead++] = (char)((buff[i] << 8) + buff[i + 1]);
        }
      }
    }

    return StringFactory.createStringFromConstantArray(chars);
  }

  public static void writeString(String s, DataOutput stream) throws IOException {
    if (s == null) {
      stream.writeInt(-1);
      return;
    }

    final int len = s.length();
    stream.writeInt(len);
    if (len == 0) {
      return;
    }

    int charsWritten = 0;
    while (charsWritten < len) {
      synchronized (BUFF) {
        final byte[] buff = BUFF;
        final int bytesWritten = Math.min((len - charsWritten) * 2, buff.length);
        for (int i = 0; i < bytesWritten; i += 2) {
          char aChar = s.charAt(charsWritten++);
          buff[i] = (byte)((aChar >>> 8) & 0xFF);
          buff[i + 1] = (byte)((aChar) & 0xFF);
        }

        stream.write(buff, 0, bytesWritten);
      }
    }
  }
}
