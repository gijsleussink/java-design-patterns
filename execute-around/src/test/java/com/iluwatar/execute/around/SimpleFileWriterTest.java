/**
 * The MIT License
 * Copyright (c) 2014-2016 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.iluwatar.execute.around;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Date: 12/12/15 - 3:21 PM
 *
 * @author Jeroen Meulemeester
 */
public class SimpleFileWriterTest {

  @TempDir
  public File testFolder;

  @Test
  public void testWriterNotNull() throws Exception {
    final File temporaryFile = new File(testFolder, "test");
    new SimpleFileWriter(temporaryFile.getPath(), Assertions::assertNotNull);
  }

  @Test
  public void testCreatesNonExistentFile() throws Exception {
    final File nonExistingFile = new File(testFolder, "non-existing-file");
    assertFalse(nonExistingFile.exists());

    new SimpleFileWriter(nonExistingFile.getPath(), Assertions::assertNotNull);
    assertTrue(nonExistingFile.exists());
  }

  @Test
  public void testContentsAreWrittenToFile() throws Exception {
    final String testMessage = "Test message";

    final File temporaryFile = new File(testFolder, "file-for-test-message");
    new SimpleFileWriter(temporaryFile.getPath(), writer -> writer.write(testMessage));
    assertTrue(Files.lines(temporaryFile.toPath()).allMatch(testMessage::equals));
  }

  @Test
  public void testRipplesIoExceptionOccurredWhileWriting() {
    String message = "Some error";
    assertThrows(IOException.class, () -> {
      final File temporaryFile = new File(testFolder, "file-for-ripple-io-exception-test");
      new SimpleFileWriter(temporaryFile.getPath(), writer -> {
        throw new IOException(message);
      });
    }, message);
  }

}
