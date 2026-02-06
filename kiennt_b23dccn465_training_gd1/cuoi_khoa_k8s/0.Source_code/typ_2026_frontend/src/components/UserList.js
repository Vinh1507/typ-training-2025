import React from 'react';
import './UserList.css';

const UserList = ({ users, onEdit, onDelete }) => {
  return (
    <div className="user-list">
      <h2>Users List</h2>
      {users.length === 0 ? (
        <p className="no-users">No users found. Add a new user to get started.</p>
      ) : (
        <div className="users-grid">
          {users.map(user => (
            <div key={user.id} className="user-card">
              <div className="user-info">
                <h3>{user.name}</h3>
                <p><strong>Email:</strong> {user.email}</p>
                {user.phone && <p><strong>Phone:</strong> {user.phone}</p>}
                {user.address && <p><strong>Address:</strong> {user.address}</p>}
              </div>
              <div className="user-actions">
                <button 
                  className="btn btn-edit"
                  onClick={() => onEdit(user)}
                >
                  Edit
                </button>
                <button 
                  className="btn btn-delete"
                  onClick={() => onDelete(user.id)}
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default UserList;
