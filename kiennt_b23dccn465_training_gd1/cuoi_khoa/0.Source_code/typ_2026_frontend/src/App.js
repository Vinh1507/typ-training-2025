import React, { useState, useEffect } from 'react';
import './App.css';
import UserList from './components/UserList';
import UserForm from './components/UserForm';
import { getUsers, createUser, updateUser, deleteUser } from './services/userService';

function App() {
  const [users, setUsers] = useState([]);
  const [editingUser, setEditingUser] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const data = await getUsers();
      setUsers(data);
      setError(null);
    } catch (err) {
      setError('Failed to load users');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateUser = async (userData) => {
    try {
      await createUser(userData);
      loadUsers();
      setError(null);
    } catch (err) {
      setError('Failed to create user');
      console.error(err);
    }
  };

  const handleUpdateUser = async (id, userData) => {
    try {
      await updateUser(id, userData);
      loadUsers();
      setEditingUser(null);
      setError(null);
    } catch (err) {
      setError('Failed to update user');
      console.error(err);
    }
  };

  const handleDeleteUser = async (id) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        await deleteUser(id);
        loadUsers();
        setError(null);
      } catch (err) {
        setError('Failed to delete user');
        console.error(err);
      }
    }
  };

  const handleEdit = (user) => {
    setEditingUser(user);
  };

  const handleCancelEdit = () => {
    setEditingUser(null);
  };

  return (
    <div className="App">
      <div className="container">
        <h1>User Management System</h1>
        
        {error && <div className="error-message">{error}</div>}
        
        <div className="content">
          <div className="form-section">
            <UserForm
              user={editingUser}
              onSubmit={editingUser ? handleUpdateUser : handleCreateUser}
              onCancel={handleCancelEdit}
            />
          </div>
          
          <div className="list-section">
            {loading ? (
              <div className="loading">Loading users...</div>
            ) : (
              <UserList
                users={users}
                onEdit={handleEdit}
                onDelete={handleDeleteUser}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
