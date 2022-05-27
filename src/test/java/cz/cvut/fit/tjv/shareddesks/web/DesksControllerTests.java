package cz.cvut.fit.tjv.shareddesks.web;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Equipment;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.SharedDesk;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.SharedDeskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class DesksControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockBean private SharedDeskRepository sharedDeskRepository;

  @Test
  void getDesks() throws Exception {
    List<SharedDesk> defaultDeskList = Collections.emptyList();
    Mockito.when(sharedDeskRepository.findAll()).thenReturn(defaultDeskList);

    mockMvc
        .perform(get("/desks"))
        .andExpect(status().isOk())
        .andExpect(view().name("DesksTemplate"))
        .andExpect(model().attribute("desks", defaultDeskList));
  }

  @Test
  void addDesk() throws Exception {
    SharedDesk deskToAdd = new SharedDesk(Equipment.STANDARD, new Branch("Olomouc"));
    List<SharedDesk> defaultDeskList = List.of(deskToAdd);
    Mockito.when(sharedDeskRepository.findAll()).thenReturn(defaultDeskList);

    mockMvc
        .perform(post("/desks/add").flashAttr("employee", deskToAdd))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/desks"));
  }

  @Test
  void deleteDesk() throws Exception {
    mockMvc
        .perform(get("/desks/delete").param("id", "1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/desks"));
  }
}
