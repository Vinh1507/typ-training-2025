import { Fragment, useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import { base } from '../service/Base.jsx'

export default function Category() {
    const [categories, setCategories] = useState([])
    const [loading, setLoading] = useState(true)
    const navigate = useNavigate()

    // Fetch categories from API
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await axios.get(`${base}/category`)
                if (response.status === 200 && response.data?.result) {
                    setCategories(response.data.result)
                }
            } catch (err) {
                console.error('Error fetching categories:', err)
            } finally {
                setLoading(false)
            }
        }

        fetchCategories()
    }, [])

    // Filter parent categories (parentId === 0 or null)
    const parents = categories.filter((c) => !c.parentId || c.parentId === 0)

    function handleChildClick(categoryId, categoryName) {
        navigate(`/category/${categoryId}`, { state: { name: categoryName } })
    }

    function handleParentClick(categoryId, categoryName) {
        navigate(`/category/${categoryId}`, { state: { name: categoryName } })
    }

    if (loading) {
        return (
            <div>
                <ul>
                    <li style={{ color: '#94a3b8', fontSize: '14px' }}>Đang tải...</li>
                </ul>
            </div>
        )
    }

    return (
        <div>
            <ul>
                {parents.map((parent) => (
                    <Fragment key={`parent-${parent.categoryId}`}>
                        <li 
                            key={parent.categoryId}
                            onClick={() => handleParentClick(parent.categoryId, parent.categoryName)}
                            style={{ cursor: 'pointer' }}
                        >
                            {parent.categoryName}
                        </li>
                        {categories
                            .filter((child) => child.parentId === parent.categoryId)
                            .map((child) => (
                                <li 
                                    key={child.categoryId} 
                                    className={'is-child'} 
                                    onClick={() => handleChildClick(child.categoryId, child.categoryName)}
                                >
                                    {child.categoryName}
                                </li>
                            ))}
                    </Fragment>
                ))}
            </ul>
        </div>
    )
}