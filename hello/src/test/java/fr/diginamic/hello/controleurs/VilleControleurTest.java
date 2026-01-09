package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Region;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.model.VilleDto;
import fr.diginamic.hello.model.mapper.VilleMapper;
import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.PaysService;
import fr.diginamic.hello.services.VilleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VilleControleur.class)
class VilleControleurMockMvcTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private VilleService villeService;

  @MockBean
  private VilleMapper villeMapper;

  @MockBean
  private DepartementService departementService;

  @MockBean
  private PaysService paysService;

  private Region paysLoire = new Region("52", "Pays de la Loire");
  private Departement maineLoire = new Departement("49", "Maine-et-Loire", paysLoire);

  private Ville angers;
  private Ville cholet;

  @BeforeEach
  void init() {
    angers = new Ville("Angers", 142_000, maineLoire);
    cholet = new Ville("Cholet", 55_000, maineLoire);

    // Mock pour searchByNom
    when(villeService.findByNomDebut("A"))
      .thenReturn(Collections.singletonList(angers));
    when(villeMapper.toDto(angers))
      .thenReturn(new VilleDto(1, "Angers", 142_000, "49", null));

    // Mock pour findAll paginée
    List<Ville> allVilles = List.of(angers, cholet);
    Page<Ville> page = new PageImpl<>(allVilles, PageRequest.of(0, 2), allVilles.size());
    when(villeService.findAll(PageRequest.of(0, 2))).thenReturn(page);
    when(villeMapper.toDto(angers)).thenReturn(new VilleDto(1, "Angers", 142_000, "49", null));
    when(villeMapper.toDto(cholet)).thenReturn(new VilleDto(2, "Cholet", 55_000, "49", null));
  }

  @Test
  void searchByNom_shouldReturnAngers() throws Exception {
    mockMvc.perform(get("/villes/search")
        .param("nom", "A")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].nom").value("Angers"))
      .andExpect(jsonPath("$[0].population").value(142000))
      .andExpect(jsonPath("$[0].codeDepartement").value("49"));
  }

  @Test
  void findAll_shouldReturnPaginatedVilles() throws Exception {
    mockMvc.perform(get("/villes")
        .param("page", "0")
        .param("size", "2")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content[0].nom").value("Angers"))
      .andExpect(jsonPath("$.content[1].nom").value("Cholet"));
  }
}
