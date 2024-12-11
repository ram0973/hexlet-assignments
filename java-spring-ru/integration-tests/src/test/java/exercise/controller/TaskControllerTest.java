package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;
import org.springframework.test.web.servlet.MvcResult;

// BEGIN

// END
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> "Title: " + faker.lorem().sentence(3))
                .supply(Select.field(Task::getDescription), () -> "Desc: " + faker.lorem().sentence(3))
                .create();
    }

    @Test
    public void testWelcomePage() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    // BEGIN

    @Test
    public void testShow() throws Exception {
        Task task = createTask();
        taskRepository.save(task);
        Task savedTask = taskRepository.findByTitle(task.getTitle()).orElseThrow(
                () -> new ResourceNotFoundException("Task not found"));
        MvcResult result = mockMvc.perform(get("/tasks/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(task.getTitle()),
                v -> v.node("description").isEqualTo(task.getDescription())
        );
    }

    @Test
    public void testCreate() throws Exception {
        Task task = createTask();
        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(task));
        mockMvc.perform(request).andExpect(status().isCreated());
        Task savedTask = taskRepository.findByTitle(task.getTitle()).orElseThrow(
                () -> new ResourceNotFoundException("Task not found"));
        assertThat(savedTask.getTitle()).isEqualTo(task.getTitle());
        assertThat(savedTask.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    public void testPut() throws Exception {
        Task task = createTask();
        taskRepository.save(task);
        Map<String, String> data = new HashMap<>();
        data.put("title", task.getTitle() + " changed");
        data.put("description", task.getDescription() + " changed");
        var request = put("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(data));
        mockMvc.perform(request).andExpect(status().isOk());
        Task savedTask = taskRepository.findByTitle(data.get("title")).orElseThrow(
                () -> new ResourceNotFoundException("Task not found"));
        assertThat(savedTask.getTitle()).isEqualTo(data.get("title"));
        assertThat(savedTask.getDescription()).isEqualTo(data.get("description"));
    }

    @Test
    public void testDelete() throws Exception {
        Task task = createTask();
        taskRepository.save(task);
        Task savedTask = taskRepository.findByTitle(task.getTitle()).orElseThrow(
                () -> new ResourceNotFoundException("Task not found"));
        var request = delete("/tasks/" + savedTask.getId());
        mockMvc.perform(request).andExpect(status().isOk());
        Optional<Task> deletedTask = taskRepository.findByTitle(task.getTitle());
        assertThat(deletedTask.isPresent()).isFalse();
    }
    // END
}
