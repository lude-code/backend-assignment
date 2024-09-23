package com.sogetti.leaseservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogetti.leaseservice.car.Car;
import com.sogetti.leaseservice.customer.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class LeaseDbApplicationTests {

  @Test
  void contextLoads() {}

  @Autowired private MockMvc mockMvc;

  @Test
  @Transactional
  void createUpdatGetCar() throws Exception {
    this.mockMvc
        .perform(
            post("/car")
                .content(asJsonString(new Car("audi", "a", "5", 4, 128.0, 30000.0, 60000.0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    this.mockMvc
        .perform(
            put("/car/{make}/{model}/{version}", "audi", "a", "5")
                .content(asJsonString(new Car("bmw", "m", "3", 4, 228.0, 50000.0, 110000.0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    this.mockMvc
        .perform(
            get("/car/{make}/{model}/{version}", "bmw", "m", "3")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.make").value("bmw"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.co2Emission").value(228.0));
    this.mockMvc
        .perform(
            get("/car/{make}/{model}/{version}", "audi", "a", "5")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void createUpdatGetCustomer() throws Exception {
    this.mockMvc
        .perform(
            post("/car")
                .content(asJsonString(new Car("audi", "a", "5", 4, 128.0, 30000.0, 60000.0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    this.mockMvc
        .perform(
            post("/customer")
                .content(
                    asJsonString(
                        new Customer(
                            "John Doe",
                            "KerkStraat",
                            "1",
                            "1000 AA",
                            "A'dam",
                            "John.Doe@gmail.com",
                            "911",
                            null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());

    this.mockMvc
        .perform(
            put("/customer/{name}", "John Doe")
                .content(
                    asJsonString(
                        new Customer(
                            "John Doe",
                            "KerkStraat",
                            "1",
                            "1000 AA",
                            "A'dam",
                            "John.Doe@gmail.com",
                            "911",
                            new Car("audi", "a", "5", 0, 0, 0, 0))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());

    this.mockMvc
        .perform(get("/customer/{name}", "John Doe").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.car.make").value("audi"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.car.co2Emission").value(128.0));
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
