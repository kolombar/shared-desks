package cz.cvut.fit.tjv.shareddesks.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.SharedDesk;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.SharedDeskRepository;
import cz.cvut.fit.tjv.shareddesks.service.dto.EmployeesZipInfo;
import cz.cvut.fit.tjv.shareddesks.service.dto.SharedDeskZipInfo;
import cz.cvut.fit.tjv.shareddesks.service.utils.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import static cz.cvut.fit.tjv.shareddesks.service.utils.FileUtils.addFileToZip;

@Service
public class SharedDeskService {

  @Autowired private SharedDeskRepository sharedDeskRepository;

  private void getZip(
          OutputStream outStream, JpaRepository<SharedDesk, Long> repository, String fileName)
          throws IOException {
    Pageable pageable = PageRequest.of(0, 30);
    ZipOutputStream zipOutputStream = new ZipOutputStream(outStream);
    List<SharedDesk> desks = repository.findAll(pageable).stream().toList();
    List<SharedDeskZipInfo> entities = desks.stream().map(SharedDeskZipInfo::new).collect(Collectors.toList());

    while (!entities.isEmpty()) {

      ObjectWriter ow =
              new ObjectMapper().findAndRegisterModules().writer().withDefaultPrettyPrinter();
      String jsonResults = ow.writeValueAsString(entities);
      byte[] jsonBytes = jsonResults.getBytes(StandardCharsets.UTF_8);
      addFileToZip(zipOutputStream, jsonBytes, fileName);

      pageable = pageable.next();
      desks = repository.findAll(pageable).stream().toList();
      entities = desks.stream().map(SharedDeskZipInfo::new).collect(Collectors.toList());
    }

    zipOutputStream.finish();
    zipOutputStream.flush();
    IOUtils.closeQuietly(zipOutputStream);
  }

  public void getDesksZip(OutputStream outStream) throws IOException {
    getZip(outStream, sharedDeskRepository, "desks.json");
  }
}
