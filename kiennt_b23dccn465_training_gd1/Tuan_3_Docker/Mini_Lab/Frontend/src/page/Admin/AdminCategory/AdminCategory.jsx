import './AdminCategory.css'
import axios from 'axios'
import { useEffect, useMemo, useState } from 'react'
import { base } from '../../../service/Base.jsx'
import Modal from './Modal.jsx'
import { Edit, Trash } from 'lucide-react'
import { App } from 'antd'
export default function AdminCategory() {
    const { message } = App.useApp();
    const [categories, setCategories] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [modalParentId, setModalParentId] = useState(0)
    const [modalMode, setModalMode] = useState('create')
    const [editingCategory, setEditingCategory] = useState(null)

    const fetchCategories = async () => {
        try {
            const response = await axios.get(`${base}/category`)
            if (response.status === 200) {
                setCategories(response.data.result)
                return
            }
            message.error(response.data?.message || 'Không thể tải danh mục')
        } catch (error) {
            message.error(error?.response?.data?.message || 'Không thể tải danh mục')
        }
    }

    useEffect(() => {
        fetchCategories()
    }, [])

    const handleEditCategory = (category) => {
        setEditingCategory(category)
        const pid = category.parentId ?? 0
        setModalParentId(pid)
        setModalMode('edit')
        setIsModalOpen(true)
    }

    const handleDeleteCategory = async (category) => {
        try {
            const response = await axios.delete(`${base}/category/${category.categoryId}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })
            if (response.status === 200) {
                message.success("Xóa danh mục thành công!")
                fetchCategories()
                return
            }
            message.error(response.data.message)
        } catch (error) {
            message.error(error.response?.data?.message || 'Có lỗi khi xóa danh mục')
        }
    }

    const handleAddParent = () => {
        setModalParentId(0)
        setEditingCategory(null)
        setModalMode('create')
        setIsModalOpen(true)
    }

    const handleAddChild = (parentCategory) => {
        setModalParentId(parentCategory.categoryId)
        setEditingCategory(null)
        setModalMode('create')
        setIsModalOpen(true)
    }

    const buildCategoryTree = (flatCategories) => {
        if (!Array.isArray(flatCategories) || flatCategories.length === 0) return []

        // Two-level optimization: partition parents and children once, then map
        const parents = []
        const childrenByParent = new Map()

        for (const cat of flatCategories) {
            const parentId = cat.parentId ?? 0
            if (!parentId || parentId === 0) {
                parents.push({ ...cat, children: [] })
            } else {
                const list = childrenByParent.get(parentId) || []
                list.push(cat)
                childrenByParent.set(parentId, list)
            }
        }

        // Attach children (only one level deep)
        for (const parent of parents) {
            const kids = childrenByParent.get(parent.categoryId) || []
            parent.children = kids.map((c) => ({ ...c, children: [] }))
        }

        return parents
    }

    const categoryTree = useMemo(() => buildCategoryTree(categories), [categories])

    const renderTree = (nodes) => {
        if (!nodes || nodes.length === 0) return null
        return (
            <ul className="admin-category-tree">
                {nodes.map((node) => (
                    <li key={node.categoryId} className="category-item">
                        <div className="category-row level-1">
                            <span className="category-name">{node.categoryName}</span>
                            <div className="category-right">
                                {typeof node.productCount === 'number' && (
                                    <span className="category-count">{node.productCount}</span>
                                )}
                                <div className="category-actions">
                                    <button className="btn btn-edit" onClick={() => handleEditCategory(node)}><Edit /></button>
                                    <button className="btn btn-delete" onClick={() => handleDeleteCategory(node)}><Trash /></button>
                                </div>
                            </div>
                        </div>
                        <ul className="children-list">
                            {(node.children || []).map((child) => (
                                <li key={child.categoryId} className="category-item">
                                    <div className="category-row level-2">
                                        <span className="category-name">{child.categoryName}</span>
                                        <div className="category-right">
                                            {typeof child.productCount === 'number' && (
                                                <span className="category-count">{child.productCount}</span>
                                            )}
                                            <div className="category-actions">
                                                <button className="btn btn-edit" onClick={() => handleEditCategory(child)}><Edit /></button>
                                                <button className="btn btn-delete" onClick={() => handleDeleteCategory(child)}><Trash /></button>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            ))}
                            <li className="category-add-item">
                                <button type="button" className="category-add-row add-child" onClick={() => handleAddChild(node)}>
                                    <span className="add-icon" aria-hidden="true">+</span>
                                    Thêm danh mục con
                                </button>
                            </li>
                        </ul>
                    </li>
                ))}
                <li className="category-add-item">
                    <button type="button" className="category-add-row add-parent" onClick={() => handleAddParent()}>
                        <span className="add-icon" aria-hidden="true">+</span>
                        Thêm danh mục cha
                    </button>
                </li>
            </ul>
        )
    }

    return (
        <div className="admin-category-container">
            <h1 className="admin-category-title">Danh mục</h1>
            <div className="admin-category-actions">

            </div>
            {categoryTree && categoryTree.length > 0 ? (
                renderTree(categoryTree)
            ) : (
                <div className="admin-category-empty">
                    <p>Không có danh mục.</p>
                    <button type="button" className="category-add-row add-parent" onClick={() => handleAddParent()}>
                        <span className="add-icon" aria-hidden="true">+</span>
                        Thêm danh mục cha
                    </button>
                </div>
            )}

            <Modal
                open={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                parentIdDefault={modalParentId}
                onCreated={fetchCategories}
                onUpdated={fetchCategories}
                mode={modalMode}
                category={editingCategory}
            />
        </div>
    )
}