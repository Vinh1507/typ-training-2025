import { useState, useEffect, useCallback, useMemo } from "react";
import { useTheme } from "./contexts/ThemeContext.jsx";
import './App.css';

function App() {
  const { theme, toggleTheme } = useTheme();

  // State quan ly task
  const [tasks, setTasks] = useState([]); // khoi tao la rong
  const [filter, setFilter] = useState('all'); // khoi tao la all
  const [searchTerm, setSearchTerm] = useState(''); // khoi tao la rong

  // su dung useEffect de lay task tu local storage
  useEffect(() => {
    const savedTasks = localStorage.getItem('tasks');
    if (savedTasks) {
      setTasks(JSON.parse(savedTasks));
    }
  }, []);

  // luu task vao local storage khi co thay doi
  useEffect(() => {
    localStorage.setItem('tasks', JSON.stringify(tasks));
  }, [tasks]);

  // them task moi
  const handleAddTask = useCallback((taskTitle) => {
    const newTask = {
      id: Date.now(),
      title: taskTitle,
      completed: false,
      createAt: new Date().toISOString()
    };
    setTasks(prev => [...prev, newTask]);
  }, []);

  // toggle trang thai completed
  const handleToggleTask = useCallback((id) => {
    setTasks(prev => prev.map(task => 
      task.id === id ? {...task, completed: !task.completed} : task
    ));
  }, []);

  // xoa task
  const handleDeleteTask = useCallback((id) => {
    setTasks(prev => prev.filter(task => task.id !== id));
  }, []);

  // filter Task
  const filteredTasks = useMemo(() => {
    let result = tasks;

    if (filter === 'active') {
      result = result.filter(task => !task.completed);
    } else if (filter === 'completed') {
      result = result.filter(task => task.completed);
    }

    if (searchTerm) {
      result = result.filter(task => 
        task.title.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    return result;
  }, [tasks, filter, searchTerm]);

  const stats = useMemo(() => {
    const total = tasks.length;
    const completed = tasks.filter(t => t.completed).length;
    const active = total - completed;
    return { total, completed, active };
  }, [tasks])

  return (
    <div className="app-container">
      <header className="app-header">
        <h1>Task Manager</h1>
        <button onClick={toggleTheme} className="theme-toggle">
          {theme === 'light' ? '🌙' : '☀️'}
        </button>
      </header>
      <div className="statistics">
        <div className="stat-item">
          <span className="stat-label">Tổng</span>
          <span className="stat-value"> {stats.total} </span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Hoàn thành</span>
          <span className="stat-value"> {stats.completed} </span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Đang làm</span>
          <span className="stat-value"> {stats.active} </span>
        </div>
      </div>

      {/* Add task form */}
      <form onSubmit={(e) => {
        e.preventDefault();
        const input = e.target.elements.taskInput;
        if (input.value.trim()) {
          handleAddTask(input.value.trim());
          input.value = '';
        }
      }} className="task-form">
        <input 
          type="text" 
          name="taskInput"
          placeholder="Thêm task mới"
          className="task-input"
        />
        <button type="submit" className="add-button">+ thêm</button>
      </form>

      {/* Filter and Search */}
      <div className="filter-bar">
        <div className="filter-buttons">
          <button
            className={filter === 'all' ? 'active' : ''}
            onClick={() => setFilter('all')}
          >
            Tất cả
          </button>
          <button
            className={filter === 'active' ? 'active' : ''}
            onClick={() => setFilter('active')}
          >
            Đang làm
          </button>
          <button
            className={filter === 'completed' ? 'active' : ''}
            onClick={() => setFilter('completed')}
          >
            Đã hoàn thành
          </button>
        </div>
        <input 
          type="text" 
          placeholder="Tìm kiếm"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
      </div>

      {/* Task List */}
      <div className="task-list">
        {filteredTasks.length === 0 ? (
          <p className="empty-message">
            {searchTerm ? 'Không tìm thấy task nào' : 'Chưa có task nào'}
          </p>
        ) : (
          filteredTasks.map(task => (
            <div key={task.id} className={`task-item ${task.completed ? 'completed' : ''}`}>
              <input
                type="checkbox"
                checked={task.completed}
                onChange={() => handleToggleTask(task.id)}
                className="task-checkbox"
              />
              <span className="task-title">{task.title}</span>
              <button 
                onClick={() => handleDeleteTask(task.id)}
                className="delete-button"
              >
                🗑️
              </button>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default App
