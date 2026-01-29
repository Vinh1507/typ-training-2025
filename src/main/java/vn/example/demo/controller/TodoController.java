package vn.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import vn.example.demo.domain.Todo;
import vn.example.demo.repository.TodoRepository;
import vn.example.demo.service.TodoService;

// @Controller
// public class TodoController {

// @Autowired
// private TodoService todoService;

// @GetMapping("/listTodo")
// public String index(Model model, @RequestParam(value = "limit", required =
// false) Integer limit) {
// model.addAttribute("todoList", todoService.findAll(limit));
// return "listTodo";
// }

// @GetMapping("/addTodo")
// public String addTodo(Model model) {
// model.addAttribute("todo", new Todo());
// return "addTodo";
// }

// @PostMapping("/addTodo")
// public String addTodo(@ModelAttribute Todo todo) {
// todoService.save(todo);
// return Optional.ofNullable(todoService.add(todo))
// .map(t -> "success")
// .orElse("failed");

// }
// }

// @RestController
// @RequestMapping("/api/v1")
// public class TodoController {

// List<Todo> todoList = new CopyOnWriteArrayList<>();

// // bạn còn nhớ @PostConstruct dùng để làm gì chứ?
// // nếu không nhớ, hãy coi lại bài viết Spring Boot #3 nhé
// @PostConstruct
// public void init() {
// // Thêm null vào List để bỏ qua vị trí số 0;
// todoList.add(null);
// }

// @GetMapping("/todo")
// public List<Todo> getTodoList() {
// return todoList;
// }

// /*
// * phần path URL bạn muốn lấy thông tin sẽ để trong ngoặc kép {}
// */
// @GetMapping("/todo/{todoId}")
// public Todo getTodo(@PathVariable(name = "todoId") Integer todoId) {
// // @PathVariable lấy ra thông tin trong URL
// // dựa vào tên của thuộc tính đã định nghĩa trong ngoặc kép /todo/{todoId}
// return todoList.get(todoId);
// }

// /*
// * @RequestBody nói với Spring Boot rằng hãy chuyển Json trong request body
// * thành đối tượng Todo
// */
// @PutMapping("/todo/{todoId}")
// public Todo editTodo(@PathVariable(name = "todoId") Integer todoId,
// @RequestBody Todo todo) {
// todoList.set(todoId, todo);
// // Trả về đối tượng sau khi đã edit
// return todo;
// }

// @DeleteMapping("/todo/{todoId}")
// public ResponseEntity deleteTodo(@PathVariable(name = "todoId") Integer
// todoId) {
// todoList.remove(todoId.intValue());
// return ResponseEntity.ok().build();
// }

// @PostMapping("/todo")
// public ResponseEntity addTodo(@RequestBody Todo todo) {
// todoList.add(todo);
// // Trả về response với STATUS CODE = 200 (OK)
// // Body sẽ chứa thông tin về đối tượng todo vừa được tạo.
// return ResponseEntity.ok().body(todo);
// }
// }

@RestController
@RequestMapping("/api/v1")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    // Tạo mới một To-do
    @PostMapping("/todo")
    public ResponseEntity<Todo> addTodo(@RequestBody Todo todo) {
        Todo saved = todoRepository.save(todo);
        return ResponseEntity.ok(saved);
    }

    // Lấy danh sách tất cả To-do
    @GetMapping("/todo")
    public List<Todo> getTodoList() {
        return todoRepository.findAll();
    }

    // Lấy thông tin một To-do theo ID
    @GetMapping("/todo/{todoId}")
    public ResponseEntity<Todo> getTodo(@PathVariable Long todoId) {
        return todoRepository.findById(todoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Sửa thông tin một To-do
    @PutMapping("/todo/{todoId}")
    public ResponseEntity<Todo> editTodo(@PathVariable Long todoId, @RequestBody Todo todo) {
        if (!todoRepository.existsById(todoId)) {
            return ResponseEntity.notFound().build();
        }
        todo.setId(todoId);
        Todo updated = todoRepository.save(todo);
        return ResponseEntity.ok(updated);
    }

    // Xóa một To-do
    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId) {
        if (!todoRepository.existsById(todoId)) {
            return ResponseEntity.notFound().build();
        }
        todoRepository.deleteById(todoId);
        return ResponseEntity.ok().build();
    }
}