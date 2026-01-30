document.addEventListener('DOMContentLoaded', () => {
  const tbody = document.querySelector('#bookTable tbody');
  
  fetch('http://localhost:8080/api/v1/books')
    .then(res => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(response => {
      // Lấy mảng sách từ response.data.books
      const books = response?.data?.books;

      if (!Array.isArray(books)) {
        tbody.innerHTML = `<tr><td colspan="2">Dữ liệu không hợp lệ từ server</td></tr>`;
        console.error('Expected array, got:', books);
        return;
      }

      if (books.length === 0) {
        tbody.innerHTML = `<tr><td colspan="2">Không có sách nào</td></tr>`;
        return;
      }

      tbody.innerHTML = books.map(book => `
        <tr>
          <td>${book.id}</td>
          <td>${book.name}</td>
        </tr>
      `).join('');
    })
    .catch(err => {
      tbody.innerHTML = `<tr><td colspan="2">Lỗi khi gọi API: ${err.message}</td></tr>`;
      console.error('Lỗi khi gọi API:', err);
    });
});
