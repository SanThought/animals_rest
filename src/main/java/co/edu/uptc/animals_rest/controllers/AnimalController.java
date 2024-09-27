package co.edu.uptc.animals_rest.controllers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// Import the new DTO
import co.edu.uptc.animals_rest.models.CategoryCount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uptc.animals_rest.models.Animal;
import co.edu.uptc.animals_rest.services.AnimalService;




@RestController
@RequestMapping("/animal")
public class AnimalController {

 private static final Logger logger = LoggerFactory.getLogger(AnimalController.class);

   @Autowired
    private AnimalService animalService;


    @GetMapping("/all")
    public List<Animal> getAnimalAll() throws IOException {
        logger.info("getAnimalAll called");
        return animalService.getAnimalAll();
    }

    @GetMapping("/range")
    public List<Animal> getAnimal(@RequestParam int from, @RequestParam int to) throws IOException {
        logger.info("getAnimal called with parameters: from = {}, to = {}", from, to);
        return animalService.getAnimalInRange(from, to);
    }

    @GetMapping("/animal/numberByCategory")
    public List<CategoryCount> getNumberByCategory() throws IOException {
        ClassPathResource resource = new ClassPathResource("animals.json");

        try (InputStream inputStream = resource.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            List<Animal> animals = Arrays.asList(mapper.readValue(inputStream, Animal[].class));

            Map<String, Long> categoryCountMap = animals.stream()
                .collect(Collectors.groupingBy(Animal::getCategory, Collectors.counting()));

            return categoryCountMap.entrySet().stream()
                .map(entry -> new CategoryCount(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            // Log the error and rethrow it to return a proper response
            System.err.println("Error reading animals data: " + e.getMessage());
            throw e;  // This will cause a 500 error and return the message
        }
    }

}
