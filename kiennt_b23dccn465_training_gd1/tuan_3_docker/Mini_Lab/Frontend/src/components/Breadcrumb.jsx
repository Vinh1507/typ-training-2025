import { Link } from "react-router-dom";
import "./Breadcrumb.css";

/**
 * Breadcrumb Component
 * @param {Array} items - Mảng các breadcrumb items
 * Mỗi item có dạng: { label: "Trang chủ", path: "/" } hoặc { label: "Current Page" } (không có path = không clickable)
 */
export default function Breadcrumb({ items = [] }) {
  if (!items || items.length === 0) return null;

  return (
    <nav className="breadcrumb-container" aria-label="Breadcrumb">
      <ol className="breadcrumb-list">
        {items.map((item, index) => {
          const isLast = index === items.length - 1;
          
          return (
            <li key={index} className="breadcrumb-item">
              {!isLast && item.path ? (
                <Link to={item.path} className="breadcrumb-link">
                  {item.label}
                </Link>
              ) : (
                <span className="breadcrumb-current">{item.label}</span>
              )}
              {!isLast && <span className="breadcrumb-separator">/</span>}
            </li>
          );
        })}
      </ol>
    </nav>
  );
}

