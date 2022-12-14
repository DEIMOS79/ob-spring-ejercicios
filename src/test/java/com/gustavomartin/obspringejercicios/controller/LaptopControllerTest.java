package com.gustavomartin.obspringejercicios.controller;

import com.gustavomartin.obspringejercicios.entity.Laptop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LaptopControllerTest {

    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;

    @Value("${app.message}")
    String message;

    @BeforeEach
    void setUp() {
        restTemplateBuilder=restTemplateBuilder.rootUri("http://localhost:"+port);
        testRestTemplate=new TestRestTemplate(restTemplateBuilder);
    }

    @Test
    void findAll() {
        System.out.println(message);
        ResponseEntity<Laptop[]> response=
                testRestTemplate.getForEntity("/api/laptops", Laptop[].class);
        assertEquals(200, response.getStatusCodeValue());

        List<Laptop> laptopList= Arrays.asList(response.getBody());
        System.out.println(laptopList.size());
    }

    @Test
    void findById() {
        System.out.println(message);
        ResponseEntity<Laptop> response=
                    testRestTemplate.getForEntity("/api/laptops/{id}", Laptop.class, 1);
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    void create() {
        System.out.println(message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json= """
                {
                    "marca": "Apple",
                    "modelo": "MacBook Air",
                    "procesador": "M1",
                    "memoria": 16,
                    "hd": 512,
                    "precio": 1679.00
                }
                """;

        HttpEntity<String> request= new HttpEntity<>(json, headers);
        ResponseEntity<Laptop> response=
                testRestTemplate.exchange("/api/laptops", HttpMethod.POST, request, Laptop.class);

        Laptop result=response.getBody();

        assertEquals(1L, result.getId());
        assertEquals("Apple", result.getMarca());

    }
}