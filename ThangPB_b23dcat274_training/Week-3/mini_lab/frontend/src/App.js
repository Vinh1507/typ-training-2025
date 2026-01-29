import React, { useEffect, useState } from "react";
import { getUsers, addUser, updateUser, deleteUser } from "./api";

export default function App() {
  const [users, setUsers] = useState([]);
  const [name, setName] = useState("");
  const [editId, setEditId] = useState(null);

  const fetchUsers = async () => {
    const res = await getUsers();
    setUsers(res.data);
  };

  useEffect(() => { fetchUsers(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name.trim()) return;
    if (editId) {
      await updateUser(editId, name);
      setEditId(null);
    } else {
      await addUser(name);
    }
    setName("");
    fetchUsers();
  };

  const handleEdit = (user) => {
    setEditId(user.id);
    setName(user.name);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Delete this user?")) {
      await deleteUser(id);
      fetchUsers();
    }
  };

  return (
    <div className="container py-4">
      <div className="card shadow-lg p-4">
        <h2 className="text-center mb-3 text-primary">User CRUD App</h2>
        <form onSubmit={handleSubmit} className="d-flex gap-2 mb-3">
          <input
            className="form-control"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Enter name"
          />
          <button className="btn btn-success">
            {editId ? "Update" : "Add"}
          </button>
        </form>

        <table className="table table-hover text-center">
          <thead className="table-primary">
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.name}</td>
                <td>
                  <button
                    className="btn btn-sm btn-warning me-2"
                    onClick={() => handleEdit(u)}
                  >
                    Edit
                  </button>
                  <button
                    className="btn btn-sm btn-danger"
                    onClick={() => handleDelete(u.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
            {users.length === 0 && (
              <tr>
                <td colSpan="3" className="text-muted">No users yet</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}