package cz.cvut.fit.tjv.shareddesks.service.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtils {

  public static void addFileToZip(ZipOutputStream zipOutputStream, byte[] data, String fileName) {
    if (data != null) {
      try {
        zipOutputStream.putNextEntry(new ZipEntry(fileName));
        zipOutputStream.write(data);
        zipOutputStream.closeEntry();
      } catch (IOException e) {
        log.warn("Unable to write file " + fileName + " to zip file.", e);
      }
    }
  }
}
